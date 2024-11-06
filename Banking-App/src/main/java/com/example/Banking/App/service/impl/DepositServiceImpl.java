package com.example.Banking.App.service.impl;

import com.example.Banking.App.entity.Account;
import com.example.Banking.App.entity.Deposit;
import com.example.Banking.App.entity.DepositStatus;
import com.example.Banking.App.entity.Loan;
import com.example.Banking.App.repository.AccountRepository;
import com.example.Banking.App.repository.DepositRepository;

import com.example.Banking.App.service.DepositService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.example.Banking.App.entity.Loan_Status.ACTIVE;

@Service
@Transactional
public class DepositServiceImpl implements DepositService {
    private final DepositRepository depositRepository;

    private final AccountRepository accountRepository;

    public DepositServiceImpl( DepositRepository depositRepository, AccountRepository accountRepository) {
        this.depositRepository = depositRepository;
        this.accountRepository = accountRepository;
    }
    @Override
    public Deposit createDeposit(Long id, double amount, int years) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount!");
        }
        if (years <= 0) {
            throw new IllegalArgumentException("Invalid amount!");
        }
        Account depositer = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));

        double interestRate = 0.09;

        Deposit tempDeposit = new Deposit();
        double earnedInterest = tempDeposit.earnedMoney(amount, interestRate,years);

        Deposit deposit = new Deposit(null , amount , earnedInterest ,
                interestRate,years*12,LocalDate.now() ,
                LocalDate.now().plusYears(years),depositer, DepositStatus.ACTIVE);


        return depositRepository.save(deposit);
    }

    @Override
    public List<Deposit> getAllDeposits() {
        List<Deposit> deposits = depositRepository.findAll();
        return deposits;
    }

    @Override
    public Deposit withdrawDeposit(Long account_id ,Long deposit_id, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount!");
        }
        Account depositer = accountRepository
                .findById(account_id)
                .orElseThrow(()-> new RuntimeException("Account not found"));

        Deposit deposit = depositRepository
                .findById(deposit_id)
                .orElseThrow(()->new RuntimeException("Deposit not found"));

        LocalDate maturityDate = deposit.getMaturityDate();
        LocalDate currentDate = LocalDate.now();

        System.out.println("Maturity Date: " + maturityDate);
        System.out.println("Current Date: " + currentDate);

        if(maturityDate.equals(currentDate)){
            deposit.setStatus(DepositStatus.CLOSED);
            System.out.println("Status updated to: " + deposit.getStatus());
        }
        if(amount > deposit.getInterestEarned()){
            throw new IllegalArgumentException("Invalid amount!");
        }
        else{
            deposit.setInterestEarned(deposit.getInterestEarned() - amount);
            depositer.setBalance(depositer.getBalance() + amount);
        }
        Account savedAccount = accountRepository.save(depositer);
        Deposit savedDeposit = depositRepository.save(deposit);

        accountRepository.saveAndFlush(depositer);
        return depositRepository.saveAndFlush(deposit);
    }
}
