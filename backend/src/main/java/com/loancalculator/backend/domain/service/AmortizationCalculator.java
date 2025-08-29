package com.loancalculator.backend.domain.service;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmortizationCalculator {

    private final int totalInstallments;
    private static final int DECIMAL_SCALE = 2;

    public AmortizationCalculator(int totalInstallments) {
        this.totalInstallments = totalInstallments;
    }

    public BigDecimal calculate(BigDecimal loanAmount, BigDecimal currentBalance, int installmentNumber) {
        if (installmentNumber == totalInstallments) {
            return currentBalance;
        }
        return loanAmount.divide(BigDecimal.valueOf(totalInstallments), DECIMAL_SCALE, RoundingMode.HALF_UP);
    }
}
