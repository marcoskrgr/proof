import type {TableColumnsType} from 'antd';
import type {LoanInstallment} from '../../types/loan';
import {formatCurrency} from '../../utils/formatter';

export const getLoanTableColumns = (): TableColumnsType<LoanInstallment> => [
    {
        title: 'Empréstimo',
        children: [
            {title: 'Data Competência', dataIndex: 'competenceDate', key: 'competenceDate'},
            {
                title: 'Valor de Empréstimo',
                dataIndex: 'loanAmount',
                key: 'loanAmount',
                render: (value) => (value > 0 ? formatCurrency(value) : '-'),
            },
            {
                title: 'Saldo Devedor',
                dataIndex: 'outstandingBalance',
                key: 'outstandingBalance',
                render: formatCurrency,
            },
        ],
    },
    {
        title: 'Parcela',
        children: [
            {title: 'Consolidada', dataIndex: 'installmentNumber', key: 'installmentNumber'},
            {title: 'Total', dataIndex: 'installmentTotal', key: 'installmentTotal', render: formatCurrency},
        ],
    },
    {
        title: 'Principal',
        children: [
            {title: 'Amortização', dataIndex: 'amortization', key: 'amortization', render: formatCurrency},
            {title: 'Saldo', dataIndex: 'principalBalance', key: 'principalBalance', render: formatCurrency},
        ],
    },
    {
        title: 'Juros',
        children: [
            {title: 'Provisão', dataIndex: 'provision', key: 'provision', render: formatCurrency},
            {title: 'Acumulado', dataIndex: 'accumulatedInterest', key: 'accumulatedInterest', render: formatCurrency},
            {title: 'Pago', dataIndex: 'paid', key: 'paid', render: formatCurrency},
        ],
    },
];