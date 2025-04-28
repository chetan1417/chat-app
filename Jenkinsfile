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
        bat 'docker build -t your-image-name .'
    }
}
stage('Login to Docker Hub') {
    steps {
        bat 'docker login -u your-username -p your-password'
    }
}
stage('Push to Docker Hub') {
    steps {
        bat 'docker push your-image-name'
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
