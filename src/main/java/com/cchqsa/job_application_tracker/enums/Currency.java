package com.cchqsa.job_application_tracker.enums;

import lombok.Getter;

@Getter
public enum Currency {
    USD("$", "US Dollar"),
    EUR("€", "Euro"),
    UAH("₴", "Ukrainian Hryvnia"),
    GBP("£", "British Pound"),
    CAD("CA$", "Canadian Dollar"),
    AUD("A$", "Australian Dollar"),
    JPY("¥", "Japanese Yen"),
    PLN("zł", "Polish Zloty");

    private final String symbol;
    private final String fullName;

    Currency(String symbol, String fullName) {
        this.symbol = symbol;
        this.fullName = fullName;
    }

}
