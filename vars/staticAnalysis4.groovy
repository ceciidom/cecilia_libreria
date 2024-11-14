def call(Map config = [:]) {
    boolean mockQualityGate = config.get('mockQualityGate', false)  // Simulación de QualityGate
    String simulatedResult = config.get('simulatedResult', 'OK')   // Resultado simulado del QualityGate

    stage('Static Code Analysis') {
        withSonarQubeEnv(env.SONARQUBE_SERVER) {
            bat """
            docker run --rm -e SONAR_HOST_URL=http://192.168.68.123:9000 -e SONAR_LOGIN=sqa_6bf918fb17fd38c86d5da2277086470bf12573da -v "%cd%:/usr/src" sonarsource/sonar-scanner-cli -Dsonar.projectKey=prueba2_jenkins -Dsonar.sources=/usr/src -Dsonar.host.url=http://192.168.68.123:9000 -Dsonar.login=sqa_6bf918fb17fd38c86d5da2277086470bf12573da
                                """
        }
    }
    stage('Quality gate') {
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
                    echo "Quality Gate passed."

                    if (qg.status != 'OK') {
                        echo "QualityGate fallido: abortando el pipeline"
                        currentBuild.result = 'ABORTED'
                        error("QualityGate fallido: ${qualityGate.status}")
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

