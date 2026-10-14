class Employee {
    private final String name; protected double baseSalary;
    Employee(String name,double baseSalary){this.name=name;this.baseSalary=baseSalary;System.out.println("Employee constructor: "+name);}
    String getName(){return name;}
    double calculatePay(){return baseSalary;}
    void describe(){System.out.printf("%s earns %.2f%n",name,calculatePay());}
}
class Manager extends Employee {
    private final double bonus;
    Manager(String name,double salary,double bonus){super(name,salary);this.bonus=bonus;System.out.println("Manager constructor: "+name);}
    @Override double calculatePay(){return super.calculatePay()+bonus;}
}
class Developer extends Employee {
    private final int overtimeHours; private static final double RATE=25;
    Developer(String name,double salary,int hours){super(name,salary);overtimeHours=hours;}
    @Override double calculatePay(){return baseSalary+overtimeHours*RATE;}
}
class Person {
    protected final String name; protected final int age;
    Person(String name,int age){this.name=name;this.age=age;}
    void display(){System.out.println("Name: "+name+", age: "+age);}
}
class Student extends Person {
    private final String course;
    Student(String name,int age,String course){super(name,age);this.course=course;}
    @Override void display(){super.display();System.out.println("Course: "+course);}
}
class Teacher extends Person {
    private final String subject;
    Teacher(String name,int age,String subject){super(name,age);this.subject=subject;}
    @Override void display(){super.display();System.out.println("Subject: "+subject);}
}
class Vehicle { Vehicle(){System.out.println("Vehicle constructor");} }
class Car extends Vehicle { Car(){System.out.println("Car constructor");} }
class ElectricCar extends Car { ElectricCar(){System.out.println("ElectricCar constructor");} }
class Engine { void start(){System.out.println("Engine started");} }
final class CompositionCar {
    private final Engine engine=new Engine();
    void drive(){engine.start();System.out.println("Car is moving");}
    public String toString(){return "Car with an engine";}
}
public class Main {
    public static void main(String[] args) {
        Employee e=new Employee("Kudzai",2000);
        Manager m=new Manager("Nyasha",3500,800);
        Developer d=new Developer("Tatenda",3000,10);
        System.out.println("----");e.describe();m.describe();d.describe();

        System.out.println("\\nPeople hierarchy:");
        new Student("Tariro",19,"Computing").display();
        new Teacher("Farai",40,"Java").display();

        System.out.println("\\nConstructor chain:");
        new ElectricCar();
        System.out.println("\\nComposition:");
        CompositionCar car=new CompositionCar();car.drive();System.out.println(car);
    }
}
