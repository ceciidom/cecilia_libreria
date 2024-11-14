// Archivo: vars/sonarAnalysis.groovy
// Archivo: vars/sonarAnalysis.groovy
def call(Map config = [:]) {
    boolean mockQualityGate = config.get('mockQualityGate', false)  // Simulación de QualityGate
    String simulatedResult = config.get('simulatedResult', 'OK')   // Resultado simulado del QualityGate
    String sonarServer = config.get('sonarServer', 'sq1')          // Nombre del servidor SonarQube

    stage('Static Code Analysis') {
        withSonarQubeEnv(sonarServer) {
            // Ejecutar comando de análisis, adaptado a sistemas UNIX o Windows
            isUnix() ? sh 'echo "Ejecución de las pruebas de calidad de código"' : bat 'echo "Ejecución de las pruebas de calidad de código"'
        }

        timeout(time: 5, unit: 'MINUTES') {
            if (mockQualityGate) { // Si está activado, simula el resultado del QualityGate
                echo "Simulando resultado del QualityGate..."
                if (simulatedResult != "OK") { // Si el resultado simulado es un fallo
                    echo "QualityGate fallido: abortando el pipeline"
                    currentBuild.result = 'ABORTED'
                    error("Pipeline abortado debido al fallo MOCK QualityGate")
                } else {
                    echo "MOCK QualityGate aprobado: continuando con el pipeline"
                }
            } else {
                echo "Esperando resultado REAL del QualityGate..."
                try {
                    def qg = waitForQualityGate()
                    if (qg.status != 'OK') {
                        echo "QualityGate fallido: abortando el pipeline"
                        currentBuild.result = 'ABORTED'
                        error("Pipeline abortado debido al fallo en el QualityGate")
                    }
                } catch (Exception e) {
                    echo "Error al obtener el resultado del QualityGate: ${e.message}"
                    currentBuild.result = 'FAILURE'
                    error("Pipeline abortado debido a un error en el QualityGate")
                }
            }
        }
    }
}
