package com.example.swed.controller;

import com.example.swed.model.Account;
import com.example.swed.model.CurrencyExchangeRequest;
import com.example.swed.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok().body(accountService.getAccounts());
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Account>> getAccountBalance(@PathVariable Integer accountId) {
        return ResponseEntity.ok().body(accountService.getAccountBalance(accountId));
    }

    @PostMapping("/{accountId}/addMoney")
    public ResponseEntity<String> addMoney(@PathVariable Integer accountId, @RequestParam BigDecimal amount) {
        try {
            accountService.addMoney(accountId, amount);
            return ResponseEntity.ok().body("Money added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding money");

        }
    }

    @PostMapping("/{accountId}/debit")
    public ResponseEntity<String> debit(@PathVariable Integer accountId, @RequestParam BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(50)) < 0){
            return ResponseEntity.badRequest().body("Minimum amount to debit is 50");
        }
        try {
            accountService.debit(accountId, amount);
            return ResponseEntity.ok().body("Money debit successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error taking money out: " + e.getMessage());
        }
    }

    @PostMapping("/convert")
    public ResponseEntity<String> convert(@RequestBody CurrencyExchangeRequest currencyExchangeRequest) {
        try {
            accountService.convert(
                    currencyExchangeRequest.getCustomerId().intValue(),
                    currencyExchangeRequest.getFrom(),
                    currencyExchangeRequest.getTo(),
                    currencyExchangeRequest.getAmount());
            return ResponseEntity.ok().body("Money convert successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error converting money: " + e.getMessage());
        }
    }

}