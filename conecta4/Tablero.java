import java.util.Vector;
import java.io.*;

public class Tablero {
    
    public static final int NFILAS = 6;
    public static final int NCOLUMNAS = 7;
    public static final int NOBJETIVO = 4;
    
    private static final String MARCA_J1 = "X";
    private static final String MARCA_J2 = "O";    
    private static final String MARCA_VACIO = " ";
    private static final String[] MARCAS = {MARCA_VACIO, MARCA_J1, MARCA_J2};
    
    private static final int VACIO = 0;
    private static final int JUGADOR1 = 1;    
    private static final int JUGADOR2 = 2;
    private static final int EMPATE = -1;
    
    private int[][] _casillas;
    private int[] _posicionLibre;    
    private int _ganador = EMPATE;
    
    
    /** Creates a new instance of Tablero */
    public Tablero() {
        this._casillas = new int[NCOLUMNAS][NFILAS];
        this._posicionLibre = new int[NCOLUMNAS];
        this.inicializar();
    }
    
    protected Object clone() {
        Tablero result = new Tablero();
        result.copiarCasillas(this._casillas);
        result.copiarPosicionLibre(this._posicionLibre);
        return(result);        
    }
    
    protected void finalize() {
        this._casillas = null;
        this._posicionLibre = null;
    }
    
    public boolean equals(Object obj) {
        int col, fila;
        
        Tablero tablero = (Tablero) obj;
        return(tablero.casillasIguales(_casillas));
    }
    
