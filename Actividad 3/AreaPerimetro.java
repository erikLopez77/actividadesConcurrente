
package areaperimetro;

public class AreaPerimetro {

   
    public static void main(String[] args) {
        Cuadrado cuadrado=new Cuadrado(5);
        Rectangulo rectangulo=new Rectangulo(5,3);
        Triangulo triangulo= new Triangulo(4,4);
        Trapecio trapecio= new Trapecio(3,6,7,8);
        
        Thread hiloCuad=new Thread(cuadrado);
        Thread hiloRect=new Thread(rectangulo);
        Thread hiloTri=new Thread(triangulo);
        Thread hiloTrap=new Thread(trapecio);
        
        System.out.println("\n⚡ EJECUTANDO OPERACIONES EN PARALELO");
        System.out.println("───────────────────────────────────────────");
        
        hiloCuad.start();
        hiloRect.start();
        hiloTri.start();
        hiloTrap.start();
    }
    
}

// Clase para la suma
class Cuadrado implements Runnable {
    private double lado;
    
    public Cuadrado(double lado) {
        this.lado = lado;
    }
    
    @Override
    public void run() {
        double area = lado * lado;
        double perimetro=lado*4;
        System.out.println("➤El área del cuadrado es: "+area+"u², y su perimetro es: "+perimetro+"u");
    }
}
class Rectangulo implements Runnable {
    private double base;
    private double altura;
    
    public Rectangulo(double base,double altura) {
        this.base = base;
        this.altura=altura;
    }
    
    @Override
    public void run() {
        double area = base * altura;
        double perimetro=base+base+altura+altura;
        System.out.println("➤El área del rectángulo es: "+area+"u², y su perimetro es: "+perimetro+"u");
    }
}
class Trapecio implements Runnable{
    private double bMenor,bMayor,altura,lado;
    
    public Trapecio(double bMenor,double bMayor,double altura, double lado){
        this.altura=altura;
        this.bMayor=bMayor;
        this.bMenor=bMenor;
        this.lado=lado;
    }
    @Override
    public void run(){
        double area=(bMayor*bMenor*altura)/2;
        double perimetro=lado+lado+bMayor+bMenor;
        
        System.out.println("➤El área del trapecio es: "+area+"u², y su perimetro es: "+perimetro+"u");
    }
}
class Triangulo implements Runnable{
    private double base,altura;
    
    public Triangulo(double base,double altura){
        this.altura=altura;
        this.base=base;
    }
    @Override
    public void run(){
        double area=(base*altura)/2;
        double perimetro=base*3;
        
        System.out.println("➤El área del triángulo es: "+area+"u², y su perimetro es: "+perimetro+"u");
    }

}