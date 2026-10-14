public abstract class Account {
    protected String accountNumber;
    protected double balance;

    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("[" + accountNumber + "] Deposit failed: Amount must be positive.");
            return;
        }
        this.balance += amount;
        System.out.println("[" + accountNumber + "] Deposited: $" + amount + " | New Balance: $" + balance);
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public abstract void withdraw(double amount);

    public abstract void endOfMonth();
}