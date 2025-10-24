
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SistemaPizzeria sistema = new SistemaPizzeria();
        
        int tiempo = 0;
        
        try {
            while (true) {
                System.out.println("\n Minuto " + tiempo + " - Sucursal: " + sistema.getSucursalActualId());
                System.out.println(" MENU PRINCIPAL");
                System.out.println("1. Nuevo cliente");
                System.out.println("2. Ver estado sucursal actual");
                System.out.println("3. Revisar colas y gestionar");
                System.out.println("4. Ver ganancias totales");
                System.out.println("5. Cambiar de sucursal");
                System.out.println("0. Salir");
                System.out.print("Seleccione opción: ");
                
                int comando = scanner.nextInt();
                if (comando == 0) break;
                
                switch(comando) {
                    case 1: sistema.crearNuevoCliente(); break;
                    case 2: sistema.mostrarEstadoSucursalActual(); break;
                    case 3: sistema.gestionarSucursalActual(); break;
                    case 4: sistema.mostrarGananciasTotales(); break;
                    case 5: 
                        System.out.print("Ingrese ID de sucursal (1 o 2): ");
                        sistema.cambiarSucursal(scanner.nextInt()); 
                        break;
                    default: System.out.println(" Comando no válido");
                }
                
                tiempo++;
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            sistema.detenerSistema();
        }
        
        System.out.println(" Sistema de Pizzerías terminado.");
        scanner.close();
    }
}