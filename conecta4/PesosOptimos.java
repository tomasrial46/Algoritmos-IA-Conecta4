import java.io.FileWriter;
import java.io.IOException;

public class PesosOptimos {

    public static void main(String[] args) {
        // Inicializar los mejores pesos 
        int[] mejoresPesos = new int[4];
        mejoresPesos[0] = 1;
        mejoresPesos[1] = 1;
        mejoresPesos[2] = 1;
        mejoresPesos[3] = 1;

        // Probar cada combinación de pesos dentro del rango dado
        for (int pesoFichasEnFila1 = 1; pesoFichasEnFila1 <= 4; pesoFichasEnFila1++) {
            for (int pesoBloqueos1 = 1; pesoBloqueos1 <= 4; pesoBloqueos1++) {
                for (int pesoOcupacionCentro1 = 1; pesoOcupacionCentro1 <= 4; pesoOcupacionCentro1++) {
                    for (int pesoOcupacionAltura1 = 1; pesoOcupacionAltura1 <= 4; pesoOcupacionAltura1++) {                
                        // Asignar pesos a los evaluadores
                        EvaluadorOptimo evaluador1 = new EvaluadorOptimo();
                        int[] pesos = {pesoFichasEnFila1, pesoBloqueos1, pesoOcupacionCentro1, pesoOcupacionAltura1};
                        cambiarPesos(evaluador1, pesos);
                        EvaluadorOptimo evaluador2 = new EvaluadorOptimo();
                        int[] pesos2 = {mejoresPesos[0], mejoresPesos[1], mejoresPesos[2], mejoresPesos[3]};
                        cambiarPesos(evaluador2, pesos2);
                        // Jugar partidas y obtener el puntaje promedio
                        int[] resultados = compararEvaluadores(evaluador1, evaluador2, 4);
                        int numGanadas = resultados[0];
                        int numEmpates = resultados[1];
                        int numPerdidas = resultados[2];
                        // Actualizar los mejores pesos si se encuentra una combinación con un puntaje más alto
                        if (numGanadas > numPerdidas) {
                            mejoresPesos[0] = pesoFichasEnFila1;
                            mejoresPesos[1] = pesoBloqueos1;
                            mejoresPesos[2] = pesoOcupacionCentro1;
                            mejoresPesos[3] = pesoOcupacionAltura1;
                        }
                    }
                }
            }
        }
                    
                
            
        

        // Guardar los mejores pesos en un archivo de texto
        guardarMejoresPesos(mejoresPesos);
    }

    public static int[] compararEvaluadores(Evaluador evaluador1, Evaluador evaluador2, int capaMaxima) {
        int numGanadas = 0;
        int numEmpates = 0;
        int numPerdidas = 0;

        // Crear jugadores y tablero para cada partida
        Jugador jugador1 = new Jugador(1);
        EstrategiaMiniMax estrategia1 = new EstrategiaMiniMax(evaluador1, capaMaxima);
        jugador1.establecerEstrategia(estrategia1);

        Jugador jugador2 = new Jugador(2);
        EstrategiaMiniMax estrategia2 = new EstrategiaMiniMax(evaluador2, capaMaxima);
        jugador2.establecerEstrategia(estrategia2);

        Tablero tablero = new Tablero();
        tablero.inicializar();

        // Jugar la partida
        Conecta4.jugar(jugador1, jugador2, tablero);

        // Determinar el resultado de la partida
        if (tablero.hayEmpate()) {
            numEmpates += 1; 
        } else if (tablero.ganaJ1()) {
            numGanadas += 1; 
        } else if (tablero.ganaJ2()) {
            numPerdidas += 1; 
        }
    
        tablero.inicializar();

        // Jugar la partida con orden inverso
        Conecta4.jugar(jugador2, jugador1, tablero);

        // Determinar el resultado de la partida
        if (tablero.hayEmpate()) {
            numEmpates += 1; 
        } else if (tablero.ganaJ1()) {
            numGanadas += 1; 
        } else if (tablero.ganaJ2()) {
            numPerdidas += 1; 
        }
        System.out.println(estrategia1.getNodos());
        return new int[]{numGanadas, numEmpates, numPerdidas};
    }

    public static void cambiarPesos(EvaluadorOptimo evaluador, int[] pesos) {
        evaluador.PONDERACION_FICHAS_EN_FILA = pesos[0];
        evaluador.PONDERACION_BLOQUEOS = pesos[1];
        evaluador.PONDERACION_OCUPACION_CENTRO = pesos[2];
        evaluador.PONDERACION_OCUPACION_ALTURA = pesos[3];
    }

    private static void guardarMejoresPesos(int[] mejoresPesos) {
        try {
            FileWriter writer = new FileWriter("mejores_pesos.txt");
            writer.write("Mejores pesos encontrados:\n");
            writer.write("Ponderación Fichas en Fila: " + mejoresPesos[0] + "\n");
            writer.write("Ponderación Bloqueos: " + mejoresPesos[1] + "\n");
            writer.write("Ponderación Ocupación Centro: " + mejoresPesos[2] + "\n");
            writer.write("Ponderación Ocupación Altura: " + mejoresPesos[3] + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
