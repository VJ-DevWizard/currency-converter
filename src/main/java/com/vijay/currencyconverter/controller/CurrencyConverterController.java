package com.vijay.currencyconverter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CurrencyConverterController {

    @GetMapping("/currency-converter")
    public String showCurrencyConverterPage() {
        return "currency-converter";
    }

}
