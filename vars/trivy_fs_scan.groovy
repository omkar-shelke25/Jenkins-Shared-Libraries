def call(){
    sh 'trivy fs --scanners vuln .'
    //sh 'trivy fs .'
    
}


/*def call() {
    try {
        sh 'trivy fs --exit-code 1 --severity CRITICAL .'
       
    } catch (Exception e) {
        echo "Vulnerabilities found! Check the Trivy report."
        currentBuild.result = 'FAILURE'
        error("Build failed due to critical vulnerabilities.")
    }
}
*/
