def call(Map config = [:]) {
    boolean abortPipeline = config.get('abortPipeline', false)

    stage('Static code analysis') {
        withSonarQubeEnv('sq1') {
            bat 'echo "Ejecución de las pruebas de calidad de código"'
        }

        timeout(time: 5, unit: 'MINUTES') {
            if (abortPipeline) {
                echo "Abortando el pipeline debido a la configuración de abortPipeline"
                currentBuild.result = 'ABORTED'
                error("Pipeline abortado debido a la configuración de abortPipeline")                
            } else {
                echo "QualityGate aprobado: continuando con el pipeline"
            }
        }
    }
}