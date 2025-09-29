public class Familia {

    // Total de piezas de pollo disponibles
    private static int piezasPollo = 45;

    // Clase para representar a cada hijo
    static class Hijo extends Thread {
        private final int id;
        private final String grupo;

        //Constructor de los hijos
        public Hijo(int id, String grupo, int prioridad) {
            this.id = id;
            this.grupo = grupo;
            setPriority(prioridad);
        }

        @Override
        public void run() {
            try {
                int pieza;
                if (piezasPollo > 0) {
                    pieza = piezasPollo--;
                    System.out.println("Hijo " + id + " (" + grupo + ") obtuvo pieza #" + pieza
                            + " de pollo. (Consomé ilimitado)");
                } else {
                    System.out.println("Hijo " + id + " (" + grupo + ") llegó tarde, sin pollo!");
                }

                // Para que otros hilos de misma prioridad también se ejecuten
                Thread.yield();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Método auxiliar: lanza a un grupo de hijos, espera a que terminen
    private static void atenderGrupo(Thread[] hijosGrupo) {
        for (Thread h : hijosGrupo) h.start();
        for (Thread h : hijosGrupo) {
            try {
                h.join(); // Espera a que todos en el grupo terminen
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("---- Grupo terminado ----\n");
    }

    public static void main(String[] args) {

        Thread madre = new Thread(() -> {

            int contador = 0;

            //  GEMELOS (5 grupo) 
            Thread[] gemelos = new Thread[10];
            for (int i = 0; i < 10; i++) {
                gemelos[i] = new Hijo(++contador, "Gemelo", Thread.MAX_PRIORITY); // prioridad alta
            }
            System.out.println("Madre: ¡Gemelos, su turno!");
            atenderGrupo(gemelos);

            //  TRILLIZOS (3 grupos) 
            Thread[] trillizos = new Thread[9];
            for (int i = 0; i < 9; i++) {
                trillizos[i] = new Hijo(++contador, "Trillizo", Thread.NORM_PRIORITY + 2);// prioridad 7
            }
            System.out.println("Madre: ¡Trillizos, su turno!");
            atenderGrupo(trillizos);

            //  CUATRILLIZOS (2 grupos) 
            Thread[] cuatrillizos = new Thread[8];
            for (int i = 0; i < 8; i++) {
                cuatrillizos[i] = new Hijo(++contador, "Cuatrillizo", Thread.NORM_PRIORITY); //prioridad 5
            }
            System.out.println("Madre: ¡Cuatrillizos, su turno!");
            atenderGrupo(cuatrillizos);

            // HIJOS ÚNICOS
            Thread[] unicos = new Thread[45 - contador];
            for (int i = 0; i < unicos.length; i++) {
                unicos[i] = new Hijo(++contador, "Unico", Thread.MIN_PRIORITY); //prioridad baja
            }
            System.out.println("Madre: ¡Hijos únicos, su turno!");
            atenderGrupo(unicos);

            System.out.println("\nTodos los hijos han recibido su pieza de pollo (o consomé ilimitado).");
        });

        madre.start();

        try {
            madre.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
