package org.example.account;

public class AccountManagerImpl implements AccountManager {
    @Override
    public void deposit(Customer customer, int amount) {
        customer.setBalance(customer.getBalance() + amount);
    }

    @Override
    public String withdraw(Customer customer, int amount) {
        int expectedBalance = customer.getBalance() - amount;
        if (expectedBalance < 0 && !customer.isVip()) {
            if (!customer.isCreditAllowed()) {
                return "insufficient account balance";
            } else if (Math.abs(expectedBalance) > customer.getMaxCredit()) {
                return "maximum credit exceeded";
            }
        }
        customer.setBalance(expectedBalance);
        return "success";
    }
}
