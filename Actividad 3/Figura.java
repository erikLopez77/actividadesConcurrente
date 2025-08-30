class Figura {
    // Métodos estáticos para cálculos
    static void triangulo(double base, double altura, double lado1, double lado2) {
        double area = (base * altura) / 2;
        double perimetro = base + lado1 + lado2;
        System.out.println("Triángulo -> Área: " + area + ", Perímetro: " + perimetro);
    }

    static void trapecio(double base1, double base2, double altura, double lado1, double lado2) {
        double area = ((base1 + base2) * altura) / 2;
        double perimetro = base1 + base2 + lado1 + lado2;
        System.out.println("Trapecio -> Área: " + area + ", Perímetro: " + perimetro);
    }

    static void cuadrado(double lado) {
        double area = lado * lado;
        double perimetro = 4 * lado;
        System.out.println("Cuadrado -> Área: " + area + ", Perímetro: " + perimetro);
    }

    static void rectangulo(double base, double altura) {
        double area = base * altura;
        double perimetro = 2 * (base + altura);
        System.out.println("Rectángulo -> Área: " + area + ", Perímetro: " + perimetro);
    }
}