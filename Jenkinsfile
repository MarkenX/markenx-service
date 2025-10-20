pipeline {
    agent any

    environment {
        GITHUB_REPO = 'ChrisJMora/udla-markenx-service'
        GITHUB_TOKEN = credentials('github-creds')
        IMAGE_NAME = 'markenx-service'
        CONTAINER_NAME = 'spring-dev'
        DB_HOST = 'db'
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

        stage('Docker Deploy with Secrets') {
            steps {
                withCredentials([
                    string(credentialsId: 'db-user-dev', variable: 'DB_USER'),
                    string(credentialsId: 'db-pass-dev', variable: 'DB_PASS'),
                    string(credentialsId: 'db-root-pass-dev', variable: 'DB_ROOT_PASS'),
                    string(credentialsId: 'db-name-dev', variable: 'DB_NAME'),
                    string(credentialsId: 'db-port-dev', variable: 'DB_PORT')
                ]) {
                    bat '''
                        echo Deteniendo contenedor previo...
                        for /f "tokens=*" %%i in ('docker ps -q --filter "name=^%CONTAINER_NAME%^" 2^>nul') do (
                            docker stop %%i
                            docker rm %%i
                        ) || echo No previous container.

                        echo Eliminando imagen previa...
                        for /f "tokens=*" %%i in ('docker images -q %IMAGE_NAME% 2^>nul') do (
                            docker rmi -f %%i
                        ) || echo No previous image.

                        echo Construyendo y desplegando con variables inyectadas...
                        set DB_USER=%DB_USER%
                        set DB_PASS=%DB_PASS%
                        set DB_ROOT_PASS=%DB_ROOT_PASS%
                        set DB_NAME=%DB_NAME%
                        set DB_PORT=%DB_PORT%
                        set DB_HOST=%DB_HOST%

                        docker compose build app
                        docker compose up -d app
                    '''
                }
            }
        }
    }

    post {
        always {
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
                     -d "{\\"state\\": \\"success\\", \\"context\\": \\"ci/jenkins\\", \\"description\\": \\"Build OK\\"}" ^
                     https://api.github.com/repos/%GITHUB_REPO%/statuses/%GIT_COMMIT%
            """
        }
        failure {
            bat """
                curl -s -o nul ^
                     -H "Authorization: token %GITHUB_TOKEN%" ^
                     -d "{\\"state\\": \\"failure\\", \\"context\\": \\"ci/jenkins\\", \\"description\\": \\"Build failed\\"}" ^
                     https://api.github.com/repos/%GITHUB_REPO%/statuses/%GIT_COMMIT%
            """
        }
    }
}
