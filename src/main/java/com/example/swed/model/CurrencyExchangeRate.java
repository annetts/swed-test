package com.example.swed.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "currency_exchange_rates")
public class CurrencyExchangeRate implements Serializable {

    @Id
    private Integer id;

    @Column(name = "currency_from", nullable = false)
    private String currency_from;

    @Column(name = "currency_to", nullable = false)
    private String currency_to;

    @Column(name = "exchange_rate", nullable = false)
    private String exchange_rate;
}
