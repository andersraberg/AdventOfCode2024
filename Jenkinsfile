node {
    git 'https://github.com/andersraberg/AdventOfCode2024.git'

    dir('../common-catalog') {
        git 'https://github.com/andersraberg/common-catalog.git'
    }

    stage('Build') {
        sh './gradlew clean build -Pversion=$BUILD_NUMBER --profile --configuration-cache --build-cache'
    }

    stage('Run') {
        sh './gradlew run'
    }

    stage('Code coverage') {
        sh './gradlew jacocoTestReport -Pversion=$BUILD_NUMBER'

        recordCoverage tools: [
            [parser: 'JACOCO', pattern: '**/build/reports/jacoco/test/jacocoTestReport.xml'] 
        ]	

    }

    stage('Sonar') {
        withSonarQubeEnv() {
            sh './gradlew sonar -Dsonar.projectKey=AdventofCode2024 -Pversion=$BUILD_NUMBER'
        }
    }
}
