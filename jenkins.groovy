pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Dypose-java/demoApiAndUi.git'
                sh 'chmod +x gradlew'
            }
        }

        stage('Run Tests') {
            parallel {
                stage('API Tests') {
                    steps {
                        sh './gradlew clean test -Dtag=API'
                    }
                }
                stage('UI Tests') {
                    steps {
                        sh './gradlew clean test -Dtag=UI -DrunIn=browser_local'
                    }
                }
            }
        }
    }

    post {
        always {
            allure includeProperties: false,
                    results: [[path: 'build/allure-results']],
                    report: 'build/allure-report'
            cleanWs()
        }
    }
}