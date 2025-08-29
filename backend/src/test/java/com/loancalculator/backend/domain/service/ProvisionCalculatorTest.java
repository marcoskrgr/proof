package com.loancalculator.backend.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ProvisionCalculatorTest {

    private ProvisionCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ProvisionCalculator();
    }

    @Test
    @DisplayName("Should return zero when days between dates is zero")
    void shouldReturnZeroWhenDaysIsZero() {
        var from = LocalDate.of(2024, 1, 1);
        var to = LocalDate.of(2024, 1, 1);
        var balance = BigDecimal.valueOf(1000);
        var accumulated = BigDecimal.valueOf(100);
        var interestRate = BigDecimal.valueOf(0.07);

        var result = calculator.calculate(balance, accumulated, from, to, interestRate);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Should calculate provision correctly for 30 days")
    void shouldCalculateProvisionFor30Days() {
        var from = LocalDate.of(2024, 1, 1);
        var to = LocalDate.of(2024, 1, 31);
        var balance = BigDecimal.valueOf(140000);
        var accumulated = BigDecimal.ZERO;
        var interestRate = BigDecimal.valueOf(0.07);

        var result = calculator.calculate(balance, accumulated, from, to, interestRate);

        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
        assertEquals(2, result.scale());
    }

    @Test
    @DisplayName("Should calculate provision with accumulated interest")
    void shouldCalculateProvisionWithAccumulated() {
        var from = LocalDate.of(2024, 1, 1);
        var to = LocalDate.of(2024, 1, 16);
        var balance = BigDecimal.valueOf(140000);
        var accumulated = BigDecimal.valueOf(500);
        var interestRate = BigDecimal.valueOf(0.07);

        var result = calculator.calculate(balance, accumulated, from, to, interestRate);

        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Should handle small amounts correctly")
    void shouldHandleSmallAmounts() {
        var from = LocalDate.of(2024, 1, 1);
        var to = LocalDate.of(2024, 1, 2);
        var balance = BigDecimal.valueOf(1);
        var accumulated = BigDecimal.ZERO;
        var interestRate = BigDecimal.valueOf(0.01);

        var result = calculator.calculate(balance, accumulated, from, to, interestRate);

        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) >= 0);
    }
}