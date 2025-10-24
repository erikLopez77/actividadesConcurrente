import java.util.*;

public class SistemaPizzeria {
    private Map<Integer, Sucursal> sucursales = new HashMap<>();
    private Map<Integer, List<Cliente>> clientesPorSucursal = new HashMap<>();
    private int sucursalActualId = 1;
    private int siguienteClienteId = 1;
    private int siguientePizzaId = 1;
    
    public SistemaPizzeria() {
        sucursales.put(1, new Sucursal(1, "Sucursal Centro", 2));
        sucursales.put(2, new Sucursal(2, "Sucursal Norte", 2));
        
        clientesPorSucursal.put(1, new ArrayList<>());
        clientesPorSucursal.put(2, new ArrayList<>());
        
        System.out.println(" Sistema de Pizzerías Inicializado");
    }
    
    public void cambiarSucursal(int nuevaSucursalId) {
        if (sucursales.containsKey(nuevaSucursalId)) {
            sucursalActualId = nuevaSucursalId;
            System.out.println("\n Cambiando a " + getSucursalActual().getNombre());
        } else {
            System.out.println(" Sucursal no válida");
        }
    }
    
    public void crearNuevoCliente() {
        Sucursal sucursalActual = getSucursalActual();
        Cliente nuevoCliente = new Cliente(siguienteClienteId, sucursalActualId, sucursalActual);
        Pizza nuevaPizza = new Pizza(siguientePizzaId, sucursalActualId);
        
        clientesPorSucursal.get(sucursalActualId).add(nuevoCliente);
        
        System.out.println("\n " + sucursalActual.getNombre() + " - NUEVO CLIENTE #" + siguienteClienteId);
        
        sucursalActual.registrarCliente(nuevoCliente);
        nuevoCliente.start();
        sucursalActual.asignarPizza(nuevaPizza);
        
        siguienteClienteId++;
        siguientePizzaId++;
    }
    
    public void mostrarEstadoSucursalActual() {
        Sucursal sucursal = getSucursalActual();
        List<Cliente> clientes = clientesPorSucursal.get(sucursalActualId);
        
        System.out.println("\n=== " + sucursal.getNombre() + " ===");
        System.out.println(" Sucursal ID: " + sucursalActualId);
        
        System.out.println("\n Clientes activos: " + countClientesActivos(clientes));
        
        System.out.println("\n Pizzas en proceso:");
        List<Pizza> pizzasEnProceso = sucursal.getTodasLasPizzasEnProceso();
        if (pizzasEnProceso.isEmpty()) {
            System.out.println("   No hay pizzas en proceso");
        } else {
            for (Pizza pizza : pizzasEnProceso) {
                System.out.println("   " + pizza);
            }
        }
        
        System.out.println("\n Empleados:");
        for (Empleado empleado : sucursal.getEmpleados()) {
            String estado = (empleado.estaLibre()) ? 
                " LIBRE" : " Ocupado con pizza #" + empleado.getPizzaActual().getId();
            System.out.println("   Empleado " + empleado.getIdEmpleado() + ": " + estado);
        }
        
        System.out.println("\n Estadísticas:");
        System.out.println("   Pizzas en espera: " + sucursal.getPizzasEnEspera());
        System.out.println("   Pizzas listas: " + sucursal.getPizzasListas());
        System.out.println("   Clientes esperando: " + sucursal.getClientesEsperando());
        System.out.println("   Pizzas vendidas: " + sucursal.getPizzasVendidas());
        System.out.println("   Ganancias: $" + sucursal.getGananciasTotales());
    }
    
    public void mostrarGananciasTotales() {
        System.out.println("\n GANANCIAS TOTALES DEL SISTEMA");
        System.out.println("=================================");
        
        double gananciasTotales = 0;
        
        for (Sucursal sucursal : sucursales.values()) {
            double gananciasSucursal = sucursal.getGananciasTotales();
            System.out.println(sucursal.getNombre() + ": $" + gananciasSucursal);
            gananciasTotales += gananciasSucursal;
        }
        
        System.out.println("TOTAL SISTEMA: $" + gananciasTotales);
    }
    
    public void gestionarSucursalActual() {
        Sucursal sucursal = getSucursalActual();
        sucursal.asignarPizzasEnEspera();
        sucursal.gestionarEntregas();
        
        List<Cliente> clientes = clientesPorSucursal.get(sucursalActualId);
        clientes.removeIf(cliente -> cliente.isPizzaRecibida());
        
        System.out.println( sucursal.getNombre() + " - Gestión completada");
    }
    
    public void detenerSistema() {
        for (Sucursal sucursal : sucursales.values()) {
            sucursal.detenerEmpleados();
        }
        for (List<Cliente> clientes : clientesPorSucursal.values()) {
            for (Cliente cliente : clientes) {
                cliente.interrupt();
            }
        }
    }
    
    private int countClientesActivos(List<Cliente> clientes) {
        int count = 0;
        for (Cliente cliente : clientes) {
            if (!cliente.isPizzaRecibida()) count++;
        }
        return count;
    }
    
    private Sucursal getSucursalActual() {
        return sucursales.get(sucursalActualId);
    }
    
    public int getSucursalActualId() { return sucursalActualId; }
}