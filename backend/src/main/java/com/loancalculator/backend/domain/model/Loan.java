package com.loancalculator.backend.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Loan(
        LocalDate startDate,
        LocalDate endDate,
        LocalDate firstPaymentDate,
        BigDecimal loanAmount,
        double interestRate
) {
    public Loan {
        if (endDate == null || !endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("A data final deve ser posterior à data inicial.");
        }

        if (firstPaymentDate == null || !firstPaymentDate.isAfter(startDate)) {
            throw new IllegalArgumentException("A data do primeiro pagamento (firstPaymentDate) deve ser posterior à data inicial (startDate).");
        }

        if (!firstPaymentDate.isBefore(endDate)) {
            throw new IllegalArgumentException("A data do primeiro pagamento deve ser anterior à data final.");
        }

        if (loanAmount == null || loanAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do empréstimo deve ser positivo.");
        }
    }
}
