package com.loancalculator.backend.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanCalculationRequestDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate firstPaymentDate;
    private BigDecimal loanAmount;
    private double interestRate;
}
