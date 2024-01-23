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
		ArrayList<ArrayList<ArrayList<String>>> matrices = new ArrayList<>();

		int nJuegos = 0;

		try {
			nJuegos = Integer.parseInt(sc.nextLine());

			if (nJuegos <= 0) {
				throw new Exception();
			}
			if (sc.nextLine().length() != 0) {
				throw new Exception();
			}

			for (int i = 0; i < nJuegos; i++) {

				ArrayList<ArrayList<String>> matriz = new ArrayList<>();

				String linea = sc.nextLine();

				if (linea.length() <= 0 || linea.length() > 20) {
					throw new Exception();
				}

				while (!linea.equals("")) {
					ArrayList<String> fila = new ArrayList<>();

					for (int j = 0; j < linea.length(); j++) {
						if (linea.charAt(j) != ('A') && linea.charAt(j) != ('V') && linea.charAt(j) != ('R')) {
							throw new Exception();

						}

						if (matriz.size() > 0 && matriz.get(0).size() != linea.length()) {
							throw new Exception();
						}
						fila.add(linea.charAt(j) + "");

					}

					matriz.add(fila);
					if (matriz.size() > 20) {
						throw new Exception();
					}
					if(!sc.hasNextLine()) {
						break;
					}
					linea = sc.nextLine();
					if (linea.length() > 20) {
						throw new Exception();
					}
				}

				matrices.add(matriz);

			}
		} catch (Exception e) {

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
		int aux = 0;
		for (ArrayList<ArrayList<String>> matriz : matrices) {

			ArrayList<ArrayList<Ficha>> juego = transformar(matriz);

			ArrayList<Ficha> puntuacion = new ArrayList<>();
			ArrayList<Ficha> parcial = new ArrayList<>();

			jugarFichas(juego, 1, 0, parcial, puntuacion);
			System.out.println("Juego "+(aux+1)+":");
			for (int i = 0; i < puntuacion.size(); i++) {
				System.out.println(puntuacion.get(i).getMovimiento());
			}
			aux++;
			if (aux != matrices.size()) {
				System.out.println();
			}
		}
	}

	public static int jugarFichas(ArrayList<ArrayList<Ficha>> juego, int m, int pts, ArrayList<Ficha> parcial,
			ArrayList<Ficha> solucionFinal) {
		ArrayList<Ficha> equipos = null;
		ArrayList<ArrayList<Ficha>> copia = clonar(juego);
		equipos = getEquipos(copia);
		for (int i = 0; i < equipos.size(); i++) {
			ArrayList<ArrayList<Ficha>> copia2 = clonar(juego);
			int eliminados = eliminar(equipos.get(i).getFila(), equipos.get(i).getColumna(), copia2) + 1;
			int p = (int) Math.pow(eliminados - 2, 2);
			Ficha jugada = new Ficha(equipos.get(i).getColor(), equipos.get(i).getFila(), equipos.get(i).getColumna(),
					p, m, eliminados, copia2.size());
			ArrayList<Ficha> aux = new ArrayList<>();
			aux.addAll(parcial);
			aux.add(jugada);

			refactorizaMatriz(copia2);
			int numFichas = quedanFichas(copia2);

			int puntosT = pts + p;
			if (numFichas == 0) {
				puntosT += 1000;
				Ficha jFinal = new Ficha(puntosT, numFichas);
				aux.add(jFinal);
				cambiaSolucion(solucionFinal, aux);
			} else {
				jugarFichas(copia2, m + 1, puntosT, aux, solucionFinal);
			}
		}
		if (equipos.size() == 0) {
			int numFichas = quedanFichas(juego);

			int puntosT = pts;
			if (numFichas == 0) {
				puntosT += 1000;
			}
			Ficha jFinal = new Ficha(puntosT, numFichas);
			ArrayList<Ficha> aux = new ArrayList<>();
			aux.addAll(parcial);
			aux.add(jFinal);
			cambiaSolucion(solucionFinal, aux);

		}
		return 0;

	}

	private static void cambiaSolucion(ArrayList<Ficha> solucionFinal, ArrayList<Ficha> aux) {
		// TODO Auto-generated method stub
		if (solucionFinal.isEmpty()) {
			solucionFinal.addAll(aux);
			return;
		}
		int puntosSolucionFinal = solucionFinal.get(solucionFinal.size() - 1).getPuntos();
		int puntosAux = aux.get(aux.size() - 1).getPuntos();
		if (puntosSolucionFinal < puntosAux) {
			solucionFinal.clear();
			solucionFinal.addAll(aux);
		} else if (puntosAux == puntosSolucionFinal) {
			for (int i = 0; i < solucionFinal.size() && i < aux.size(); i++) {
				int filaSolucion = solucionFinal.get(i).getFila();
				int columnaSolucion = solucionFinal.get(i).getColumna();
				int filaAux = aux.get(i).getFila();
				int columnaAux = aux.get(i).getColumna();
				if (filaSolucion < filaAux) {
					solucionFinal.clear();
					solucionFinal.addAll(aux);
				} else if (filaSolucion == filaAux) {
					if (columnaAux < columnaSolucion) {
						solucionFinal.clear();
						solucionFinal.addAll(aux);
					} else if (columnaAux > columnaSolucion) {
						return;
					}
				} else {
					return;
				}
			}
		}

	}

	private static int quedanFichas(ArrayList<ArrayList<Ficha>> copia2) {
		// TODO Auto-generated method stub
		int fichas = 0;
		for (int i = 0; i < copia2.size(); i++) {
			for (int j = 0; j < copia2.get(i).size(); j++) {
				if (copia2.get(i).get(j).getColor() != ' ') {
					fichas++;
				}
			}
		}
		return fichas;
	}

	public static ArrayList<Ficha> getEquipos(ArrayList<ArrayList<Ficha>> matriz) {
		ArrayList<Ficha> posicion = new ArrayList<>();
		ArrayList<Ficha> fichas = new ArrayList<>();

		for (int i = 0; i < matriz.size(); i++) {
			for (int j = 0; j < matriz.get(0).size(); j++) {
				if (matriz.get(i).get(j).getColor() != ' ' && !fichas.contains(matriz.get(i).get(j))) {
					ArrayList<Ficha> equipo = new ArrayList<>();
					getEquipo(matriz, i, j, equipo);
					fichas.addAll(equipo);
					if (equipo.size() > 1) {
						Ficha mejor = equipo.get(0);
						for (Ficha ficha : equipo) {
							if (mejor.getFila() < ficha.getFila()) {
								mejor = ficha;
							} else if (mejor.getFila() == ficha.getFila() && mejor.getColumna() > ficha.getColumna()) {
								mejor = ficha;
							}
						}
						if (!posicion.contains(mejor)) {
							posicion.add(mejor);
						}

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
						if (matriz.get(k).get(j).getColor() != ' ') {
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
					fichasElimindas += eliminar(filaNueva, columnaNueva, matriz) + 1;
				}
			}
		}

		return fichasElimindas;

	}

}
