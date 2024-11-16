// Archivo: vars/staticAnalysis.groovy
def call(boolean abortPipeline = false) {
    script {
        echo "Ejecución de las pruebas de calidad de código..."

        // Simulación del análisis estático
        bat """
        echo Simulando análisis estático de código...
        timeout /t 10
        echo Análisis estático completado.
        """

        // Simulación de timeout y resultado de Quality Gate
        timeout(time: 5, unit: 'MINUTES') {
            def qualityGateStatus = "OK" // Cambiar a "FAILED" para simular fallo en el Quality Gate
            echo "Resultado del Quality Gate: ${qualityGateStatus}"

            // Evaluar si se debe abortar el pipeline según el parámetro abortPipeline y el estado del Quality Gate
            if (abortPipeline && qualityGateStatus != "OK") {
                error "Pipeline abortado debido a que el Quality Gate falló y abortPipeline está configurado como true."
            }
        }

        echo "Pipeline continuará ya que el Quality Gate pasó o abortPipeline está configurado como false."
    }
}
