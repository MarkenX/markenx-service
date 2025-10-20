pipeline {
    agent any

    environment {
        GITHUB_REPO = 'ChrisJMora/udla-markenx-service'
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
                echo 'Ejecutando pruebas unitarias...'
                bat 'mvn test'
            }
        }

        stage('Code Quality Analysis') {
            steps {
                echo 'Analizando calidad del código con SonarQube...'
                withSonarQubeEnv('sonarqube-server') {
                    bat '''
                        mvn sonar:sonar ^
                            -Dsonar.projectKey=udla-markenx-service ^
                            -Dsonar.host.url=%SONAR_HOST_URL% ^
                            -Dsonar.login=%SONAR_AUTH_TOKEN%
                    '''
                }
            }
        }

        stage('Inject .env Secret File') {
            steps {
                echo 'Inyectando archivo .env para Docker Compose...'
                withCredentials([file(credentialsId: 'docker-compose-secret-env-file', variable: 'ENV_FILE')]) {
                    bat 'copy "%ENV_FILE%" .env'
                }
            }
        }

        stage('Deploy App Only') {
            steps {
                echo 'Desplegando solo el contenedor de la app...'
                bat '''
                    REM Detener y eliminar solo el contenedor de la app
                    docker ps -q --filter "name=spring-dev" | findstr . >nul && docker stop spring-dev && docker rm spring-dev || echo "Contenedor spring-dev no estaba corriendo"

                    REM Reconstruir la imagen de la app
                    docker compose --env-file .env build --no-cache app

                    REM Levantar solo la app
                    docker compose --env-file .env up -d app

                    REM Verificar que el contenedor esté corriendo
                    docker ps --filter "name=spring-dev" --format "table {{.Names}}\t{{.Status}}"
                '''
            }
        }
    }

    post {
        always {
            echo 'Limpiando archivo .env...'
            bat 'del /f .env 2>nul || echo .env no encontrado'

            echo 'Marcando estado pendiente en GitHub...'
            bat """
                curl -s -o nul ^
                     -H "Authorization: token %GITHUB_TOKEN%" ^
                     -d "{\\"state\\": \\"pending\\", \\"context\\": \\"ci/jenkins\\"}" ^
                     https://api.github.com/repos/%GITHUB_REPO%/statuses/%GIT_COMMIT%
            """
        }
        success {
            echo 'Pipeline completado correctamente.'
            bat """
                curl -s -o nul ^
                     -H "Authorization: token %GITHUB_TOKEN%" ^
                     -d "{\\"state\\": \\"success\\", \\"context\\": \\"ci/jenkins\\", \\"description\\": \\"App redeployed\\"}" ^
                     https://api.github.com/repos/%GITHUB_REPO%/statuses/%GIT_COMMIT%
            """
        }
        failure {
            echo 'Pipeline fallido. Revisa Jenkins y Docker.'
            bat """
                curl -s -o nul ^
                     -H "Authorization: token %GITHUB_TOKEN%" ^
                     -d "{\\"state\\": \\"failure\\", \\"context\\": \\"ci/jenkins\\", \\"description\\": \\"Deploy failed\\"}" ^
                     https://api.github.com/repos/%GITHUB_REPO%/statuses/%GIT_COMMIT%
            """
        }
    }
}
