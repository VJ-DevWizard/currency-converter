package com.vijay.currencyconverter.service;

public interface CurrencyConverterStrategy {
    String convert(double amount, String from, String to);
}
