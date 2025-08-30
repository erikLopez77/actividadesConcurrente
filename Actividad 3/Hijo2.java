class Hijo2 extends Thread {
    public void run() {
        System.out.println("Hilo hijo 2 iniciado");

        // Nietos
        Thread nieto3 = new Thread(() -> Figura.cuadrado(5));
        Thread nieto4 = new Thread(() -> Figura.rectangulo(4, 6));

        nieto3.start();
        nieto4.start();

        try {
            nieto3.join();
            nieto4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Hilo hijo 2 terminado");
    }
}