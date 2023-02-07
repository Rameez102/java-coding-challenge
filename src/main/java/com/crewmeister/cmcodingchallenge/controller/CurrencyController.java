package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.dto.CurrencyExchangeRateDto;
import com.crewmeister.cmcodingchallenge.service.CurrencyExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.List;

/**
 * The controller for exchange rates and amount conversion.
 */
@RestController()
@RequestMapping("/api")
public class CurrencyController {

    @Autowired
    CurrencyExchangeRateService currencyExchangeRateService;

    // As a client, I want to get a list of all available currencies
    @GetMapping(value = "/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getCurrencies() {
        return currencyExchangeRateService.getAllCurrencies();
    }


    // As a client, I want to get all EUR-FX exchange rates at all available dates as a collection
    @GetMapping(value = "/exchange/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CurrencyExchangeRateDto>> geAllExchangeRates() {
        return  new ResponseEntity<List<CurrencyExchangeRateDto>>(currencyExchangeRateService.getAllExchangeRates(), HttpStatus.OK);
    }

    // As a client, I want to get the EUR-FX exchange rate at particular day
    @GetMapping(value = "/exchange/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CurrencyExchangeRateDto>> getAllExcangeRateByDate(@PathVariable("date") String date) {
        return  new ResponseEntity<List<CurrencyExchangeRateDto>>(currencyExchangeRateService.getExchangeRateOfDate(date), HttpStatus.OK);
    }

    // As a client, I want to get a foreign exchange amount for a given currency converted to EUR on a particular day
    @GetMapping(value = "/exchange/{currency}/{date}/{amount}")
    public String getExchangeRateOfDate(@PathVariable("currency") String currency, @PathVariable("date") String date, @PathVariable("amount") Double amount) throws ParseException {
        return currencyExchangeRateService.getExchangeAmount(currency, date, amount);
    }
}
