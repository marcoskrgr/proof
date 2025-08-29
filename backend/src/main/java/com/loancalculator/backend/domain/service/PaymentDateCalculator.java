package com.loancalculator.backend.domain.service;

import com.loancalculator.backend.domain.model.Loan;
import java.time.LocalDate;
import java.time.YearMonth;

public class PaymentDateCalculator {

    private final int totalInstallments;

    public PaymentDateCalculator(int totalInstallments) {
        this.totalInstallments = totalInstallments;
    }

    public LocalDate getPaymentDate(Loan loan, int installmentNumber) {
        if (installmentNumber == totalInstallments) {
            return loan.endDate();
        }
        return loan.firstPaymentDate().plusMonths(installmentNumber - 1);
    }

    public boolean hasMonthEndBetween(LocalDate currentDate, LocalDate nextPaymentDate) {
        var yearMonth = YearMonth.from(currentDate);
        var endOfMonth = yearMonth.atEndOfMonth();
        return endOfMonth.isAfter(currentDate) && endOfMonth.isBefore(nextPaymentDate);
    }

    public LocalDate getMonthEnd(LocalDate date) {
        var yearMonth = YearMonth.from(date);
        return yearMonth.atEndOfMonth();
    }
}