package com.example.onlinebankingsystem.controller;

import com.example.onlinebankingsystem.model.Account;
import com.example.onlinebankingsystem.service.BankSystemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class BankSystemController {

    private final BankSystemService bankSystemService; //inject dependence

    private static final String REDIRECT_HOME = "redirect:/";

    private static final String TRANSFER_PAGE = "transfer";

    private static final String WITHDRAW_PAGE = "withdraw";

    private static final String HOME_PAGE = "home";

    private static final String NEW_ACCOUNT_PAGE = "new_account";

    private static final String DEPOSIT_PAGE = "deposit";

    private static final String ACCOUNT_ATTRIBUTE = "account";

    private static final String ACCOUNT_NUMBER_ATTRIBUTE = "accountNumber";

    // - easier to see dependencies and amount of them
    // - easier to mock in some cases
    // - looks more natural
    public BankSystemController(BankSystemService bankSystemService) {
        this.bankSystemService = bankSystemService;
    }

    @GetMapping("/")
    public String showHomePage() {
        return HOME_PAGE;
    }

    @GetMapping("/accounts/new")
    public String openAccountForm(Model model){
        model.addAttribute(ACCOUNT_ATTRIBUTE, new Account());
        return NEW_ACCOUNT_PAGE;
    }


    @PostMapping("/accounts/new")
    public String openAccount(@ModelAttribute Account account) {
        bankSystemService.saveAccount(account);
        return REDIRECT_HOME;
    }

    @GetMapping("/transactions/deposit")
    public String depositForm() {
        return DEPOSIT_PAGE;
    }

    @PostMapping("/transactions/deposit")
    public String deposit(@RequestParam(ACCOUNT_NUMBER_ATTRIBUTE) Long accountNumber, @RequestParam(ACCOUNT_ATTRIBUTE) BigDecimal amount) {
        bankSystemService.deposit(accountNumber, amount);
        return REDIRECT_HOME;
    }

    @GetMapping("/transactions/withdraw")
    public String withdrawForm() {
        return WITHDRAW_PAGE;
    }

    @PostMapping("/transactions/withdraw")
    public String withdraw(@RequestParam(ACCOUNT_NUMBER_ATTRIBUTE) Long accountNumber, @RequestParam(ACCOUNT_ATTRIBUTE) BigDecimal amount, Model model) {
        if (!bankSystemService.withdraw(accountNumber, amount)) {
            model.addAttribute("message", "You don't have so much money.");
            return WITHDRAW_PAGE;
        }
        return REDIRECT_HOME;
    }

    @GetMapping("/transactions/transfer")
    public String transferForm() {
        return TRANSFER_PAGE;
    }

    @PostMapping("/transactions/transfer")
    public String transfer(@RequestParam("fromAccount") Long fromAccount, @RequestParam("toAccount") Long toAccount,
                           @RequestParam("amount") BigDecimal amount, Model model) {
        if (!bankSystemService.transfer(fromAccount, toAccount ,amount)) {
            model.addAttribute("message", fromAccount + " account don't have so much money.");
            return TRANSFER_PAGE;
        }
        return REDIRECT_HOME;
    }
}
