
public class Pizza {
    private int id;
    private volatile PizzaState estado;
    private int tiempoRestante;
    private double precio;
    private int sucursalId;
    
    public Pizza(int id, int sucursalId) {
        this.id = id;
        this.sucursalId = sucursalId;
        this.estado = PizzaState.SOLICITADA;
        this.tiempoRestante = 0;
        this.precio = 150.0;
    }
    
    // Getters y Setters (igual que antes)
    public int getId() { return id; }
    public int getSucursalId() { return sucursalId; }
    public double getPrecio() { return precio; }
    
    public PizzaState getEstado() { 
        synchronized(this) { return estado; }
    }
    
    public void setEstado(PizzaState estado) {
        synchronized(this) { this.estado = estado; }
    }
    
    public int getTiempoRestante() { 
        synchronized(this) { return tiempoRestante; }
    }
    
    public void setTiempoRestante(int tiempo) {
        synchronized(this) { this.tiempoRestante = tiempo; }
    }
    
    public void decrementarTiempo() {
        synchronized(this) {
            if (tiempoRestante > 0) tiempoRestante--;
        }
    }
    
    @Override
    public String toString() {
        return "Pizza #" + id + " [" + getEstado() + "] - Sucursal " + sucursalId;
    }
}