package com.vijay.currencyconverter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vijay.currencyconverter.dto.CurrencyValuesDto;

public class ObjectMapperUtil {

    public static double getTargetCurrencyRate(String input, String targetCurrencyType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CurrencyValuesDto currencyValues = mapper.readValue(input, CurrencyValuesDto.class);
        switch (targetCurrencyType) {
            case "inr":
                return currencyValues.getInr();
            case "eur":
                return currencyValues.getEur();
            case "jpy":
                return currencyValues.getJpy();
            default:
                return currencyValues.getUsd();
        }
    }
}
