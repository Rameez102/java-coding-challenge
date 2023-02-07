package com.crewmeister.cmcodingchallenge.dto;

import lombok.*;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CurrencyExchangeRateDto {

    long id;
    Date date;
    double rate;
    String currency;
}
