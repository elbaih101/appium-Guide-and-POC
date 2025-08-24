pipeline {
    agent {
        label "${slave}"
    }

    options {
        skipDefaultCheckout true
        timeout(time: 30, unit: 'MINUTES')
    }

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('Clone Project') {
            steps {
                checkout([
                        $class                           : 'GitSCM',
                        branches                         : [[name: "${gitlabBranch}"]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions                       : [
                                [$class             : 'SubmoduleOption',
                                 disableSubmodules  : false,
                                 parentCredentials  : true,
                                 recursiveSubmodules: true,
                                 reference          : '',
                                 trackingSubmodules : false]
                        ],
                        userRemoteConfigs                : [[
                                                                    url          : 'git@git.vf-eg.internal.vodafone.com:V25MHanafy2/appium-guide-and-poc.git',
                                                                    credentialsId: '7b6d439f-1c79-416a-9192-eede38b0af48'
                                                            ]]
                ])
            }
        }

        stage('Start Emulator and Logcat') {
            steps {
                bat """
                Stop-Process -Name "qemu-system-x86_64" -Force -ErrorAction SilentlyContinue
                Stop-Process -Name "adb" -Force -ErrorAction SilentlyContinue
                Stop-Process -Name "node" -Force -ErrorAction SilentlyContinue
                Stop-Process -Name "emulator" -Force -ErrorAction SilentlyContinue
                 
                emulator -list-avds
                             
                Start-Process emulator -ArgumentList @(
                    "-avd", ${avdName},
                    "-wipe-data",
                    "-no-window",
                    "-gpu", "swiftshader_indirect",
                    "-memory", "18000",
                    "-cores", "14",
                    "-no-boot-anim",
                    "-no-audio",
                    "-no-snapshot",
                    "-no-snapshot-save",
                    "-prop", "persist.sys.language=en",
                    "-prop", "persist.sys.country=US"
                ) -NoNewWindow
                 
                Start-Sleep -Seconds 20
                                 
                # Wait for adb to connect
                adb wait-for-device
                 
                # Wait for boot to complete
                while ((adb shell getprop sys.boot_completed).Trim() -ne "1") {
                    Start-Sleep -Seconds 2
                }
                 
                Write-Output "Emulator booted."
                 
                # Extra wait for system services
                Start-Sleep -Seconds 20

            """
            }
        }


        stage('Run the mvn install') {
            parallel {
                stage('Run Maven') {
                    steps {

                        catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {

                            echo 'Starting maven build'
                            dir('Appium') {

                                script {
                                    //  def androidVersion = params.androidVersion.split(':')[0]
                                    //  def avdName = params.androidVersion.split(': ')[1].trim()

                                    wrap([$class: "MaskPasswordsBuildWrapper", varPasswordPairs: [[password: ntPassword]]]) {
                                        wrap([$class: 'BuildUser']) {
                                            bat """
                                    
                                    adb shell settings put global http_proxy ""
                                    set http_proxy=http://10.230.189.34:3128
								    set https_proxy=http://10.230.189.34:3128
									echo %http_proxy%

                                    // mvn -DENVIRONMENT=Production -DREMOTE=yes -DNT_ACCOUNT=${BUILD_USER_ID} -DCVM_NT_ACCOUNT=${BUILD_USER_ID} -DCVM_PASSWORD="${ntPassword}" -DPASSWORD="${ntPassword}"  -DANDROID_VERSION=11 -DAVD_NAME=test clean test  
                                    
                                    mvn -DENVIRONMENT=Production -DREMOTE=yes \
                                    -DNT_ACCOUNT=${BUILD_USER_ID} \
                                    -DCVM_NT_ACCOUNT=${BUILD_USER_ID} \
                                    -DCVM_PASSWORD="${ntPassword}" \
                                    -DPASSWORD="${ntPassword}" \
                                    -DANDROID_VERSION=11 \
                                    -DAVD_NAME=test \
                                    clean test
                                    """
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        stage('Stop Logcat') {
            steps {
                bat """
        taskkill /IM adb.exe /F
        """
            }
        }

    }

    post {

        always {


            catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                bat """
                    taskkill /IM qemu-system-x86_64.exe /F
                    taskkill /IM emulator.exe /F
                    taskkill /IM adb.exe /F
                    taskkill /IM node.exe /F
                    """
            }
        }
        success {
            script {
                allure([
                        includeProperties: false,
                        jdk              : '',
                        properties       : [],
                        reportBuildPolicy: 'ALWAYS',
                        results          : [[path: 'appium-Guide-and-POC/allure-results']]
                ])

                bat 'allure generate appium-Guide-and-POC/allure-results --single-file -o reports/allure'

                powershell '''
                    Compress-Archive -Path reports/allure -DestinationPath allure.zip -Force
                '''

                archiveArtifacts artifacts: 'allure.zip', fingerprint: true

                echo 'Sending success email with Allure report'

                emailext attachLog: true,
                        attachmentsPattern: 'allure.zip',
                        body: """${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}<br>
                        More info at: ${env.BUILD_URL}<br>
                        Allure Report: ${env.BUILD_URL}allure/""",
                        to: "${emailReceiver}",
                        subject: "✅ Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
                        mimeType: 'text/html'
            }
        }

        failure {
            script {
                allure([
                        includeProperties: false,
                        jdk              : '',
                        properties       : [],
                        reportBuildPolicy: 'ALWAYS',
                        results          : [[path: 'appium-Guide-and-POC/allure-results']]
                ])

                bat 'allure generate appium-Guide-and-POC/allure-results --single-file -o reports/allure'

                powershell '''
                    Compress-Archive -Path reports/allure -DestinationPath allure.zip -Force
                '''

                archiveArtifacts artifacts: 'allure.zip', fingerprint: true

                echo 'Sending failure email with Allure report'

                emailext attachLog: true,
                        attachmentsPattern: 'allure.zip',
                        body: """${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}<br>
                        More info at: ${env.BUILD_URL}<br>
                        Allure Report: ${env.BUILD_URL}allure/""",
                        to: "${emailReceiver}",
                        subject: "❌ Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
                        mimeType: 'text/html'
            }
        }
    }

}
