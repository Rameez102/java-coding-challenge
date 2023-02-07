package com.crewmeister.cmcodingchallenge.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CurrencyExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @NotNull
    @Temporal(TemporalType.DATE)
    Date date;

    @NotNull
    double rate;

    @NotNull
    String currency;

    public CurrencyExchangeRate(Date date, Double rate, String currency) {
        this.date = date;
        this.rate = rate;
        this.currency = currency;
    }
}
