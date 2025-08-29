package com.loancalculator.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanScheduleEntryDTO {
    private LocalDate competenceDate;
    private BigDecimal loanAmount;
    private BigDecimal outstandingBalance;
    private String installmentNumber;
    private BigDecimal installmentTotal;
    private BigDecimal amortization;
    private BigDecimal principalBalance;
    private BigDecimal provision;
    private BigDecimal accumulatedInterest;
    private BigDecimal paid;
}
