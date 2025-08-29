package com.loancalculator.backend.application.usecase;

import com.loancalculator.backend.application.port.in.LoanCalculationPort;
import com.loancalculator.backend.domain.model.Loan;
import com.loancalculator.backend.domain.model.LoanScheduleEntry;
import com.loancalculator.backend.domain.service.LoanScheduleCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanCalculationUseCase implements LoanCalculationPort {

    private final LoanScheduleCalculator calculator;

    public LoanCalculationUseCase(LoanScheduleCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public List<LoanScheduleEntry> calculateLoan(Loan loan) {
        return calculator.calculateLoanSchedule(loan);
    }
}
