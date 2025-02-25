
def call(String imageName){
    sh "sudo docker build -t ${imageName} ." 
}
