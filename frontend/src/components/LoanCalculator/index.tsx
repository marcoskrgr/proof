import React, {useState} from 'react';
import {Table, Form, DatePicker, Button, InputNumber, message, Spin, Typography, Alert} from 'antd';
import {calculateLoan} from '../../api/loanService';
import type {LoanCalculationForm, LoanInstallment} from '../../types/loan';
import {getLoanTableColumns} from './columns';
import {useLoanCalculatorStyles} from './styles';

const {Title} = Typography;

const LoanCalculator: React.FC = () => {
    const {styles} = useLoanCalculatorStyles();
    const [dataSource, setDataSource] = useState<LoanInstallment[]>([]);
    const [loading, setLoading] = useState(false);
    const [form] = Form.useForm<LoanCalculationForm>();
    const [calculationError, setCalculationError] = useState<string | null>(null);

    const columns = getLoanTableColumns();

    const handleCalculate = async (values: LoanCalculationForm) => {
        setLoading(true);
        setDataSource([]);
        setCalculationError(null);

        try {
            const data = await calculateLoan(values);
            setDataSource(data);
            message.success('Cálculo realizado com sucesso!');
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Ocorreu um erro desconhecido.';
            setCalculationError(errorMessage);
            console.error('Falha no cálculo:', error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.loanCalculatorContainer}>
            {calculationError && (
                <div className={styles.errorAlertContainer}>
                    <Alert
                        message="Erro no Cálculo"
                        description={calculationError}
                        type="error"
                        showIcon
                        closable
                        onClose={() => setCalculationError(null)}
                    />
                </div>
            )}

            <Title level={2} style={{marginBottom: '24px', textAlign: 'center'}}>
                Calculadora de Empréstimos
            </Title>

            <Form
                form={form}
                className={styles.formContainer}
                layout="inline"
                onFinish={handleCalculate}
                size="middle"
            >
                <Form.Item label="Data Inicial" name="startDate"
                           rules={[{required: true, message: 'Campo obrigatório'}]}>
                    <DatePicker format="DD/MM/YYYY" style={{width: 140}}/>
                </Form.Item>
                <Form.Item
                    label="Data Final"
                    name="endDate"
                    dependencies={['startDate']}
                    rules={[
                        {required: true, message: 'Campo obrigatório'},
                        ({getFieldValue}) => ({
                            validator(_, value) {
                                if (!value || !getFieldValue('startDate') || value.isAfter(getFieldValue('startDate'))) {
                                    return Promise.resolve();
                                }
                                return Promise.reject(new Error('A data final deve ser maior que a data inicial.'));
                            },
                        }),
                    ]}
                >
                    <DatePicker format="DD/MM/YYYY" style={{width: 140}}/>
                </Form.Item>
                <Form.Item
                    label="Primeiro Pagamento"
                    name="firstPaymentDate"
                    dependencies={['startDate', 'endDate']}
                    rules={[
                        {required: true, message: 'Campo obrigatório'},
                        ({getFieldValue}) => ({
                            validator(_, value) {
                                const startDate = getFieldValue('startDate');
                                const endDate = getFieldValue('endDate');
                                if (!value || !startDate || !endDate) {
                                    return Promise.resolve();
                                }
                                if (value.isAfter(startDate) && value.isBefore(endDate)) {
                                    return Promise.resolve();
                                }
                                return Promise.reject(new Error('Deve ser entre a data inicial e a final.'));
                            },
                        }),
                    ]}
                >
                    <DatePicker format="DD/MM/YYYY" style={{width: 150}}/>
                </Form.Item>
                <Form.Item label="Valor do Empréstimo" name="loanAmount"
                           rules={[{required: true, message: 'Campo obrigatório'}]}>
                    <InputNumber<number>
                        style={{width: 180}}
                        addonBefore="R$"
                        formatter={(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, '.')}
                        parser={(value) => Number(value?.replace(/R\$\s?|(\.*)/g, '').replace(',', '.')) || 0}
                        decimalSeparator=","
                    />
                </Form.Item>
                <Form.Item label="Taxa de Juros" name="interestRate"
                           rules={[{required: true, message: 'Campo obrigatório'}]}>
                    <InputNumber<number>
                        style={{width: 120}}
                        addonAfter="%"
                        step="0.1"
                    />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" loading={loading}>
                        Calcular
                    </Button>
                </Form.Item>
            </Form>

            <Spin spinning={loading} size="large" tip="Calculando...">
                <Table<LoanInstallment>
                    className={styles.customTable}
                    columns={columns}
                    dataSource={dataSource}
                    bordered
                    size="middle"
                    tableLayout="fixed"
                    scroll={{y: 600}}
                    pagination={{pageSize: 50, showSizeChanger: true}}
                    style={{marginTop: '24px'}}
                />
            </Spin>
        </div>
    );
};

export default LoanCalculator;