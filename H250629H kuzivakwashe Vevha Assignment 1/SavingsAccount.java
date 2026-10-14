public class SavingsAccount extends Account {
    private double minimumBalance;
    private double interestRate; // e.g., 0.05 for 5%

    public SavingsAccount(String accountNumber, double initialBalance, double minimumBalance, double interestRate) {
        super(accountNumber, initialBalance);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("[" + accountNumber + "] Withdrawal failed: Amount must be positive.");
            return;
        }

        if (balance - amount < minimumBalance) {
            System.out.printf("[%s] Withdrawal of $%.2f REJECTED: Balance cannot fall below minimum required ($%.2f). Current: $%.2f%n",
                    accountNumber, amount, minimumBalance, balance);
        } else {
            balance -= amount;
            System.out.printf("[%s] Withdrew: $%.2f | New Balance: $%.2f%n", accountNumber, amount, balance);
        }
    }

    @Override
    public void endOfMonth() {
        double interest = balance * interestRate;
        balance += interest;
        System.out.printf("[%s] End of month interest added ($%.2f) | New Balance: $%.2f%n",
                accountNumber, interest, balance);
    }
}