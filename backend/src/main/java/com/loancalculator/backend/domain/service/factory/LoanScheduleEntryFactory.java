package com.loancalculator.backend.domain.service.factory;

import com.loancalculator.backend.domain.model.LoanScheduleEntry;
import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanScheduleEntryFactory {

    public LoanScheduleEntry createInitialEntry(LocalDate startDate, BigDecimal loanAmount) {
        return new LoanScheduleEntry(
                startDate,
                loanAmount,
                loanAmount,
                null,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                loanAmount,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
    }

    public LoanScheduleEntry createMonthEndEntry(
            LocalDate date,
            BigDecimal balance,
            BigDecimal provision,
            BigDecimal accumulated
    ) {
        return new LoanScheduleEntry(
                date,
                BigDecimal.ZERO,
                balance.add(accumulated),
                null,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                balance,
                provision,
                accumulated,
                BigDecimal.ZERO
        );
    }

    public LoanScheduleEntry createPaymentEntry(
            LocalDate date,
            String installmentNumber,
            BigDecimal amortization,
            BigDecimal provision,
            BigDecimal newBalance,
            BigDecimal newAccumulated,
            BigDecimal paid,
            BigDecimal total
    ) {
        return new LoanScheduleEntry(
                date,
                BigDecimal.ZERO,
                newBalance.add(newAccumulated),
                installmentNumber,
                total,
                amortization,
                newBalance,
                provision,
                newAccumulated,
                paid
        );
    }
}