pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub') // Using the Jenkins credential
        IMAGE_NAME = 'chetananeja/chat-application' // Your Docker Hub repository
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
                bat 'docker build -t $IMAGE_NAME .' // Builds the image using the repository name
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
                bat 'docker push $IMAGE_NAME' // Pushes the image to your Docker Hub repository
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
