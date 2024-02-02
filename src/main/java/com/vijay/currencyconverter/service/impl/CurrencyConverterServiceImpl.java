package com.vijay.currencyconverter.service.impl;

import com.vijay.currencyconverter.service.CurrencyConverterService;
import com.vijay.currencyconverter.service.CurrencyConverterStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    private static final Set<String> supportedCurrencyTypes = new HashSet<>();
    private final ApplicationContext applicationContext;

    static {
        supportedCurrencyTypes.add("USD");
        supportedCurrencyTypes.add("INR");
        supportedCurrencyTypes.add("JPY");
        supportedCurrencyTypes.add("EUR");
    }

    public CurrencyConverterServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public String convertCurrency(double amount, String from, String to) {
        if (!supportedCurrencyTypes.contains(from) || !supportedCurrencyTypes.contains(to))
            return "Unsupported Currency";
        CurrencyConverterStrategy currencyConverterStrategy = getStrategy(to);
        return currencyConverterStrategy.convert(amount, from, to);

    }

    private CurrencyConverterStrategy getStrategy(String to) {
        switch (to) {
            case "INR":
                return (CurrencyConverterStrategy) applicationContext.getBean("InrTargetCurrencyConverter");
            case "EUR":
                return (CurrencyConverterStrategy) applicationContext.getBean("EurTargetCurrencyConverter");
            case "JPY":
                return (CurrencyConverterStrategy) applicationContext.getBean("JpyTargetCurrencyConverter");
            default:
                return (CurrencyConverterStrategy) applicationContext.getBean("UsdTargetCurrencyConverter");
        }
    }

}
