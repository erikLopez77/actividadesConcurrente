
import java.util.logging.Level;
import java.util.logging.Logger;


public class ActividadesConcurrente {

    public static void main(String[] args) {
        System.out.println("Entrada al cine, se muestran los anuncios mientras llega la gente y demás ...");
        
        Thread mensajes = new Thread(()->{
            System.out.println("Inicio a usar el celular");
            try{
                Thread.sleep(3000);
                System.out.println("Empiezo a contestar mensajes, revisar instagram \n Apago el tono de llamada \n");
            }catch(InterruptedException e){
                System.out.println("Mensajes interrumpidos");
            }
        });
        
        Thread baño=new Thread(()->{
            try{
                Thread.sleep(2000);
                System.out.println("Una vez que salgo del baño, procedo a lavar mis manos y a regresar a la sala de cine \n");
            }catch(InterruptedException e){
                System.out.println("Mensajes interrumpidos");
            }
        });
        
        mensajes.start();
        baño.start();
        
        try {
            mensajes.join();
            baño.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ActividadesConcurrente.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i=3;i>=0;i--){
            try {
                Thread.sleep(1000);
                System.out.println("La pelicula inicia en "+i);
            } catch (InterruptedException ex) {
                Logger.getLogger(ActividadesConcurrente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.out.println("Inicia la pelicula");
    }
    
}
