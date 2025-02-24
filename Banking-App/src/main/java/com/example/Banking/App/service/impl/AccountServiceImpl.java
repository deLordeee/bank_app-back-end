package com.example.Banking.App.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.Banking.App.dto.AccountDto;
import com.example.Banking.App.dto.TransactionDto;
import com.example.Banking.App.entity.Account;
import com.example.Banking.App.entity.kafka.Producer;
import com.example.Banking.App.mapper.AccountMapper;
import com.example.Banking.App.repository.AccountRepository;
import com.example.Banking.App.repository.LoanRepository;
import com.example.Banking.App.repository.TransactoinRepository;
import com.example.Banking.App.service.AccountService;
import com.example.Banking.App.service.JWT.JWTService;
import com.example.Banking.App.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactoinRepository transactoinRepository;

    private final LoanRepository loanRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;


    private Producer accountProducer;


    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    public AccountServiceImpl(Producer accountProducer,AccountRepository accountRepository, TransactoinRepository transactoinRepository, LoanRepository loanRepository) {
        this.accountRepository = accountRepository;
        this.transactoinRepository = transactoinRepository;
        this.loanRepository = loanRepository;
        this.accountProducer = accountProducer;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapTonAccount(accountDto);
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Account savedAccount = accountRepository.save(account);
        accountProducer.sendMessage(AccountMapper.mapToAccountDto(savedAccount));
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));

        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Invalid amount!");
        }
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));

        double total = account.getBalance() + amount;
        account.setBalance(total);

        // Create and save transaction
        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setType("DEPOSIT");
        transaction.setTransactionDate(LocalDateTime.now());
        transactoinRepository.save(transaction);

        // Publish transaction event
        TransactionDto transactionDto = new TransactionDto();
        // Set transaction details
        accountProducer.sendTransactionMessage(transactionDto);

        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        if(amount <=0){
            throw new IllegalArgumentException("Invalid amount!");
        }
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));


        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Not enough money!");
        }

        double total = account.getBalance() - amount;
        account.setBalance(total);

        // Save the withdrawal transaction
        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setType("WITHDRAWAL");
        transaction.setTransactionDate(LocalDateTime.now());

        account.setMonthly_spending(account.getMonthly_spending() + amount);

        transactoinRepository.save(transaction);
// Publish transaction event
        TransactionDto transactionDto = new TransactionDto();
        // Set transaction details
        accountProducer.sendTransactionMessage(transactionDto);

        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }


    @Override
    public String verify(Account account) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(account.getAccountHolderName(),account.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(account.getAccountHolderName());
        }
        return "Fail";
    }

    @Override
    public AccountDto getAccountByName(String accountHolderName) {
        Account account = accountRepository.findByAccountHolderName(accountHolderName);
                //.orElseThrow(() -> new ResourceNotFoundException("Account", "accountHolderName", accountHolderName));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public Account findByAccountHolderName(String name) {

        Account account = accountRepository.findByAccountHolderName(name);
        return account;
    }


    // Add this mapper method if not already present


    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(AccountMapper::mapToAccountDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));

        accountRepository.deleteById(id);
    }

    @Override
    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactoinRepository.findByAccountId(accountId);
    }

    public List<AccountDto> transfer(Long sender_id , Long receiver_id , double amount){
        if(amount <=0){
            throw new IllegalArgumentException("Invalid amount!");
        }
        Account sender = accountRepository
                .findById(sender_id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));
        Account receiver = accountRepository
                .findById(receiver_id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));
        if(sender_id == receiver_id){
            throw new RuntimeException("Cannot send money to your own account!");
        }

        if (sender.getBalance() < amount) {
            throw new RuntimeException("Not enough money!");
        }

        System.out.println(sender.getMonthly_spending());
        sender.setMonthly_spending(sender.getMonthly_spending() + amount);

        receiver.setMonthly_earn(receiver.getMonthly_earn() + amount);

        double sender_total = sender.getBalance() - amount;
        sender.setBalance(sender_total);

        double receiver_total = receiver.getBalance() + amount;
        receiver.setBalance(receiver_total);

        // Publish transaction event
        TransactionDto transactionDto = new TransactionDto();
        // Set transaction details
        accountProducer.sendTransactionMessage(transactionDto);

        Account savedAccount = accountRepository.save(sender);
        Account savedAccount2 = accountRepository.save(receiver);
        List<AccountDto> savedAccounts = Arrays.asList(
                AccountMapper.mapToAccountDto(savedAccount),
                AccountMapper.mapToAccountDto(savedAccount2)
        );
        return  savedAccounts;
    }


}