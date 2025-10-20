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
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Tests') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Code Quality') {
            steps {
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
                withCredentials([file(credentialsId: 'docker-env-file-dev', variable: 'ENV_FILE')]) {
                    bat 'copy "%ENV_FILE%" .env'
                }
            }
        }

        stage('Deploy Only App') {
            steps {
                bat '''
                    docker compose --env-file .env stop app || echo app no estaba corriendo
                    docker compose --env-file .env rm -f app || echo app no existía

                    docker compose --env-file .env build --no-cache app
                    docker compose --env-file .env up -d app

                    docker ps --filter "name=spring-dev" --format "table {{.Names}}\t{{.Status}}"
                '''
            }
        }
    }

    post {
        always {
            bat 'del /f .env 2>nul || echo .env no encontrado'

            bat """
                curl -s -o nul ^
                     -H "Authorization: token %GITHUB_TOKEN%" ^
                     -d "{\\"state\\": \\"pending\\", \\"context\\": \\"ci/jenkins\\"}" ^
                     https://api.github.com/repos/%GITHUB_REPO%/statuses/%GIT_COMMIT%
            """
        }
        success {
            bat """
                curl -s -o nul ^
                     -H "Authorization: token %GITHUB_TOKEN%" ^
                     -d "{\\"state\\": \\"success\\", \\"context\\": \\"ci/jenkins\\", \\"description\\": \\"App redeployed\\"}" ^
                     https://api.github.com/repos/%GITHUB_REPO%/statuses/%GIT_COMMIT%
            """
        }
        failure {
            bat """
                curl -s -o nul ^
                     -H "Authorization: token %GITHUB_TOKEN%" ^
                     -d "{\\"state\\": \\"failure\\", \\"context\\": \\"ci/jenkins\\", \\"description\\": \\"Deploy failed\\"}" ^
                     https://api.github.com/repos/%GITHUB_REPO%/statuses/%GIT_COMMIT%
            """
        }
    }
}
