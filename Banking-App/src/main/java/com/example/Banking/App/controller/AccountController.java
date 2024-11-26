package com.example.Banking.App.controller;


import com.example.Banking.App.dto.AccountDto;
import com.example.Banking.App.entity.Account;
import com.example.Banking.App.entity.Loan;
import com.example.Banking.App.service.AccountService;
import com.example.Banking.App.transaction.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private AccountService accountService;

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/createAccount")
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto){
        return new ResponseEntity<>(accountService.createAccount(accountDto) , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String login(@RequestBody Account account){
        return accountService.verify(account);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id){
        AccountDto accountDto = accountService.getAccountById(id);
        return ResponseEntity.ok(accountDto);
    }

    @PutMapping("/{id}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Long id ,
                                              @RequestBody Map<String , Double> request){

        Double amount = request.get("amount");
        AccountDto accountDto = accountService.deposit(id , amount);
        return  ResponseEntity.ok(accountDto);

    }

    @PostMapping("{id}/withdraw")
    public ResponseEntity<AccountDto> withdraw(@PathVariable Long id ,
                                               @RequestBody Map<String , Double> request ){
        Double amount = request.get("amount");
        AccountDto accountDto = accountService.withdraw(id , amount);
        return  ResponseEntity.ok(accountDto);
    }
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        List<AccountDto> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted successfully");
    }
    @GetMapping("/{accountId}/transactions")
    public List<Transaction> getTransactionHistory(@PathVariable Long accountId) {
        return accountService.getTransactionHistory(accountId);
    }

    @PutMapping("/{sender_id}/transfer/{receiver_id}")
    public ResponseEntity<List<AccountDto>> transfer(@PathVariable Long sender_id , @PathVariable Long receiver_id , @RequestBody Map<String , Double> request){
        Double amount = request.get("amount");
        List<AccountDto> accountDto = accountService.transfer(sender_id , receiver_id , amount);
        return  ResponseEntity.ok(accountDto);
    }


}
