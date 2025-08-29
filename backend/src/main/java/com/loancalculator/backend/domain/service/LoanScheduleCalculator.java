package com.loancalculator.backend.domain.service;

import com.loancalculator.backend.domain.model.Loan;
import com.loancalculator.backend.domain.model.LoanScheduleEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LoanScheduleCalculator {

    private static final int TOTAL_INSTALLMENTS = 120;
    private static final int DAYS_BASE = 360;
    private static final int DECIMAL_SCALE = 2;

    public List<LoanScheduleEntry> calculateLoanSchedule(Loan loan) {
        List<LoanScheduleEntry> schedule = new ArrayList<>();

        BigDecimal loanAmount = loan.loanAmount();
        BigDecimal interestRate = BigDecimal.valueOf(loan.interestRate() / 100.0);
        BigDecimal currentBalance = loanAmount;
        BigDecimal accumulatedInterest = BigDecimal.ZERO;
        LocalDate currentDate = loan.startDate();
        int installmentNumber = 0;

        schedule.add(new LoanScheduleEntry(
                loan.startDate(),
                loanAmount,
                loanAmount,
                null,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                loanAmount,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        ));

        while (installmentNumber < TOTAL_INSTALLMENTS) {
            LocalDate nextPaymentDate = getNextPaymentDate(loan, installmentNumber);

            if (shouldAddMonthEndEntry(installmentNumber, currentDate, nextPaymentDate)) {
                YearMonth yearMonth = YearMonth.from(currentDate);
                LocalDate endOfMonth = yearMonth.atEndOfMonth();

                BigDecimal provision = calculateProvision(currentBalance, accumulatedInterest,
                        currentDate, endOfMonth, interestRate);
                accumulatedInterest = accumulatedInterest.add(provision);

                schedule.add(new LoanScheduleEntry(
                        endOfMonth,
                        BigDecimal.ZERO,
                        currentBalance.add(accumulatedInterest),
                        null,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        currentBalance,
                        provision,
                        accumulatedInterest,
                        BigDecimal.ZERO
                ));

                currentDate = endOfMonth;
            }

            installmentNumber++;

            BigDecimal provision = calculateProvision(currentBalance, accumulatedInterest,
                    currentDate, nextPaymentDate, interestRate);

            String consolidated = installmentNumber + "/" + TOTAL_INSTALLMENTS;

            BigDecimal amortization = calculateAmortization(loanAmount, currentBalance, installmentNumber);
            BigDecimal tempAccumulated = accumulatedInterest.add(provision);
            BigDecimal total = amortization.add(tempAccumulated);
            BigDecimal newBalance = currentBalance.subtract(amortization);
            BigDecimal newAccumulated = tempAccumulated.subtract(tempAccumulated);

            schedule.add(new LoanScheduleEntry(
                    nextPaymentDate,
                    BigDecimal.ZERO,
                    newBalance.add(newAccumulated),
                    consolidated,
                    total,
                    amortization,
                    newBalance,
                    provision,
                    newAccumulated,
                    tempAccumulated
            ));

            currentBalance = newBalance;
            accumulatedInterest = newAccumulated;
            currentDate = nextPaymentDate;
        }

        return schedule;
    }

    private LocalDate getNextPaymentDate(Loan loan, int installmentNumber) {
        if (installmentNumber + 1 == TOTAL_INSTALLMENTS) {
            return loan.endDate();
        }
        return loan.firstPaymentDate().plusMonths(installmentNumber);
    }

    private boolean shouldAddMonthEndEntry(int installmentNumber, LocalDate currentDate, LocalDate nextPaymentDate) {
        YearMonth yearMonth = YearMonth.from(currentDate);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        return endOfMonth.isAfter(currentDate) && endOfMonth.isBefore(nextPaymentDate);
    }

    private BigDecimal calculateAmortization(
            BigDecimal loanAmount,
            BigDecimal currentBalance,
            int installmentNumber
    ) {
        if (installmentNumber == TOTAL_INSTALLMENTS) {
            return currentBalance;
        }
        return loanAmount.divide(BigDecimal.valueOf(TOTAL_INSTALLMENTS), DECIMAL_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateProvision(
            BigDecimal balance,
            BigDecimal accumulated,
            LocalDate from,
            LocalDate to,
            BigDecimal interestRate
    ) {
        long days = ChronoUnit.DAYS.between(from, to);

        if (days == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal onePlusRate = interestRate.add(BigDecimal.ONE);
        BigDecimal daysFraction = BigDecimal.valueOf(days)
                .divide(BigDecimal.valueOf(DAYS_BASE), 10, RoundingMode.HALF_UP);

        double baseRate = onePlusRate.doubleValue();
        double exponent = daysFraction.doubleValue();
        double compoundFactor = Math.pow(baseRate, exponent);

        BigDecimal compoundFactorBD = BigDecimal.valueOf(compoundFactor);
        BigDecimal growthFactor = compoundFactorBD.subtract(BigDecimal.ONE);
        BigDecimal baseAmount = balance.add(accumulated);

        return growthFactor
                .multiply(baseAmount)
                .setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);
    }
}