    public String toString() {
        StringBuilder result = new StringBuilder();
        
        // Color ANSI para cada jugador
        String colorX = "\u001B[36;1m"; // Azul para "X"
        String colorO = "\u001B[35;1m"; // Rosa para "O"
        String resetColor = "\u001B[0m"; // Color default
        
        for (int fila = NFILAS - 1; fila >= 0; fila--) {
            result.append("|");
            for (int col = 0; col < NCOLUMNAS; col++) {
                int casilla = _casillas[col][fila];
                String marca = MARCAS[casilla];
                if (marca.equals(MARCA_J1)) {
                    result.append(colorX).append(marca).append(resetColor).append("|");
                } else if (marca.equals(MARCA_J2)) {
                    result.append(colorO).append(marca).append(resetColor).append("|");
                } else {
                    result.append(marca).append("|");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }
    
    
    public boolean[] columnasLibres() {
        boolean [] result = new boolean[NCOLUMNAS];
        int col;
        
        for (col =0; col < NCOLUMNAS; col++){
           result[col] = (_posicionLibre[col]< NFILAS);
        }
        return(result);
    }
    
    boolean esFinal() {
        return(_ganador != 0);
    }
    
   /* Función que chequea el estado del tablero para comprobar
    * si es una posicion final y tomar nota de ello en el atributo
    * privado _ganador
    * 
    * Debe ser llamada una vez creado el tablero, porque las demas
    * funciones consultarán la variable _ganador (en lugar de reevaluar
    * el tablero de nuevo cada vez )
    */
    
    public void obtenerGanador() {
        int col, fila, jugador;
                
        _ganador = 0; // no hay ganador
        for (col=0; col < NCOLUMNAS; col++) {
            for (fila=0; fila < NFILAS; fila++) {
                jugador = _casillas[col][fila];
                if (jugador != VACIO) {
                    if (hayLineaVertical(col,fila,jugador) ||
                        hayLineaHorizontal(col,fila,jugador) ||
                        hayLineaDiagonal(col,fila,jugador)) {
                            _ganador = jugador;
                            return;
                        }
                }
            }
        }
        // no gana ninguno de los dos
        // comprobar si hay empate (columnas agotadas)
        boolean empate=true;
        for (col=0; col < NCOLUMNAS; col++) {
          empate = empate && (_posicionLibre[col] == NFILAS);  
        }
        if (empate) {
            _ganador = EMPATE;
        }
    }
    
    void inicializar() {
        int col, fila;
        
        for (col=0; col < NCOLUMNAS; col++) {
            for (fila=0; fila < NFILAS; fila++) {
                _casillas[col][fila] = VACIO;
            }
            _posicionLibre[col] = 0;
        }
        _ganador=0;
    }
    
    public void mostrar() {
        int col;
        
        System.out.println();
        System.out.print(this.toString());
        
        // Solo para ponerlo bonito
        System.out.print("_");
        for (col=0; col < NCOLUMNAS; col++) {
           System.out.print("__");   
        }
        System.out.println();
        System.out.print("|");
        for (col=0; col < NCOLUMNAS; col++) {
           System.out.print(col+"|");   
        }
        System.out.println();
    }
    
    public boolean ganaJ1() {
        return(_ganador == JUGADOR1);
    }
    
    public boolean ganaJ2() {
        return(_ganador == JUGADOR2);
    }
    
    public boolean hayEmpate() {
        return(_ganador == EMPATE);
    }
    
    public void anadirFicha(int columna, int jugador) {
        if (_posicionLibre[columna] < NCOLUMNAS-1) {
            _casillas[columna][_posicionLibre[columna]] = jugador;
            _posicionLibre[columna]++;
        }
    }
    
    private boolean hayLineaVertical(int col, int fila, int jugador) {
        int j;
        int numCasillas = 0;
        
        for (j = fila; j < NFILAS; j++) {
           if (_casillas[col][j] == jugador) {
               numCasillas++;
           }
           else {
               break;
           }
        }
        return (numCasillas >= NOBJETIVO);        
    }
    
    private boolean hayLineaHorizontal(int col, int fila, int jugador) {
       int i;
        int numCasillas = 0;
        
        for (i = col; i < NCOLUMNAS; i++) {
           if (_casillas[i][fila] == jugador) {
               numCasillas++;
           }
           else {
               break;
           }
        }
        return (numCasillas >= NOBJETIVO);             
    }
    
    private boolean hayLineaDiagonal(int col, int fila, int jugador) {
        int i,j,k;
        int numCasillas = 0;
        
        //diagonales "crecientes"
        for (k=0; k < NOBJETIVO; k++) {
           i = col+k;
           j = fila+k;
           if ((i < NCOLUMNAS) && (j < NFILAS)) {
               if (_casillas[i][j] == jugador) {
                   numCasillas++;
               }
               else {
                   break;
               }
           }
           else {
               break;
           }
        }
        if (numCasillas >= NOBJETIVO) {
            return(true);
        }
        
        //diagonales "decrecientes"
        numCasillas=0;
        for (k=0; k < NOBJETIVO; k++) {
           i = col+k;
           j = fila-k;
           if ((i < NCOLUMNAS) && (j >= 0)) {
               if (_casillas[i][j] == jugador) {
                   numCasillas++;
               }
               else {
                   break;
               }
           }
           else {
               break;
           }
        }
        return (numCasillas >= NOBJETIVO);                
    }    
    
    private void copiarCasillas(int[][] casillas) {
        int col, fila;
        
        for (col=0; col < NCOLUMNAS; col++) {
            for (fila=0; fila < NFILAS; fila++) {
                this._casillas[col][fila] = casillas[col][fila];
            }
        }       
    }    
    
    private void copiarPosicionLibre(int[] posicionLibre) {
        int col, fila;
        
        for (col=0; col < NCOLUMNAS; col++) {
            this._posicionLibre[col] = posicionLibre[col];
        }       
    }
    
    public boolean casillasIguales(int[][] casillas) {
       int col, fila;
       for (col=0; col < NCOLUMNAS; col++) {
          for (fila=0; fila < NFILAS; fila++) {
              if (this._casillas[col][fila] != casillas[col][fila]){
                  return(false);  // Son distintos -> salir devolviendo falso
              }
          }
       }               
       return (true); // Si llega todas las casillas son iguales
    }
    
    public boolean finalJuego() {
        return(_ganador != 0);
    }
    
    public int ganador() {
        return(_ganador);
    }
    
    public boolean esGanador(int jugador) {
        return(jugador == _ganador);
    }

    public int contarFilasDeTres(int jugador) {
        int contadorFilas = 0;
    
        contadorFilas += contarFilasHorizontales(jugador);
    
        contadorFilas += contarFilasVerticales(jugador);
    
        contadorFilas += contarFilasDiagonales(jugador);
    
        return contadorFilas;
    }
    
    private int contarFilasHorizontales(int jugador) {
        int contadorFilas = 0;
    
        for (int fila = 0; fila < NFILAS; fila++) {
            for (int col = 0; col < NCOLUMNAS - 2; col++) {
                if (_casillas[col][fila] == jugador &&
                    _casillas[col + 1][fila] == jugador &&
                    _casillas[col + 2][fila] == jugador) {
                    contadorFilas++;
                }
            }
        }
    
        return contadorFilas;
    }
    
    private int contarFilasVerticales(int jugador) {
        int contadorFilas = 0;
    
        for (int col = 0; col < NCOLUMNAS; col++) {
            for (int fila = 0; fila < NFILAS - 2; fila++) {
                if (_casillas[col][fila] == jugador &&
                    _casillas[col][fila + 1] == jugador &&
                    _casillas[col][fila + 2] == jugador) {
                    contadorFilas++;
                }
            }
        }
    
        return contadorFilas;
    }
    
    private int contarFilasDiagonales(int jugador) {
        int contadorFilas = 0;
    
        // Contar diagonales ascendentes
        for (int fila = 0; fila < NFILAS - 2; fila++) {
            for (int col = 0; col < NCOLUMNAS - 2; col++) {
                if (_casillas[col][fila] == jugador &&
                    _casillas[col + 1][fila + 1] == jugador &&
                    _casillas[col + 2][fila + 2] == jugador) {
                    contadorFilas++;
                }
            }
        }
    
        // Contar diagonales descendentes
        for (int fila = 0; fila < NFILAS - 2; fila++) {
            for (int col = 0; col < NCOLUMNAS - 2; col++) {
                if (_casillas[col][fila + 2] == jugador &&
                    _casillas[col + 1][fila + 1] == jugador &&
                    _casillas[col + 2][fila] == jugador) {
                    contadorFilas++;
                }
            }
        }
    
        return contadorFilas;
    }

    public int contarFichasEnColumnas(int jugador, int[] columnas) {
        int totalFichas = 0;
    
        for (int columna : columnas) {
            // Verificar si la columna está dentro de los límites del tablero
            if (columna >= 0 && columna < NCOLUMNAS) {
                // Iterar sobre las filas de la columna y contar las fichas del jugador
                for (int fila = 0; fila < NFILAS; fila++) {
                    if (_casillas[columna][fila] == jugador) {
                        totalFichas++;
                    }
                }
            }
        }
    
        return totalFichas;
    }

    public int contarFichasEnFilas(int jugador, int[] filas) {
        int totalFichas = 0;
    
        for (int fila : filas) {
            // Verificar si la fila está dentro de los límites del tablero
            if (fila >= 0 && fila < NFILAS) {
                // Iterar sobre las columnas de la fila y contar las fichas del jugador
                for (int columna = 0; columna < NCOLUMNAS; columna++) {
                    if (_casillas[columna][fila] == jugador) {
                        totalFichas++;
                    }
                }
            }
        }
    
        return totalFichas;
    }

    public int contarBloqueos(Tablero tablero, int jugador) {
        int totalBloqueos = 0;
        int Rival;
        if (jugador == 1){
            Rival = 2;
        } else {
            Rival = 1;
        }
        // Iterar sobre todas las posibles jugadas posibles del oponente
        for (int columna = 0; columna < Tablero.NCOLUMNAS; columna++) {
            // Clonar el tablero para simular la jugada del oponente
            Tablero tableroClon = (Tablero) tablero.clone();
            tableroClon.anadirFicha(columna, Rival);
            // Verificar si la jugada ganaría el juego para el oponente
            tableroClon.obtenerGanador();
            if (tableroClon.esFinal() && tableroClon.esGanador(Rival)) {
                totalBloqueos += 1;
                
            }
        }
    
        return totalBloqueos;
    }

}  // Fin clase Tablero
