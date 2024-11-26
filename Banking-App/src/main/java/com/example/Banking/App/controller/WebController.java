package com.example.Banking.App.controller;

import com.example.Banking.App.dto.AccountDto;
import com.example.Banking.App.entity.Account;
import com.example.Banking.App.entity.Deposit;
import com.example.Banking.App.entity.Loan;
import com.example.Banking.App.service.AccountService;
import com.example.Banking.App.service.DepositService;
import com.example.Banking.App.service.LoanService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class WebController {

    private final AccountService accountService;
    private final LoanService loanService;
    private final DepositService depositService;

    public WebController(AccountService accountService, LoanService loanService, DepositService depositService) {
        this.accountService = accountService;
        this.loanService = loanService;
        this.depositService = depositService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        AccountDto account = accountService.getAccountByName(userDetails.getUsername());
        model.addAttribute("account", account);
        return "dashboard";
    }

    @GetMapping("/deposit")
    public String depositPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        AccountDto account = accountService.getAccountByName(userDetails.getUsername());
        List<Deposit> deposits = depositService.getDepositsByAccountId(account.getId());

        model.addAttribute("account", account);
        model.addAttribute("deposits", deposits);
        return "deposit";
    }

    @GetMapping("/view/loans")  // Changed from "/loans" to "/view/loans"
    public String loansPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        AccountDto account = accountService.getAccountByName(userDetails.getUsername());
        List<Loan> loans = loanService.getLoansByAccountId(account.getId());

        model.addAttribute("account", account);
        model.addAttribute("loans", loans);
        return "loans";
    }

    @GetMapping("/transfer")
    public String transferPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        AccountDto account = accountService.getAccountByName(userDetails.getUsername());
        model.addAttribute("account", account);
        model.addAttribute("currentUserId", account.getId());
        return "transfer";
    }

    @GetMapping("/withdraw")
    public String withdrawPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        AccountDto account = accountService.getAccountByName(userDetails.getUsername());
        model.addAttribute("account", account);
        return "withdraw";
    }

    @PostMapping("/withdraw/{id}")
    public String withdraw(@PathVariable Long id, @RequestParam Double amount) {
        accountService.withdraw(id, amount);
        return "redirect:/dashboard";
    }
    @PostMapping("/deposit/{id}/createDeposit")
    public String createDeposit(@PathVariable Long id,
                                @RequestParam Double amount,
                                @RequestParam Integer maturityDuration) {
        depositService.createDeposit(id, amount, maturityDuration);
        return "redirect:/deposit";
    }

    @PostMapping("/deposit/{account_id}/{deposit_id}/withdrawDeposit")
    public String withdrawDeposit(@PathVariable Long account_id,
                                  @PathVariable Long deposit_id,
                                  @RequestParam Double amount) {
        depositService.withdrawDeposit(account_id, deposit_id, amount);
        return "redirect:/deposit";
    }

    @PostMapping("/loans/{id}/takeLoan")
    public String takeLoan(@PathVariable Long id,
                           @RequestParam Double amount,
                           @RequestParam Integer durationInYears) {
        loanService.takeLoan(id, amount, durationInYears);
        return "redirect:/view/loans";
    }

    @PostMapping("/loans/{id}/payLoan")
    public String payLoan(@PathVariable Long id, @RequestParam(required = true) Double amount) {
        if (amount == null) {
            throw new RuntimeException("Amount is required!");
        }
        loanService.payLoan(id, amount);
        return "redirect:/view/loans";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam("senderId") Long senderId,
                           @RequestParam("recipientName") String recipientName,
                           @RequestParam("amount") Double amount,
                           RedirectAttributes redirectAttributes) {
        try {
            AccountDto receiverAccount = accountService.getAccountByName(recipientName);
            accountService.transfer(senderId, receiverAccount.getId(), amount);
            redirectAttributes.addFlashAttribute("success", "Transfer completed successfully");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/transfer";
        }
    }
    @PostMapping("/register")
    public String registerAccount(@ModelAttribute AccountDto accountDto) {
        accountService.createAccount(accountDto);
        return "redirect:/login?registration=success";
    }
    @PostMapping("/deposit/{account_id}/{deposit_id}/withdrawAll")
    public String withdrawAll(@PathVariable Long account_id, @PathVariable Long deposit_id) {
        depositService.withdrawAll(account_id, deposit_id);
        return "redirect:/deposit";
    }
}
