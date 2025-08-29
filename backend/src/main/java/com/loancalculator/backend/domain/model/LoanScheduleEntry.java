package com.loancalculator.backend.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanScheduleEntry(
        LocalDate competenceDate,
        BigDecimal loanAmount,
        BigDecimal outstandingBalance,
        String installmentNumber,
        BigDecimal installmentTotal,
        BigDecimal amortization,
        BigDecimal principalBalance,
        BigDecimal provision,
        BigDecimal accumulatedInterest,
        BigDecimal paid
) {}
