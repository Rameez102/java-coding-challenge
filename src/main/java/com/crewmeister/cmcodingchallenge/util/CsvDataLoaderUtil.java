package com.crewmeister.cmcodingchallenge.util;

import com.crewmeister.cmcodingchallenge.entity.CurrencyExchangeRate;
import com.crewmeister.cmcodingchallenge.service.CsvService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Component
public class CsvDataLoaderUtil implements CommandLineRunner {

    @Autowired
    CsvService csvService;

    private static final Logger log = LoggerFactory.getLogger(CsvDataLoaderUtil.class);

    @Override
    public void run(String... args) throws IOException {
        log.info("Load files of Bundesbank daily exchange rate urls from property file");
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("euro-fx.properties");
        List<String> urls = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.toList());

        int numberOfFilesToLoad;

        if (urls.size() > 0)
        {
            numberOfFilesToLoad = urls.size();
            int noOfThreads = 9;

            if (numberOfFilesToLoad < 9)
                noOfThreads = numberOfFilesToLoad;

            int filesPerThread = numberOfFilesToLoad / noOfThreads;
            String[][] urlDistributionArray = new String[noOfThreads][filesPerThread];

            log.info("Distribute the exchange rate files into noOfThreads threads");
            for (int i = 0; i < noOfThreads; i++) {
                for (int j = i * filesPerThread, k = 0; j < i * filesPerThread + filesPerThread; j++, k++) {
                    urlDistributionArray[i][k] = urls.get(j);
                }
            }

            ExecutorService executorService = Executors.newFixedThreadPool(noOfThreads);

            log.info("Creating runnable objects to create threads");
            for (int i = 0; i < noOfThreads; i++) {
                int finalI = i;
                Runnable uploadThread$i = () -> {
                    for (int urlId = 0; urlId < filesPerThread; urlId++) {
                        readOneDataFile(urlDistributionArray[finalI][urlId]);
                    }
                };
                executorService.execute(uploadThread$i);
            }

            executorService.shutdown();
        } else
            log.error("No files to load!");

    }


    public void readOneDataFile(String url) {
        RestTemplate restTemplate = new RestTemplate();
        Resource resource;
        resource = restTemplate.getForObject(url, Resource.class);
        List<CurrencyExchangeRate> exchangeRates = null;

        log.info("Extracting Currency Unit/Code fromt the file name");
        String currencyUnit = resource.getFilename().split("\\.")[2];

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(br).withSkipLines(9).build();
            List<String[]> allData = csvReader.readAll();
            exchangeRates = new ArrayList<>();

            for (String[] rows : allData) {
                LocalDate date = LocalDate.parse(rows[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                Date mydate = Date.valueOf(date);
                Double rate = 0.00;

                log.info("Initializing exchangeRate value if valid number");
                if (!rows[1].trim().equals(".")) {
                    rate = Double.valueOf(rows[1]);
                }
                CurrencyExchangeRate exchangeRate = new CurrencyExchangeRate(mydate, rate, currencyUnit);
                exchangeRates.add(exchangeRate);
            }

            log.info("saving exchange rates file");
            this.uploadOneDataFile(exchangeRates);

        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
         }
    }

    public void uploadOneDataFile(List<CurrencyExchangeRate> exchangeRates) {
        csvService.save(exchangeRates);
    }

}
