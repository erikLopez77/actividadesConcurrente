import java.util.*;

public class PizzeriaSimulation {
    // Estados de las pizzas
    enum PizzaState { SOLICITADA, EN_PREPARACION, HORNEANDO, LISTA, ENTREGADA }
    
    // Clase para representar una pizza
    static class Pizza {
        int id;
        PizzaState estado;
        int tiempoRestante;
        
        Pizza(int id) {
            this.id = id;
            this.estado = PizzaState.SOLICITADA;
            this.tiempoRestante = 0;
        }
        
        @Override
        public String toString() {
            return "Pizza #" + id + " [" + estado + "]";
        }
    }
    
    // Clase para representar un empleado
    static class Empleado {
        int id;
        Pizza pizzaActual;
        
        Empleado(int id) {
            this.id = id;
            this.pizzaActual = null;
        }
        
        void trabajar() {
            if (pizzaActual == null) return;
            
            switch(pizzaActual.estado) {
                case SOLICITADA:
                    pizzaActual.estado = PizzaState.EN_PREPARACION;
                    pizzaActual.tiempoRestante = 2; // Tiempo de preparación
                    System.out.println("Empleado " + id + " preparando pizza #" + pizzaActual.id);
                    break;
                    
                case EN_PREPARACION:
                    if (--pizzaActual.tiempoRestante <= 0) {
                        pizzaActual.estado = PizzaState.HORNEANDO;
                        pizzaActual.tiempoRestante = 3; // Tiempo de horneado
                        System.out.println("Empleado " + id + " horneando pizza #" + pizzaActual.id);
                    }
                    break;
                    
                case HORNEANDO:
                    if (--pizzaActual.tiempoRestante <= 0) {
                        pizzaActual.estado = PizzaState.LISTA;
                        System.out.println("¡Pizza #" + pizzaActual.id + " lista para entregar!");
                    }
                    break;
            }
        }
        
        boolean estaLibre() {
            return pizzaActual == null || pizzaActual.estado == PizzaState.LISTA;
        }
        
        Pizza entregarPizza() {
            Pizza pizza = pizzaActual;
            pizzaActual = null;
            return pizza;
        }
    }
    
    // Clase para representar un cliente
    static class Cliente {
        int id;
        Pizza pizza;
        
        Cliente(int id) {
            this.id = id;
        }
        
        void recibirPizza(Pizza pizza) {
            this.pizza = pizza;
            pizza.estado = PizzaState.ENTREGADA;
            System.out.println("Cliente #" + id + " recibió " + pizza);
        }
    }
    
    // Clase para representar al jefe
    static class Jefe {
        List<Empleado> empleados = new ArrayList<>();
        
        void contratarEmpleados(int cantidad) {
            for (int i = 1; i <= cantidad; i++) {
                empleados.add(new Empleado(i));
            }
        }
        
        void asignarPizza(Pizza pizza) {
            for (Empleado empleado : empleados) {
                if (empleado.estaLibre()) {
                    empleado.pizzaActual = pizza;
                    System.out.println("Jefe asignó " + pizza + " al empleado " + empleado.id);
                    return;
                }
            }
            System.out.println("¡Todos los empleados están ocupados! Pizza #" + pizza.id + " en espera");
        }
        
        void gestionarEntrega(Cliente cliente) {
            for (Empleado empleado : empleados) {
                if (empleado.pizzaActual != null && 
                    empleado.pizzaActual.estado == PizzaState.LISTA && 
                    empleado.pizzaActual.id == cliente.id) {
                    
                    Pizza pizza = empleado.entregarPizza();
                    cliente.recibirPizza(pizza);
                    return;
                }
            }
            System.out.println("El cliente #" + cliente.id + " está esperando su pizza...");
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        Jefe jefe = new Jefe();
        
        // Contratar empleados
        jefe.contratarEmpleados(2);
        
        // Lista de clientes y pizzas
        List<Cliente> clientes = new ArrayList<>();
        List<Pizza> pizzas = new ArrayList<>();
        
        // Contador de tiempo
        int tiempo = 0;
        int siguienteClienteId = 1;
        
        while (true) {
            System.out.print("\nMinuto " + tiempo + " > ");
            System.out.println("Comandos: \n 1 = Nuevo cliente,\n 2 = Ver estado,\n 3 = Salir");
            int comando = scanner.nextInt();
            
            if (comando == 3) break;
            
            // Nuevo cliente
            if (comando == 1) {
                Cliente nuevoCliente = new Cliente(siguienteClienteId);
                Pizza nuevaPizza = new Pizza(siguienteClienteId);
                
                clientes.add(nuevoCliente);
                pizzas.add(nuevaPizza);
                
                System.out.println("¡Llegó cliente #" + siguienteClienteId + " y pidió una pizza!");
                jefe.asignarPizza(nuevaPizza);
                
                siguienteClienteId++;
            }
            
            // Ver estado actual
            if (comando == 2) {
                System.out.println("\n--- ESTADO ACTUAL ---");
                System.out.println("Clientes en espera: " + clientes.size());
                
                System.out.println("Pizzas:");
                for (Pizza pizza : pizzas) {
                    System.out.println("  " + pizza);
                }
                
                System.out.println("Empleados:");
                for (Empleado empleado : jefe.empleados) {
                    String estado = (empleado.pizzaActual == null) ? 
                        "LIBRE" : "Trabajando en " + empleado.pizzaActual;
                    System.out.println("  Empleado " + empleado.id + ": " + estado);
                }
            }
            
            // Procesar trabajo de empleados
            for (Empleado empleado : jefe.empleados) {
                empleado.trabajar();
            }
            
            // Intentar entregar pizzas listas
            for (Cliente cliente : clientes) {
                jefe.gestionarEntrega(cliente);
            }
            
            // Remover clientes si se cumple esa condicion
            clientes.removeIf(cliente -> cliente.pizza != null);
            
            tiempo++;
        }
        
        System.out.println("Simulación terminada.");
        scanner.close();
    }
}