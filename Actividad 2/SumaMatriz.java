import java.util.*;
import java.util.Random;

public class SumaMatriz {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        Random rand = new Random();
        
        System.out.println("Escribe el valor de i: (valores enteros)");
        int i = leer.nextInt();

        System.out.println("Escribe el valor de j: (valores enteros)");
        int j = leer.nextInt();

        int[][] matriz = new int[i][j];
        
        // Llenar matriz con valores aleatorios
        for (int x = 0; x < i; x++) {
            for (int y = 0; y < j; y++) {
                matriz[x][y] = rand.nextInt(100);
                System.out.println("[" + x + "," + y + "]= " + matriz[x][y]);
            }
        }   
        
        System.out.println("¿Deseas sumar por filas (f) o columnas (c)? ");
        String letra = leer.next().toLowerCase();  // Convertir a minúscula

        if(letra.equals("f")){
            int[] sumaFilas = new int[i];
            
            // Calcular suma por filas
            for (int x = 0; x < i; x++) {
                for (int y = 0; y < j; y++) {
                    sumaFilas[x] += matriz[x][y];
                }
                System.out.println("Suma fila " + x + ": " + sumaFilas[x]);
            }
            
        } else if(letra.equals("c")){
            int[] sumaColumnas = new int[j];
            
            // Calcular suma por columnas
            for (int y = 0; y < j; y++) {
                for (int x = 0; x < i; x++) {
                    sumaColumnas[y] += matriz[x][y];
                }
                System.out.println("Suma columna " + y + ": " + sumaColumnas[y]);
            }
        } else {
            System.out.println("Opción no válida. Debes ingresar 'f' o 'c'");
        }
        
        leer.close();
    }
}