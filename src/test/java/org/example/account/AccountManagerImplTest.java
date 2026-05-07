package org.example.account;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AccountManagerImplTest {
    AccountManager am = new AccountManagerImpl();
    Customer c = new Customer();

    @Test
    void givenCustomerWithSufficientBalance_whenWithdraw_thenWithdrawAndSucceed() {
        // Arrange
        c.setBalance(1000);

        // Act
        String result = am.withdraw(c, 500);

        // Assert
        assertThat(result).isEqualTo("success");
        assertThat(c.getBalance()).isEqualTo(500);
    }

    @Test
    void givenVIPCustomerWithInsufficientBalance_whenWithdraw_thenCreditAndSucceed() {
        // Arrange
        c.setBalance(10);
        c.setVip(true);

        // Act
        String result = am.withdraw(c, 500);

        // Assert
        assertThat(result).isEqualTo("success");
        assertThat(c.getBalance()).isEqualTo(-490);
    }

    @Test
    void givenCreditAllowedCustomerWithInsufficientBalanceAndMaxCreditNotExceeded_whenWithdraw_thenCreditAndSucceed() {
        // Arrange
        c.setBalance(800);
        c.setCreditAllowed(true);
        c.setMaxCredit(700);

        // Act
        String result = am.withdraw(c, 1500);

        // Assert
        assertThat(result).isEqualTo("success");
        assertThat(c.getBalance()).isEqualTo(-700);
    }

    @Test
    void givenCreditAllowedCustomerWithInsufficientBalanceAndMaxCreditExceeded_whenWithdraw_thenCreditAndSucceed() {
        // Arrange
        c.setBalance(800);
        c.setCreditAllowed(true);
        c.setMaxCredit(700);

        // Act
        String result = am.withdraw(c, 2000);

        // Assert
        assertThat(result).isEqualTo("maximum credit exceeded");
        assertThat(c.getBalance()).isEqualTo(800);
    }

    @Test
    void givenCustomerWithInsufficientBalance_whenWithdraw_thenCreditAndSucceed(){
        // Arrange
        c.setBalance(1500);

        // Act
        String result = am.withdraw(c, 2000);

        // Assert
        assertThat(result).isEqualTo("insufficient account balance");
        assertThat(c.getBalance()).isEqualTo(1500);
    }
}
