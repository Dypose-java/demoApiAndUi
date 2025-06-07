pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'chmod +x gradlew'
            }
        }

        stage('Run API Tests') {
            when { expression { params.RUN_TAG == 'API' } }
            steps {
                sh './gradlew clean test -Dtag=API'
            }
        }

        stage('Run UI Tests') {
            when { expression { params.RUN_TAG == 'UI' } }
            steps {
                withEnv([
                        'selenide.runIn=browser_selenoid',
                        'selenide.chromeoptions.args=--headless,--no-sandbox,--disable-dev-shm-usage,--disable-gpu'
                ]) {
                    sh './gradlew clean test -Dtag=UI -DrunIn=browser_selenoid'
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

parameters {
    choice(name: 'RUN_TAG', choices: ['API', 'UI', 'ALL'], description: 'Что запускать?')
}