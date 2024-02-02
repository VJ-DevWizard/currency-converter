package com.vijay.currencyconverter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vijay.currencyconverter.service.CurrencyConverterService;
import com.vijay.currencyconverter.service.CurrencyConverterStrategy;
import com.vijay.currencyconverter.service.RuntimeCurrencyConverter;
import com.vijay.currencyconverter.service.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CurrencyConversionControllerTest {

    @InjectMocks
    private CurrencyConversionController currencyConversionController;
    @Mock
    RestTemplate restTemplate;
    @Mock
    ApplicationContext applicationContext;
    private RuntimeCurrencyConverter runtimeCurrencyConverter;

    private String responseJson = "{\"date\": \"2024-02-01\", \"usd\": {\"eur\": 0.92444585, \"inr\": 83.0692613, \"jpy\": 146.58613841, \"usd\": 1}}";


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        runtimeCurrencyConverter = new RuntimeCurrencyConverterImpl(restTemplate);
        CurrencyConverterService currencyConverterService = new CurrencyConverterServiceImpl(applicationContext);
        currencyConversionController = new CurrencyConversionController(currencyConverterService);
    }


    @Test
    void convertCurrencyUnsupportedType() {
        assertEquals("Unsupported Currency", currencyConversionController.convertCurrency(2, "CAD", "USD"));
    }

    @Test
    void convertCurrencyFailedConversion() {
        CurrencyConverterStrategy inrCurrencyConverterStrategy = new InrTargetCurrencyConverter(runtimeCurrencyConverter, 1);
        when(applicationContext.getBean("InrTargetCurrencyConverter")).thenReturn(inrCurrencyConverterStrategy);
        assertEquals("Error while converting currency. Sorry for the inconvenience", currencyConversionController.convertCurrency(2, "USD", "INR"));
    }

    @Test
    void convertCurrencyTargetInr() {
        CurrencyConverterStrategy inrCurrencyConverterStrategy = new InrTargetCurrencyConverter(runtimeCurrencyConverter, 1);
        when(applicationContext.getBean("InrTargetCurrencyConverter")).thenReturn(inrCurrencyConverterStrategy);
        when(restTemplate.getForObject("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/usd.min.json",
                JsonNode.class)).thenReturn(getJsonObjectFromString());
        assertEquals("166.1 INR", currencyConversionController.convertCurrency(2, "USD", "INR"));
    }

    @Test
    void convertCurrencyTargetEur() {
        CurrencyConverterStrategy eurCurrencyConverterStrategy = new EurTargetCurrencyConverter(runtimeCurrencyConverter, 3);
        when(applicationContext.getBean("EurTargetCurrencyConverter")).thenReturn(eurCurrencyConverterStrategy);
        when(restTemplate.getForObject("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/usd.min.json",
                JsonNode.class)).thenReturn(getJsonObjectFromString());
        assertEquals("1.849 EUR", currencyConversionController.convertCurrency(2, "USD", "EUR"));
    }

    @Test
    void convertCurrencyTargetUsd() {
        CurrencyConverterStrategy usdCurrencyConverterStrategy = new UsdTargetCurrencyConverter(runtimeCurrencyConverter, 1);
        when(applicationContext.getBean("UsdTargetCurrencyConverter")).thenReturn(usdCurrencyConverterStrategy);
        when(restTemplate.getForObject("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/usd.min.json",
                JsonNode.class)).thenReturn(getJsonObjectFromString());
        assertEquals("2.0 USD", currencyConversionController.convertCurrency(2, "USD", "USD"));
    }

    @Test
    void convertCurrencyTargetJpy() {
        CurrencyConverterStrategy jpyCurrencyConverterStrategy = new JpyTargetCurrencyConverter(runtimeCurrencyConverter, 0);
        when(applicationContext.getBean("JpyTargetCurrencyConverter")).thenReturn(jpyCurrencyConverterStrategy);
        when(restTemplate.getForObject("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/usd.min.json",
                JsonNode.class)).thenReturn(getJsonObjectFromString());
        assertEquals("293 JPY", currencyConversionController.convertCurrency(2, "USD", "JPY"));
    }

    private JsonNode getJsonObjectFromString() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readValue(responseJson, JsonNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonNode;
    }
}