import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EvaluadorOptimo extends Evaluador {
    // Ponderaciones para cada rasgo de evaluación
    public float PONDERACION_FICHAS_EN_FILA;
    public float PONDERACION_BLOQUEOS;
    public  float PONDERACION_OCUPACION_CENTRO;
    public  float PONDERACION_OCUPACION_ALTURA;

    public EvaluadorOptimo() {
        leerPesosDesdeArchivo("mejores_pesos.txt");
    }
    private void leerPesosDesdeArchivo(String archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("Ponderación Fichas en Fila:")) {
                    PONDERACION_FICHAS_EN_FILA = Float.parseFloat(linea.split(": ")[1]);
                } else if (linea.startsWith("Ponderación Bloqueos:")) {
                    PONDERACION_BLOQUEOS = Float.parseFloat(linea.split(": ")[1]);
                } else if (linea.startsWith("Ponderación Ocupación Centro:")) {
                    PONDERACION_OCUPACION_CENTRO = Float.parseFloat(linea.split(": ")[1]);
                } else if (linea.startsWith("Ponderación Ocupación Altura:")) {
                    PONDERACION_OCUPACION_ALTURA = Float.parseFloat(linea.split(": ")[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int valoracion(Tablero tablero, int jugador) {
        Double valoracionTotal = 0.0;

        int fichasEnFila = evaluarFichasEnFilaDe3(tablero, jugador);
        valoracionTotal += fichasEnFila * PONDERACION_FICHAS_EN_FILA;

        int bloqueos = evaluarBloqueos(tablero, jugador);
        valoracionTotal += bloqueos * PONDERACION_BLOQUEOS;

        int ocupacionCentro = evaluarOcupacionCentro(tablero, jugador);
        valoracionTotal += ocupacionCentro * PONDERACION_OCUPACION_CENTRO;

        int ocupacionAltura = evaluarOcupacionAltura(tablero, jugador);
        valoracionTotal += ocupacionAltura * PONDERACION_OCUPACION_ALTURA;

        return valoracionTotal.intValue();
    }

    private int evaluarFichasEnFilaDe3(Tablero tablero, int jugador) {
        int valorheurístico = tablero.contarFilasDeTres(jugador) *100;
        return valorheurístico;
    }

    private int evaluarBloqueos(Tablero tablero, int jugador) {
        int valorheurístico = tablero.contarBloqueos(tablero, jugador) * -100;
        return valorheurístico;
    }

    private int evaluarOcupacionCentro(Tablero tablero, int jugador) {
        int[] columnas = {2, 3, 4};
        int valorheurístico =  tablero.contarFichasEnColumnas(jugador,columnas) * 100;
        return valorheurístico; 
    }

    private int evaluarOcupacionAltura(Tablero tablero, int jugador) {
        int[] filas = {0, 1, 2, 3};
        int valorheurístico = tablero.contarFichasEnFilas(jugador,filas) * 100;
        return valorheurístico;
    }
}
