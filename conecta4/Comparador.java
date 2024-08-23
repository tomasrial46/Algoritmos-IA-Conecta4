public class Comparador {
    public static void main(String[] args) {
        EvaluadorOptimo evaluadorOptimo = new EvaluadorOptimo();
        EvaluadorOptimo evaluadorInicial = new EvaluadorOptimo();
        int[] pesos = {1, 1, 1, 1};
        PesosOptimos.cambiarPesos(evaluadorInicial, pesos);
        EvaluadorAleatorio evaluadorAleatorio = new EvaluadorAleatorio();

        int[] profundidades = {2, 3, 4, 5};
        for (int profundidad : profundidades) {
            System.out.println("Profundidad Máxima de Búsqueda: " + profundidad);

            // Comparación entre Heurística Óptima y Heurística Inicial
            System.out.println("Optimo vs Inicial:");
            int[] resultadosOI = PesosOptimos.compararEvaluadores(evaluadorOptimo, evaluadorInicial, profundidad);
            System.out.println("Ganadas: " + resultadosOI[0] + ", Empates: " + resultadosOI[1] + ", Perdidos: " + resultadosOI[2]);

            // Comparación entre Heurística Óptima y Heurística Aleatoria
            System.out.println("Optimo vs Aleatorio:");
            int[] resultadosOA = PesosOptimos.compararEvaluadores(evaluadorOptimo, evaluadorAleatorio, profundidad);
            System.out.println("Ganadas: " + resultadosOA[0] + ", Empates: " + resultadosOA[1] + ", Perdidos: " + resultadosOA[2]);

            // Comparación entre Heurística Inicial y Heurística Aleatoria
            System.out.println("Inicial vs Aleatorio:");
            int[] resultadosIA = PesosOptimos.compararEvaluadores(evaluadorInicial, evaluadorAleatorio, profundidad);
            System.out.println("Ganadas: " + resultadosIA[0] + ", Empates: " + resultadosIA[1] + ", Perdidos: " + resultadosIA[2]);
        }
    }
}
