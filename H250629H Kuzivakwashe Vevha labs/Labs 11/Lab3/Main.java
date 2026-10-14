import java.util.*;
class BankAccount {
    private final String accountNumber;
    private String ownerName;
    private double balance;
    private final List<String> history=new ArrayList<>();
    BankAccount(String number,String owner,double opening) {
        if(opening<0) throw new IllegalArgumentException("Opening balance cannot be negative");
        accountNumber=Objects.requireNonNull(number);
        setOwnerName(owner); balance=opening; history.add("Opened: "+opening);
    }
    String getAccountNumber(){return accountNumber;}
    String getOwnerName(){return ownerName;}
    double getBalance(){return balance;}
    void setOwnerName(String name){if(name==null||name.isBlank())throw new IllegalArgumentException("Owner name required");ownerName=name.trim();}
    void deposit(double amount){if(amount<=0)throw new IllegalArgumentException("Deposit must be positive");balance+=amount;history.add("Deposit: "+amount);}
    boolean withdraw(double amount){if(amount<=0)throw new IllegalArgumentException("Withdrawal must be positive");if(amount>balance)return false;balance-=amount;history.add("Withdrawal: "+amount);return true;}
    List<String> getHistory(){return Collections.unmodifiableList(new ArrayList<>(history));}
    public String toString(){return String.format("%s [%s] balance: %.2f",accountNumber,ownerName,balance);}
}
class Student {
    private final String name; private int marks;
    Student(String name,int marks){this.name=name;setMarks(marks);}
    void setMarks(int marks){if(marks<0||marks>100)throw new IllegalArgumentException("Marks must be 0 to 100");this.marks=marks;}
    int getMarks(){return marks;}
    String getGrade(){return marks>=80?"A":marks>=65?"B":marks>=50?"C":"F";}
    public String toString(){return name+": "+marks+" ("+getGrade()+")";}
}
final class ImmutablePerson {
    private final String name; private final int age;
    ImmutablePerson(String name,int age){this.name=Objects.requireNonNull(name);if(age<0)throw new IllegalArgumentException("Age cannot be negative");this.age=age;}
    String getName(){return name;} int getAge(){return age;}
}
class Team {
    private final List<String> members=new ArrayList<>();
    void addMember(String name){members.add(name);}
    List<String> getMembers(){return Collections.unmodifiableList(new ArrayList<>(members));}
}
class Temperature {
    private final double kelvin;
    Temperature(double kelvin){if(kelvin<0)throw new IllegalArgumentException("Below absolute zero");this.kelvin=kelvin;}
    double getCelsius(){return kelvin-273.15;} double getFahrenheit(){return getCelsius()*9/5+32;}
}
public class Main {
    public static void main(String[] args) {
        BankAccount acc=new BankAccount("ACC-001","Tendai",500);
        acc.deposit(250); System.out.println(acc);
        System.out.println("Withdraw 100: "+acc.withdraw(100));
        System.out.println("Withdraw 5000: "+acc.withdraw(5000));
        System.out.println(acc);
        try{acc.deposit(-50);}catch(IllegalArgumentException e){System.out.println("Rejected: "+e.getMessage());}
        System.out.println("History: "+acc.getHistory());

        Student s=new Student("Rudo",85); System.out.println(s);
        try{s.setMarks(120);}catch(IllegalArgumentException e){System.out.println("Invalid marks rejected");}
        System.out.println(new ImmutablePerson("Kuda",20).getName()+" is immutable");
        Team team=new Team();team.addMember("Chipo");System.out.println("Team members: "+team.getMembers());
        Temperature temp=new Temperature(300);System.out.printf("300 K = %.2f C = %.2f F%n",temp.getCelsius(),temp.getFahrenheit());
    }
}
