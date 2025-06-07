pipeline {
    agent any

    environment {
      /*  // Настройки Selenoid
        SELENOID_URL = 'http://selenoid:4444/wd/hub'
        BROWSER = 'chrome'
        BROWSER_VERSION = '127.0'*/

        // Настройки Allure
        ALLURE_RESULTS = 'build/allure-results'
        ALLURE_REPORT = 'build/allure-report'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Dypose-java/demoApiAndUi'
            }
        }

        stage('Clean workspace') {
            steps {
                sh ' clean'
            }
        }

        stage('Run API Tests') {
            steps {
                sh 'gradle test -Dtag=API'
            }
            post {
                always {
                    // Сохраняем результаты для Allure
                    allure includeProperties: false, jdk: '', results: [[path: 'build/allure-results/api']]
                }
            }
        }

        stage('Run UI Tests') {
            steps {
                sh """
                    gradle test \
                    -Dtag=UI \
                    -DrunIn=browser_selenoid 
                """
            }
            post {
                always {
                    // Сохраняем результаты для Allure
                    allure includeProperties: false, jdk: '', results: [[path: 'build/allure-results/ui']]
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                script {
                    // Объединяем результаты API и UI тестов
                    sh 'mkdir -p build/allure-results/combined'
                    sh 'cp -r build/allure-results/api/* build/allure-results/combined/ || true'
                    sh 'cp -r build/allure-results/ui/* build/allure-results/combined/ || true'

                    // Генерируем отчет
                    allure([
                            includeProperties: false,
                            jdk: '',
                            report: 'build/allure-report',
                            results: [[path: 'build/allure-results/combined']]
                    ])
                }
            }
        }
    }

    post {
        always {
            // Публикуем Allure отчет
            allure([
                    includeProperties: false,
                    jdk: '',
                    report: 'build/allure-report',
                    results: [[path: 'build/allure-results/combined']]
            ])

            // Очистка workspace
            cleanWs()
        }
        success {
            slackSend channel: '#reports', message: "Тесты завершены. Allure отчет: ${env.BUILD_URL}allure/"
        }
        failure {
            slackSend channel: '#reports', message: "Некоторые тесты упали. Allure отчет: ${env.BUILD_URL}allure/"
        }
    }
}