package com.example.Banking.App.service.api;

import com.example.Banking.App.entity.api.ExchangeRateResponse;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRateService {
    private static final String API_KEY = "d9c94864393242008b6d6574";

    public ExchangeRateResponse getExchangeRates(String baseCurrency) {
        try {
            String urlStr = String.format("https://v6.exchangerate-api.com/v6/%s/latest/%s", API_KEY, baseCurrency);
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                Gson gson = new Gson();
                return gson.fromJson(reader, ExchangeRateResponse.class);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch exchange rates", e);
        }
    }
    public Map<String, Double> getSpecificRates(String baseCurrency) {
        ExchangeRateResponse fullResponse = getExchangeRates(baseCurrency);
        Map<String, Double> specificRates = new HashMap<>();

        // Get only EUR, UAH, GBP, PLN rates
        specificRates.put("EUR", fullResponse.getConversion_rates().get("EUR"));
        specificRates.put("UAH", fullResponse.getConversion_rates().get("UAH"));
        specificRates.put("GBP", fullResponse.getConversion_rates().get("GBP"));
        specificRates.put("PLN", fullResponse.getConversion_rates().get("PLN"));

        return specificRates;
    }
}