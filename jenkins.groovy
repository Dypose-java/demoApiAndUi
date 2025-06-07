pipeline {
    agent any

    environment {
        // Для локального Selenoid используем host.docker.internal
        SELENOID_URL = 'http://host.docker.internal:4444/wd/hub'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                        url: 'https://github.com/Dypose-java/demoApiAndUi.git'
                sh 'chmod +x gradlew'
            }
        }

        stage('Verify Selenoid') {
            steps {
                script {
                    // Проверяем доступность Selenoid
                    def status = sh(
                            script: "curl -s -o /dev/null -w '%{http_code}' ${env.SELENOID_URL}/status || echo 500",
                            returnStdout: true
                    ).trim()

                    if (status != '200') {
                        error """Selenoid недоступен по адресу ${env.SELENOID_URL}
                                Проверьте что:
                                1. Selenoid запущен: docker ps
                                2. Порт проброшен: docker run -p 4444:4444 ...
                                3. Для Jenkins в Docker используйте host.docker.internal"""
                    }
                }
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
                        script {
                            // Уникальный ID сессии для каждого запуска
                            def sessionId = UUID.randomUUID().toString()

                            sh """
                                ./gradlew clean test \
                                -Dtag=UI \
                                -DrunIn=browser_selenoid \
                                -Dselenide.remote=${env.SELENOID_URL} \
                                -Dbrowser=chrome \
                                -Dbrowser.version=127.0 \
                                -Dselenoid.options.name=${sessionId} \
                                -Dselenoid.options.enableVNC=true \
                                -Dselenoid.options.enableVideo=false \
                                -Dselenoid.options.screenResolution=1920x1080x24
                            """
                        }
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