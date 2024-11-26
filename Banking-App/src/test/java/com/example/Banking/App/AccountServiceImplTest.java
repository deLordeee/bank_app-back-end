package com.example.Banking.App;

import com.example.Banking.App.dto.AccountDto;
import com.example.Banking.App.entity.Account;
import com.example.Banking.App.repository.AccountRepository;
import com.example.Banking.App.repository.LoanRepository;
import com.example.Banking.App.repository.TransactoinRepository;
import com.example.Banking.App.service.JWT.JWTService;
import com.example.Banking.App.service.impl.AccountServiceImpl;
import com.example.Banking.App.transaction.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactoinRepository transactionRepository;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void createAccount_Success() {
        // Arrange
        AccountDto inputDto = new AccountDto();
        inputDto.setAccountHolderName("John Doe");
        inputDto.setPassword("password123");

        Account savedAccount = new Account();
        savedAccount.setId(1L);
        savedAccount.setAccountHolderName("John Doe");

        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        // Act
        AccountDto result = accountService.createAccount(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getAccountHolderName());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void deposit_Success() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        AccountDto result = accountService.deposit(1L, 500.0);

        // Assert
        assertEquals(1500.0, result.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void withdraw_Success() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        AccountDto result = accountService.withdraw(1L, 500.0);

        // Assert
        assertEquals(500.0, result.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }
}