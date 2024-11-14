def call(Map config = [:]) {  // Acepta un mapa de configuración opcional
    stage('Static Code Analysis') {
        withSonarQubeEnv('sq1') {
            bat 'echo "Ejecución de las pruebas de calidad de código"'
                    }
        
        // Verificar el parámetro abortPipeline si está configurado en true
        if (config.get('abortPipeline', false)) {
            echo "Abortando el pipeline debido a la configuración de abortPipeline"
            currentBuild.result = 'ABORTED'
            error("Pipeline abortado debido a la configuración de abortPipeline")
        }
    }
}
