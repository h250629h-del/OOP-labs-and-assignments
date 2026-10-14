import java.util.*;
enum Day { MON,TUE,WED,THU,FRI,SAT,SUN;
    boolean isWeekend(){return this==SAT||this==SUN;}
}
enum Level { LOW,MEDIUM,HIGH }
enum Operation {
    ADD("+"){double apply(double a,double b){return a+b;}},
    MULTIPLY("*"){double apply(double a,double b){return a*b;}};
    final String symbol; Operation(String symbol){this.symbol=symbol;}
    abstract double apply(double a,double b);
}
class Outer {
    private String message="Outer message";
    class Inner { void show(){System.out.println(message+" / inner class");} }
    static class Nested { void show(){System.out.println("Static nested class");} }
}
class Point {
    private final int x,y;Point(int x,int y){this.x=x;this.y=y;}
    public boolean equals(Object o){if(this==o)return true;if(!(o instanceof Point p))return false;return x==p.x&&y==p.y;}
    public int hashCode(){return Objects.hash(x,y);}
    public String toString(){return "Point("+x+", "+y+")";}
}
record Coordinate(int x,int y){Coordinate{if(x<0||y<0)throw new IllegalArgumentException("Negative coordinate");}double distance(){return Math.sqrt(x*x+y*y);}}
record Student(String name,int age){}
public class Main {
    public static void main(String[] args) {
        Day day=Day.WED;System.out.println(day+" is a "+(day.isWeekend()?"Weekend":"Weekday")+", ordinal "+day.ordinal());
        for(Day d:Day.values())System.out.print(d+" ");System.out.println();
        for(Level l:Level.values()){switch(l){case LOW->System.out.println("LOW: proceed carefully");case MEDIUM->System.out.println("MEDIUM: pay attention");case HIGH->System.out.println("HIGH: take action");}}
        new Outer().new Inner().show();new Outer.Nested().show();
        System.out.println("Earth gravity: "+String.format("%.2f",9.8)+" m/s^2");
        System.out.println(Operation.ADD.symbol+" result: "+Operation.ADD.apply(6,7));
        System.out.println(Operation.MULTIPLY.symbol+" result: "+Operation.MULTIPLY.apply(6,7));
        Point p1=new Point(1,2),p2=new Point(1,2);System.out.println(p1==p2);System.out.println(p1.equals(p2));
        Set<Point> points=new HashSet<>();points.add(p1);points.add(p2);System.out.println("Set size: "+points.size());System.out.println(p1);
        Coordinate c=new Coordinate(3,4);System.out.println(c);System.out.println("Distance: "+c.distance());
        Student st1=new Student("Tariro",20),st2=new Student("Tariro",20);System.out.println(st1);System.out.println("Equal records? "+st1.equals(st2));System.out.println(st1.name()+" "+st1.age());
    }
}
