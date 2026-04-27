package bank;

/*
deposit(double amount) — 存款，餘額增加
withdraw(double amount) — 提款，餘額不足時印出警告，不能扣款
getBalance() — 印出目前餘額
 */
public class BankAccount {
    String owner; // 帳戶持有人
    double balance; // 餘額

    public BankAccount() {}
    public BankAccount(String owner, double balance) {
        this.owner = owner;
        this.balance = balance;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
}
