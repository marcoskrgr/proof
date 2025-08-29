export const formatCurrency = (value: number | undefined | null): string => {
    if (value === null || value === undefined) {
        return '0,00';
    }
    return new Intl.NumberFormat('pt-BR', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    }).format(value);
};