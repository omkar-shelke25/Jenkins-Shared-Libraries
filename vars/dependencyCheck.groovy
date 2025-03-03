def call() {
    dependencyCheck(
        additionalArguments: '--scan ./ --format ALL',
        odcInstallation: 'OWASP-Dependency-Check'
    )
    dependencyCheckPublisher(
        pattern: '**/dependency-check-report.xml',
        failedTotalCritical: 0,
        failedTotalHigh: 1,
        unstableTotalMedium: 5,
        defaultExcludes: true
    )
}
