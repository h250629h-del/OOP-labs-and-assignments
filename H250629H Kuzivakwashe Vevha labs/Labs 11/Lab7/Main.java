import java.io.*;
import java.util.*;
class InsufficientFundsException extends Exception {
    private final double shortfall;
    InsufficientFundsException(String message,double shortfall){super(message);this.shortfall=shortfall;}
    double getShortfall(){return shortfall;}
}
class Wallet {
    private double balance; Wallet(double balance){if(balance<0)throw new IllegalArgumentException("Negative balance");this.balance=balance;}
    void spend(double amount)throws InsufficientFundsException{
        if(amount<=0)throw new IllegalArgumentException("Amount must be positive");
        if(amount>balance)throw new InsufficientFundsException("Cannot spend "+amount+"; balance is "+balance,amount-balance);
        balance-=amount;
    }
    double getBalance(){return balance;}
}
class InvalidAgeException extends Exception { InvalidAgeException(String message){super(message);} }
public class Main {
    static int divide(int a,int b){return a/b;}
    static int safeAt(int[] values,int index){try{return values[index];}catch(ArrayIndexOutOfBoundsException e){System.out.println("Bad index");return -1;}}
    static void validateAge(int age)throws InvalidAgeException{if(age<0||age>120)throw new InvalidAgeException("Age must be 0 to 120");}
    static void divideUserInput(){
        Scanner sc=new Scanner(System.in);
        try{
            System.out.print("Enter numerator: ");int a=sc.nextInt();
            System.out.print("Enter denominator: ");int b=sc.nextInt();
            System.out.println("Answer: "+divide(a,b));
        }catch(InputMismatchException e){System.out.println("Please enter integers only");}
        catch(ArithmeticException e){System.out.println("Cannot divide by zero");}
    }
    public static void main(String[] args) {
        try{
            System.out.println("10 / 2 = "+divide(10,2));
            System.out.println("10 / 0 = "+divide(10,0));
        }catch(ArithmeticException e){System.out.println("Math problem: "+e.getMessage());}
        finally{System.out.println("finally always runs");}
        System.out.println("Array value: "+safeAt(new int[]{10,20,30},1));
        System.out.println("Invalid array value: "+safeAt(new int[]{10,20,30},5));
        try{validateAge(150);}catch(InvalidAgeException e){System.out.println(e.getMessage());}
        Wallet w=new Wallet(100);
        try{w.spend(60);System.out.println("Balance now: "+w.getBalance());w.spend(60);}
        catch(InsufficientFundsException e){System.out.println(e.getMessage());System.out.println("Short by: "+e.getShortfall());}
        try(BufferedWriter writer=new BufferedWriter(new FileWriter("exception-demo.txt"))){writer.write("Resource safely closed");System.out.println("File written with try-with-resources");}
        catch(IOException e){System.out.println("File error: "+e.getMessage());}
        // Uncomment to practise interactive input:
        // divideUserInput();
    }
}
