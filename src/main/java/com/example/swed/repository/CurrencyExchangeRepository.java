package com.example.swed.repository;

import com.example.swed.model.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchangeRate, Integer> {

    @Query("SELECT c.exchange_rate FROM CurrencyExchangeRate c WHERE c.currency_from = :fromCurrency AND c.currency_to = :toCurrency")
    BigDecimal findExchangeRate(@Param("fromCurrency") String fromCurrency, @Param("toCurrency") String toCurrency);
}
