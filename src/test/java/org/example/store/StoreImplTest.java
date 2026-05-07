package org.example.store;

import org.example.account.AccountManager;
import org.example.account.Customer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class StoreImplTest {
    AccountManager accountManager = mock(AccountManager.class);
    Store store = new StoreImpl(accountManager);
    Product product = new Product();
    Customer customer = new Customer();

    @Test
    void givenProductWithSufficientQuantityAndSuccessfulPayment_whenBuy_thenUpdateQuantity() {
        // Arrange
        when(accountManager.withdraw(any(), anyInt())).thenReturn("success");
        product.setQuantity(4);
        product.setPrice(100);

        // Act
        store.buy(product, customer);

        // Assert
        assertThat(product.getQuantity()).isEqualTo(3);
        verify(accountManager, times(1)).withdraw(customer, product.getPrice());
    }

    @Test
    void givenProductWithInsufficientQuantity_whenBuy_thenThrowException() {
        // Arrange
        product.setQuantity(0);

        // Act and Assert
        assertThatThrownBy(()-> store.buy(product, customer))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product out of stock");
        verify(accountManager, never()).withdraw(any(), anyInt());
    }

    @Test
    void givenProductWithSufficientQuantityAndFailedPayment_whenBuy_thenThrowException() {
        // Arrange
        when(accountManager.withdraw(any(), anyInt())).thenReturn("insufficient account balance");
        product.setQuantity(3);

        // Act and Assert
        assertThatThrownBy(()-> store.buy(product, customer))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("Payment failure:");
        assertThat(product.getQuantity()).isEqualTo(3);
    }
}
