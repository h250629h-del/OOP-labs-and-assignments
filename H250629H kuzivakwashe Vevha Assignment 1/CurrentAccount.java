public class CurrentAccount extends Account {
    private double overdraftLimit;
    private double monthlyFee;

    public CurrentAccount(String accountNumber, double initialBalance, double overdraftLimit, double monthlyFee) {
        super(accountNumber, initialBalance);
        this.overdraftLimit = overdraftLimit;
        this.monthlyFee = monthlyFee;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("[" + accountNumber + "] Withdrawal failed: Amount must be positive.");
            return;
        }

        if (balance - amount < -overdraftLimit) {
            System.out.printf("[%s] Withdrawal of $%.2f REJECTED: Exceeds overdraft limit ($%.2f). Current: $%.2f%n",
                    accountNumber, amount, overdraftLimit, balance);
        } else {
            balance -= amount;
            System.out.printf("[%s] Withdrew: $%.2f | New Balance: $%.2f%n", accountNumber, amount, balance);
        }
    }

    @Override
    public void endOfMonth() {
        balance -= monthlyFee;
        System.out.printf("[%s] End of month maintenance fee deducted ($%.2f) | New Balance: $%.2f%n",
                accountNumber, monthlyFee, balance);
    }
}