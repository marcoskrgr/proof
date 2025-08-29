package com.loancalculator.backend.domain.service.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class LoanScheduleEntryFactoryTest {

    private LoanScheduleEntryFactory factory;

    @BeforeEach
    void setUp() {
        factory = new LoanScheduleEntryFactory();
    }

    @Test
    @DisplayName("Should create initial entry correctly")
    void shouldCreateInitialEntry() {
        var startDate = LocalDate.of(2024, 1, 1);
        var loanAmount = BigDecimal.valueOf(140000);

        var entry = factory.createInitialEntry(startDate, loanAmount);

        assertEquals(startDate, entry.competenceDate());
        assertEquals(loanAmount, entry.loanAmount());
        assertEquals(loanAmount, entry.outstandingBalance());
        assertNull(entry.installmentNumber());
        assertEquals(BigDecimal.ZERO, entry.installmentTotal());
        assertEquals(BigDecimal.ZERO, entry.amortization());
        assertEquals(loanAmount, entry.principalBalance());
        assertEquals(BigDecimal.ZERO, entry.provision());
        assertEquals(BigDecimal.ZERO, entry.accumulatedInterest());
        assertEquals(BigDecimal.ZERO, entry.paid());
    }

    @Test
    @DisplayName("Should create month end entry correctly")
    void shouldCreateMonthEndEntry() {
        var date = LocalDate.of(2024, 1, 31);
        var balance = BigDecimal.valueOf(140000);
        var provision = BigDecimal.valueOf(791.58);
        var accumulated = BigDecimal.valueOf(791.58);

        var entry = factory.createMonthEndEntry(date, balance, provision, accumulated);

        assertEquals(date, entry.competenceDate());
        assertEquals(BigDecimal.ZERO, entry.loanAmount());
        assertEquals(balance.add(accumulated), entry.outstandingBalance());
        assertNull(entry.installmentNumber());
        assertEquals(BigDecimal.ZERO, entry.installmentTotal());
        assertEquals(BigDecimal.ZERO, entry.amortization());
        assertEquals(balance, entry.principalBalance());
        assertEquals(provision, entry.provision());
        assertEquals(accumulated, entry.accumulatedInterest());
        assertEquals(BigDecimal.ZERO, entry.paid());
    }

    @Test
    @DisplayName("Should create payment entry correctly")
    void shouldCreatePaymentEntry() {
        var date = LocalDate.of(2024, 2, 15);
        var installmentNumber = "1/120";
        var amortization = BigDecimal.valueOf(1166.67);
        var provision = BigDecimal.valueOf(397.47);
        var newBalance = BigDecimal.valueOf(138833.33);
        var newAccumulated = BigDecimal.ZERO;
        var paid = BigDecimal.valueOf(1189.05);
        var total = BigDecimal.valueOf(2355.72);

        var entry = factory.createPaymentEntry(date, installmentNumber, amortization,
                provision, newBalance, newAccumulated, paid, total);

        assertEquals(date, entry.competenceDate());
        assertEquals(BigDecimal.ZERO, entry.loanAmount());
        assertEquals(newBalance.add(newAccumulated), entry.outstandingBalance());
        assertEquals(installmentNumber, entry.installmentNumber());
        assertEquals(total, entry.installmentTotal());
        assertEquals(amortization, entry.amortization());
        assertEquals(newBalance, entry.principalBalance());
        assertEquals(provision, entry.provision());
        assertEquals(newAccumulated, entry.accumulatedInterest());
        assertEquals(paid, entry.paid());
    }
}
