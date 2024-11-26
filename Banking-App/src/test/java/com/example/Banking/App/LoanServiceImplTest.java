package com.example.Banking.App;

import com.example.Banking.App.entity.Account;
import com.example.Banking.App.entity.Loan;
import com.example.Banking.App.repository.AccountRepository;
import com.example.Banking.App.repository.LoanRepository;
import com.example.Banking.App.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.Banking.App.entity.Loan_Status.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    void takeLoan_Success() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Loan result = loanService.takeLoan(1L, 5000.0, 2);

        // Assert
        assertNotNull(result);
        assertEquals(5000.0, result.getPrincipal());
        assertEquals(ACTIVE, result.getStatus());
    }

    @Test
    void payLoan_Success() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setRemainToPay(500.0);
        loan.setBorrowerAccount(account);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // Act
        Loan result = loanService.payLoan(1L, 200.0);

        // Assert
        assertEquals(300.0, result.getRemainToPay());
    }
}