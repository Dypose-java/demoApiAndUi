pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master',
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
                            // Генерируем уникальный user-data-dir для каждого запуска
                            def userDataDir = "/tmp/chrome-profile-${UUID.randomUUID()}"

                            sh """
                                ./gradlew clean test \
                                -Dtag=UI \
                                -DrunIn=browser_local \
                                -Dchrome.args="--user-data-dir=${userDataDir}" 
                            """

                            // Очищаем временный профиль
                            sh "rm -rf ${userDataDir} || true"
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