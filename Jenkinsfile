pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub') // Using the Jenkins credential
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'master', 
                    url: 'https://github.com/chetan1417/chat-app.git', 
                    credentialsId: 'chetananeja14' // Git credentials
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t chetananeja/chat-application .' // Manually set the image name here
            }
        }

        stage('Login to Docker Hub') {
            steps {
                bat 'echo %DOCKERHUB_CREDENTIALS_PSW% | docker login -u %DOCKERHUB_CREDENTIALS_USR% --password-stdin'
                // Logs into Docker Hub using the credentials stored in Jenkins
            }
        }

        stage('Push to Docker Hub') {
            steps {
                bat 'docker push chetananeja/chat-application' // Manually set the image name here
            }
        }
    }

    post {
        success {
            echo 'CI/CD Pipeline Completed Successfully!'
        }
        failure {
            echo 'Something went wrong!'
        }
    }
}
