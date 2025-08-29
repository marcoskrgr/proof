package com.loancalculator.backend.application.port.in;

import com.loancalculator.backend.domain.model.Loan;
import com.loancalculator.backend.domain.model.LoanScheduleEntry;

import java.util.List;

public interface LoanCalculationPort {
    List<LoanScheduleEntry> calculateLoan(Loan loan);
}
