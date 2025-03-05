def call() {
    timeout(time: 1, unit: "MINUTES") { // Sets a timeout of 1 minute
        waitForQualityGate abortPipeline: false // Waits for SonarQube Quality Gate
    }
}
