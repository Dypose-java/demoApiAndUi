pipeline {
    agent any

    environment {
        ALLURE_RESULTS = 'build/allure-results'
        ALLURE_REPORT = 'build/allure-report'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Dypose-java/demoApiAndUi'
            }
        }

        stage('Set Permissions') {
            steps {
                sh 'chmod +x gradlew'  // Критически важный этап
            }
        }

        stage('Run API Tests') {
            steps {
                sh './gradlew clean test -Dtag=API'
            }
            post {
                always {
                    allure includeProperties: false, results: [[path: 'build/allure-results/api']]
                }
            }
        }

        stage('Run UI Tests') {
            steps {
                sh './gradlew test -Dtag=UI -DrunIn=browser_selenoid'
            }
            post {
                always {
                    allure includeProperties: false, results: [[path: 'build/allure-results/ui']]
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                script {
                    sh 'mkdir -p build/allure-results/combined'
                    sh 'cp -r build/allure-results/api/* build/allure-results/combined/ || true'
                    sh 'cp -r build/allure-results/ui/* build/allure-results/combined/ || true'
                    allure([
                            includeProperties: false,
                            report: 'build/allure-report',
                            results: [[path: 'build/allure-results/combined']]
                    ])
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}