class Book {
    private static int count=0;
    private final int id;
    private String title, author;
    private double price;
    static { System.out.println("Book class loaded"); }
    { id=++count; }
    Book(String title,String author,double price) { this.title=title; this.author=author; this.price=price; }
    Book(String title,String author) { this(title,author,0.0); }
    Book() { this("Untitled","Unknown"); }
    static int getCount() { return count; }
    void display() { System.out.printf("#%d %s by %s ($%.2f)%n",id,title,author,price); }
}
class Circle {
    private static int count=0; private final double radius;
    Circle() { this(1.0); }
    Circle(double radius) { if(radius<=0) throw new IllegalArgumentException("Radius must be positive"); this.radius=radius; count++; }
    double area() { return Math.PI*radius*radius; }
    static int getCount() { return count; }
}
class Employee {
    private static int nextId=1; private final int id; private final String name; private final double salary;
    Employee(String name,double salary) { this.id=nextId++; this.name=name; this.salary=salary; }
    Employee(String name) { this(name,0); }
    Employee() { this("Unknown",0); }
    Employee(Employee other) { this(other.name,other.salary); }
    public String toString() { return "Employee #"+id+": "+name+", salary="+salary; }
}
class Temperature {
    private final double celsius;
    private Temperature(double celsius) { if(celsius < -273.15) throw new IllegalArgumentException("Below absolute zero"); this.celsius=celsius; }
    static Temperature fromCelsius(double c) { return new Temperature(c); }
    static Temperature fromFahrenheit(double f) { return new Temperature((f-32)*5/9); }
    double getCelsius() { return celsius; }
    double getFahrenheit() { return celsius*9/5+32; }
}
class InitOrder {
    static int a=show("static field");
    int b=show("instance field");
    static { show("static block"); }
    { show("instance block"); }
    InitOrder() { show("constructor"); }
    static int show(String s) { System.out.println(s); return 1; }
}
public class Main {
    public static void main(String[] args) {
        Book b1=new Book("Clean Code","Robert Martin",39.99);
        Book b2=new Book("Effective Java","Joshua Bloch"); Book b3=new Book();
        b1.display(); b2.display(); b3.display(); System.out.println("Total books: "+Book.getCount());

        System.out.println("\\nCircle area: "+new Circle().area());
        new Circle(2); System.out.println("Circles created: "+Circle.getCount());
        Employee e1=new Employee("Tendai",1200); Employee e2=new Employee(e1);
        System.out.println(e1); System.out.println(e2);
        Temperature t=Temperature.fromFahrenheit(98.6);
        System.out.printf("Temperature: %.1f C, %.1f F%n",t.getCelsius(),t.getFahrenheit());

        System.out.println("\\nInitialisation order:");
        new InitOrder(); new InitOrder();
    }
}
