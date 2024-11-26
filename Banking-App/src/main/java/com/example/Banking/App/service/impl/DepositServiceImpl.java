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
    public Deposit createDeposit(Long id, double amount, int months) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount!");
        }
        if (months <= 0) {
            throw new IllegalArgumentException("Invalid months!");
        }

        Account depositer = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));

        // First check if there's enough balance
        if (depositer.getBalance() < amount) {
            throw new IllegalArgumentException("Not enough money!");
        }

        // Then deduct the amount
        depositer.setBalance(depositer.getBalance() - amount);
        accountRepository.save(depositer);

        double interestRate = 0.09;
        Deposit tempDeposit = new Deposit();
        double earnedInterest = tempDeposit.earnedMoney(amount, interestRate, months);

        Deposit deposit = new Deposit(null, amount, earnedInterest,
                interestRate, months, LocalDate.now(),
                LocalDate.now().plusYears(months/12), depositer, DepositStatus.ACTIVE);

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
    @Override
    public List<Deposit> getDepositsByAccountId(Long accountId) {
        return depositRepository.findByDepositerAccount_Id(accountId);
    }
    @Override
    public void withdrawAll(Long accountId, Long depositId) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new RuntimeException("Deposit not found"));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        double totalAmount = deposit.getPrincipal() + deposit.getInterestEarned();
        account.setBalance(account.getBalance() + totalAmount);

        accountRepository.save(account);
        depositRepository.delete(deposit);
    }
}
