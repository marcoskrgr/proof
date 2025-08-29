package com.loancalculator.backend.domain.service;

import com.loancalculator.backend.domain.model.Loan;
import com.loancalculator.backend.domain.service.factory.LoanScheduleEntryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class LoanScheduleCalculatorIntegrationTest {

    private LoanScheduleCalculator calculator;
    private Loan testLoan;

    @BeforeEach
    void setUp() {
        var provisionCalculator = new ProvisionCalculator();
        var amortizationCalculator = new AmortizationCalculator(120);
        var dateCalculator = new PaymentDateCalculator(120);
        var entryFactory = new LoanScheduleEntryFactory();

        calculator = new LoanScheduleCalculator(120, provisionCalculator,
                amortizationCalculator, dateCalculator, entryFactory);

        testLoan = new Loan(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2034, 1, 1),
                LocalDate.of(2024, 2, 15),
                BigDecimal.valueOf(140000),
                7.0
        );
    }

    @Test
    @DisplayName("Should generate complete loan schedule")
    void shouldGenerateCompleteLoanSchedule() {
        var schedule = calculator.calculateLoanSchedule(testLoan);

        assertNotNull(schedule);
        assertFalse(schedule.isEmpty());

        var firstEntry = schedule.get(0);
        assertEquals(testLoan.startDate(), firstEntry.competenceDate());
        assertEquals(testLoan.loanAmount(), firstEntry.loanAmount());

        long paymentEntries = schedule.stream()
                .filter(entry -> entry.installmentNumber() != null)
                .count();
        assertEquals(120, paymentEntries);

        var finalPayment = schedule.stream()
                .filter(entry -> "120/120".equals(entry.installmentNumber()))
                .findFirst()
                .orElseThrow();
        assertEquals(testLoan.endDate(), finalPayment.competenceDate());
        assertEquals(BigDecimal.ZERO.setScale(2), finalPayment.principalBalance());
    }

    @Test
    @DisplayName("Should maintain balance consistency throughout schedule")
    void shouldMaintainBalanceConsistency() {
        var schedule = calculator.calculateLoanSchedule(testLoan);

        for (var entry : schedule) {
            var expectedOutstanding = entry.principalBalance().add(entry.accumulatedInterest());
            assertEquals(expectedOutstanding, entry.outstandingBalance(),
                    "Outstanding balance mismatch at " + entry.competenceDate());

            if (entry.installmentNumber() != null) {
                var expectedTotal = entry.amortization().add(entry.paid());
                assertEquals(expectedTotal, entry.installmentTotal(),
                        "Total mismatch at installment " + entry.installmentNumber());
            }
        }
    }

    @Test
    @DisplayName("Should include December 31st provision entry")
    void shouldIncludeDecember31stProvisionEntry() {
        var schedule = calculator.calculateLoanSchedule(testLoan);

        var hasDecember31Entry = schedule.stream()
                .anyMatch(entry -> entry.competenceDate().equals(LocalDate.of(2033, 12, 31)));

        assertTrue(hasDecember31Entry, "Should have December 31st provision entry");
    }
}
