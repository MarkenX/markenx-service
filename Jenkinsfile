pipeline {
    agent any

    environment {
        GITHUB_REPO = 'ChrisJMora/udla-markenx-service'
        GITHUB_TOKEN = credentials('github-creds')
        IMAGE_NAME = 'markenx-service-app'
        CONTAINER_NAME = 'markenx-service'
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

        stage('Deploy Docker Compose') {
            steps {
                echo 'Desplegando todos los servicios desde docker-compose.dev.yml local...'
                bat '''
                    docker compose --env-file .env -f docker-compose.dev.yml up -d --build
                '''
            }
        }
    }
    
    post {
    success {
        echo 'Pipeline completado correctamente.'

        withCredentials([usernamePassword(credentialsId: 'gmail-smtp-creds', usernameVariable: 'SMTP_USER', passwordVariable: 'SMTP_PASS')]) {
            emailext(
                subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    <h2>Build Exitoso</h2>
                    <p><strong>Proyecto:</strong> ${env.JOB_NAME}</p>
                    <p><strong>Build #:</strong> ${env.BUILD_NUMBER}</p>
                    <p><a href="${env.BUILD_URL}">Ver en Jenkins</a></p>
                """,
                to: 'daviandy3@gmail.com',
                from: '${SMTP_USER}',
                replyTo: 'daviandy3@gmail.com',
                mimeType: 'text/html',
                attachLog: true,
                smtpHost: 'smtp.gmail.com',
                smtpPort: '587',
                smtpAuth: true,
                smtpUsername: '${SMTP_USER}',
                smtpPassword: '${SMTP_PASS}',
                useSsl: false
            )
        }

        bat """
            curl -s -o nul ^
                 -H "Authorization: token %GITHUB_TOKEN%" ^
                 -d "{\\"state\\": \\"success\\", \\"context\\": \\"ci/jenkins\\", \\"description\\": \\"App redeployed\\"}" ^
                 https://api.github.com/repos/%GITHUB_REPO%/statuses/%GIT_COMMIT%
        """
    }

    failure {
        echo 'Pipeline fallido.'

        withCredentials([usernamePassword(credentialsId: 'gmail-smtp-creds', usernameVariable: 'SMTP_USER', passwordVariable: 'SMTP_PASS')]) {
            emailext(
                subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    <h2>Build Fallido</h2>
                    <p><strong>Proyecto:</strong> ${env.JOB_NAME}</p>
                    <p><strong>Build #:</strong> ${env.BUILD_NUMBER}</p>
                    <p><a href="${env.BUILD_URL}">Ver logs</a></p>
                """,
                to: 'daviandy3@gmail.com',
                from: '${SMTP_USER}',
                replyTo: 'daviandy3@gmail.com',
                mimeType: 'text/html',
                attachLog: true,
                smtpHost: 'smtp.gmail.com',
                smtpPort: '587',
                smtpAuth: true,
                smtpUsername: '${SMTP_USER}',
                smtpPassword: '${SMTP_PASS}',
                useSsl: false
            )
        }

        bat """
            curl -s -o nul ^
                 -H "Authorization: token %GITHUB_TOKEN%" ^
                 -d "{\\"state\\": \\"failure\\", \\"context\\": \\"ci/jenkins\\", \\"description\\": \\"Deploy failed\\"}" ^
                 https://api.github.com/repos/%GITHUB_REPO%/statuses/%GIT_COMMIT%
        """
    }
}
}
