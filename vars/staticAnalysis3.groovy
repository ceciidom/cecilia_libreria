def call(Map config = [:]) {
    // Obtiene el valor de abortPipeline y el nombre de la rama de la variable de entorno
    boolean abortPipeline = config.get('abortPipeline', false)
    String branchName = env.BRANCH_NAME ?: 'unknown'  // Asigna "unknown" si no se encuentra la variable

    stage('Static code analysis') {
        withSonarQubeEnv('sq1') {
            // Ejecutar el análisis de código (usa 'bat' para Windows o 'sh' para UNIX)
            bat 'echo "Ejecución de las pruebas de calidad de código"'
        }

        timeout(time: 5, unit: 'MINUTES') {
            if (abortPipeline) {
                echo "Abortando el pipeline debido a la configuración de abortPipeline"
                currentBuild.result = 'ABORTED'
                error("Pipeline abortado debido a la configuración de abortPipeline")
            } else if (branchName == 'master') {
                echo "Abortando el pipeline debido a que la rama es 'master'"
                currentBuild.result = 'ABORTED'
                error("Pipeline abortado debido a la rama 'master'")
            } else if (branchName.startsWith('hotfix')) {
                echo "Abortando el pipeline debido a que la rama comienza con 'hotfix'"
                currentBuild.result = 'ABORTED'
                error("Pipeline abortado debido a la rama 'hotfix'")
            } else {
                // Si no se cumple ninguna condición para abortar, continuar
                echo "QualityGate aprobado en la rama '${branchName}': continuando con el pipeline"
            }
        }
    }
}
