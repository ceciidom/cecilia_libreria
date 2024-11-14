// Archivo: vars/staticAnalysis.groovy
def call() {
    stage('Static Code Analysis') {
        withSonarQubeEnv('SonarQube') { // Ajusta 'SonarQube' al ID de tu servidor en Jenkins
            // Usa bat para Windows y sh para Linux
            bat 'echo "Ejecución de las pruebas de calidad de código"' // o usa sh si estás en Linux
        }
    }
}
