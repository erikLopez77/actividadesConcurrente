class Hijo1 extends Thread {
    public void run() {
        System.out.println("Hilo hijo 1 iniciado");

        // Nietos
        Thread nieto1 = new Thread(() -> Figura.triangulo(3, 4, 5, 4));
        Thread nieto2 = new Thread(() -> Figura.trapecio(3, 5, 4, 4, 4));

        nieto1.start();
        nieto2.start();

        try {
            nieto1.join();
            nieto2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Hilo hijo 1 terminado");
    }
}