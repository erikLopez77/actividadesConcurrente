public class Empleado extends Thread {
    private int id;
    private int sucursalId;
    private volatile Pizza pizzaActual;
    private final Object lock = new Object();
    private boolean tieneTrabajo = false;
    private boolean detener = false;
    private final Sucursal sucursal;
    
    public Empleado(int id, int sucursalId, Sucursal sucursal) {
        this.id = id;
        this.sucursalId = sucursalId;
        this.sucursal = sucursal;
    }
    
    public void asignarPizza(Pizza pizza) {
        synchronized(lock) {
            this.pizzaActual = pizza;
            this.tieneTrabajo = true;
            System.out.println(" Sucursal " + sucursalId + " - Empleado " + id + " recibió pizza #" + pizza.getId());
            lock.notify();
        }
    }
    
    public void detener() {
        synchronized(lock) {
            this.detener = true;
            lock.notify();
        }
    }
    
    @Override
    public void run() {
        while (!detener) {
            try {
                synchronized(lock) {
                    while (!tieneTrabajo && !detener) {
                        System.out.println(" Sucursal " + sucursalId + " - Empleado " + id + " esperando trabajo...");
                        lock.wait();
                    }
                }
                
                if (detener) break;
                trabajar();
                
                if (pizzaActual != null && pizzaActual.getEstado() == PizzaState.LISTA) {
                    synchronized(lock) {
                        System.out.println(" Sucursal " + sucursalId + " - Empleado " + id + " terminó " + pizzaActual);
                        sucursal.notificarPizzaLista(pizzaActual, this);
                        tieneTrabajo = false;
                        pizzaActual = null;
                    }
                }
                
                Thread.sleep(1000);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println(" Sucursal " + sucursalId + " - Empleado " + id + " terminó su turno");
    }
    
    private void trabajar() {
        if (pizzaActual == null) return;
        
        synchronized(pizzaActual) {
            switch(pizzaActual.getEstado()) {
                case SOLICITADA:
                    pizzaActual.setEstado(PizzaState.EN_PREPARACION);
                    pizzaActual.setTiempoRestante(2);
                    System.out.println(" Sucursal " + sucursalId + " - Empleado " + id + " PREPARANDO pizza #" + pizzaActual.getId());
                    break;
                    
                case EN_PREPARACION:
                    pizzaActual.decrementarTiempo();
                    System.out.println(" Sucursal " + sucursalId + " - Empleado " + id + " preparando pizza #" + pizzaActual.getId() + 
                                     " (" + pizzaActual.getTiempoRestante() + " min)");
                    if (pizzaActual.getTiempoRestante() <= 0) {
                        pizzaActual.setEstado(PizzaState.HORNEANDO);
                        pizzaActual.setTiempoRestante(3);
                        System.out.println(" Sucursal " + sucursalId + " - Empleado " + id + " HORNEANDO pizza #" + pizzaActual.getId());
                    }
                    break;
                    
                case HORNEANDO:
                    pizzaActual.decrementarTiempo();
                    System.out.println(" Sucursal " + sucursalId + " - Empleado " + id + " horneando pizza #" + pizzaActual.getId() + 
                                     " (" + pizzaActual.getTiempoRestante() + " min)");
                    if (pizzaActual.getTiempoRestante() <= 0) {
                        pizzaActual.setEstado(PizzaState.LISTA);
                        System.out.println(" Sucursal " + sucursalId + " - ¡Pizza #" + pizzaActual.getId() + " LISTA!");
                    }
                    break;
            }
        }
    }
    
    public boolean estaLibre() {
        synchronized(lock) {
            return !tieneTrabajo;
        }
    }
    
    public Pizza getPizzaActual() {
        synchronized(lock) {
            return pizzaActual;
        }
    }
    
    public int getSucursalId() { return sucursalId; }
    public int getIdEmpleado() { return id; }
}