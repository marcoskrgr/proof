package com.loancalculator.backend.application.mapper;

import com.loancalculator.backend.application.dto.LoanScheduleEntryDTO;
import com.loancalculator.backend.domain.model.LoanScheduleEntry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanScheduleEntryMapper {
    public static LoanScheduleEntryDTO toDTO(LoanScheduleEntry entry) {
        return new LoanScheduleEntryDTO(
                entry.competenceDate(),
                entry.loanAmount(),
                entry.outstandingBalance(),
                entry.installmentNumber(),
                entry.installmentTotal(),
                entry.amortization(),
                entry.principalBalance(),
                entry.provision(),
                entry.accumulatedInterest(),
                entry.paid()
        );
    }

    public static List<LoanScheduleEntryDTO> toDTOList(List<LoanScheduleEntry> entries) {
        return entries.stream()
                .map(LoanScheduleEntryMapper::toDTO)
                .collect(Collectors.toList());
    }
}
