package com.xprotech.library

@Library('shared-library') _

pipeline {
    agent any

    stages {
        stage('docker build') {
            steps {
                script {
                    com.xprotech.library.dockerLib.build(DockerfilePath: "image/Dockerfile",
                            DockerImage: "image/image:1.0.0-${BUILD_ID}",
                            DockerContext: "02-first-pipeline")
                }
            }
        }
        stage('docker push') {
            steps {
                script {
                    com.xprotech.library.dockerLib.push(DockerImage: "image/image:1.0.0-${BUILD_ID}")
                }
            }
        }
    }
}