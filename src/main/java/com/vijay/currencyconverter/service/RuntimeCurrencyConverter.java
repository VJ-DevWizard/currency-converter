package com.vijay.currencyconverter.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface RuntimeCurrencyConverter {
    JsonNode fetchDynamicConversion(String baseCurrencyType);
}
