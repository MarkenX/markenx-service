# Guía de Despliegue - MarkenX API

## Resumen de Configuración

La aplicación MarkenX ahora está completamente configurada para desplegarse en Kubernetes usando CircleCI y ArgoCD con el siguiente orden de inicialización:

**MySQL → Keycloak → Spring Boot API**

## Componentes

### 1. MySQL
- **Imagen**: mysql:8.0
- **Puerto**: 3306
- **Bases de datos**: markenx_db y keycloak_db (creadas via initdbScripts)
- **Usuarios**: markenx_user y keycloak_user
- **Health Checks**: liveness y readiness probes configurados
- **Recursos**: 500m CPU, 512Mi memoria

### 2. Keycloak
- **Imagen**: quay.io/keycloak/keycloak:24.0.1
- **Puerto**: 7080
- **Realm**: markenx (importado desde keycloak-realm-config.json)
- **Usuarios**: admin/admin, student@example.com/password, profesor@example.com/password
- **Health Checks**: /health/live y /health/ready
- **Recursos**: 1000m CPU, 1Gi memoria
- **InitContainer**: Espera a que MySQL esté listo antes de iniciar

### 3. MarkenX API
- **Imagen**: markenx/markenx:latest (construida por CircleCI)
- **Puerto**: 8080
- **Perfil**: prod
- **InitContainers**: 
  - wait-for-mysql (espera a MySQL en puerto 3306)
  - wait-for-keycloak (espera a Keycloak en puerto 7080)
- **Variables de entorno**: Configuradas en values.yaml con URLs templating

## Pipeline CI/CD

### CircleCI (.circleci/config.yml)
**Workflow en branch main:**
1. **build**: Compila la aplicación con Maven (mvn clean package -DskipTests)
2. **test**: Ejecuta tests unitarios (mvn test)
3. **sonar**: Análisis estático con SonarCloud
4. **docker_build_push**: Construye imagen Docker y la sube a DockerHub como markenx/markenx:latest
5. **notify_slack**: Notifica resultado en Slack

**Requisitos de contextos:**
- SonarCloud: Variable SONAR_TOKEN
- DockerHub: Variables DOCKERHUB_USER y DOCKERHUB_PASS
- Slack: Variable SLACK_DEFAULT_CHANNEL

### ArgoCD (argocd-app-markenx.yaml)
- **Repositorio**: https://github.com/markenx/markenx-service.git
- **Branch**: main
- **Path**: helm/markenx
- **Namespace**: markenx (se crea automáticamente)
- **Sync Policy**: 
  - Automated con prune y selfHeal habilitados
  - Sincroniza automáticamente cambios del repositorio

## Manifiestos de Helm

### Chart (helm/markenx/)
```
templates/
├── deployment.yaml          # API con initContainers
├── service.yaml             # ClusterIP para API
├── mysql-deployment.yaml    # MySQL con initdbScripts
├── mysql-service.yaml       # ClusterIP para MySQL
├── mysql-configmap.yaml     # Scripts de inicialización
├── keycloak-deployment.yaml # Keycloak con initContainer
├── keycloak-service.yaml    # ClusterIP para Keycloak
├── keycloak-configmap.yaml  # Configuración del realm
└── ...

keycloak-realm-config.json   # Configuración del realm MarkenX
values.yaml                  # Valores de configuración
Chart.yaml                   # Metadata del chart
```

## Validación del Despliegue

### Pre-requisitos
1. Cluster de Kubernetes activo
2. kubectl configurado
3. Helm 3.x instalado
4. CircleCI configurado con contextos (SonarCloud, DockerHub, Slack)
5. ArgoCD instalado en el cluster

### Paso 1: Validar Chart de Helm localmente
```bash
cd /c/Users/cjacome/school/markenx/markenx-service

# Validar sintaxis del chart
helm lint helm/markenx

# Ver manifiestos generados (dry-run)
helm template markenx helm/markenx --debug

# Instalar en cluster local (opcional)
helm install markenx helm/markenx --namespace markenx --create-namespace
```

### Paso 2: Verificar Pipeline de CircleCI
1. Push a branch main:
```bash
git add .
git commit -m "Configure Kubernetes deployment with MySQL and Keycloak"
git push origin main
```

2. Verificar en CircleCI:
   - Build job completa exitosamente
   - Tests pasan
   - SonarCloud analiza código
   - Docker image se construye y pushea a DockerHub
   - Notificación llega a Slack

3. Confirmar imagen en DockerHub:
```bash
docker pull markenx/markenx:latest
docker images | grep markenx
```

### Paso 3: Desplegar con ArgoCD
1. Aplicar configuración de ArgoCD:
```bash
kubectl apply -f argocd-app-markenx.yaml
```

2. Verificar sincronización:
```bash
# Ver estado de la aplicación
kubectl get application markenx -n argocd

# Ver detalles
kubectl describe application markenx -n argocd
```

3. Monitorear despliegue:
```bash
# Ver pods en namespace markenx
kubectl get pods -n markenx -w

# Verificar orden de inicio
kubectl get events -n markenx --sort-by='.lastTimestamp'
```

### Paso 4: Validar Orden de Inicialización

