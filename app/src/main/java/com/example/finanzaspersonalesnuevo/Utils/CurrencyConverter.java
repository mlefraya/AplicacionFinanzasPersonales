package com.example.finanzaspersonalesnuevo.Utils;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {
    // Factores de conversión con base 1 EUR
    private static final Map<String, Double> rates = new HashMap<>();
    static {
        rates.put("EUR", 1.00);
        rates.put("USD", 1.10);
        rates.put("GBP", 0.85);
        rates.put("JPY", 140.00);
        rates.put("CHF", 0.98);
        rates.put("CAD", 1.45);
        rates.put("AUD", 1.60);
        rates.put("CNY", 7.40);
        rates.put("INR", 90.00);
        rates.put("BRL", 5.30);
        rates.put("RUB", 90.00);
        rates.put("MXN", 18.50);
        rates.put("SGD", 1.50);
        rates.put("NZD", 1.70);
        rates.put("SEK", 11.00);
        rates.put("NOK", 11.00);
        rates.put("KRW", 1450.00);
        rates.put("ARS", 900.00);
        rates.put("ZAR", 18.00);
    }

    /**
     * Convierte una cantidad dada en EUR a la moneda destino.
     * @param amountEur cantidad en EUR
     * @param targetCurrency código ISO de la moneda destino
     * @return importe convertido (si no existe la tasa, devuelve el original en EUR)
     */
    public static double fromEur(double amountEur, String targetCurrency) {
        Double rate = rates.get(targetCurrency);
        return (rate != null) ? amountEur * rate : amountEur;
    }

    /**
     * Convierte una cantidad dada en la moneda destino a EUR.
     * @param amountLocal cantidad en la moneda local
     * @param localCurrency código ISO de la moneda local
     * @return importe convertido a EUR (si no existe la tasa, devuelve el original)
     */
    public static double toEur(double amountLocal, String localCurrency) {
        Double rate = rates.get(localCurrency);
        return (rate != null && rate != 0.0) ? amountLocal / rate : amountLocal;
    }
}
