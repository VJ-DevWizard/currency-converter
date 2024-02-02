package com.vijay.currencyconverter.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.vijay.currencyconverter.service.CurrencyConverterStrategy;
import com.vijay.currencyconverter.service.RuntimeCurrencyConverter;
import com.vijay.currencyconverter.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service("InrTargetCurrencyConverter")
public class InrTargetCurrencyConverter implements CurrencyConverterStrategy {
    private final RuntimeCurrencyConverter runtimeCurrencyConverter;
    private final Integer precision;

    public InrTargetCurrencyConverter(RuntimeCurrencyConverter runtimeCurrencyConverter, @Value("${currency.inr.precision}") Integer precision) {
        this.runtimeCurrencyConverter = runtimeCurrencyConverter;
        this.precision = precision;
    }

    @Override
    public String convert(double amount, String from, String to) {
        JsonNode response = runtimeCurrencyConverter.fetchDynamicConversion(from.toLowerCase());
        if (response == null) {
            return "Error while converting currency. Sorry for the inconvenience";
        }
        try {
            double conversionRate = ObjectMapperUtil.getTargetCurrencyRate(response.get(from.toLowerCase()).toString(), to.toLowerCase());
            BigDecimal result = BigDecimal.valueOf(amount * conversionRate);
            result = result.setScale(precision, RoundingMode.HALF_UP);
            return String.format("%s %s", result, to);
        } catch (JsonProcessingException e) {
            return "Error while converting currency. Sorry for the inconvenience";
        }
    }
}
