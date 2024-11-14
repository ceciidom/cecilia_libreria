def call(Map config = [:]) { 
    boolean abortOnQualityGate = config.get('abortOnQualityGate', false)
    boolean abortPipeline = config.get('abortPipeline', false)
    boolean mockQualityGate = config.get('mockQualityGate', false)
    
    stage('Static Code Analysis') {
        withSonarQubeEnv('sq1') {
            bat 'echo "Ejecución de las pruebas de calidad de código"'
        }
        
        timeout(time: 5, unit: 'MINUTES') {
            if(mockQualityGate) {
                echo "Simulando resultado del QualityGate..."
                def simulatedResult = "OK"
                
                if (simulatedResult != "OK" && abortOnQualityGate) {
                    echo "QualityGate fallido: abortando el pipeline"
                    currentBuild.result = 'ABORTED'
                    error("Pipeline abortado debido al fallo en el QualityGate")
                } else {
                    echo "QualityGate aprobado: continuando con el pipeline"
                }

            } else {
                echo "Esperando resultado del QualityGate..."
                def qg = waitForQualityGate()  

                if (qg.status != 'OK' && abortOnQualityGate) {
                    echo "QualityGate fallido: abortando el pipeline"
                    currentBuild.result = 'ABORTED'
                    error("Pipeline abortado debido al fallo en el QualityGate")
                }
            }
        } 

        if (abortPipeline) {
                echo "Abortando el pipeline debido a la configuración de abortPipeline"
                currentBuild.result = 'ABORTED'
                error("Pipeline abortado debido a la configuración de abortPipeline")
            }    
    }
}
