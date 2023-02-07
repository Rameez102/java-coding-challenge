package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.dto.CurrencyExchangeRateDto;
import com.crewmeister.cmcodingchallenge.entity.CurrencyExchangeRate;
import com.crewmeister.cmcodingchallenge.exception.ResourceNotFoundException;
import com.crewmeister.cmcodingchallenge.repository.CurrencyExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.util.DateUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * This class will handle the operation related to exchange rates
 */
@Service
public class CurrencyExchangeRateService {

    @Autowired
    CurrencyExchangeRateRepository exchangeRateRepo;

    @Autowired
    ObjectMapper objectMapper;


    /**
     * The method returns the list of currencies.
     *
     * @return List of currency.
     * @params
     */
    public List<String> getAllCurrencies() {
        List<String> currencies = Optional.ofNullable(exchangeRateRepo.findDistinctCurrency()).orElseThrow(() -> new ResourceNotFoundException());

        return currencies;

    }


    /**
     * The method returns the list of all exchange rates.
     *
     * @return List of all exchange rates.
     * @params
     */
    public List<CurrencyExchangeRateDto> getAllExchangeRates() {

        List<CurrencyExchangeRate> exchangeRates = Optional.ofNullable(exchangeRateRepo.findAll()).orElseThrow(() -> new ResourceNotFoundException());
        List<CurrencyExchangeRateDto> exchangeRateDtoList = objectMapper.convertValue(exchangeRates, new TypeReference<List<CurrencyExchangeRateDto>>() {});
        return  exchangeRateDtoList;
    }


    /**
     * The method returns the list of all exchange rates based on date.
     *
     * @return List of exchange rates.
     * @params Date
     */
    public List<CurrencyExchangeRateDto> getExchangeRateOfDate(String strDate) {

        Date date = DateUtil.convertStringToDate(strDate);
        List<CurrencyExchangeRate> exchangeRates = Optional.ofNullable(exchangeRateRepo.findByDate(date)).orElseThrow(() -> new ResourceNotFoundException());
        List<CurrencyExchangeRateDto> exchangeRateDtoList = objectMapper.convertValue(exchangeRates, new TypeReference<List<CurrencyExchangeRateDto>>() {});
        return  exchangeRateDtoList;
    }


    /**
     * The method returns the list of all exchange rates based on date and currency unit code.
     *
     * @return List of exchange rates.
     * @params inputDate, currencyUnit code
     */
    public String getExchangeAmount(String currency, String date, Double inputAmount) throws ParseException {

        Double exchangeRate = exchangeRateRepo.findByDateAndCurrency(DateUtil.convertStringToDate(date), currency).getRate();
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        Double convertedAmount = Double.parseDouble(decimalFormat.format(inputAmount / exchangeRate));
        return "Converted Amount is : " + convertedAmount;

    }
}
