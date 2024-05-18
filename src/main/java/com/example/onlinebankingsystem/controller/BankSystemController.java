package com.example.onlinebankingsystem.controller;

import com.example.onlinebankingsystem.model.Account;
import com.example.onlinebankingsystem.service.BankSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class BankSystemController {

    @Autowired
    private BankSystemService bankSystemService; //inject dependence

    @GetMapping("/")
    public String showHomePage() {
        return "home";
    }

    //click "Open new account" open form
    @GetMapping("/accounts/new")
    public String openAccountForm(Model model){
        model.addAttribute("account", new Account());
        return "new-account";
    }

    //click "open Account" save account
    @PostMapping("/accounts/new")
    public String openAccount(@ModelAttribute Account account) {
        bankSystemService.saveAccount(account);
        return "redirect:/";
    }

    @GetMapping("/transactions/deposit")
    public String depositForm() {
        return "deposit";
    }

    @PostMapping("/transactions/deposit")
    public String deposit(@RequestParam("accountNumber") Long id, @RequestParam("amount") BigDecimal amount) {
        bankSystemService.deposit(id, amount);
        return "redirect:/";
    }

    @GetMapping("/transactions/withdraw")
    public String withdrawForm() {
        return "withdraw";
    }

    @PostMapping("/transactions/withdraw")
    public String withdraw(@RequestParam("accountNumber") Long id, @RequestParam("amount") BigDecimal amount, Model model) {
        if (!bankSystemService.withdraw(id, amount)) {
            model.addAttribute("message", "You don't have so much money.");
            return "withdraw";
        }
        return "redirect:/";
    }
}
