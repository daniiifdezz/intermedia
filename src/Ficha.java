public class Ficha {
    private char character;
   
    private int fila;
    private int columna;
    
    private int puntuacionObtenida;
    private int movimiento;
    private int fichasEliminadas;
    private int filaCord;

    public Ficha(char character, int fila, int columna) {
        this.character = character;
        this.fila = fila;
        this.columna = columna;
    }
    
    public Ficha(char character, int fila, int columna, int puntuacionObtenida, int movimiento, int fichasEliminadas, int tamFila) {
    	this(character, fila, columna+1);
    	this.puntuacionObtenida=puntuacionObtenida;
    	this.movimiento=movimiento;
    	this.fichasEliminadas=fichasEliminadas;
    	this.filaCord= tamFila-fila;
    }
    
    public Ficha(int puntuacionObtenida, int fichasEliminadas) {
    	this.puntuacionObtenida=puntuacionObtenida;
    	this.fichasEliminadas=fichasEliminadas;
    	this.movimiento=-1;
    	
    }
    
    public int getPuntos() {
    	return this.puntuacionObtenida;
    }
    public int getFila(){

        return fila;

    }
    public int getColumna(){
        return columna;
    }
    public char getColor( ) {
        return character;
    }


    @Override
    public boolean equals(Object o){
        if(o instanceof Ficha){
            Ficha f=(Ficha)o;
            return f.getFila()==this.getFila()&&this.getColumna() == f.getColumna() && this.character == f.character;
        }else{
            return false;
        }
    }
    
    public String getMovimiento() {
    	if(movimiento==-1) {
    		String frase="Puntuación final: "+this.puntuacionObtenida;
    		if(this.puntuacionObtenida==1) {
    			frase+=", quedando "+this.fichasEliminadas;
    		}else {
    			frase+=", quedando "+this.fichasEliminadas;

    		}
    		if(this.fichasEliminadas==1) {
    			frase+=" ficha.";
    		}else {
    			frase+=" fichas.";
    		}
    		return frase;
    	}else {
    		String frase= "Movimiento "+this.movimiento+" en ("+this.filaCord+", "+this.columna+"): eliminó "+this.fichasEliminadas+" fichas de color "+this.character+" y obtuvo "+this.puntuacionObtenida;
    		if(this.puntuacionObtenida==1) {
    			frase+=" punto.";
    		}else {
    			frase+=" puntos.";
    		}
    		return frase;
    	}
    }
    
    public String toString() {
    	return this.character+"";
    }

	public void setColor(char c) {
		// TODO Auto-generated method stub
		this.character=c;
	}
}
