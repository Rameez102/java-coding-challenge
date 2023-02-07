package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.entity.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface CurrencyExchangeRateRepository extends JpaRepository<CurrencyExchangeRate, Long> {

    @Query(value = "select distinct(cer.currency) from CurrencyExchangeRate cer")
    List<String> findDistinctCurrency();

    List<CurrencyExchangeRate> findByDate(Date date);

    CurrencyExchangeRate findByDateAndCurrency(Date date, String currencyUnit);

}
