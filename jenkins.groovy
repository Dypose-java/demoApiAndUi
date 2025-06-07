pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout([
                        $class: 'GitSCM',
                        branches: [[name: '*/main']],  // Явно указываем ветку
                        extensions: [],
                        userRemoteConfigs: [[
                                                    url: 'https://github.com/Dypose-java/demoApiAndUi.git',
                                                    credentialsId: ''  // Оставьте пустым для публичного репозитория
                                            ]]
                ])
                sh 'chmod +x gradlew'  // Даем права на выполнение
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
            allure([
                    includeProperties: false,
                    results: [[path: 'build/allure-results']],
                    report: 'build/allure-report'
            ])
            cleanWs()
        }
    }
}