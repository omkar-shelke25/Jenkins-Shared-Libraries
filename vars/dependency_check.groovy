def call() {
    dependencyCheck(
        additionalArguments: '--scan ./ --format ALL --nvdApiKey $NVD_API_KEY',
        odcInstallation: 'OWASP'
    )
    dependencyCheckPublisher(
        pattern: '**/dependency-check-report.xml',
        failedTotalCritical: 0,
        failedTotalHigh: 1,
        unstableTotalMedium: 5,
        defaultExcludes: true
    )
}
