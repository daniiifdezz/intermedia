
import java.util.ArrayList;
import java.util.Scanner;

public class Intermedia {

    private int fila;
    private int columna;
    private char character;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        leerJuego();

    }

    public static void leerJuego() {

        Scanner sc = new Scanner(System.in);

        int nJuegos = 0;

        try {
            nJuegos = Integer.parseInt(sc.nextLine());

        } catch (Exception e) {
            System.out.println(("Error has introducido texto"));
            System.exit(0);
        }
        if (nJuegos <= 0) {
            System.out.println("Numero de juegos negativo");
            System.exit(0);
        }
        if (sc.nextLine().length() != 0) {
            System.out.println("Linea no vacia");
            System.exit(0);
        }

        ArrayList<ArrayList<ArrayList<String>>> matrices = new ArrayList<>();

        for (int i = 0; i < nJuegos; i++) {

            ArrayList<ArrayList<String>> matriz = new ArrayList<>();

            String linea = sc.nextLine();

            if (linea.length() <= 0 || linea.length() > 20) {
                System.out.println("Primera linea de la matriz no correcta");
                System.exit(0);
            }

            while (!linea.equals("")) {
                ArrayList<String> fila = new ArrayList<>();

                for (int j = 0; j < linea.length(); j++) {
                    if (linea.charAt(j) != ('A') && linea.charAt(j) != ('V') && linea.charAt(j) != ('R')) {
                        System.out.println("Has introducido caracteres erroneos");
                        System.exit(0);

                    }

                    if (matriz.size() > 0 && matriz.get(0).size() != linea.length()) {
                        System.out.println("El numero de columnas no coincide con las filas");
                        System.exit(0);
                    }
                    fila.add(linea.charAt(j) + "");

                }

                matriz.add(fila);
                if (matriz.size() > 20) {
                    System.out.println("Exceso de filas");
                    System.exit(0);
                }
                linea = sc.nextLine();
                if (linea.length() > 20) {
                    System.out.println("Exceso de columnas");
                    System.exit(0);
                }
            }

            matrices.add(matriz);

        }
        juega(matrices);
    }

    enum Movimientos {

        ARRIBA(0, -1), ABAJO(0, 1), IZQUIERDA(-1, 0), DERECHA(1, 0);

        private int fila;
        private int columna;

        private Movimientos(int columna, int fila) {
            this.columna = columna;
            this.fila = fila;

        }

        public int getFila() {
            return this.fila;
        }

        public int getColumna() {
            return this.columna;
        }
    }

    private static boolean posicionCorrecta(int filaO, int columnaO, Movimientos mov,
            ArrayList<ArrayList<Ficha>> matriz) {

        int filaNueva = filaO + mov.getFila();
        int columnaNueva = columnaO + mov.getColumna();

        if (filaNueva < 0 || filaNueva >= matriz.size() || columnaNueva < 0 || columnaNueva >= matriz.get(0).size()) {
            return false;
        } else {
            return true;
        }

    }

    public static int contarEquipos(int fila, int columna, ArrayList<ArrayList<Ficha>> matriz,
            ArrayList<Ficha> fichasUsadas) {
        // como ya la voy a usar la añado
        fichasUsadas.add(matriz.get(fila).get(columna));

        int movimientoPosibles = 0;

        for (Movimientos mov : Movimientos.values()) {
            if (posicionCorrecta(fila, columna, mov, matriz)) {
                int filaNueva = fila + mov.getFila();
                int columnaNueva = columna + mov.getColumna();

                if (matriz.get(fila).get(columna).getColor() == (matriz.get(filaNueva).get(columnaNueva).getColor())) {
                    // comprobamos que no la hemos usado
                    if (!fichasUsadas.contains(matriz.get(filaNueva).get(columnaNueva))) {
                        movimientoPosibles++;
                        movimientoPosibles += contarEquipos(filaNueva, columnaNueva, matriz, fichasUsadas);
                    }

                }
            }
        }

        return movimientoPosibles;
    }

    public static ArrayList<ArrayList<Ficha>> transformar(ArrayList<ArrayList<String>> matriz) {

        ArrayList<ArrayList<Ficha>> matrizF = new ArrayList<>();

        for (int i = 0; i < matriz.size(); i++) {

            ArrayList<Ficha> fila = new ArrayList<>();

            for (int j = 0; j < matriz.get(0).size(); j++) {
                // creo una ficha character fila columna
                Ficha ficha = new Ficha(matriz.get(i).get(j).charAt(0), i, j);
                fila.add(ficha);
            }

            matrizF.add(fila);

        }
        return matrizF;

    }

    public static void juega(ArrayList<ArrayList<ArrayList<String>>> matrices) {
        for (ArrayList<ArrayList<String>> matriz : matrices) {

            ArrayList<ArrayList<Ficha>> juego = transformar(matriz);

            ArrayList<Integer> puntuaciones = new ArrayList<>();

            jugarFichas(juego);

        }
    }
    public static int jugarFichas(ArrayList<ArrayList<Ficha>> juego){
        ArrayList<Ficha> equipos = null;
        ArrayList<ArrayList<Ficha>> copia = clonar(juego);
        for(int i =0; i< equipos.size(); i++){
            ArrayList<ArrayList<Ficha>> copia2 = clonar(juego);
            int eliminados = eliminar(i, i, equipos.get(i).getFila().equipos.get(i).getColumna());
            refactorizaMatriz(copia2);
            jugarFichas(copia2);

        }
        return 0;

    }

    public static ArrayList<Ficha> getEquipos(ArrayList<ArrayList<Ficha>> matriz) {
        ArrayList<Ficha> posicion = new ArrayList<>();

        for (int i = 0; i < matriz.size(); i++) {
            for (int j = 0; j < matriz.get(0).size(); j++) {

                ArrayList<Ficha> equipo = new ArrayList<>();
                getEquipo(matriz, i, j, equipo);
                if (equipo.size() > 1) {
                    Ficha mejor = equipo.get(0);
                    for (Ficha ficha : equipo) {
                        if (mejor.getFila() < ficha.getFila()) {
                            mejor = ficha;
                        } else if (mejor.getFila() == ficha.getFila() && mejor.getColumna() > mejor.getColumna()) {
                            mejor = ficha;
                        }
                    }
                    if (!posicion.contains(mejor)) {
                        posicion.add(mejor);
                    }

                }
            }

        }
        return posicion;

    }

    public static ArrayList<ArrayList<Ficha>> clonar(ArrayList<ArrayList<Ficha>> matriz) {
        ArrayList<ArrayList<Ficha>> clon = new ArrayList<>();

        for (int i = 0; i < matriz.size(); i++) {
            ArrayList<Ficha> fila = new ArrayList<>();
            for (int j = 0; j < matriz.get(0).size(); j++) {
                Ficha ficha = new Ficha(matriz.get(i).get(j).getColor(), i, j);
                fila.add(ficha);
            }
            clon.add(fila);
        }
        return clon;

    }

    public static void getEquipo(ArrayList<ArrayList<Ficha>> matriz, int fila, int columna, ArrayList<Ficha> equipo) {

        equipo.add(matriz.get(fila).get(columna));

        for (Movimientos mov : Movimientos.values()) {
            if (posicionCorrecta(fila, columna, mov, matriz)) {
                int filaNueva = fila + mov.getFila();
                int columnaNueva = columna + mov.getColumna();
                if (matriz.get(fila).get(columna).getColor() == (matriz.get(filaNueva).get(columnaNueva).getColor())) {
                    if (!equipo.contains(matriz.get(filaNueva).get(columnaNueva))) {
                        getEquipo(matriz, filaNueva, columnaNueva, equipo);
                    }
                }

            }
        }
    }

    public static void refactorizaMatriz(ArrayList<ArrayList<Ficha>> matriz) {

        // colocamos filas
        for (int i = matriz.size() - 1; i >= 0; i--) {

            for (int j = matriz.get(0).size() - 1; j >= 0; j--) {
                if (matriz.get(i).get(j).getColor() == ' ') {
                    // buscamos sustituto, que tiene que estar en la misma columna
                    for (int k = i - 1; k >= 0; k--) {
                        if (matriz.get(i).get(j).getColor() != ' ') {
                            // aqui hay algo mas que estar vacio
                            Ficha ficha = new Ficha(matriz.get(k).get(j).getColor(), i, j);
                            matriz.get(i).set(j, ficha);
                            matriz.get(k).get(j).setColor(' ');
                            break;
                            // hacer setters
                        }
                    }

                }

            }
        }
        // aqui localizamos columna vacia
        for (int j = 0; j < matriz.get(0).size(); j++) {
            boolean vacia = true;
            for (int i = 0; i < matriz.size(); i++) {
                if (matriz.get(i).get(j).getColor() != ' ') {
                    vacia = false;
                    break;
                }

            }
            if (vacia) {
                // es decirq ue tengo una columna vacia, por lo que tengoque refactorizar a la
                // izqd
                // busco una columna que tenga elementos
                for (int k = j + 1; k < matriz.get(0).size(); k++) {
                    boolean vacia2 = true;
                    // buscamos columna llena para sustituirlo
                    for (int i = 0; i < matriz.size(); i++) {
                        if (matriz.get(i).get(k).getColor() != ' ') {
                            vacia2 = false;
                            break;

                        }
                    }
                    if (vacia2 == false) {
                        // tengo la columna que sustituye
                        // recorremos fila para guardar en las col de j las col de k
                        for (int i = 0; i < matriz.size(); i++) {
                            Ficha ficha = new Ficha(matriz.get(i).get(k).getColor(), i, j);
                            matriz.get(i).set(j, ficha);

                            matriz.get(i).get(k).setColor(' ');

                        }
                    }
                    break;
                }
            }
        }

    }

    public static int eliminar(int fila, int columna, ArrayList<ArrayList<Ficha>> matriz) {

        int fichasElimindas = 0;

        char c = matriz.get(fila).get(columna).getColor();

        matriz.get(fila).get(columna).setColor(' ');

        for (Movimientos mov : Movimientos.values()) {
            if (posicionCorrecta(fila, columna, mov, matriz)) {
                int filaNueva = fila + mov.getFila();
                int columnaNueva = columna + mov.getColumna();

                if (c == (matriz.get(filaNueva).get(columnaNueva).getColor())) {
                    fichasElimindas++;
                    fichasElimindas += eliminar(filaNueva, columnaNueva, matriz);
                }
            }
        }

        return fichasElimindas;

    }

}
