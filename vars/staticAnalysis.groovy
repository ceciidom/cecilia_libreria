def call(boolean abortPipeline = false, int timeout = 5) {
    // Iniciar análisis estático de SonarQube
    stage('Static Code Analysis') {
        withSonarQubeEnv('SonarQube') { // Asumiendo que el servidor SonarQube está configurado en Jenkins
            sh 'echo "Ejecución de las pruebas de calidad de código"' // Placeholder para el análisis real
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
