package com.loancalculator.backend.application.dto;

import java.util.List;

public record LoanCalculationResponseDTO(
        List<LoanScheduleEntryDTO> schedule
) {}
