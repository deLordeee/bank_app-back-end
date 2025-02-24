package com.example.Banking.App.controller;

import com.example.Banking.App.service.api.ExchangeRateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/rates")
    public String showRates(Model model) {
        Map<String, Double> rates = exchangeRateService.getSpecificRates("USD");
        model.addAttribute("rates", rates);
        return "rates";
    }
}

