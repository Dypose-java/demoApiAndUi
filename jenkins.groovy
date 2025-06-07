task_branch = "${TEST_BRANCH_NAME}"
def branch_cutted = task_branch.contains("origin") ? task_branch.split('/')[1] : task_branch.trim()
currentBuild.displayName = "$branch_cutted"
base_git_url = "https://github.com/Dypose-java/demoApiAndUi.git"

node {
    withEnv(["branch=${branch_cutted}", "base_url=${base_git_url}"]) {
        stage("Checkout Branch") {
            if (!"$branch_cutted".contains("main")) {
                try {
                    getProject("$base_git_url", "$branch_cutted")
                } catch (err) {
                    echo "Failed to checkout branch $branch_cutted"
                    currentBuild.result = 'FAILURE'
                    error("Failed to checkout branch: ${err}")
                }
            } else {
                echo "Current branch is main"
                checkout scm // Для main ветки используем стандартный checkout
            }

            // Проверяем наличие gradlew после checkout
            if (!fileExists('gradlew')) {
                error("gradlew file not found in the project root!")
            }
        }

        stage("Prepare Workspace") {
            sh 'ls -la' // Для отладки - посмотрим содержимое директории
            sh 'chmod +x gradlew' // Даем права на выполнение
        }

        try {
            stage("Run tests") {
                parallel(
                        'Api Tests': {
                            runTestWithTag("API")
                        },
                        'Ui Tests': {
                            sh ".gradle clean test -Dtag=UI -DrunIn=browser_selenoid"
                            //runTestWithTag("UI", "browser_selenoid")
                        }
                )
            }
        } catch (err) {
            echo "Tests execution failed: ${err}"
            currentBuild.result = 'UNSTABLE'
        } finally {
            stage("Allure") {
                generateAllure()
            }
        }
    }
}

def getTestStages(testTags) {
    def stages = [:]
    testTags.each { tag ->
        stages["${tag}"] = {
            runTestWithTag(tag)
        }
    }
    return stages
}

def runTestWithTag(String tag, String runIn = null) {
    try {
        // Проверяем наличие gradlew перед выполнением
        if (!fileExists('gradlew')) {
            error("gradlew file not found when trying to run ${tag} tests!")
        }

        def gradleCommand = "./gradlew clean test -Dtag=${tag}"

        if (runIn) {
            gradleCommand += " -DrunIn=${runIn}"
        }

        sh label: "Run ${tag}" + (runIn ? " (${runIn})" : ""),
                script: gradleCommand

        return true
    } catch (Exception e) {
        echo "Some tests failed for ${tag}: ${e.getMessage()}"
        currentBuild.result = 'UNSTABLE'
        return false
    }
}

def getProject(String repo, String branch) {
    cleanWs()
    checkout([
            $class: 'GitSCM',
            branches: [[name: branch]],
            extensions: [[
                                 $class: 'RelativeTargetDirectory',
                                 relativeTargetDir: 'project'
                         ]],
            userRemoteConfigs: [[url: repo]]
    ])
    dir('project') {
        // Теперь рабочая директория - project
    }
}

def generateAllure() {
    try {
        if (fileExists('build/allure-results')) {
            allure([
                    includeProperties: true,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'build/allure-results']]
            ])
        } else {
            echo "No Allure results found to generate report"
        }
    } catch (err) {
        echo "Failed to generate Allure report: ${err}"
    }
}