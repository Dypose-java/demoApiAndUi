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
                    echo "Failed get branch $branch_cutted"
                    throw ("${err}")
                }
            } else {
                echo "Current branch is main"
            }
        }


        try {
            stage("Run tests") {
                parallel(
                        'Api Tests': {
                            runTestWithTag("API")
                        },
                        'Ui Tests': {
                            runTestWithTag("UI","browser_selenoid")
                        }
                )
            }
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
        // Формируем команду Gradle
        def gradleCommand = "./gradlew clean test -Dtag=${tag}"

        // Добавляем параметр runIn если он указан
        if (runIn) {
            gradleCommand += " -DrunIn=${runIn}"
        }

        // Выполняем команду с лейблом
        labelledShell(label: "Run ${tag}" + (runIn ? " (${runIn})" : ""),
                script: """
                    chmod +x gradlew
                    ${gradleCommand}
                    """)

        return true
    } catch (Exception e) {
        echo "Some tests failed for ${tag}: ${e.getMessage()}"
        currentBuild.result = 'UNSTABLE'
        return false
    }
}

def getProject(String repo, String branch) {
    cleanWs()
    checkout scm: [
            $class           : 'GitSCM', branches: [[name: branch]],
            userRemoteConfigs: [[
                                        url: repo
                                ]]
    ]
}

def generateAllure() {
    allure([
            includeProperties: true,
            jdk              : '',
            properties       : [],
            reportBuildPolicy: 'ALWAYS',
            results          : [[path: 'build/allure-results']]
    ])
}
