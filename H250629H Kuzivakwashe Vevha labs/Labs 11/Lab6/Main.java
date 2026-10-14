import java.util.*;
abstract class Shape {
    abstract double area();
    abstract double perimeter();
}
class Circle extends Shape {
    private final double r; Circle(double r){this.r=r;}
    double area(){return Math.PI*r*r;} double perimeter(){return 2*Math.PI*r;}
}
class Rectangle extends Shape {
    protected final double w,h; Rectangle(double w,double h){this.w=w;this.h=h;}
    double area(){return w*h;} double perimeter(){return 2*(w+h);}
}
class Square extends Rectangle { Square(double side){super(side,side);} }
interface Flyable { void fly(); }
class Bird implements Flyable { public void fly(){System.out.println("Bird flaps its wings");} }
class Aeroplane implements Flyable { public void fly(){System.out.println("Aeroplane uses engines");} }
abstract class Account {
    protected double balance; Account(double balance){this.balance=balance;}
    void deposit(double amount){if(amount<=0)throw new IllegalArgumentException("Deposit must be positive");balance+=amount;}
    abstract double monthlyFee();
    double getBalance(){return balance;}
}
class SavingsAccount extends Account { SavingsAccount(double b){super(b);} double monthlyFee(){return 2.0;} }
class CurrentAccount extends Account { CurrentAccount(double b){super(b);} double monthlyFee(){return 5.0;} }
class Player implements Comparable<Player> {
    String name; int score; Player(String n,int s){name=n;score=s;}
    public int compareTo(Player p){return Integer.compare(p.score,score);}
    public String toString(){return name+"("+score+")";}
}
interface HelloA { default void hello(){System.out.println("Hello from A");} }
interface HelloB { default void hello(){System.out.println("Hello from B");} }
class Greeter implements HelloA,HelloB { public void hello(){HelloA.super.hello();HelloB.super.hello();} }
public class Main {
    public static void main(String[] args) {
        Shape[] shapes={new Circle(2),new Rectangle(3,4),new Square(5)};
        for(Shape s:shapes)System.out.printf("%s area=%.2f perimeter=%.2f%n",s.getClass().getSimpleName(),s.area(),s.perimeter());
        Flyable[] flyers={new Bird(),new Aeroplane()};for(Flyable f:flyers)f.fly();
        Account[] accounts={new SavingsAccount(100),new CurrentAccount(200)};
        for(Account a:accounts){a.deposit(50);System.out.println("Balance "+a.getBalance()+", fee "+a.monthlyFee());}
        List<Player> players=new ArrayList<>(List.of(new Player("Anesu",70),new Player("Blessing",95),new Player("Chenai",82)));
        Collections.sort(players);System.out.println("By score: "+players);
        players.sort(Comparator.comparing(p->p.name));System.out.println("By name: "+players);
        new Greeter().hello();
    }
}
