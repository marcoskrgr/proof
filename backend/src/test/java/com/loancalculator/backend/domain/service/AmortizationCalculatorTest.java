package com.loancalculator.backend.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class AmortizationCalculatorTest {

    private AmortizationCalculator calculator;
    private final int TOTAL_INSTALLMENTS = 120;

    @BeforeEach
    void setUp() {
        calculator = new AmortizationCalculator(TOTAL_INSTALLMENTS);
    }

    @Test
    @DisplayName("Should calculate regular monthly amortization")
    void shouldCalculateRegularAmortization() {
        var loanAmount = BigDecimal.valueOf(140000);
        var currentBalance = BigDecimal.valueOf(100000);
        var installmentNumber = 50;

        var result = calculator.calculate(loanAmount, currentBalance, installmentNumber);

        var expected = BigDecimal.valueOf(1166.67);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should return remaining balance for final installment")
    void shouldReturnRemainingBalanceForFinalInstallment() {
        var loanAmount = BigDecimal.valueOf(140000);
        var currentBalance = BigDecimal.valueOf(1166.27);

        var result = calculator.calculate(loanAmount, currentBalance, TOTAL_INSTALLMENTS);

        assertEquals(currentBalance, result);
    }

    @Test
    @DisplayName("Should handle small loan amounts")
    void shouldHandleSmallLoanAmounts() {
        var loanAmount = BigDecimal.valueOf(1000);
        var currentBalance = BigDecimal.valueOf(500);
        var installmentNumber = 1;

        var result = calculator.calculate(loanAmount, currentBalance, installmentNumber);

        var expected = BigDecimal.valueOf(8.33);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should maintain precision with two decimal places")
    void shouldMaintainPrecision() {
        var loanAmount = BigDecimal.valueOf(100000);
        var currentBalance = BigDecimal.valueOf(50000);
        var installmentNumber = 1;

        var result = calculator.calculate(loanAmount, currentBalance, installmentNumber);

        assertEquals(2, result.scale());
    }
}