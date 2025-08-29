import type { LoanCalculationForm, LoanInstallment } from '../types/loan';
import dayjs from 'dayjs';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export const calculateLoan = async (
    params: LoanCalculationForm
): Promise<LoanInstallment[]> => {
    const payload = {
        startDate: dayjs(params.startDate).format('YYYY-MM-DD'),
        endDate: dayjs(params.endDate).format('YYYY-MM-DD'),
        firstPaymentDate: dayjs(params.firstPaymentDate).format('YYYY-MM-DD'),
        loanAmount: params.loanAmount,
        interestRate: params.interestRate,
    };

    const response = await fetch(`${API_BASE_URL}/api/v1/loan-calculator/calculate`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
    });

    if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData?.message || 'Falha ao calcular o empréstimo.');
    }

    const data = await response.json();
    return data.schedule;
};
