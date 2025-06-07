pipeline {
    agent any

    environment {
        ALLURE_RESULTS = 'build/allure-results'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Dypose-java/demoApiAndUi.git'
            }
        }

        stage('Set Permissions') {
            steps {
                sh 'chmod +x gradlew'
            }
        }

        stage('Run Tests') {
            failFast false // Продолжать выполнение при падении тестов
            parallel {
                stage('API Tests') {
                    steps {
                        script {
                            try {
                                sh './gradlew clean test -Dtag=API'
                            } catch (e) {
                                echo "API tests failed: ${e.getMessage()}"
                                currentBuild.result = 'UNSTABLE'
                            }
                        }
                    }
                    post {
                        always {
                            allure includeProperties: false, results: [[path: 'build/allure-results']]
                        }
                    }
                }

                stage('UI Tests') {
                    steps {
                        script {
                            try {
                                sh """
                                    ./gradlew clean test \
                                    -Dtag=UI \
                                    -DrunIn=browser_selenoid 
                                """
                            } catch (e) {
                                echo "UI tests failed: ${e.getMessage()}"
                                currentBuild.result = 'UNSTABLE'
                            }
                        }
                    }
                    post {
                        always {
                            allure includeProperties: false, results: [[path: 'build/allure-results']]
                        }
                    }
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                script {
                    allure([
                            includeProperties: false,
                            report: 'build/allure-report',
                            results: [[path: 'build/allure-results']]
                    ])
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'build/allure-report/**', fingerprint: true
            cleanWs()
        }
        success {
            slackSend channel: '#reports',
                    message: "Тесты завершены. Allure отчет: ${env.BUILD_URL}allure/"
        }
        unstable {
            slackSend channel: '#reports',
                    message: "Некоторые тесты упали. Allure отчет: ${env.BUILD_URL}allure/"
        }
        failure {
            slackSend channel: '#reports',
                    message: "Сборка провалена. Подробности: ${env.BUILD_URL}"
        }
    }
}