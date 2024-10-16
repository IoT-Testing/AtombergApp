pipeline {
    environment {
        QODANA_TOKEN = credentials('qodana-token')
    }
    agent {
        docker {
            args '''
                -v "${WORKSPACE}":/data/project
                --entrypoint=""
                '''
            image 'jetbrains/qodana-jvm-community'
        }
    }
    stages {
        stage('Qodana') {
            when {
                branch 'master'
                branch 'hc/fixes'
            }
            steps {
                sh '''qodana'''
            }
        }
    }
}