package com.example.Banking.App.service;

import com.example.Banking.App.dto.AccountDto;
import com.example.Banking.App.entity.Account;
import com.example.Banking.App.entity.Loan;
import com.example.Banking.App.transaction.Transaction;

import java.util.List;
import java.util.Optional;


public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountById(Long id);

    AccountDto deposit(Long id , double amount);

    AccountDto withdraw(Long id , double amount);

    List<AccountDto> getAllAccounts();
    void deleteAccount(Long id);

    List<Transaction> getTransactionHistory(Long accoutId);

    List<AccountDto> transfer(Long sender_id , Long receiver_id , double amount);

    String verify(Account account);


    AccountDto getAccountByName(String accountHolderName);


}
