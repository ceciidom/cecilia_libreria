// Archivo: vars/staticAnalysis.groovy
def call(boolean abortPipeline = false) {
    script {
        echo "Ejecución de las pruebas de calidad de código..."

        // Simulación del análisis estático
        bat """
        echo Simulando análisis estático de código...
        ping 127.0.0.1 -n 10 >nul
        echo Análisis estático completado.
        """

        // Simulación de timeout y resultado de Quality Gate
        timeout(time: 5, unit: 'MINUTES') {
            def qualityGateStatus = abortPipeline ? 'ERROR' : 'OK'// Cambiar a "FAILED" para simular fallo en el Quality Gate
            echo "Resultado del Quality Gate: ${qualityGateStatus}"

            // Evaluar si se debe abortar el pipeline según el parámetro abortPipeline y el estado del Quality Gate
            if (qualityGateStatus != "OK") {
                error "El Quality Gate falló y el pipeline será abortado porque abortPipeline está configurado como ${abortPipeline}"            }
        }

        echo "El Quality Gate fue exitoso. Continuando con el pipeline..."
    }
}
