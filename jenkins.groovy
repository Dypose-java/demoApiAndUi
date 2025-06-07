pipeline {
    agent any

    environment {
        ALLURE_RESULTS = 'build/allure-results'
        ALLURE_REPORT = 'build/allure-report'
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
                sh 'mkdir -p build/allure-results'
            }
        }

        stage('Run Tests') {
            failFast false
            parallel {
                stage('API Tests') {
                    steps {
                        script {
                            try {
                                // API тесты без properties файла
                                sh './gradlew clean test -Dtag=API'
                            } catch (e) {
                                echo "API tests failed: ${e.getMessage()}"
                                currentBuild.result = 'UNSTABLE'
                            }
                        }
                    }
                    post {
                        always {
                            sh 'mkdir -p build/allure-results/api'
                            sh 'cp -r build/test-results/test/* build/allure-results/api/ || true'
                        }
                    }
                }

                stage('UI Tests') {
                    steps {
                        script {
                            try {
                                // Чтение настроек UI из browser_selenoid.properties
                                def uiProps = readProperties file: 'resources/configs/browser_selenoid.properties'
                                sh """
                                    ./gradlew clean test \
                                    -Dtag=UI \
                                    -DrunIn=browser_selenoid \
                                    -Dselenoid.url=${uiProps['ui.remote']} \
                                    -Dbrowser=${uiProps['ui.browser']} \
                                    -Dbrowser.version=${uiProps['ui.browser.version']} \
                                    -Dheadless=${uiProps['ui.headless']} \
                                    -DbaseUrl=${uiProps['ui.url']} \
                                    -Dbrowser.size=${uiProps['ui.browser.size']} \
                                    -Dtimeout=${uiProps['ui.browser.timeOut']} \
                                    -DpageLoadTimeout=${uiProps['ui.pageLoadTimeout']}
                                """
                            } catch (e) {
                                echo "UI tests failed: ${e.getMessage()}"
                                currentBuild.result = 'UNSTABLE'
                            }
                        }
                    }
                    post {
                        always {
                            sh 'mkdir -p build/allure-results/ui'
                            sh 'cp -r build/test-results/test/* build/allure-results/ui/ || true'
                        }
                    }
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
            sh 'rm -rf build/allure-results || true'
            archiveArtifacts artifacts: 'build/allure-report/**', fingerprint: true, allowEmptyArchive: true
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