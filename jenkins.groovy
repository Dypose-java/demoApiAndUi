pipeline {
    agent any

    environment {
        SELENOID_URL = 'http://127.0.0.1:4444/wd/hub'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                        url: 'https://github.com/Dypose-java/demoApiAndUi.git'
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
                        script {
                            sh """
                                ./gradlew clean test \
                                -Dtag=UI \
                                -DrunIn=browser_selenoid \
                                -Dselenide.remote=${env.SELENOID_URL} \
                                -Dselenoid.options.enableVNC=true \
                                -Dselenoid.options.enableVideo=true
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