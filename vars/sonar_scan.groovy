def call(String projectKey, String sonarHost, String sonarToken) {
    withSonarQubeEnv('SonarQube') { // Ensure 'SonarQube' matches Jenkins configuration
        sh """
            sonar-scanner \
            -Dsonar.projectKey=${projectKey} \
            -Dsonar.sources=. \
            -Dsonar.host.url=${sonarHost} \
            -Dsonar.login=${sonarToken}
        """
    }
}
