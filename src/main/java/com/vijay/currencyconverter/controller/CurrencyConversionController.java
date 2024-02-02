package com.vijay.currencyconverter.controller;

import com.vijay.currencyconverter.service.CurrencyConverterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyConversionController {
    private final CurrencyConverterService currencyConverterService;

    public CurrencyConversionController(CurrencyConverterService currencyConverterService) {
        this.currencyConverterService = currencyConverterService;
    }

    @GetMapping("/convert")
    public String convertCurrency(@RequestParam double amount,
                                  @RequestParam String from,
                                  @RequestParam String to) {
        return currencyConverterService.convertCurrency(amount, from, to);
    }

}
