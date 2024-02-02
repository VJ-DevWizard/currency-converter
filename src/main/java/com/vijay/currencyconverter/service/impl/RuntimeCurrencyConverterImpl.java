package com.vijay.currencyconverter.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.vijay.currencyconverter.service.RuntimeCurrencyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class RuntimeCurrencyConverterImpl implements RuntimeCurrencyConverter {

    private final RestTemplate restTemplate;
    private String baseUrl = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/%s.min.json";

    public RuntimeCurrencyConverterImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public JsonNode fetchDynamicConversion(String baseCurrencyType) {
        String url = String.format(baseUrl, baseCurrencyType);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(headers);
        try {
            JsonNode response=restTemplate.getForObject(url,JsonNode.class);
            return response;
        } catch (Exception ex) {
            log.error("Error while fetching currency conversion values", ex);
            return null;
        }
    }
}
