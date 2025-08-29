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
        if (startDate == null) {
            throw new IllegalArgumentException("A data inicial é obrigatória.");
        }

        if (endDate == null) {
            throw new IllegalArgumentException("A data final é obrigatória.");
        }

        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("A data final deve ser posterior à data inicial.");
        }

        if (firstPaymentDate == null) {
            throw new IllegalArgumentException("A data do primeiro pagamento é obrigatória.");
        }

        if (!firstPaymentDate.isAfter(startDate)) {
            throw new IllegalArgumentException("A data do primeiro pagamento deve ser posterior à data inicial.");
        }

        if (!firstPaymentDate.isBefore(endDate)) {
            throw new IllegalArgumentException("A data do primeiro pagamento deve ser anterior à data final.");
        }

        if (loanAmount == null) {
            throw new IllegalArgumentException("O valor do empréstimo é obrigatório.");
        }

        if (loanAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do empréstimo deve ser positivo.");
        }

        if (interestRate < 0) {
            throw new IllegalArgumentException("A taxa de juros não pode ser negativa.");
        }

        if (interestRate > 100) {
            throw new IllegalArgumentException("A taxa de juros não pode ser superior a 100%.");
        }
    }
}
