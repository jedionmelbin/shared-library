package com.xprotech.library

def call(Map pipelineParams) {
    pipeline {
        agent any

        stages {
            stage("docker build") {
                steps {
                    script {
                        com.xprotech.library.dockerLib.build()
                    }
                }
            }

            stage("docker push") {
                steps {
                    script {
                        com.xprotech.library.dockerLib.push(DockerImage: pipelineParams.dockerImage)
                    }
                }
            }
        }
    }
}