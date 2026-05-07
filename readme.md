# Mockito Task
## Task Description
This task demonstrates unit testing in Java using **JUnit 5**, **Mockito**, and **AssertJ**. It covers two modules: an account management system and a store purchase flow, with tests written to reflect proper isolation between units.

---

## Modules

### AccountManager

Handles customer balance and withdrawal logic

**Withdrawal rules:**
- If the customer has sufficient balance, then deduct and return `"success"`
- If the balance is insufficient:
    - VIP customers may go into negative balance freely
    - Credit-allowed customers may go negative up to their `maxCredit` limit
    - Otherwise return `"insufficient account balance"`
    - If credit limit is exceeded then return `"maximum credit exceeded"`

### Store

Handles product purchase flow and delegates payment to `AccountManager`.

**Buy rules:**
- If product quantity is 0 then throw `RuntimeException("Product out of stock")`
- Call `AccountManager.withdraw()` with the customer and product price
- If withdrawal fails then throw `RuntimeException("Payment failure: <reason>")`
- On success decrement product quantity by 1

---

## Tests

### AccountManagerImplTest

Tests the real `AccountManagerImpl` in isolation (no mocks).

| Test | Scenario |
|------|----------|
| `givenCustomerWithSufficientBalance_whenWithdraw_thenWithdrawAndSucceed` | Normal withdrawal within balance |
| `givenVIPCustomerWithInsufficientBalance_whenWithdraw_thenCreditAndSucceed` | VIP bypasses balance check |
| `givenCreditAllowedCustomerWithInsufficientBalanceAndMaxCreditNotExceeded_whenWithdraw_thenCreditAndSucceed` | Credit used within limit |
| `givenCreditAllowedCustomerWithInsufficientBalanceAndMaxCreditExceeded_whenWithdraw_thenDisplayErrorAndFail` | Credit limit exceeded |
| `givenCustomerWithInsufficientBalance_whenWithdraw_thenDisplayErrorAndFail` | No credit, insufficient balance |

### StoreImplTest

Tests `StoreImpl` in isolation using a **Mockito mock** for `AccountManager`. Mockito is used here to avoid retesting tested logic, the aim is to test the reaction of store to different withdrawal outputs.

| Test | Scenario                                                               |
|------|------------------------------------------------------------------------|
| `givenProductWithSufficientQuantityAndSuccessfulPayment_whenBuy_thenUpdateQuantity` | Normal Scenario quantity decremented, correct price passed to withdraw |
| `givenProductWithInsufficientQuantity_whenBuy_thenThrowExceptionWithMsgOutOfStock` | Out of stock exception thrown, withdraw never called                   |
| `givenProductWithSufficientQuantityAndFailedPayment_whenBuy_thenThrowExceptionPaymentFailure` | Payment fails and exception is thrown, quantity unchanged              |

---