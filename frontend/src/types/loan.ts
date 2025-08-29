import type { Dayjs } from 'dayjs';
import React from 'react';

export interface LoanCalculationForm {
    startDate: Dayjs;
    endDate: Dayjs;
    firstPaymentDate: Dayjs;
    loanAmount: number;
    interestRate: number;
}

export interface LoanInstallment {
    key: React.Key;
    competenceDate: string;
    loanAmount: number;
    outstandingBalance: number;
    installmentNumber: string;
    installmentTotal: number;
    amortization: number;
    principalBalance: number;
    provision: number;
    accumulatedInterest: number;
    paid: number;
}