**Secuencia esperada:**
1. MySQL pod inicia primero
2. MySQL readiness probe pasa (puede tardar 10-30s)
3. Keycloak initContainer espera a MySQL
4. Keycloak pod inicia
5. Keycloak readiness probe pasa (puede tardar 60-120s)
6. API initContainers esperan a MySQL y Keycloak
7. API pod inicia
8. API listo para recibir tráfico

```bash
# Ver logs de initContainers
kubectl logs -n markenx deployment/markenx -c wait-for-mysql
kubectl logs -n markenx deployment/markenx -c wait-for-keycloak

# Ver logs de MySQL
kubectl logs -n markenx deployment/markenx-mysql

# Ver logs de Keycloak
kubectl logs -n markenx deployment/markenx-keycloak

# Ver logs de la API
kubectl logs -n markenx deployment/markenx
```

### Paso 5: Probar Endpoints

```bash
# Port-forward para acceso local
kubectl port-forward -n markenx service/markenx 8080:8080

# Probar health endpoint
curl http://localhost:8080/actuator/health

# Probar autenticación con Keycloak
curl -X POST http://localhost:8080/api/markenx/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "student@example.com", "password": "password"}'

# Probar endpoint protegido
curl http://localhost:8080/api/markenx/students/me/tasks \
  -H "Authorization: Bearer <TOKEN>"
```

## Troubleshooting

### MySQL no inicia
```bash
# Ver logs
kubectl logs -n markenx deployment/markenx-mysql

# Verificar ConfigMap
kubectl get configmap markenx-mysql-init -n markenx -o yaml

# Verificar variables de entorno
kubectl describe pod -n markenx -l app=mysql
```

### Keycloak no inicia
```bash
# Ver logs de initContainer
kubectl logs -n markenx -l app=keycloak -c wait-for-mysql

# Ver logs de Keycloak
kubectl logs -n markenx -l app=keycloak

# Verificar health endpoints
kubectl exec -n markenx -l app=keycloak -- curl localhost:7080/health/ready
```

### API no inicia
```bash
# Ver logs de initContainers
kubectl logs -n markenx deployment/markenx -c wait-for-mysql
kubectl logs -n markenx deployment/markenx -c wait-for-keycloak

# Ver logs de aplicación
kubectl logs -n markenx deployment/markenx -f

# Verificar variables de entorno
kubectl exec -n markenx deployment/markenx -- env | grep -E "SPRING|KEYCLOAK"
```

### ArgoCD no sincroniza
```bash
# Ver eventos de ArgoCD
kubectl logs -n argocd deployment/argocd-application-controller | grep markenx

# Forzar sincronización
kubectl patch application markenx -n argocd --type merge -p '{"operation": {"initiatedBy": {"username": "admin"}, "sync": {"revision": "main"}}}'

# Ver detalles de sincronización
argocd app get markenx
argocd app sync markenx
```

## Configuración Actual

### values.yaml - Configuración Crítica
- **MySQL**:
  - root password: root_password_2025!
  - database: markenx_db
  - user: markenx_user / markenx_password_2025!
  - initdbScripts: Crea keycloak_db y keycloak_user

- **Keycloak**:
  - admin: admin / admin
  - database: keycloak_db en {{ .Release.Name }}-mysql
  - realm: Importado desde keycloak-realm-config.json
  - URL interna: http://{{ .Release.Name }}-keycloak:7080

- **API**:
  - datasource: jdbc:mysql://{{ .Release.Name }}-mysql:3306/markenx_db
  - keycloak server: http://{{ .Release.Name }}-keycloak:7080
  - OAuth2 issuer: http://{{ .Release.Name }}-keycloak:7080/realms/markenx

### Diferencias con docker-compose.dev.yml
- **Persistencia**: En Kubernetes usa emptyDir (datos se pierden al reiniciar pod)
- **Networking**: Services en lugar de network bridge
- **Health checks**: Kubernetes probes en lugar de Docker healthchecks
- **Init**: initContainers en lugar de depends_on con service_healthy

## Próximos Pasos

1. **Producción**:
   - Agregar PersistentVolumeClaims para MySQL (mantener datos)
   - Configurar Ingress para exponer API externamente
   - Agregar TLS/SSL con cert-manager
   - Configurar backups de base de datos

2. **Monitoring**:
   - Agregar Prometheus metrics
   - Configurar Grafana dashboards
   - Alertas en Slack/PagerDuty

3. **Seguridad**:
   - Usar Secrets en lugar de valores planos en values.yaml
   - Configurar Network Policies
   - Habilitar Pod Security Policies

4. **Alta Disponibilidad**:
   - Múltiples réplicas de API con HPA
   - MySQL en configuración master-replica
   - Keycloak clustering

## Comandos Útiles

```bash
# Ver todos los recursos
kubectl get all -n markenx

# Ver configuración completa
helm get values markenx -n markenx
helm get manifest markenx -n markenx

# Reiniciar deployment
kubectl rollout restart deployment/markenx -n markenx
kubectl rollout status deployment/markenx -n markenx

# Eliminar todo
helm uninstall markenx -n markenx
kubectl delete namespace markenx

# Ver recursos de ArgoCD
kubectl get applications -n argocd
kubectl describe application markenx -n argocd
```
