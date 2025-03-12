package com.example.swed.service;

import com.example.swed.model.Account;
import com.example.swed.model.Transaction;
import com.example.swed.repository.AccountRepository;
import com.example.swed.repository.CurrencyExchangeRepository;
import com.example.swed.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CurrencyExchangeRepository currencyExchangeRepository;
    private final RestTemplate restTemplate;

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getAccountBalance(Integer accountId) {
        return accountRepository.findAccountsById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public void addMoney(Integer accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        transactionRepository.save(new Transaction(
                null,
                accountId,
                "DEPOSIT",
                amount,
                new Timestamp(System.currentTimeMillis()))
        );
    }

    public void debit(Integer accountId, BigDecimal amount) {

        String url = "https://httpstat.us/200";
        String response = restTemplate.getForObject(url, String.class);
        log.info("Response from external system: " + response);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        transactionRepository.save(new Transaction(
                null,
                accountId,
                "DEBIT",
                amount,
                new Timestamp(System.currentTimeMillis()))
        );
    }

    public void convert(Integer customerId, String from, String to, BigDecimal amount) {
        Account accountFrom = accountRepository.findByCustomerIdAndCurrency(customerId, from)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        BigDecimal rate = currencyExchangeRepository.findExchangeRate(from, to);
        Optional<Account> accountTo = accountRepository.findByCustomerIdAndCurrency(customerId, to);
        if (accountTo.isEmpty()) {
            accountTo = Optional.of(accountRepository.save(
                    new Account(customerId, new BigDecimal(0), to))
            );
        }
        BigDecimal convertedAmount = amount.multiply(rate);
        //TODO: Alatest siit edasi oleks vaja andmebaasi tasandil panna nt lukk peale, et
        // midagi nende lahe action vahel ei juhtuks
        accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
        accountRepository.save(accountFrom);
        accountTo.get().setBalance(accountTo.get().getBalance().add(convertedAmount));
        accountRepository.save(accountTo.get());
        transactionRepository.save(new Transaction(
                null,
                accountFrom.getId(),
                "CONVERT",
                convertedAmount,
                new Timestamp(System.currentTimeMillis()))
        );
    }
}