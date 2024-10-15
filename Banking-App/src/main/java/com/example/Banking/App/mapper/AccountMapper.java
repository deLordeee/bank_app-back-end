package com.example.Banking.App.mapper;

import com.example.Banking.App.dto.AccountDto;
import com.example.Banking.App.entity.Account;

public class AccountMapper {

    public static Account mapTonAccount(AccountDto accountDto){
        Account account = new Account(
                accountDto.getId(),
                accountDto.getAccountHolderName(),
                accountDto.getBalance()

        );
        return  account;
    }

    public static AccountDto mapToAccountDto(Account account){
        AccountDto accountDto = new AccountDto(
                account.getId(),
                account.getAccountHolderName(),
                account.getBalance()

        );
        return accountDto;
    }
}
