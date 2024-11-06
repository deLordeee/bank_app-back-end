package com.example.Banking.App.service;

import com.example.Banking.App.entity.Deposit;


import java.util.List;

public interface DepositService {

    Deposit createDeposit(Long id , double amount , int years);
    List<Deposit> getAllDeposits();

    Deposit withdrawDeposit(Long account_id,Long loan_id , double amount);
}
