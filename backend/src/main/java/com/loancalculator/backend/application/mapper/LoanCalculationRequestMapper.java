package com.loancalculator.backend.application.mapper;

import com.loancalculator.backend.application.dto.LoanCalculationRequestDTO;
import com.loancalculator.backend.domain.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanCalculationRequestMapper {
    public static Loan toDomain(LoanCalculationRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("O DTO não pode ser nulo");
        }

        return new Loan(
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getFirstPaymentDate(),
                dto.getLoanAmount(),
                dto.getInterestRate()
        );
    }
}
