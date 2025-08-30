public class HilosFiguras {
    public static void main(String[] args) {
        System.out.println("Hilo padre iniciado");

        Hijo1 h1 = new Hijo1();
        Hijo2 h2 = new Hijo2();

        h1.start();
        h2.start();

        // El padre NO espera a los hijos
        System.out.println("Hilo padre terminado");
    }
}