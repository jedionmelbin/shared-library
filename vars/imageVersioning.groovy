#!/usr/bin/env groovy

def call(String projectDir = '.', String baseVersion = '1.0.0') {
    dir(projectDir) {
        def versionOutput = sh(
                script: './mvnw -q -Dexec.executable="echo" -Dexec.args=\'${project.version}\' --non-recursive exec:exec',
                returnStdout: true
        ).trim()

        echo "Versión actual del proyecto Maven: ${versionOutput}"

        def matcher = versionOutput =~ /(\d+)\.(\d+)\.(\d+)(?:-(\w+))?/
        if (!matcher) {
            echo "No se detectó versión válida en el pom.xml, usando base ${baseVersion}"
            matcher = baseVersion =~ /(\d+)\.(\d+)\.(\d+)/
        }

        def (major, minor, patch) = [matcher[0][1], matcher[0][2], matcher[0][3]].collect { it.toInteger() }
        patch++
        if (patch > 10) {
            patch = 0; minor++
        }
        if (minor > 10) {
            minor = 0; major++
        }

        def imageVersion = "${major}.${minor}.${patch}"
        def buildSuffix = matcher[0][4] ?: 'SNAPSHOT'

        // Asigna variables globales accesibles desde el pipeline
        env.IMAGE_VERSION = imageVersion
        env.BUILD_SUFFIX = buildSuffix
        env.ECR_VERSION = "${env.IMAGE_VERSION}-${env.BUILD_NUMBER}"

        echo """
         Versión de imagen calculada:
           - IMAGE_VERSION = ${env.IMAGE_VERSION}
           - BUILD_SUFFIX  = ${env.BUILD_SUFFIX}
           - ECR_VERSION   = ${env.ECR_VERSION}
        """
    }
}
