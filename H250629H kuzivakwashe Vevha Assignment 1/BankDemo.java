import java.util.ArrayList;
import java.util.List;

public class BankDemo {
    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();

        // Savings Account: Min balance $100, 5% interest
        accounts.add(new SavingsAccount("SAV-101", 500.00, 100.00, 0.05));

        // Current Account: Overdraft limit $200, $15 monthly fee
        accounts.add(new CurrentAccount("CUR-201", 100.00, 200.00, 15.00));

        System.out.println("=== 1. DEMONSTRATING EDGE CASES ===");

        // Savings edge case: Reject withdrawal taking balance below minimum ($100)
        System.out.println("\n-- SavingsAccount Edge Case --");
        accounts.get(0).withdraw(450.00); // Fails (would leave $50)
        accounts.get(0).withdraw(350.00); // Succeeds (leaves $150)

        // Current edge case: Overdraft within limit ($200)
        System.out.println("\n-- CurrentAccount Overdraft Case --");
        accounts.get(1).withdraw(250.00); // Succeeds (leaves -$150)
        accounts.get(1).withdraw(100.00); // Fails (would reach -$250, past -$200 limit)

        System.out.println("\n=== 2. POLYMORPHIC LOOP (No Subclass Casting) ===");

        // Polymorphic processing calling methods via Account reference
        for (Account acc : accounts) {
            System.out.println("\nProcessing Account: " + acc.getAccountNumber());
            acc.withdraw(20.00);  // Invokes appropriate subclass withdraw()
            acc.endOfMonth();     // Invokes appropriate subclass endOfMonth()
        }
    }
}