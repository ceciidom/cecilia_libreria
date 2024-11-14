def call(boolean abortPipeline = false, int timeout = 5) {
    // Iniciar análisis estático de SonarQube
    stage('Static Code Analysis') {
        withSonarQubeEnv('SonarQube') { // Ajusta el nombre del servidor SonarQube si es necesario
            bat 'echo "Ejecución de las pruebas de calidad de código"' // Aquí reemplazamos sh con bat
        }
    }
    
    // Esperar el resultado del quality gate de SonarQube
    stage('Quality Gate') {
        timeout(time: timeout, unit: 'MINUTES') {
            def qualityGate = waitForQualityGate()
            if (qualityGate.status != 'OK') {
                echo "Quality Gate failed: ${qualityGate.status}"
                if (abortPipeline) {
                    error "Pipeline abortado debido a fallo en el Quality Gate."
                }
            } else {
                echo "Quality Gate passed."
            }
        }
    }
}
