pipeline {
    agent any

    environment {
        GITHUB_REPO = 'ChrisJMora/udla-markenx-service'
        GITHUB_TOKEN = credentials('github-creds')
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
                withCredentials([file(credentialsId: 'docker-compose-secret-env-file', variable: 'ENV_FILE')]) {
                    bat "copy \"%ENV_FILE%\" .env"
                    bat 'type .env'
                }
            }
        }

        stage('Docker Deploy') {
            steps {
                bat '''
                    echo Desplegando...
                    docker compose --env-file .env build app
                    docker compose --env-file .env up -d app
                    echo Contenedor desplegado.
                '''
            }
        }
    }

    post {
        always {
            bat 'del /f .env 2>nul || echo .env no encontrado'

            echo 'Enviando estado a GitHub...'
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
                     -d "{\\"state\\": \\"success\\", \\"context\\": \\"ci/jenkins\\", \\"description\\": \\"Deploy OK\\"}" ^
                     https://api.github.com/repos/%GITHUB_REPO%/statuses/%GIT_COMMIT%
            """
        }
        failure {
            bat """
                curl -s -o nul ^
                     -H "Authorization: token %GITHUB_TOKEN%" ^
                     -d "{\\"state\\": \\"failure\\", \\"context\\": \\"ci/jenkins\\", \\"description\\": \\"Deploy fallido\\"}" ^
                     https://api.github.com/repos/%GITHUB_REPO%/statuses/%GIT_COMMIT%
            """
        }
    }
}
