package com.example.swed;

import com.example.swed.controller.AccountController;
import com.example.swed.model.Account;
import com.example.swed.service.AccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private static AccountService accountService;

    Account account1_EUR = new Account(1, 2, new BigDecimal("1000"), "EUR");
    Account account1_USD = new Account(2, 2, new BigDecimal("2000"), "USD");
    Account account2 = new Account(3, 3, new BigDecimal("2000"), "USD");
    List<Account> accounts = Arrays.asList(account1_EUR, account2);

    @Test
    public void testGetAllAccounts() throws Exception {
        Mockito.when(accountService.getAccounts()).thenReturn(accounts);
        mockMvc.perform(get("/api/accounts/"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"account_id\":1,\"balance\":1000},{\"account_id\":3,\"balance\":2000}]"));
    }


    @Test
    public void testGetAccount() throws Exception {
        Mockito.when(accountService.getAccountBalance(1)).thenReturn(Collections.singletonList(account1_EUR));
        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"account_id\":1,\"balance\":1000}]"));
    }

    @Test
    public void testGetAccountTwoCurrencies() throws Exception {
        Mockito.when(accountService.getAccountBalance(1)).thenReturn(Arrays.asList(account1_EUR, account1_USD));
        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"account_id\":1,\"balance\":1000, \"currency\":\"EUR\"}, " +
                                "{\"account_id\":2,\"balance\":2000,  \"currency\":\"USD\"}]"));
    }

}