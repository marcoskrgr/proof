package com.loancalculator.backend.domain.service;

import com.loancalculator.backend.domain.model.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PaymentDateCalculatorTest {

    private PaymentDateCalculator calculator;
    private Loan testLoan;
    private final int TOTAL_INSTALLMENTS = 120;

    @BeforeEach
    void setUp() {
        calculator = new PaymentDateCalculator(TOTAL_INSTALLMENTS);
        testLoan = new Loan(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2034, 1, 1),
                LocalDate.of(2024, 2, 15),
                BigDecimal.valueOf(140000),
                7.0
        );
    }

    @Test
    @DisplayName("Should calculate regular payment dates")
    void shouldCalculateRegularPaymentDates() {
        var firstPayment = calculator.getPaymentDate(testLoan, 1);
        var secondPayment = calculator.getPaymentDate(testLoan, 2);

        assertEquals(LocalDate.of(2024, 2, 15), firstPayment);
        assertEquals(LocalDate.of(2024, 3, 15), secondPayment);
    }

    @Test
    @DisplayName("Should return end date for final installment")
    void shouldReturnEndDateForFinalInstallment() {
        var finalPayment = calculator.getPaymentDate(testLoan, TOTAL_INSTALLMENTS);

        assertEquals(testLoan.endDate(), finalPayment);
    }

    @Test
    @DisplayName("Should detect month end between dates")
    void shouldDetectMonthEndBetweenDates() {
        var currentDate = LocalDate.of(2024, 1, 15);
        var nextPaymentDate = LocalDate.of(2024, 2, 15);

        var hasMonthEnd = calculator.hasMonthEndBetween(currentDate, nextPaymentDate);

        assertTrue(hasMonthEnd);
    }

    @Test
    @DisplayName("Should not detect month end when dates are in same month")
    void shouldNotDetectMonthEndInSameMonth() {
        var currentDate = LocalDate.of(2024, 1, 15);
        var nextPaymentDate = LocalDate.of(2024, 1, 20);

        var hasMonthEnd = calculator.hasMonthEndBetween(currentDate, nextPaymentDate);

        assertFalse(hasMonthEnd);
    }

    @Test
    @DisplayName("Should get correct month end")
    void shouldGetCorrectMonthEnd() {
        var date = LocalDate.of(2024, 2, 15);
        var monthEnd = calculator.getMonthEnd(date);

        assertEquals(LocalDate.of(2024, 2, 29), monthEnd);
    }
}
