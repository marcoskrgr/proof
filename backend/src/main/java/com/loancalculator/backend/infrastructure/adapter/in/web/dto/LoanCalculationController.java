package com.loancalculator.backend.infrastructure.adapter.in.web.dto;

import com.loancalculator.backend.application.dto.LoanCalculationRequestDTO;
import com.loancalculator.backend.application.dto.LoanCalculationResponseDTO;
import com.loancalculator.backend.application.dto.LoanScheduleEntryDTO;
import com.loancalculator.backend.application.mapper.LoanCalculationRequestMapper;
import com.loancalculator.backend.application.mapper.LoanScheduleEntryMapper;
import com.loancalculator.backend.application.port.in.LoanCalculationPort;
import com.loancalculator.backend.domain.model.Loan;
import com.loancalculator.backend.domain.model.LoanScheduleEntry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-calculator")
public class LoanCalculationController {

    private final LoanCalculationPort loanCalculationPortPort;

    public LoanCalculationController(LoanCalculationPort loanCalculationPortPort) {
        this.loanCalculationPortPort = loanCalculationPortPort;
    }

    @PostMapping("/calculate")
    public LoanCalculationResponseDTO simulateLoan(@RequestBody LoanCalculationRequestDTO request) {
        Loan loan = LoanCalculationRequestMapper.toDomain(request);
        List<LoanScheduleEntry> loanScheduleEntryList = loanCalculationPortPort.calculateLoan(loan);
        List<LoanScheduleEntryDTO> dtoList = LoanScheduleEntryMapper.toDTOList(loanScheduleEntryList);
        return new LoanCalculationResponseDTO(dtoList);
    }

}
