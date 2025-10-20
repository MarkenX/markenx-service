pipeline {
    agent any

    environment {
        SONAR_HOST = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonar-token')
        GITHUB_REPO = 'https://github.com/ChrisJMora/udla-markenx-service.git'
        GITHUB_TOKEN = credentials('github-creds')
        IMAGE_NAME = 'markenx-service'
        CONTAINER_NAME = 'spring-dev'
    }

    stages {

        stage('Build') {
            steps {
                echo 'Compilando y empaquetando la aplicación Spring Boot...'
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Tests') {
            steps {
                echo 'Ejecutando pruebas unitarias y generando cobertura...'
                bat 'mvn test'
            }
        }

        stage('Code Quality Analysis') {
            steps {
                echo 'Analizando calidad del código con SonarQube...'
                withSonarQubeEnv('sonarqube-server') {
                    bat '''
                        mvn sonar:sonar \
                            -Dsonar.projectKey=markenx-service \
                            -Dsonar.host.url=${SONAR_HOST} \
                            -Dsonar.login=${SONAR_TOKEN}
                    '''
                }
            }
        }

        stage('Docker Deploy') {
            steps {
                echo 'Reconstruyendo e iniciando contenedor Docker del servicio Spring Boot...'
                bat '''
                    echo "Deteniendo y eliminando contenedor previo si existe..."
                    docker ps -q --filter "name=${CONTAINER_NAME}" | grep -q . && docker stop ${CONTAINER_NAME} && docker rm ${CONTAINER_NAME} || echo "No hay contenedor previo."

                    echo "Eliminando imagen previa si existe..."
                    docker images -q ${IMAGE_NAME} | grep -q . && docker rmi ${IMAGE_NAME} || echo "No hay imagen previa."

                    echo "Reconstruyendo imagen y levantando servicio..."
                    docker compose build app
                    docker compose up -d app

                    echo "Contenedor ${CONTAINER_NAME} desplegado exitosamente."
                '''
            }
        }
    }

    post {
        always {
            echo 'Ejecutando tareas post-pipeline...'
            bat """
            curl -H "Authorization: token ${GITHUB_TOKEN}" \
                 -H "Accept: application/vnd.github.v3+json" \
                 -d '{"state": "success", "context": "ci/jenkins", "description": "Pipeline completado"}' \
                 https://api.github.com/repos/${GITHUB_REPO}/statuses/${GIT_COMMIT}
            """
        }

        success {
            echo 'Pipeline completado exitosamente.'
        }

        failure {
            echo 'Pipeline fallido. Verifica los logs de Jenkins y Docker.'
            bat """
            curl -H "Authorization: token ${GITHUB_TOKEN}" \
                 -H "Accept: application/vnd.github.v3+json" \
                 -d '{"state": "failure", "context": "ci/jenkins", "description": "Pipeline fallido"}' \
                 https://api.github.com/repos/${GITHUB_REPO}/statuses/${GIT_COMMIT}
            """
        }
    }
}
