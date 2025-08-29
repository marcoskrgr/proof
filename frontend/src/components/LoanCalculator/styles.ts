import { createStyles } from 'antd-style';

export const useLoanCalculatorStyles = createStyles(({ css, token }) => ({
    loanCalculatorContainer: css`
        box-sizing: border-box;
        padding: 24px;
        width: 100%;
        max-width: 1400px;
        margin: 0 auto;
    `,
    formContainer: css`
        margin-bottom: 24px;
        display: flex;
        gap: 16px;
        align-items: flex-end;
        flex-wrap: wrap;
        justify-content: center;
        width: 100%;

        .ant-form-item {
            margin-bottom: 0;
        }
    `,
    customTable: css`
        .ant-table-body, .ant-table-content {
            scrollbar-width: thin;
            scrollbar-color: ${token.colorBorder} transparent;
        }
    `,
    errorAlertContainer: css`
        position: absolute;
        top: 24px;
        right: 24px;
        z-index: 10;
        max-width: 500px;
        width: 20%;
        text-align: center;
    `,
}));