package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.entity.CurrencyExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.CurrencyExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for CsvDataLoaderUtil class.
 */
@Service
public class CsvService {

    @Autowired
    CurrencyExchangeRateRepository exchangeRateRepo;

    public void save(List<CurrencyExchangeRate> exchangeRateList) {
        exchangeRateRepo.saveAll(exchangeRateList);
    }

    public List<CurrencyExchangeRate> getAll() {
        return exchangeRateRepo.findAll();
    }

}
