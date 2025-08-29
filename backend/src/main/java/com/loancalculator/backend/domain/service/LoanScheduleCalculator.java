package com.loancalculator.backend.domain.service;

import com.loancalculator.backend.domain.model.Loan;
import com.loancalculator.backend.domain.model.LoanScheduleEntry;
import com.loancalculator.backend.domain.service.factory.LoanScheduleEntryFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LoanScheduleCalculator {

    private final int totalInstallments;
    private final ProvisionCalculator provisionCalculator;
    private final AmortizationCalculator amortizationCalculator;
    private final PaymentDateCalculator dateCalculator;
    private final LoanScheduleEntryFactory entryFactory;

    public LoanScheduleCalculator(
        int totalInstallments,
        ProvisionCalculator provisionCalculator,
        AmortizationCalculator amortizationCalculator,
        PaymentDateCalculator dateCalculator,
        LoanScheduleEntryFactory entryFactory
    ) {
        this.totalInstallments = totalInstallments;
        this.provisionCalculator = provisionCalculator;
        this.amortizationCalculator = amortizationCalculator;
        this.dateCalculator = dateCalculator;
        this.entryFactory = entryFactory;
    }

    public List<LoanScheduleEntry> calculateLoanSchedule(Loan loan) {
        var schedule = new ArrayList<LoanScheduleEntry>();

        var loanAmount = loan.loanAmount();
        var interestRate = BigDecimal.valueOf(loan.interestRate() / 100.0);
        var currentBalance = loanAmount;
        var accumulatedInterest = BigDecimal.ZERO;
        var currentDate = loan.startDate();
        var installmentNumber = 0;

        schedule.add(entryFactory.createInitialEntry(loan.startDate(), loanAmount));

        while (installmentNumber < totalInstallments) {
            var nextPaymentDate = dateCalculator.getPaymentDate(loan, installmentNumber + 1);

            if (dateCalculator.hasMonthEndBetween(currentDate, nextPaymentDate)) {
                var monthEndDate = dateCalculator.getMonthEnd(currentDate);

                var provision = provisionCalculator.calculate(
                        currentBalance, accumulatedInterest, currentDate, monthEndDate, interestRate);
                accumulatedInterest = accumulatedInterest.add(provision);

                schedule.add(entryFactory.createMonthEndEntry(
                        monthEndDate, currentBalance, provision, accumulatedInterest));

                currentDate = monthEndDate;
            }

            installmentNumber++;

            var provision = provisionCalculator.calculate(
                    currentBalance, accumulatedInterest, currentDate, nextPaymentDate, interestRate);

            var amortization = amortizationCalculator.calculate(
                    loanAmount, currentBalance, installmentNumber);

            var tempAccumulated = accumulatedInterest.add(provision);
            var paid = tempAccumulated;
            var total = amortization.add(paid);
            var newBalance = currentBalance.subtract(amortization);
            var newAccumulated = tempAccumulated.subtract(paid);

            var installmentLabel = installmentNumber + "/" + totalInstallments;

            schedule.add(entryFactory.createPaymentEntry(
                    nextPaymentDate, installmentLabel, amortization, provision,
                    newBalance, newAccumulated, paid, total));

            currentBalance = newBalance;
            accumulatedInterest = newAccumulated;
            currentDate = nextPaymentDate;
        }

        return schedule;
    }
}