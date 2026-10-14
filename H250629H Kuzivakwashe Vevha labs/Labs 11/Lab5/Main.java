class Calculator {
    int add(int a,int b){return a+b;}
    int add(int a,int b,int c){return a+b+c;}
    double add(double a,double b){return a+b;}
    String add(String a,String b){return a+b;}
}
class Shape { String name(){return "Generic shape";} double area(){return 0;} }
class Circle extends Shape { private final double r; Circle(double r){this.r=r;} @Override String name(){return "Circle";} @Override double area(){return Math.PI*r*r;} }
class Rectangle extends Shape { private final double w,h; Rectangle(double w,double h){this.w=w;this.h=h;} @Override String name(){return "Rectangle";} @Override double area(){return w*h;} }
class Triangle extends Shape { private final double b,h; Triangle(double b,double h){this.b=b;this.h=h;} @Override String name(){return "Triangle";} @Override double area(){return 0.5*b*h;} }
class Animal { String type="Animal"; void speak(){System.out.println("...");} static void info(){System.out.println("Animal info");} }
class Dog extends Animal { String type="Dog"; @Override void speak(){System.out.println("Woof");} void fetch(){System.out.println("Fetching...");} static void info(){System.out.println("Dog info");} }
class Cat extends Animal { @Override void speak(){System.out.println("Meow");} }
class Cow extends Animal { @Override void speak(){System.out.println("Moo");} }
class Duck extends Animal { @Override void speak(){System.out.println("Quack");} }
public class Main {
    public static void main(String[] args) {
        Calculator c=new Calculator();
        System.out.println(c.add(2,3));System.out.println(c.add(2,3,4));
        System.out.println(c.add(2.5,3.5));System.out.println(c.add("Java","OOP"));System.out.println(c.add(2,3.5));

        Shape[] shapes={new Circle(5),new Rectangle(4,6),new Triangle(3,8)};
        double total=0;
        for(Shape s:shapes){System.out.printf("%-10s area = %.2f%n",s.name(),s.area());total+=s.area();}
        System.out.printf("Total area = %.2f%n",total);

        Animal[] animals={new Dog(),new Cat(),new Cow(),new Duck()};
        for(Animal a:animals)a.speak();
        Animal a=new Dog();a.speak();System.out.println(a.type);a.info();
        if(a instanceof Dog dog)dog.fetch();
        Animal plain=new Animal();if(!(plain instanceof Dog))System.out.println("plain is not a Dog; downcast avoided");
    }
}
