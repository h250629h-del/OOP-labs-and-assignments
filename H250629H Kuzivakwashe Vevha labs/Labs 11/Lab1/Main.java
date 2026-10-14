import java.time.Year;
import java.util.Arrays;

class Student {
    String name;
    int rollNo;
    double gpa;
    void display() { System.out.printf("Name: %s, Roll No: %d, GPA: %.1f%n", name, rollNo, gpa); }
    boolean isHonours() { return gpa >= 3.5; }
}
class Car {
    String brand; int year; double mileage;
    Car(String brand, int year, double mileage) { this.brand=brand; this.year=year; this.mileage=mileage; }
    void display() { System.out.printf("%s, year %d, mileage %.1f km%n", brand, year, mileage); }
    boolean isAntique() { return Year.now().getValue() - year > 25; }
}
class Rectangle {
    double width, height;
    Rectangle(double width, double height) { this.width=width; this.height=height; }
    double area() { return width*height; }
    double perimeter() { return 2*(width+height); }
}
class Address { String city; Address(String city) { this.city=city; } }
class Person { String name; Address address; Person(String name, Address address) { this.name=name; this.address=address; } }

public class Main {
    public static void main(String[] args) {
        System.out.println("== Student objects ==");
        Student s1=new Student(); s1.name="Tariro"; s1.rollNo=101; s1.gpa=3.8;
        Student s2=new Student(); s2.name="Farai"; s2.rollNo=102; s2.gpa=3.1;
        s1.display(); s2.display();
        System.out.println(s1.name+" honours? "+s1.isHonours());
        System.out.println(s2.name+" honours? "+s2.isHonours());

        System.out.println("\\n== References and null ==");
        Student a=new Student(); a.name="Original";
        Student b=a; b.name="Changed via b";
        Student c=new Student(); c.name="Changed via b";
        System.out.println("a.name = "+a.name);
        System.out.println("a == b: "+(a==b));
        System.out.println("a == c: "+(a==c));
        Student d=null;
        System.out.println("d is null: "+(d==null));

        System.out.println("\\n== Car exercise: three cars ==");
        Car[] cars={new Car("Toyota",1998,180000),new Car("Honda",2015,90000),new Car("Mazda",2000,140000)};
        for(Car car:cars) { car.display(); System.out.println("Antique? "+car.isAntique()); }

        System.out.println("\\n== Rectangle exercise ==");
        Rectangle[] rectangles={new Rectangle(2,3),new Rectangle(4,5),new Rectangle(1,9),new Rectangle(6,2),new Rectangle(3,4)};
        Rectangle largest=Arrays.stream(rectangles).max((x,y)->Double.compare(x.area(),y.area())).orElseThrow();
        for(Rectangle r:rectangles) System.out.printf("Area %.1f, perimeter %.1f%n",r.area(),r.perimeter());
        System.out.println("Largest area: "+largest.area());

        Rectangle r1=new Rectangle(2,4); Rectangle r2=r1; r2.width=99;
        System.out.println("Aliasing: r1.width after changing r2 = "+r1.width);

        Person p=new Person("Rudo",new Address("Bindura"));
        System.out.println(p.name+" lives in "+p.address.city);
        Person noAddress=new Person("Tapiwa",null);
        System.out.println("Address exists? "+(noAddress.address!=null));
    }
}
