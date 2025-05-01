node {
    git 'https://github.com/andersraberg/AdventOfCode2024.git'
    stage('Build') {
        sh './gradlew clean build -Pversion=$BUILD_NUMBER --profile --configuration-cache --build-cache'
    }

    stage('Run') {
        sh './gradlew run'
    }

    stage('Code coverage') {
        sh './gradlew jacocoTestReport -Pversion=$BUILD_NUMBER'
        recordCoverage tools: [jacoco(pattern: 'build/reports/jacoco/test/jacocoTestReport.xml')]
    }

    stage('Sonar') {
        withSonarQubeEnv() {
            sh './gradlew sonar -Dsonar.projectKey=AdventofCode2024 -Pversion=$BUILD_NUMBER'
        }
    }

    stage('Report') {
        junit 'build/test-results/**/*.xml'
        sh 'mv build/reports/profile/*.html build/reports/profile/index.html'
        publishHTML([allowMissing: false,
                     alwaysLinkToLastBuild: false,
                     keepAll: true,
                     reportDir: 'build/reports/profile/',
                     reportFiles: 'index.html',
                     reportName: 'Gradle Build Profile'])
    }
    
}
