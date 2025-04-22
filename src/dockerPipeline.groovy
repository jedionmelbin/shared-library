def call(Map pipelineParams) {
    pipeline {
        agent any

        stages {
            stage("docker build") {
                steps {
                    script {
                        dockerLib.build()
                    }
                }
            }

            stage("docker push") {
                steps {
                    script {
                        dockerLib.push(DockerImage: pipelineParams.dockerImage)
                    }
                }
            }
        }
    }
}