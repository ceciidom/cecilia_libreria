def call(boolean abortPipeline = false) {
    script {
        echo "Ejecución de las pruebas de calidad de código..."
        def scannerHome = tool name: 'SonarQube Scanner 1', type: 'hudson.plugins.sonar.SonarRunnerInstallation'

        withSonarQubeEnv(env.SONARQUBE_SERVER) {
            bat """
            "${scannerHome}\\bin\\sonar-scanner" ^
            -D"sonar.projectKey=Recuperacion_Cecilia" ^
            -D"sonar.sources=%WORKSPACE%\\src" ^
            -D"sonar.host.url=http://192.168.68.123:9000" ^
            -D"sonar.login=sqa_6bf918fb17fd38c86d5da2277086470bf12573da"
            """            
        }
        timeout(time: 10, unit: 'MINUTES') {
            def qualityGate = waitForQualityGate()
            
            if (qualityGate.status != 'OK') {
                error "Build failed due to SonarQube Quality Gate: ${qualityGate.status}"
             }
         }

        echo "El Quality Gate fue exitoso. Continuando con el pipeline..."
    }
}