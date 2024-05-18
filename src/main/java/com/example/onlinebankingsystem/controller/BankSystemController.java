package com.example.onlinebankingsystem.controller;

import com.example.onlinebankingsystem.model.Account;
import com.example.onlinebankingsystem.service.BankSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String createNewAccountForm(Model model){
        model.addAttribute("account", new Account());
        return "new-account";
    }

    //click "open Account" save account
    @PostMapping("/accounts/new")
    public String saveNewAccount(@ModelAttribute Account account) {
        bankSystemService.saveAccount(account);
        return "redirect:/";
    }
}
