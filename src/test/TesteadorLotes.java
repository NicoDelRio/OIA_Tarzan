package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TesteadorLotes {
	
	public static void main(String[] args) throws IOException {
	
		String dirOutEsperados = "Estructura de Carpetas\\Preparacion de la Prueba\\Lote de Prueba\\Salida Esperada\\";
		String dirOutObtenidos = "Estructura de Carpetas\\Ejecucion de Prueba\\Salida Obtenida\\";
	
		File[] filesEsperados = new File(dirOutEsperados).listFiles();
		File[] filesObtenidos = new File(dirOutObtenidos).listFiles();
		
		for (int i = 0; i < filesObtenidos.length; i++) {
			if((filesEsperados[i].getName()).equals(filesObtenidos[i].getName())) {
				if(compareFilesText(filesEsperados[i],filesObtenidos[i])) {
					System.out.println("Lote: " + filesEsperados[i].getName() + " esperado COINCIDE con obtenido.\n");
				}
				else {
					System.out.println("REVISAR!!!");
					System.out.println("Lote: " + filesEsperados[i].getName() + " esperado NO COINCIDE con obtenido.\n");
				}
			}
		}
		
	}
	
	public static boolean compareFilesText(File f1, File f2) throws IOException {
		FileReader fr1 = new FileReader(f1);
		FileReader fr2 = new FileReader(f2);
		BufferedReader fb1 = new BufferedReader(fr1);
		BufferedReader fb2 = new BufferedReader(fr2);
		String linea1 = fb1.readLine();
		String linea2 = fb2.readLine();
		while(linea1 != null && linea2 != null) {
			if(!linea1.equals(linea2)) {
				fb1.close();
				fb2.close();
				return false;
			}
			linea1 = fb1.readLine();
			linea2 = fb2.readLine();
		}
		fb1.close();
		fb2.close();
		if(!(linea1 == null && linea2 == null)) {
			return false;
		}
		return true;
	}
}
