public class EstrategiaAlphaBeta extends Estrategia {
    
    private Evaluador _evaluador;
    private int _capaMaxima;
    public int numNodos = 0;
    public int numbusquedas = 0;
    private int _jugadorMAX; 

    public EstrategiaAlphaBeta() {
    }
    
    public EstrategiaAlphaBeta(Evaluador evaluador, int capaMaxima) {
       this.establecerEvaluador(evaluador);  
       this.establecerCapaMaxima(capaMaxima);
    }
    
    public int buscarMovimiento(Tablero tablero, int jugador) {
     
        boolean movimientosPosibles[] = tablero.columnasLibres();
        Tablero nuevoTablero;
        int col,valorSucesor;
        int mejorPosicion=-1;  
        int mejorValor=_evaluador.MINIMO; 

        _jugadorMAX = jugador; 
        for (col=0; col<Tablero.NCOLUMNAS; col++) {
            if (movimientosPosibles[col]) { 
                
                nuevoTablero = (Tablero) tablero.clone();
                nuevoTablero.anadirFicha(col,jugador);
                nuevoTablero.obtenerGanador();

                
                valorSucesor = AlphaBeta(nuevoTablero,Jugador.alternarJugador(jugador),1,_evaluador.MINIMO, _evaluador.MAXIMO);   
                numbusquedas++;             
                nuevoTablero = null; 
                
                         
                if (valorSucesor >= mejorValor) {
                    mejorValor = valorSucesor;
                    mejorPosicion = col;
                }
            }
        }
        return(mejorPosicion);        
    }
    
    
    public int AlphaBeta(Tablero tablero, int jugador, int capa, int a, int b) {
        if (tablero.esGanador(_jugadorMAX)) {
            return(_evaluador.MAXIMO);        }
        else if (tablero.esGanador(Jugador.alternarJugador(_jugadorMAX))){
            return(_evaluador.MINIMO);
        } 
        else if (tablero.hayEmpate()) {
            return 0;
        }
        else if (capa == _capaMaxima) {
            return _evaluador.valoracion(tablero, _jugadorMAX);
        }
        else {
    
        boolean[] movimientosPosibles = tablero.columnasLibres();
        Tablero nuevoTablero;
        int valor = 0;
        int col;
        int a_actual = 0;
        int b_actual = 0;
    
        if (esCapaMAX(capa)) {
            a_actual = a;
            valor = Evaluador.MINIMO;
            for (col = 0; col < Tablero.NCOLUMNAS; col++) {
                if (movimientosPosibles[col]) {
                    if (a_actual >= b) {
                        break;
                    }
                    else{
                    numNodos++;
                    nuevoTablero = (Tablero) tablero.clone();
                    nuevoTablero.anadirFicha(col, jugador);
                    nuevoTablero.obtenerGanador();
    
                    valor = maximo2(valor, AlphaBeta(nuevoTablero, Jugador.alternarJugador(jugador), capa + 1, a_actual, b));
                    a_actual = maximo2(a, valor);
                    }
                    
                }}
            } else if (esCapaMIN(capa)) {
            b_actual = b;
            valor = Evaluador.MAXIMO;
            for (col = 0; col < Tablero.NCOLUMNAS; col++) {
                if (movimientosPosibles[col]) {
                    if (b_actual <= a) {
                        break;
                    }
                    else{
                    numNodos++;
                    nuevoTablero = (Tablero) tablero.clone();
                    nuevoTablero.anadirFicha(col, jugador);
                    nuevoTablero.obtenerGanador();
    
                    valor = minimo2(valor, AlphaBeta(nuevoTablero, Jugador.alternarJugador(jugador), capa + 1, a, b_actual));
                    b_actual = minimo2(b, valor);
                    }
                    
                }
            }
        }
        return valor;
    }
}


    
   public void establecerCapaMaxima(int capaMaxima) {
      _capaMaxima = capaMaxima;
   }
   
   public void establecerEvaluador(Evaluador evaluador) {
      _evaluador = evaluador;
   }
    private static final boolean esCapaMIN(int capa) {
        return((capa % 2)==1); // es impar
    }
    
    private static final boolean esCapaMAX(int capa) {
        return((capa % 2)==0); // es par
    }
    
    private static final int maximo2(int v1, int v2) {
        if (v1 > v2)
            return(v1);
        else
            return(v2);
    }
    
    private static final int minimo2(int v1, int v2) {
        if (v1 < v2)
            return(v1);
        else
            return(v2);    
    }

    public int getNodos() {
        return numNodos/numbusquedas;
    }


    
}  // Fin clase EstartegiaAlphaBeta
