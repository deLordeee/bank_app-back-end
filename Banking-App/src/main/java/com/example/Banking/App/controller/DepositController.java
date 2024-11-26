package com.example.Banking.App.controller;

import com.example.Banking.App.entity.Deposit;
import com.example.Banking.App.entity.Loan;
import com.example.Banking.App.service.DepositService;
import com.example.Banking.App.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {
    @Autowired
    private DepositService depositService;

    @GetMapping
    public ResponseEntity<List<Deposit>> getAllDeposits(){
        List<Deposit> deposits = depositService.getAllDeposits();
        return ResponseEntity.ok(deposits);
    }

    @PutMapping("/{id}/createDeposit")
    public ResponseEntity<Deposit> createDeposit(@PathVariable Long id , @RequestBody Map<String, Double> request){
        double amount = request.get("amount");
        double years = request.get("maturityDuration");

        Deposit deposit = depositService.createDeposit(id ,amount , (int)years);
        return ResponseEntity.ok(deposit);
    }
    @PutMapping("/{account_id}/{deposit_id}/withdrawDeposit")
    public ResponseEntity<Deposit> withdrawDeposit(@PathVariable Long account_id ,@PathVariable Long deposit_id ,@RequestBody Map<String, Double> request ){
        double amount = request.get("amount");

        Deposit deposit = depositService.withdrawDeposit(account_id , deposit_id , amount);
        return ResponseEntity.ok(deposit);
    }
    @PostMapping("/{account_id}/{deposit_id}/withdrawAll")
    public String withdrawAll(@PathVariable Long account_id, @PathVariable Long deposit_id) {
        depositService.withdrawAll(account_id, deposit_id);
        return "redirect:/deposit/" + account_id;
    }
}
