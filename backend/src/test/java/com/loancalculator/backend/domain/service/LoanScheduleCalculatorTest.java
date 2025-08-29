package com.loancalculator.backend.domain.service;

import com.loancalculator.backend.domain.model.Loan;
import com.loancalculator.backend.domain.model.LoanScheduleEntry;
import com.loancalculator.backend.domain.service.factory.LoanScheduleEntryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoanScheduleCalculatorTest {

    @Mock private ProvisionCalculator provisionCalculator;
    @Mock private AmortizationCalculator amortizationCalculator;
    @Mock private PaymentDateCalculator dateCalculator;
    @Mock private LoanScheduleEntryFactory entryFactory;

    private LoanScheduleCalculator calculator;
    private Loan testLoan;

    @BeforeEach
    void setUp() {
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
    @DisplayName("Should create initial entry")
    void shouldCreateInitialEntry() {
        var initialEntry = mock(LoanScheduleEntry.class);
        when(entryFactory.createInitialEntry(any(), any())).thenReturn(initialEntry);
        when(dateCalculator.getPaymentDate(any(), eq(1))).thenReturn(LocalDate.of(2024, 2, 15));
        when(dateCalculator.hasMonthEndBetween(any(), any())).thenReturn(false);
        when(provisionCalculator.calculate(any(), any(), any(), any(), any())).thenReturn(BigDecimal.valueOf(100));
        when(amortizationCalculator.calculate(any(), any(), anyInt())).thenReturn(BigDecimal.valueOf(1166.67));
        when(entryFactory.createPaymentEntry(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(mock(LoanScheduleEntry.class));

        var result = calculator.calculateLoanSchedule(testLoan);

        verify(entryFactory).createInitialEntry(testLoan.startDate(), testLoan.loanAmount());
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle month end entries")
    void shouldHandleMonthEndEntries() {
        var initialEntry = mock(LoanScheduleEntry.class);
        var monthEndEntry = mock(LoanScheduleEntry.class);
        var paymentEntry = mock(LoanScheduleEntry.class);

        when(entryFactory.createInitialEntry(any(), any())).thenReturn(initialEntry);
        when(entryFactory.createMonthEndEntry(any(), any(), any(), any())).thenReturn(monthEndEntry);
        when(entryFactory.createPaymentEntry(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(paymentEntry);

        when(dateCalculator.getPaymentDate(any(), eq(1))).thenReturn(LocalDate.of(2024, 2, 15));
        when(dateCalculator.hasMonthEndBetween(any(), any())).thenReturn(true);
        when(dateCalculator.getMonthEnd(any())).thenReturn(LocalDate.of(2024, 1, 31));
        when(provisionCalculator.calculate(any(), any(), any(), any(), any())).thenReturn(BigDecimal.valueOf(100));
        when(amortizationCalculator.calculate(any(), any(), anyInt())).thenReturn(BigDecimal.valueOf(1166.67));

        var loan = new Loan(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2024, 2, 15),
                BigDecimal.valueOf(10000),
                2.0
        );

        calculator.calculateLoanSchedule(loan);

        verify(entryFactory, atLeastOnce()).createMonthEndEntry(any(), any(), any(), any());
        verify(entryFactory, atLeastOnce()).createPaymentEntry(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Should calculate all installments")
    void shouldCalculateAllInstallments() {
        var initialEntry = mock(LoanScheduleEntry.class);
        var paymentEntry = mock(LoanScheduleEntry.class);

        when(entryFactory.createInitialEntry(any(), any())).thenReturn(initialEntry);
        when(entryFactory.createPaymentEntry(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(paymentEntry);

        when(dateCalculator.hasMonthEndBetween(any(), any())).thenReturn(false);
        when(dateCalculator.getPaymentDate(any(), anyInt())).thenAnswer(invocation -> {
            int installment = invocation.getArgument(1);
            if (installment == 120) return testLoan.endDate();
            return testLoan.firstPaymentDate().plusMonths(installment - 1);
        });

        when(provisionCalculator.calculate(any(), any(), any(), any(), any())).thenReturn(BigDecimal.valueOf(100));
        when(amortizationCalculator.calculate(any(), any(), anyInt())).thenReturn(BigDecimal.valueOf(1166.67));

        var result = calculator.calculateLoanSchedule(testLoan);

        verify(entryFactory, times(120)).createPaymentEntry(any(), any(), any(), any(), any(), any(), any(), any());
    }
}