package com.example.swed.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyExchangeRequest {
    private String from;
    private String to;
    private BigDecimal amount;
    private BigDecimal customerId;
}
