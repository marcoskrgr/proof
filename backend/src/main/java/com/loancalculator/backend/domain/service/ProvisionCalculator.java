package com.loancalculator.backend.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProvisionCalculator {

    private static final int DAYS_BASE = 360;
    private static final int DECIMAL_SCALE = 2;

    public BigDecimal calculate(BigDecimal balance, BigDecimal accumulated, LocalDate from, LocalDate to, BigDecimal interestRate) {
        var days = ChronoUnit.DAYS.between(from, to);

        if (days == 0) {
            return BigDecimal.ZERO;
        }

        var onePlusRate = interestRate.add(BigDecimal.ONE);
        var daysFraction = BigDecimal.valueOf(days)
                .divide(BigDecimal.valueOf(DAYS_BASE), 10, RoundingMode.HALF_UP);

        var baseRate = onePlusRate.doubleValue();
        var exponent = daysFraction.doubleValue();
        var compoundFactor = Math.pow(baseRate, exponent);

        var compoundFactorBD = BigDecimal.valueOf(compoundFactor);
        var growthFactor = compoundFactorBD.subtract(BigDecimal.ONE);
        var baseAmount = balance.add(accumulated);

        return growthFactor
                .multiply(baseAmount)
                .setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);
    }
}
