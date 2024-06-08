package com.example.onlinebankingsystem.controller;

import com.example.onlinebankingsystem.exception.NotEnoughAmountException;
import com.example.onlinebankingsystem.exception.NotFoundException;
import com.example.onlinebankingsystem.model.Account;
import com.example.onlinebankingsystem.service.BankSystemService;
import com.example.onlinebankingsystem.util.AccountNumberGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
public class BankSystemController {

    private final BankSystemService bankSystemService; //inject dependence

    private static final String REDIRECT_HOME = "redirect:/";

    private static final String TRANSFER_PAGE = "transfer";

    private static final String WITHDRAW_PAGE = "withdraw";

    private static final String HOME_PAGE = "home";

    private static final String NEW_ACCOUNT_PAGE = "new-account";

    private static final String DEPOSIT_PAGE = "deposit";

    private static final String ACCOUNT_ATTRIBUTE = "account";

    private static final String AMOUNT_ATTRIBUTE = "amount";

    private static final String ACCOUNT_NUMBER_ATTRIBUTE = "accountNumber";

    private static final String FROM_ACCOUNT_ATTRIBUTE = "fromAccount";

    private static final String TO_ACCOUNT_ATTRIBUTE = "toAccount";

    private static final String MESSAGE_ATTRIBUTE = "message";


    public BankSystemController(BankSystemService bankSystemService) {
        this.bankSystemService = bankSystemService;
    }

    @GetMapping("/")
    public String showHomePage() {
        return HOME_PAGE;
    }

    @GetMapping("/accounts/new")
    public String creatAccountForm(Model model) {
        model.addAttribute(ACCOUNT_ATTRIBUTE, new Account());
        return NEW_ACCOUNT_PAGE;
    }

    @PostMapping("/accounts/new")
    public String creatAccount(@ModelAttribute Account account,
                               @SessionAttribute(name = "userId") Long userId
    ) {
        account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        bankSystemService.openNewAccount(account, userId);
        return REDIRECT_HOME;
    }

    @GetMapping("/transactions/deposit")
    public String depositForm() {
        return DEPOSIT_PAGE;
    }

    @PostMapping("/transactions/deposit")
    public String deposit(@RequestParam(ACCOUNT_NUMBER_ATTRIBUTE) String accountNumber,
                          @RequestParam(AMOUNT_ATTRIBUTE) BigDecimal amount,
                          Model model) {
        try {
            bankSystemService.deposit(accountNumber, amount);
        } catch (NotFoundException ex) {
            model.addAttribute(MESSAGE_ATTRIBUTE, ex.getMessage());
            return DEPOSIT_PAGE;
        }
        return REDIRECT_HOME;
    }

    @GetMapping("/transactions/withdraw")
    public String withdrawForm() {
        return WITHDRAW_PAGE;
    }

    @PostMapping("/transactions/withdraw")
    public String withdraw(@RequestParam(ACCOUNT_NUMBER_ATTRIBUTE) String accountNumber,
                           @RequestParam(AMOUNT_ATTRIBUTE) BigDecimal amount,
                           Model model) {

        try {
            bankSystemService.withdraw(accountNumber, amount);
        } catch (NotEnoughAmountException | NotFoundException ex) {
            model.addAttribute(MESSAGE_ATTRIBUTE, ex.getMessage());
            return WITHDRAW_PAGE;
        }
        return REDIRECT_HOME;
    }

    @GetMapping("/transactions/transfer")
    public String transferForm() {
        return TRANSFER_PAGE;
    }

    @PostMapping("/transactions/transfer")
    public String transfer(@RequestParam(FROM_ACCOUNT_ATTRIBUTE) String  fromAccount,
                           @RequestParam(TO_ACCOUNT_ATTRIBUTE) String toAccount,
                           @RequestParam(AMOUNT_ATTRIBUTE) BigDecimal amount,
                           Model model) {
        try {
            bankSystemService.transfer(fromAccount, toAccount, amount);
        } catch (NotEnoughAmountException | NotFoundException ex) {
            model.addAttribute(MESSAGE_ATTRIBUTE, ex.getMessage());
            return TRANSFER_PAGE;
        }
        return REDIRECT_HOME;
    }
}
