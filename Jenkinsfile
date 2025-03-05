@Library("Sharable") _

pipeline {
    agent { label 'jenkins-agent' }

    environment {
        NVD_API_KEY = "${env.NVD_API_KEY}" 
        SONAR_TOKEN = credentials('sonar-token') 
        ARTIFACTORY_CREDENTIALS = credentials('artifactory-creds') 
        DOCKER_CREDENTIALS = credentials('docker-hub') 
        SLACK_WEBHOOK = credentials('slack-webhook') 
    }

    options {
        timeout(time: 40, unit: 'MINUTES') 
        buildDiscarder(logRotator(numToKeepStr: '10')) 
        ansiColor('xterm') 
    }

    stages {
        stage("Checkout The Code") {
            steps {
                script {
                    checkout scm
                }
            }
        }

        stage("Code Quality: Linting") {
            steps {
                script {
                    echo "Running ESLint..."
                    sh "npm run lint"
                }
            }
        }

        stage("Dependency Installation") {
            steps {
                script {
                    sh "npm install"
                }
            }
        }

        stage("Security Scan: Trivy") {
            steps {
                script {
                    trivy_fs_scan()
                }
            }
        }

        stage("Code Quality: SonarQube") {
            steps {
                script {
                    sonar("Sonar", "travel-blog", "travel-blog")
                }
            }
        }

        stage("SonarQube: Quality Gate") {
            steps {
                script {
                    sonar_QualityGate()
                }
            }
        }

        stage("Run Unit Tests") {
            steps {
                script {
                    echo "Running Unit Tests..."
                    sh "npm test -- --testPathPattern=backend/tests/unit/controllers --coverage --reporters=jest-junit"
                    junit 'jest-junit.xml'
                }
            }
        }

        stage("Run Integration Tests") {
            steps {
                script {
                    echo "Running Integration Tests..."
                    sh "npm test -- --testPathPattern=backend/tests/integration/controllers --coverage --reporters=jest-junit"
                    junit 'jest-junit.xml'
                }
            }
        }

        stage("Database Migrations") {
            steps {
                script {
                    echo "Running Database Migrations..."
                    sh "npm run migrate" // Use Flyway/Liquibase if applicable
                }
            }
        }

        stage("Performance Testing") {
            steps {
                script {
                    echo "Running Performance Tests..."
                    sh "k6 run load-test.js" // Example using k6
                }
            }
        }

        stage("E2E Testing") {
            steps {
                script {
                    echo "Running E2E Tests..."
                    sh "npx cypress run" // Example using Cypress
                }
            }
        }

        stage("Build Application") {
            steps {
                script {
                    echo "Building the application..."
                    sh "npm run build"
                }
            }
        }

        stage("Containerization: Build & Push Docker Image") {
            steps {
                script {
                    echo "Building Docker Image..."
                    sh """
                        docker build -t my-app:latest .
                        echo $DOCKER_CREDENTIALS | docker login -u my-user --password-stdin
                        docker tag my-app:latest my-dockerhub-repo/my-app:latest
                        docker push my-dockerhub-repo/my-app:latest
                    """
                }
            }
        }

        stage("Deploy to Staging") {
            steps {
                script {
                    echo "Deploying to Staging..."
                    sh "./deploy.sh staging"
                }
            }
        }

        stage("Deploy to Production") {
            steps {
                script {
                    echo "Deploying to Production..."
                    sh "./deploy.sh production"
                }
            }
        }

        stage("Post-Deployment Health Check") {
            steps {
                script {
                    echo "Running Health Check..."
                    sh "curl -f http://my-app.com/health || exit 1"
                }
            }
        }

        stage("Rollback on Failure") {
            when {
                expression { currentBuild.result == 'FAILURE' }
            }
            steps {
                script {
                    echo "Rolling back to previous version..."
                    sh "./rollback.sh"
                }
            }
        }
    }

    post {
        always {
            script {
                node {
                    echo 'Cleaning up workspace...'
                    cleanWs()
                }
            }
        }
        success {
            echo 'Pipeline executed successfully!'
            sh "curl -X POST -H 'Content-Type: application/json' --data '{\"text\": \"✅ Jenkins Build Success!\"}' $SLACK_WEBHOOK"
        }
        failure {
            script {
                echo 'Pipeline failed! Sending notifications...'
                sh "curl -X POST -H 'Content-Type: application/json' --data '{\"text\": \"❌ Jenkins Build Failed!\"}' $SLACK_WEBHOOK"
            }
        }
    }
}
