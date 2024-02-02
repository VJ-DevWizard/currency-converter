# Currency Conversion Application

## Overview

The Currency Conversion Application is a Java-based SpringBoot application that allows users to convert amounts between different currencies. It uses a strategy design pattern to support multiple currency types and provides flexibility in handling various conversion scenarios.

## Features

- **Multiple Currencies:** Supports conversion between USD, EUR, INR, and JPY.
- **Flexible Design:** Utilizes the Strategy design pattern for extensibility.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) installed (minimum version 8).
- Maven for building and managing dependencies.

### Build and Run

1. Clone the repository:

    ```bash
    git clone https://github.com/VJ-DevWizard/currency-converter.git
    cd currency-converter
    ```

2. Build the application:

    ```bash
    mvn clean install
    ```

3. Run the application:

    ```bash
    java -jar target/currencyconverter-1.0.jar
   
    Application can also be run with SpringBoot
    ```

4. Open your web browser and navigate to `http://localhost:8081/currency-converter` to access the application.

## Usage

1. Enter the amount to convert.
2. Select the source currency.
3. Select the target currency.
4. Click the "Convert" button.
5. View the converted amount.

## Design and Architecture

The application follows the Strategy design pattern for currency conversion. Different currency conversion strategies are implemented as separate classes, allowing for easy extension and maintenance.

### Project Structure

- **src/main/java/com/vijay/currencyconverter/**
    - **controller/**: Contains the web controllers.
    - **service/**: Holds the business logic and currency conversion strategies.
    - **dto/**: Defines data models used by the application.

### Example Strategy Implementation

- **UsdTargetCurrencyConverter.java**: Converts currency to USD using a dynamic exchange rate.

```java
@Component("UsdTargetCurrencyConverter")
public class UsdTargetCurrencyConverter implements CurrencyConverterStrategy {
    private final RuntimeCurrencyConverter runtimeCurrencyConverter;
    private final Integer precision;

    public UsdTargetCurrencyConverter(RuntimeCurrencyConverter runtimeCurrencyConverter, @Value("${currency.usd.precision}") Integer precision) {
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