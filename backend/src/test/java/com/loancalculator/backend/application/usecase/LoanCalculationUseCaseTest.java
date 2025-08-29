package com.loancalculator.backend.application.usecase;

import com.loancalculator.backend.domain.model.Loan;
import com.loancalculator.backend.domain.model.LoanScheduleEntry;
import com.loancalculator.backend.domain.service.LoanScheduleCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoanCalculationUseCaseTest {

    @Mock private LoanScheduleCalculator calculator;
    private LoanCalculationUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new LoanCalculationUseCase(calculator);
    }

    @Test
    @DisplayName("Should delegate to loan schedule calculator")
    void shouldDelegateToCalculator() {
        var loan = new Loan(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2034, 1, 1),
                LocalDate.of(2024, 2, 15),
                BigDecimal.valueOf(140000),
                7.0
        );

        var expectedEntries = List.of(mock(LoanScheduleEntry.class));
        when(calculator.calculateLoanSchedule(loan)).thenReturn(expectedEntries);

        var result = useCase.calculateLoan(loan);

        verify(calculator).calculateLoanSchedule(loan);
        assertEquals(expectedEntries, result);
    }

    @Test
    @DisplayName("Should handle null loan gracefully")
    void shouldHandleNullLoan() {
        when(calculator.calculateLoanSchedule(null)).thenThrow(new IllegalArgumentException("Loan cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> useCase.calculateLoan(null));
    }
}
