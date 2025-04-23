def call(String mavenWrapper = './mvnw') {
    // 1. Obtener versión actual
    def versionOutput = sh(
            script: "${mavenWrapper} -q -Dexec.executable='echo' " +
                    "-Dexec.args='\\\${project.version}' --non-recursive exec:exec",
            returnStdout: true
    ).trim()
    echo "Version Output: ${versionOutput}"

    // 2. Parsear con regex
    def matcher = versionOutput =~ /(\d+)\.(\d+)\.(\d+)(?:-(\w+))?/
    if (!matcher) {
        error "No se pudo parsear la versión: ${versionOutput}"
    }

    // 3. Incrementar patch y rotar si hace falta
    def major = matcher[0][1].toInteger()
    def minor = matcher[0][2].toInteger()
    def patch = matcher[0][3].toInteger() + 1
    def suffix = matcher[0][4] ?: 'SNAPSHOT'

    if (patch > 10) {
        patch = 0
        minor++
    }
    if (minor > 10) {
        minor = 0
        major++
    }
    if (major > 10) {
        error "Reached maximum version 10.10.10"
    }

    // 4. Exportar a env
    env.MAJOR_VERSION       = major.toString()
    env.MINOR_VERSION       = minor.toString()
    env.INCREMENTAL_VERSION = patch.toString()
    env.BUILD_SUFFIX        = suffix
    env.IMAGE_VERSION       = "${major}.${minor}.${patch}"

    echo "Nueva IMAGE_VERSION = ${env.IMAGE_VERSION}"
}
