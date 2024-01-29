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
			nJuegos = Integer.parseInt(sc.nextLine()); // intentamos leer un numeor desde la entrada de usuario y alamacenamos 

			if (nJuegos <= 0) { //verificamos si en nJuegos es mayor que cero o si la siguiente linea esta vacia
				throw new Exception();//si no se cumple tiramos excepcion
			}
			if (sc.nextLine().length() != 0) {
				throw new Exception();
			}

			for (int i = 0; i < nJuegos; i++) { // se ejecuta nJuegos veces

				ArrayList<ArrayList<String>> matriz = new ArrayList<>(); //cremos una lista bidimensional para almacenar filas y columnas de la matriz actual del juego

				String linea = sc.nextLine(); // leemos una linea de la entrada del usuario y la metneemos en variable linea

				if (linea.length() <= 0 || linea.length() > 20) { // verificamos la longitud de la linea
					throw new Exception();
				}

				while (!linea.equals("")) { // se ejecuta mientras la linea no este vacia
					ArrayList<String> fila = new ArrayList<>();//creamos una lista para almacenar los elementosd  de la fila actual

					for (int j = 0; j < linea.length(); j++) { //reoccorremos cada caracter de la linea
						if (linea.charAt(j) != ('A') && linea.charAt(j) != ('V') && linea.charAt(j) != ('R')) { //&& linea.charAt(j) != ('Z')
							throw new Exception(); //verificamos si los caracteres son distitnos a AVR si es asi nos tira excpecion

						}

						if (matriz.size() > 0 && matriz.get(0).size() != linea.length()) {
							throw new Exception();//si la matriz ya tiene filas y la linea que tenemos tiene longitud diferente de las lineas anteriores, tiramos expcepcion
						}
						fila.add(linea.charAt(j) + "");//agreagamos el caracter actual convertido a la lista "fila"

					}

					matriz.add(fila); //añadimos la fila a la matriz que tnemos creada
					if (matriz.size() > 20) {//si la cantidad de filas es mayor que 20 entonhces expcepcion 
						throw new Exception();
					}
					if (!sc.hasNextLine()) {
						break;
					}
					linea = sc.nextLine();//leemos la siguiente linea
					if (linea.length() > 20) {//si la linea tiene longitud mayor que 20 entonces expecion
						throw new Exception();
					}
				}

				matrices.add(matriz); //si no añadimos matriz a la lista de matrices

			}
		} catch (Exception e) {

		}
		juega(matrices);
	}

	enum Movimientos {
		//primero definimos las constantescon sus respectivos desplazamientos
		ARRIBA(0, -1), ABAJO(0, 1), IZQUIERDA(-1, 0), DERECHA(1, 0);

		private int fila;
		private int columna;

		private Movimientos(int columna, int fila) { //declaramos el constructor privado
			this.columna = columna; //inicializamos las vataibles fila y columna asociadas a cada constante del enum
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
	/*
	 * metodo que cuenta el numero de fichas conectadas al mismo color de la matriz
	 */
	public static int contarEquipos(int fila, int columna, ArrayList<ArrayList<Ficha>> matriz,
			ArrayList<Ficha> fichasUsadas) {//toma la posicion inicial fila y columna en la matriz, la matriz fe fichas y una lista de fichas ya usadas
		// como ya la voy a usar la añado
		fichasUsadas.add(matriz.get(fila).get(columna));//añadimos la ficha actual a la lista de fichas usadas

		int movimientoPosibles = 0; //cuenta la cantidad de movimiento posibles desde la posicion dada

		for (Movimientos mov : Movimientos.values()) { // recorremos todas las direcciones posibles definidas por el enum de Movimientos
			if (posicionCorrecta(fila, columna, mov, matriz)) { //verificamos si la posicion resultante del movimiento es valida en la matriz es decir que no se salga de los limites de la matriz
				int filaNueva = fila + mov.getFila(); //calculamos las nuevas coordenadas segun el movimiento actual de la fila
				int columnaNueva = columna + mov.getColumna();// y de la columna

				//comprobamos si las fichas tienen el mismo color
				if (matriz.get(fila).get(columna).getColor() == (matriz.get(filaNueva).get(columnaNueva).getColor())) {
					

					// comprobamos que no la hemos usado
					if (!fichasUsadas.contains(matriz.get(filaNueva).get(columnaNueva))) {
						movimientoPosibles++; //si se cumple incrementamos los movimientos posibles


						//realizamos llamada recursiva a contar equipos desde la nueva posicion
						movimientoPosibles += contarEquipos(filaNueva, columnaNueva, matriz, fichasUsadas);
					}

				}
			}
		}

		return movimientoPosibles;
	}

	public static ArrayList<ArrayList<Ficha>> transformar(ArrayList<ArrayList<String>> matriz) {
		//toma la matriz bidimensional y devolvemos la matriz bidimensional de objetos "ficha"

		//creamos la matriz que contiene los objetos de Ficha
		ArrayList<ArrayList<Ficha>> matrizF = new ArrayList<>();
		

		//recorremos las filas de la matriz de cadenas
		for (int i = 0; i < matriz.size(); i++) {
			
			//para cada fila de la matriz se crea una lista 
			ArrayList<Ficha> fila = new ArrayList<>();


			//recorremos las columnas de la matriz de cadenas
			for (int j = 0; j < matriz.get(0).size(); j++) {
				//para cada posicion creamos una nueva instancia de la clase ficha con el caracter en esa posicion ademas de las coordenadas i, j
				Ficha ficha = new Ficha(matriz.get(i).get(j).charAt(0), i, j);
				fila.add(ficha);//añadimos la instancia de ficha a la lista fila 
			}

			matrizF.add(fila); // y ahora la lista fila la metemos en la matrizF

		}
		return matrizF; //devolvemos esta matriz que contiene objetos ficha basados en la informacion de la matriz de cadenas

	}

	/*
	 * coordina la ejecucion del juego basada en una lista de matrices
	 */
	public static void juega(ArrayList<ArrayList<ArrayList<String>>> matrices) {
		int aux = 0;//lleva la cuenta de la posicion actual en la lista de matrices


		for (ArrayList<ArrayList<String>> matriz : matrices) {//recorremos cada matriz en la lista de las matrices


			//transformamos la matriz a una matriz de objetos de ficha
			ArrayList<ArrayList<Ficha>> juego = transformar(matriz);

			//creaamos lista para almacenar las soluciones del juego
			ArrayList<ArrayList<Ficha>> mejoresSolucionesFinales = new ArrayList<>();


			//almacenamos en parcial las fichas que se van jugando durante el proceso
			ArrayList<Ficha> parcial = new ArrayList<>();


			jugarFichas(juego, 1, 0, parcial, mejoresSolucionesFinales);

			/*
			 * boolean contieneZ = false;
			for (ArrayList<String> fila : matriz) {
				for (String elemento : fila) {
					if (elemento.contains("Z")) {
						contieneZ = true;
						break;
					}
				}
				if (contieneZ) {
					break;
				}
			}
			if (contieneZ) {
				System.out.println("Juego " + (aux + 1) + ": con Z");
			} else {
			}
			 */
			
			//imprimimos por consola el numero del juego actual
			
				System.out.println("Juego " + (aux + 1) + ":");
			
	
			//recorremos las mejores soluciones finales encontradas en el juego
			for (int i = 0; i < mejoresSolucionesFinales.size(); i++) {

				//imprimimos la informacion sobre la solucion actual
				System.out.println("Solución " + (i + 1) + " de " + mejoresSolucionesFinales.size() + ":");

				//dentro del bucle imprimimos en consola los movimientos de la solucion acutal
				for (int k = 0; k < mejoresSolucionesFinales.get(i).size(); k++) {
					System.out.println(mejoresSolucionesFinales.get(i).get(k).getMovimiento());
				}
				// System.out.println(mejoresSolucionesFinales.get(0).get(i).getMovimiento());
			}

			//incrementamos la variable para psar al siguiente juego en la lista
			aux++;
			//verificamos si hay mas juegos para jugar, si es asi imprimimos espacio para separar la salida en consola de los diferentes juegos
			if (aux != matrices.size()) {
				System.out.println();
			}
		}
	}


	/*
	 * exploramos las diferentes jugadas que hay en un juego y encontramos la mejor solucion en terminos de puntos acumulados
	 */
	public static int jugarFichas(ArrayList<ArrayList<Ficha>> juego, int m, int pts, ArrayList<Ficha> parcial,
			ArrayList<ArrayList<Ficha>> mejoresSolucionesFinales) {
				//tomamos la matriz del juego, el numero de movimiento actual m, los puntos acumulados, una lista parcial de las fichas que almacena las fichas  jugadas hasta el momento
				//y otra lista para almacenar las mejores soluciones encontradas "mejoresSolucionesFinales"

		ArrayList<Ficha> equipos = null;//esta almacena los grupos de fichas en el juego

		//copiamos la matriz del juego, la clonamos en "copia"
		ArrayList<ArrayList<Ficha>> copia = clonar(juego); 


		equipos = getEquipos(copia);//obtenemos los equipos que hay en la copia

		//recorremos cada grupo de fichas en la lista equipos
		for (int i = 0; i < equipos.size(); i++) {

			//realizamos otra copia de nuestro juego
			ArrayList<ArrayList<Ficha>> copia2 = clonar(juego);

			//eliminamos el grupo de fichas actual y obtenemos el numero de fichas eliminadas
			int eliminados = eliminar(equipos.get(i).getFila(), equipos.get(i).getColumna(), copia2) + 1;
			int p = (int) Math.pow(eliminados - 2, 2);

			//representamos en jugada, la jugada actual con la informacion sbre color, posicion, puntos, movimientos, fichas elimidas y el tamaño actual de la matriz
			Ficha jugada = new Ficha(equipos.get(i).getColor(), equipos.get(i).getFila(), equipos.get(i).getColumna(),
					p, m, eliminados, copia2.size());


			//creamos lista aux para representar el estado actual del juego incluyendo la jugada actual
			ArrayList<Ficha> aux = new ArrayList<>();
			aux.addAll(parcial);
			aux.add(jugada);

			
			refactorizaMatriz(copia2);

			//obtenemos el numero de fichas restantes en la matriz copia2
			int numFichas = quedanFichas(copia2);

			//calculamos puntos despues de la jugada actual
			int puntosT = pts + p;


			if (numFichas == 0) { //verificamos si no quedan fichas en el juego

				puntosT += 1000; // si no quedan fichas metemos 1000 puntos
				
				//creamos ficha que representa el estado final de la jugada
				Ficha jFinal = new Ficha(puntosT, numFichas);
				aux.add(jFinal);

				//llamamos al metodo cambia solucion para actualiar la lista de mejores soluciones finales
				cambiaSolucion(mejoresSolucionesFinales, aux);
			} else {

				//si quedan fichas volvemos a llamar al metood con la nueva matriz,
				// con el siguiente movimiento, con los puntos acumulados, la lista aux y las mejoresSolucionesFinales
				jugarFichas(copia2, m + 1, puntosT, aux, mejoresSolucionesFinales);
			}
		}

		//verificamos que no haya mas grupos de fichas en el juego
		if (equipos.size() == 0) {
			int numFichas = quedanFichas(juego);

			int puntosT = pts;//si no hay mas calculamos los puntosFinales
			if (numFichas == 0) {
				puntosT += 1000;
			}
			//jFinal represneta el estado final de la jugada
			Ficha jFinal = new Ficha(puntosT, numFichas);

			ArrayList<Ficha> aux = new ArrayList<>();
			aux.addAll(parcial);
			aux.add(jFinal);
			cambiaSolucion(mejoresSolucionesFinales, aux); // actualizamos la lista de mejores soluciones finales

		}
		return 0;

	}

	/*
	 * Gestionamos las mejores soluciones finales encontradas durante el proceso de juego
	 */
	private static void cambiaSolucion(ArrayList<ArrayList<Ficha>> mejoresSolucionesFinales, ArrayList<Ficha> aux) {
	
		//verificamos si la lista de soluciones finales esta vacia si es asi metemos aux  a la lista y termina la funcion
		if (mejoresSolucionesFinales.isEmpty()) {
			mejoresSolucionesFinales.add(aux);
			return;	
		}

		//si no esta vacia, obtenemos puntos de la ultima ficha en la solucin final actal y la ultima ficha en la nueva solucion temporal
		int puntosSolucionFinal = mejoresSolucionesFinales.get(0).get(mejoresSolucionesFinales.get(0).size() - 1)
				.getPuntos();

		int puntosAux = aux.get(aux.size() - 1).getPuntos();

		//verificamos si los puntos de la nueva solucion temporal son mayores que la solucion final actual
		if (puntosSolucionFinal < puntosAux) {
			mejoresSolucionesFinales.clear();
			mejoresSolucionesFinales.add(aux);//si son menores vaciamos mejoreSolucionesFinalesy le agregamos la nueva solucion temporal
		} else if (puntosAux == puntosSolucionFinal) {

			//recorremos todas las soluciones finales actuales
			for (int a = 0; a < mejoresSolucionesFinales.size(); a++) {

				//recorremos las fichas de ambas soluciones
				for (int i = 0; i < mejoresSolucionesFinales.get(a).size() && i < aux.size(); i++) {

					//obtenemos coordenadas de la fila y columna de la final actual y de la nueva solucion temporal
					int filaSolucion = mejoresSolucionesFinales.get(a).get(i).getFila();
					int columnaSolucion = mejoresSolucionesFinales.get(a).get(i).getColumna();
					int filaAux = aux.get(i).getFila();
					int columnaAux = aux.get(i).getColumna();


					//si son menores se agrega la nueva solucion actual y se termina la funcion
					if (filaSolucion < filaAux) {
						mejoresSolucionesFinales.add(a, aux);
						return;

						//si son igiuales se continua comparando la siguente ficha
					} else if (filaSolucion == filaAux) {
						if (columnaAux < columnaSolucion) {
							mejoresSolucionesFinales.add(a, aux);
							return;

							//si es mayor se rompe el bucle y se continua con la siguiente solucion final actual
						} else if (columnaAux > columnaSolucion) {
							break;
						}
					} else {
						break;
					}
				}

			}

			//si no se ha agregado ninguna soliucion temporal en las comparaciones se agrega al dinal de la lista
			//de los mejores soluciones finales
			mejoresSolucionesFinales.add(aux);

		}

	}

	/*
	 * toma como parametro la matriz de fichas copia2 y devuelve un entero de la cantidad de fichas restantes
	 */
	private static int quedanFichas(ArrayList<ArrayList<Ficha>> copia2) {
		int fichas = 0;

		//recorremos cada elemento en la matriz
		for (int i = 0; i < copia2.size(); i++) {
			for (int j = 0; j < copia2.get(i).size(); j++) {
				if (copia2.get(i).get(j).getColor() != ' ') {
					fichas++; // si el color en la ficha actual no es un espacio en blanco entonces fihcas ++
				}
			}
		}
		return fichas; //devuelve la cantidad de fichas contadas en la matriz copia2 es decir las restantes que quedan
	}


	/*
	 * devuelve las posiciones de las fichas que forman equipos en la matriz
	 */
	public static ArrayList<Ficha> getEquipos(ArrayList<ArrayList<Ficha>> matriz) {

		//incializamos posicion que almacena las fichas que forman parte de un equipo
		//y fichas que almacena fichsa que ya han sido procesadas para evitar duplicados
		ArrayList<Ficha> posicion = new ArrayList<>();
		ArrayList<Ficha> fichas = new ArrayList<>();


		//recorremos filas y columnas de la matriz
		for (int i = 0; i < matriz.size(); i++) {
			for (int j = 0; j < matriz.get(0).size(); j++) {

				//verificamos si el color de la ficha en la pos actual no es espacio en blanco y si esa ficha
				// no ha sido procesada es decir que no esta en la lista de fichas
				if (matriz.get(i).get(j).getColor() != ' ' && !fichas.contains(matriz.get(i).get(j))) {

					//creamos lista equipo y llamamos a la funcion getEquipo para obtener fichas que pertenece al equipo 
					//al que pertenece la ficha en la pos actual
					ArrayList<Ficha> equipo = new ArrayList<>();
					getEquipo(matriz, i, j, equipo);

					//agregamos las fichas del equipo a la lista fichas para evitar duplicados
					fichas.addAll(equipo);

					/*
					 * si la cantidad de fichas es mayor que 1, se busca que tieene la pos mas alta(fila mas baja y columna mas a la izquierda),
					 * y se agrega a la lista posicion si aun no esta presente en ella
					 */
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
		//retorna la posicion que contiene las posiciones de las fichas que forman parte de algun equipo en la matriz
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


		//agrega la ficha en la posicion actual al equipo
		equipo.add(matriz.get(fila).get(columna));


		//iteramos sobre todos los movimientos posibles
		for (Movimientos mov : Movimientos.values()) {

			//comprobamos si el movimiento es valido es decir si no se sale fuera del rango de la matriz
			if (posicionCorrecta(fila, columna, mov, matriz)) {
				int filaNueva = fila + mov.getFila();
				int columnaNueva = columna + mov.getColumna();

				/*
				 * verificamos si la pos actual tiene el mismo color que la ficha en la nueva posicion despues del movimiento
				 * si es asi, y la ficha en la nueva posicion no esta en el equipo, realizamos llamda 
				 * recursiva con las nuevas coordenadas
				 */
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

	/* 
	 * elimina fichas conectadas a un mismo color en la matriz, a partir de  una posicion dada
	*/

	public static int eliminar(int fila, int columna, ArrayList<ArrayList<Ficha>> matriz) {

		
		int fichasElimindas = 0;

		//obtenemos el color de la fficha en la posicion actual
		char c = matriz.get(fila).get(columna).getColor();

		//establece color de la ficha en la pos actual como espacio en blanco, marcando la ficha como eliminada

		matriz.get(fila).get(columna).setColor(' ');

		//recorremos todos los movimientos posibles
		for (Movimientos mov : Movimientos.values()) {

			//verificamos si estamos dentro del rango de lamatriz
			if (posicionCorrecta(fila, columna, mov, matriz)) {
				int filaNueva = fila + mov.getFila();
				int columnaNueva = columna + mov.getColumna();

				// verifica si la ficha en la nueva posicion despues del movimiento tiene el mismo color que la ficha en la pos actual
				//si es asi llamamos recursivamente con las nuevas coordenadas,y sumamos 1 al resultado
				if (c == (matriz.get(filaNueva).get(columnaNueva).getColor())) {
					fichasElimindas += eliminar(filaNueva, columnaNueva, matriz) + 1;
				}
			}
		}
		//el proceso continua hasta que no haya fichas del mismo color conectadas en la matriz.

		return fichasElimindas;

	}

}
