
public class Cliente extends Thread {
    private int id;
    private int sucursalId;
    private volatile Pizza pizza;
    private final Sucursal sucursal;
    private volatile boolean pizzaRecibida = false;
    private final Object lock = new Object();
    
    public Cliente(int id, int sucursalId, Sucursal sucursal) {
        this.id = id;
        this.sucursalId = sucursalId;
        this.sucursal = sucursal;
    }
    
    @Override
    public void run() {
        synchronized(lock) {
            try {
                while (!pizzaRecibida) {
                    System.out.println(" Sucursal " + sucursalId + " - Cliente #" + id + " esperando su pizza...");
                    lock.wait();
                }
                System.out.println(" Sucursal " + sucursalId + " - Cliente #" + id + " satisfecho, se va a casa");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public void recibirPizza(Pizza pizza) {
        synchronized(lock) {
            this.pizza = pizza;
            pizza.setEstado(PizzaState.ENTREGADA);
            pizzaRecibida = true;
            sucursal.registrarVenta(pizza.getPrecio());
            lock.notify();
        }
        System.out.println(" Sucursal " + sucursalId + " - Cliente #" + id + " RECIBIÓ " + pizza);
    }
    
    public int getIdCliente() { return id; }
    public int getSucursalId() { return sucursalId; }
    public boolean isPizzaRecibida() { return pizzaRecibida; }
    public Pizza getPizza() { return pizza; }
}