def call(Map config = [:]) {
    boolean mockQualityGate = config.get('mockQualityGate', false)  // Indica si se debe usar el Quality Gate simulado
    String simulatedResult = config.get('simulatedResult', 'OK')   // Resultado simulado del Quality Gate ('OK' o 'FAILED')

    stage('Static Code Analysis') {
        withSonarQubeEnv(env.SONARQUBE_SERVER) { // Reemplaza 'SonarQube' con el ID configurado en Jenkins
            bat """
            docker run --rm -e SONAR_HOST_URL=http://192.168.68.123:9000 -e SONAR_LOGIN=sqa_6bf918fb17fd38c86d5da2277086470bf12573da -v "%cd%:/usr/src" sonarsource/sonar-scanner-cli -Dsonar.projectKey=D_Jenkins_Ejercicio_5 -Dsonar.sources=/usr/src -Dsonar.host.url=http://192.168.68.123:9000 -Dsonar.login=sqa_6bf918fb17fd38c86d5da2277086470bf12573da
            """
        }
    }

    stage('Quality Gate') {
        timeout(time: 5, unit: 'MINUTES') {
            try {
                // Intento de obtener el resultado del Quality Gate real
                def qg = waitForQualityGate()
                
                if (qg.status != 'OK') {
                    echo "Quality Gate fallido: abortando el pipeline"
                    currentBuild.result = 'ABORTED'
                    error("Quality Gate fallido: ${qg.status}")
                } else {
                    echo "Quality Gate passed."
                }
            } catch (Exception e) {
                echo "Error al obtener el resultado del Quality Gate: ${e.message}. Procediendo con Mock Quality Gate"
                mockQualityGate = true
            }

            // Lógica del Mock Quality Gate
            if (mockQualityGate) {
                echo "Simulando resultado del Quality Gate..."
                
                if (simulatedResult != "OK") { // Si el resultado simulado es un fallo
                    echo "Mock Quality Gate fallido: abortando el pipeline"
                    currentBuild.result = 'ABORTED'
                    error("Pipeline abortado debido al fallo del Mock Quality Gate")
                } else {
                    echo "Mock Quality Gate aprobado: continuando con el pipeline"
                }
            }
        }
    }
}

