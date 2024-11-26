package com.example.Banking.App;

import com.example.Banking.App.entity.Account;
import com.example.Banking.App.entity.Deposit;
import com.example.Banking.App.entity.DepositStatus;
import com.example.Banking.App.repository.AccountRepository;
import com.example.Banking.App.repository.DepositRepository;
import com.example.Banking.App.service.impl.DepositServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepositServiceImplTest {
    @Mock
    private DepositRepository depositRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DepositServiceImpl depositService;

    @Test
    void createDeposit_Success() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(depositRepository.save(any(Deposit.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Deposit result = depositService.createDeposit(1L, 500.0, 12);

        // Assert
        assertNotNull(result);
        assertEquals(500.0, result.getPrincipal());
        assertEquals(DepositStatus.ACTIVE, result.getStatus());
    }

    @Test
    void withdrawDeposit_Success() {
        // Arrange
        LocalDate maturityDate = LocalDate.of(2025, 11, 26);

        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);

        Deposit deposit = new Deposit();
        deposit.setId(1L);
        deposit.setInterestEarned(100.0);
        deposit.setDepositerAccount(account);
        deposit.setMaturityDate(maturityDate);
        deposit.setStatus(DepositStatus.ACTIVE);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(depositRepository.findById(1L)).thenReturn(Optional.of(deposit));
        when(depositRepository.saveAndFlush(deposit)).thenReturn(deposit);
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        // Act
        Deposit result = depositService.withdrawDeposit(1L, 1L, 50.0);

        // Assert
        assertNotNull(result);
        assertEquals(50.0, result.getInterestEarned());
        verify(depositRepository).saveAndFlush(any(Deposit.class));
        verify(accountRepository).saveAndFlush(any(Account.class));
    }
}