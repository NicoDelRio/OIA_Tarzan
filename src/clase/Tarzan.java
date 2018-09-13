package clase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;


public class Tarzan extends EjercicioOIA{

	private File entrada;
	private File salida;
	private ArrayList<Arbol> listaArbol_in;
	private ArrayList<Arista> superSalto;
	private boolean [] matrizAdyacencia;
	private int ordenMatriz;
	private int tamVector;
	private Distancia[] v_dist;
	private Distancia[] v_distAux;
	
	private ArrayList<Arbol> listaArbol_out;
	
	public Tarzan(File entrada, File salida) {
		super(entrada, salida);
		this.entrada = entrada;
		this.salida = salida;
	}

	@Override
	/*
	 * Complejidad Computacional = O(N + N + N + N^2 + N^3 + N) = O(N^3)
	 */
	public void resolver() {
		try {
			Scanner entrada = new Scanner(this.entrada);
			int posX,posY;
			listaArbol_in = new ArrayList<Arbol>();
			superSalto = new ArrayList<Arista>();
			while(entrada.hasNext()) { // CC = O(N)
				posX = entrada.nextInt(); // CC = O(1)
				posY = entrada.nextInt(); // CC = O(1)
				listaArbol_in.add(new Arbol(posX,posY)); // CC = O(1)
			}
			entrada.close();
			
			/*
			 * Resuelto con Matriz de Adyacencia Simetrica Booleana 
			 */
			ordenMatriz = listaArbol_in.size();
			tamVector = (int) Math.round(((Math.pow(ordenMatriz,2) - ordenMatriz) / 2));
			matrizAdyacencia = new boolean [tamVector];
			double distancia;
			/*
			 * Calcula y genera las aristas
			 * Las aristas de tamaño <= 50, se carga en la Matriz de Adyacencia
			 * Las aristas de tamaño >50 y <= 100, se carga en la lista "superSalto"
			 */
			for (int i = 0; i < ordenMatriz -1; i++) { // CC = O(N)
				for (int j = i + 1; j < ordenMatriz; j++) {
					distancia = calcularDistancia(listaArbol_in.get(i), listaArbol_in.get(j)); // CC = O(1)
					if(distancia <= 50) {
						matrizAdyacencia[indiceVector(i, j)] = true; // CC = O(1)
					}
//					if(distancia > 50 && distancia <= 100) {
					else if(distancia <= 100) {
						superSalto.add(new Arista(i, j)); // CC = O(1)
					}
				}
			}
			
			v_dist = new Distancia[ordenMatriz];
			v_distAux = new Distancia[ordenMatriz];
			
			for (int i = 0; i < v_dist.length; i++) { // CC = O(N)
				v_dist[i] = new Distancia(Integer.MAX_VALUE,Integer.MAX_VALUE); // CC = O(1)
			}
			
			/*
			 * Calcula el BFS desde el arbol inicial con las aristas <=50 (saltos normales)
			 */
			v_distAux = bfs(0); // CC = O(N^2)
			if(v_dist[v_dist.length - 1].getCantSaltos() > v_distAux[v_distAux.length-1].getCantSaltos()) { // CC = O(N)
				for (int i = 0; i < v_dist.length; i++) { // CC = O(N)
					v_dist[i] = v_distAux[i]; // CC = O(1)
				}
			}
			
			/*
			 * Calcula el BFS desde el arbol inicial, agregandole a la Matriz de Ady de aristas <=50,
			 * una arista de supersalto (una a la vez).
			 * El vector "v_dist" queda con la menor cantidad de saltos necesarios entre el primer
			 * y último arbol.
			 */
			for (Arista arista : superSalto) { // CC = O(N^3)
				matrizAdyacencia[indiceVector(arista.getPosNodo1(), arista.getPosNodo2())] = true; // CC = O(1)
				v_distAux = bfs(0); // CC = O(N^2)
				if(v_dist[v_dist.length - 1].getCantSaltos() > v_distAux[v_distAux.length-1].getCantSaltos()) {
					for (int i = 0; i < v_dist.length; i++) { // CC = O(N)
						v_dist[i] = v_distAux[i]; // CC = O(1)
					}
				}
				matrizAdyacencia[indiceVector(arista.getPosNodo1(), arista.getPosNodo2())] = false; // CC = O(1)
			}
//			for (int i = 0; i < v_dist.length; i++) {
//				System.out.println("Nodo: " + i + " - Saltos: " + v_dist[i].getCantSaltos() + " - Nodo Ant: " +  v_dist[i].getPosNodoAnt());
//			}
			
			
			PrintWriter salida = new PrintWriter(this.salida);

			if(v_dist[v_dist.length - 1].getCantSaltos() == Integer.MAX_VALUE) { // CC = O(1)
				System.out.println("NO HAY RUTA.");
				salida.println("NO HAY RUTA.");
			}
			else {
				Stack<Integer> pilaPosicionNodos = new Stack<Integer>();
				pilaPosicionNodos.add(v_dist.length - 1); // CC = O(1)
				int posAnt = v_dist[v_dist.length - 1].getPosNodoAnt(); // CC = O(1)
				while (posAnt != 0) { // CC = O(N)
					pilaPosicionNodos.add(posAnt); // CC = O(1)
					posAnt = v_dist[posAnt].getPosNodoAnt(); // CC = O(1)
				}
				if(v_dist.length - 1 != 0) { // CC = O(1)
					pilaPosicionNodos.add(0); // CC = O(1)
				}
				while (!pilaPosicionNodos.empty()) { // CC = O(N)
					posAnt = pilaPosicionNodos.pop(); // CC = O(1)
					System.out.println(listaArbol_in.get(posAnt).getPosX() + " " + listaArbol_in.get(posAnt).getPosY());
					salida.println(listaArbol_in.get(posAnt).getPosX() + " " + listaArbol_in.get(posAnt).getPosY());
				}
			}
			
			salida.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/*
	 * Las salidas pueden ser multiples.
	 * Depende de los datos de entrada.
	 * El del enunciado, es salida única.
	 * 
	 * Hay que hacer un programa probador.
	 * Para que la salida sea valida, se debe chequear:
	 * - La cantidad de arboles del .out debe coincidir con la cantidad mínima de saltos calculada por BFS.
	 * - Los arboles del .out deben existir en el .in.
	 * - El primer y último arbol del .in debe coincidir con el del .out
	 * - La distancia entre cada árbol debe ser <=50 o como máximo una puede ser <=100.
	 * 
	 */
	
	@SuppressWarnings({ "unused", "unlikely-arg-type" })
	@Override
	public boolean validacion() {
		try {
			int posX,posY;
			Scanner entrada_in = new Scanner(this.entrada);
			listaArbol_in = new ArrayList<Arbol>();
			superSalto = new ArrayList<Arista>();
			while(entrada_in.hasNext()) { // CC = O(N)
				posX = entrada_in.nextInt(); // CC = O(1)
				posY = entrada_in.nextInt(); // CC = O(1)
				listaArbol_in.add(new Arbol(posX,posY)); // CC = O(1)
			}
			entrada_in.close();

			Scanner entrada_out = new Scanner(this.salida);
			listaArbol_out = new ArrayList<Arbol>();
			superSalto = new ArrayList<Arista>();
			while(entrada_out.hasNext()) { // CC = O(N)
				posX = entrada_out.nextInt(); // CC = O(1)
				posY = entrada_out.nextInt(); // CC = O(1)
				listaArbol_out.add(new Arbol(posX,posY)); // CC = O(1)
			}
			entrada_out.close();

			/*
			 * La cantidad de arboles del .out debe coincidir con la cantidad mínima de saltos calculada por BFS.
			 */

			ordenMatriz = listaArbol_in.size();
			tamVector = (int) Math.round(((Math.pow(ordenMatriz,2) - ordenMatriz) / 2));
			matrizAdyacencia = new boolean [tamVector];
			double distancia;
			for (int i = 0; i < ordenMatriz -1; i++) { // CC = O(N)
				for (int j = i + 1; j < ordenMatriz; j++) {
					distancia = calcularDistancia(listaArbol_in.get(i), listaArbol_in.get(j)); // CC = O(1)
					if(distancia <= 50) {
						matrizAdyacencia[indiceVector(i, j)] = true; // CC = O(1)
					}
					else if(distancia <= 100) {
						superSalto.add(new Arista(i, j)); // CC = O(1)
					}
				}
			}
			
			v_dist = new Distancia[ordenMatriz];
			v_distAux = new Distancia[ordenMatriz];
			
			for (int i = 0; i < v_dist.length; i++) { // CC = O(N)
				v_dist[i] = new Distancia(Integer.MAX_VALUE,Integer.MAX_VALUE); // CC = O(1)
			}
			
			v_distAux = bfs(0); // CC = O(N^2)
			if(v_dist[v_dist.length - 1].getCantSaltos() > v_distAux[v_distAux.length-1].getCantSaltos()) { // CC = O(N)
				for (int i = 0; i < v_dist.length; i++) { // CC = O(N)
					v_dist[i] = v_distAux[i]; // CC = O(1)
				}
			}
			for (Arista arista : superSalto) { // CC = O(N^3)
				matrizAdyacencia[indiceVector(arista.getPosNodo1(), arista.getPosNodo2())] = true; // CC = O(1)
				v_distAux = bfs(0); // CC = O(N^2)
				if(v_dist[v_dist.length - 1].getCantSaltos() > v_distAux[v_distAux.length-1].getCantSaltos()) {
					for (int i = 0; i < v_dist.length; i++) { // CC = O(N)
						v_dist[i] = v_distAux[i]; // CC = O(1)
					}
				}
				matrizAdyacencia[indiceVector(arista.getPosNodo1(), arista.getPosNodo2())] = false; // CC = O(1)
			}
			if(v_dist[v_dist.length - 1].getCantSaltos() == Integer.MAX_VALUE) { // CC = O(1)
				if(!listaArbol_out.get(0).equals("NO HAY RUTA.")) {
					System.out.println(" (NO HAY RUTA) ");
					return false;
				}
			} else {
				if(v_dist[v_dist.length - 1].getCantSaltos() + 1 != listaArbol_out.size()) {
					System.out.println(" (No coincide cant arboles .out con BFS) ");
					return false;
				}
			}
			
			
			/*
			 * Los arboles del .out deben existir en el .in.
			 */
			for (Arbol arbol : listaArbol_out) {
				if(!listaArbol_in.contains(arbol)) {
					System.out.println(" (Los arboles del .out deben existir en el .in) ");
					return false;
				}
			}
			
			/*
			 * El primer y último arbol del .in debe coincidir con el del .out
			 */
			if(!listaArbol_in.get(0).equals(listaArbol_out.get(0))) {
				System.out.println(" (El primer arbol del .in debe coincidir con el del .out) ");
				return false;
			}
			if(!listaArbol_in.get(listaArbol_in.size()- 1).equals(listaArbol_out.get(listaArbol_out.size()- 1))) {
				System.out.println(" (El último arbol del .in debe coincidir con el del .out) ");
				return false;
			}
			
			/*
			 * La distancia entre cada árbol debe ser <=50 o como máximo una puede ser <=100.
			 */
			
			boolean superSaltoRealizado = false;
			if(listaArbol_out.size() > 1) {
				for (int i = 0; i < listaArbol_out.size() - 1; i++) {
					distancia = calcularDistancia(listaArbol_out.get(i), listaArbol_out.get(i+1)); // CC = O(1)
					if(distancia <= 50) {
						break;
					}
					else if(distancia <= 100 && !superSaltoRealizado) {
						superSaltoRealizado = true;
						break;
					}
					System.out.println(" (La distancia entre cada árbol debe ser <=50 o como máximo una puede ser <=100) ");
					return false;
				}				
			}

			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}
	
	public double calcularDistancia(Arbol a1, Arbol a2) { // CC = O(1)
		return Math.sqrt(Math.pow((a2.getPosX() - a1.getPosX()), 2) + Math.pow((a2.getPosY() - a1.getPosY()), 2));
	}
	
	public int indiceVector(int fil, int col) { // CC = O(1)
		if(fil < col) {
			return fil * ordenMatriz + col - (fil * fil + 3 * fil + 2) / 2;
		}
		return col * ordenMatriz + fil - (col * col + 3 * col + 2) / 2;
	}
	
	public Distancia[] bfs(int nodoInicial) { // CC = O(N^2)
		Distancia [] distancia = new Distancia [ordenMatriz];
		for (int i = 0; i < distancia.length; i++) { // CC = O(N)
			distancia[i] = new Distancia(Integer.MAX_VALUE,Integer.MAX_VALUE); // CC = O(1)
		}
		boolean [] estado = new boolean[ordenMatriz];
		Queue<Integer> cola = new LinkedList<>();
		estado[nodoInicial] = true; // CC = O(1)
		cola.add(nodoInicial); // CC = O(1)
		distancia[nodoInicial].setCantSaltos(0); // CC = O(1)
		distancia[nodoInicial].setPosNodoAnt(nodoInicial); // CC = O(1)
		int nodo;
		while(!cola.isEmpty()) { // CC = O(N^2)
			nodo = cola.poll(); // CC = O(1)
			for (int i = 1; i < ordenMatriz; i++) { // CC = O(N)
				if(matrizAdyacencia[indiceVector(nodo, i)] == true && estado[i] == false) { // CC = O(1)
					estado[i] = true; // CC = O(1)
					cola.add(i); // CC = O(1)
					distancia[i].setCantSaltos(distancia[nodo].getCantSaltos() + 1); // CC = O(1)
					distancia[i].setPosNodoAnt(nodo); // CC = O(1)
				}
			}
		}
		return distancia;
	}
		
	public static void main(String[] args) {
		String dirIn = "Estructura de Carpetas\\Preparacion de la Prueba\\Lote de Prueba\\Entrada\\";
		String dirOut = "Estructura de Carpetas\\Ejecucion de Prueba\\Salida Obtenida\\";
		File[] v_file = new File(dirIn).listFiles();
		for (File fileIn : v_file) {
			File fileOut = new File(dirOut + fileIn.getName().replace(".in", ".out"));
			
			System.out.println("Ejecutando: " + fileIn.getName());
			long ini = System.currentTimeMillis();
			
			EjercicioOIA ejer = new Tarzan(fileIn, fileOut);
			ejer.resolver();
			
			long fin = System.currentTimeMillis();
			System.out.println("Finalizado. Tiempo de Ejecucion: " + (fin - ini) + " milisegundos.\n");
		}
		
		String dirInPP = "Estructura de Carpetas\\Preparacion de la Prueba\\Programa Probador\\Lote de Prueba PP\\Entrada\\";
		String dirOutPP = "Estructura de Carpetas\\Preparacion de la Prueba\\Programa Probador\\Lote de Prueba PP\\Salida Esperada (validas e invalidas)\\";
		
		String [] v_fileInPP = {
				"SalidaMultiple",
				"SalidaMultiple",
				"SalidaMultiple",
				"SalidaMultiple",
				"SalidaMultiple",
				"SalidaMultiple2_EnFila",
				"SalidaMultiple2_EnFila",
				"SalidaMultiple2_EnFila",
				"SalidaMultiple2_EnFila",
				"SalidaMultiple2_EnFila",
				"SalidaMultiple2_EnFila",
				"SalidaMultiple2_EnFila",
				"SinSolucion",
				"SinSolucion"
				
		};	
		String [] v_fileOutPP = {
				"SalidaMultiple_Invalida_1",
				"SalidaMultiple_Invalida_2",
				"SalidaMultiple_Invalida_3",
				"SalidaMultiple_Valida_1",
				"SalidaMultiple_Valida_2",
				"SalidaMultiple2_EnFila_Invalida_1",
				"SalidaMultiple2_EnFila_Invalida_2",
				"SalidaMultiple2_EnFila_Invalida_3",
				"SalidaMultiple2_EnFila_Invalida_4",
				"SalidaMultiple2_EnFila_Valida_1",
				"SalidaMultiple2_EnFila_Valida_2",
				"SalidaMultiple2_EnFila_Valida_3",
				"SinSolucion_Invalida",
				"SinSolucion_Valida"
		};
		
		for (int i = 0; i < v_fileInPP.length; i++) {
			File fileInPP = new File(dirInPP + v_fileInPP[i] + ".in");
			File fileOutPP = new File(dirOutPP + v_fileOutPP[i] + ".out");
			
			long ini = System.currentTimeMillis();
			System.out.print("Ejecutando: \n"
					+ "In: " + fileInPP.getName() + "\n"
					+ "Out: " + fileOutPP.getName() + "\n");
			
			EjercicioOIA ejer = new Tarzan(fileInPP, fileOutPP);
			
			if(ejer.validacion()) {
				System.out.println("Salida VALIDA");
			} else {
				System.out.println("Salida INVALIDA");
			}
				
			long fin = System.currentTimeMillis();
			System.out.println("Finalizado. Tiempo de Ejecucion: " + (fin - ini) + " milisegundos.\n");
		}
	}

}
