pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        IMAGE_NAME = 'chetan1417/chat-application'
    }

    stages {
        stage('Checkout Code') {
    steps {
        git branch: 'master', 
            url: 'https://github.com/chetan1417/chat-app.git', 
            credentialsId: 'chetananeja14'
    }
}



        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME:latest .'
            }
        }

        stage('Login to Docker Hub') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                sh 'docker push $IMAGE_NAME:latest'
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
