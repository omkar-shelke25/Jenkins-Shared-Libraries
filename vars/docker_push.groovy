def call(String imageName, String tag) {
    echo "Docker Login and Push The Image In Artifactory"
    
    // Use withCredentials to securely fetch credentials from Jenkins' credentials store
    withCredentials([usernamePassword(credentialsId: 'docker', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        // Login to Docker using the credentials
        sh "docker login -u ${USERNAME} -p ${PASSWORD}"
        
        
        
        // Push the tagged Docker image to Docker Hub or your Artifactory
        sh "docker push ${USERNAME}/${imageName}:${tag}"
    }
}
