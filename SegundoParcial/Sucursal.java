import java.util.*;

public class Sucursal {
    private int id;
    private String nombre;
    private List<Empleado> empleados = new ArrayList<>();
    private final Queue<Pizza> pizzasEnEspera = new LinkedList<>();
    private final Map<Integer, Cliente> clientesEsperandoPizza = new HashMap<>();
    private final Map<Integer, Pizza> pizzasListasParaEntrega = new HashMap<>();
    private final Object sucursalLock = new Object();
    
    private double gananciasTotales = 0.0;
    private int pizzasVendidas = 0;
    private int clientesAtendidos = 0;
    
    public Sucursal(int id, String nombre, int numeroEmpleados) {
        this.id = id;
        this.nombre = nombre;
        contratarEmpleados(numeroEmpleados);
    }
    
    private void contratarEmpleados(int cantidad) {
        for (int i = 1; i <= cantidad; i++) {
            Empleado empleado = new Empleado(i, this.id, this);
            empleados.add(empleado);
            empleado.start();
        }
        System.out.println(" " + nombre + " - " + cantidad + " empleados contratados");
    }
    
    public void detenerEmpleados() {
        for (Empleado empleado : empleados) {
            empleado.detener();
        }
    }
    
    public void asignarPizza(Pizza pizza) {
        synchronized(sucursalLock) {
            boolean asignada = false;
            
            for (Empleado empleado : empleados) {
                if (empleado.estaLibre()) {
                    empleado.asignarPizza(pizza);
                    asignada = true;
                    break;
                }
            }
            
            if (!asignada) {
                pizzasEnEspera.add(pizza);
                System.out.println(  nombre + " - Pizza #" + pizza.getId() + " EN ESPERA. Cola: " + pizzasEnEspera.size());
            }
        }
        asignarPizzasEnEspera();
    }
    
    public void notificarPizzaLista(Pizza pizza, Empleado empleado) {
        synchronized(sucursalLock) {
            pizzasListasParaEntrega.put(pizza.getId(), pizza);
            System.out.println(" " + nombre + " - Pizza #" + pizza.getId() + " LISTA para entrega");
            sucursalLock.notifyAll();
        }
        gestionarEntregas();
        asignarPizzasEnEspera();
    }
    
    public void registrarCliente(Cliente cliente) {
        synchronized(sucursalLock) {
            clientesEsperandoPizza.put(cliente.getIdCliente(), cliente);
            System.out.println(" " + nombre + " - Cliente #" + cliente.getIdCliente() + " registrado");
        }
    }
    
    public void registrarVenta(double monto) {
        synchronized(sucursalLock) {
            gananciasTotales += monto;
            pizzasVendidas++;
            clientesAtendidos++;
            System.out.println(" " + nombre + " - Venta registrada: $" + monto + " | Total: $" + gananciasTotales);
        }
    }
    
    public void gestionarEntregas() {
        synchronized(sucursalLock) {
            Iterator<Map.Entry<Integer, Pizza>> iterator = pizzasListasParaEntrega.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Pizza> entry = iterator.next();
                int pizzaId = entry.getKey();
                Pizza pizza = entry.getValue();
                
                Cliente cliente = clientesEsperandoPizza.get(pizzaId);
                if (cliente != null) {
                    cliente.recibirPizza(pizza);
                    iterator.remove();
                    clientesEsperandoPizza.remove(pizzaId);
                    System.out.println(" " + nombre + " - ENTREGA: Pizza #" + pizzaId + " → Cliente #" + cliente.getIdCliente());
                }
            }
        }
    }
    
    public void asignarPizzasEnEspera() {
        synchronized(sucursalLock) {
            if (pizzasEnEspera.isEmpty()) return;
            
            Iterator<Pizza> iterator = pizzasEnEspera.iterator();
            while (iterator.hasNext()) {
                Pizza pizza = iterator.next();
                for (Empleado empleado : empleados) {
                    if (empleado.estaLibre()) {
                        empleado.asignarPizza(pizza);
                        iterator.remove();
                        System.out.println(" " + nombre + " - Asignando pizza #" + pizza.getId() + " al empleado " + empleado.getIdEmpleado());
                        break;
                    }
                }
            }
        }
    }
    
    // Getters
    public List<Empleado> getEmpleados() { return new ArrayList<>(empleados); }
    public int getPizzasEnEspera() { synchronized(sucursalLock) { return pizzasEnEspera.size(); } }
    public int getPizzasListas() { synchronized(sucursalLock) { return pizzasListasParaEntrega.size(); } }
    public int getClientesEsperando() { synchronized(sucursalLock) { return clientesEsperandoPizza.size(); } }
    
    public List<Pizza> getTodasLasPizzasEnProceso() {
        synchronized(sucursalLock) {
            List<Pizza> todas = new ArrayList<>();
            todas.addAll(pizzasEnEspera);
            todas.addAll(pizzasListasParaEntrega.values());
            for (Empleado empleado : empleados) {
                if (!empleado.estaLibre()) {
                    todas.add(empleado.getPizzaActual());
                }
            }
            return todas;
        }
    }
    
    public double getGananciasTotales() { synchronized(sucursalLock) { return gananciasTotales; } }
    public int getPizzasVendidas() { synchronized(sucursalLock) { return pizzasVendidas; } }
    public int getClientesAtendidos() { synchronized(sucursalLock) { return clientesAtendidos; } }
    public String getNombre() { return nombre; }
    public int getId() { return id; }
}