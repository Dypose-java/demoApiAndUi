pipeline {
    agent any

    environment {
        SELENOID_URL = 'http://127.0.0.1:4444/wd/hub'
        BROWSER = 'chrome'
        BROWSER_VERSION = '127.0'
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
                sh 'chmod +x gradlew'
            }
        }

        stage('Check Selenoid') {
            steps {
                script {
                    sh '''
                        echo "Checking Selenoid connection..."
                        curl -v $SELENOID_URL/status || true
                    '''
                }
            }
        }

        stage('Run API Tests') {
            steps {
                sh './gradlew clean test -Dtag=API'
            }
            post {
                always {
                    allure includeProperties: false, results: [[path: 'build/allure-results']]
                }
            }
        }

        stage('Run UI Tests') {
            steps {
                script {
                    sh """
                        ./gradlew test \
                        -Dtag=UI \
                        -DrunIn=browser_selenoid \
                        -Dselenoid.url=$SELENOID_URL \
                        -Dbrowser=$BROWSER \
                        -Dbrowser.version=$BROWSER_VERSION
                    """
                }
            }
            post {
                always {
                    allure includeProperties: false, results: [[path: 'build/allure-results']]
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                script {
                    sh 'mkdir -p build/allure-results'
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
    }
}