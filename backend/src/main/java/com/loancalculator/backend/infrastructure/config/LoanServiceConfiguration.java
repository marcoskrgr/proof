package com.loancalculator.backend.infrastructure.config;

import com.loancalculator.backend.domain.service.*;
import com.loancalculator.backend.domain.service.factory.LoanScheduleEntryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanServiceConfiguration {

    @Bean
    public ProvisionCalculator provisionCalculator() {
        return new ProvisionCalculator();
    }

    @Bean
    public AmortizationCalculator amortizationCalculator(@Value("${loan.total-installments:120}") int totalInstallments) {
        return new AmortizationCalculator(totalInstallments);
    }

    @Bean
    public PaymentDateCalculator paymentDateCalculator(@Value("${loan.total-installments:120}") int totalInstallments) {
        return new PaymentDateCalculator(totalInstallments);
    }

    @Bean
    public LoanScheduleEntryFactory loanScheduleEntryFactory() {
        return new LoanScheduleEntryFactory();
    }

    @Bean
    public LoanScheduleCalculator loanScheduleCalculator(
            @Value("${loan.total-installments:120}") int totalInstallments,
            ProvisionCalculator provisionCalculator,
            AmortizationCalculator amortizationCalculator,
            PaymentDateCalculator paymentDateCalculator,
            LoanScheduleEntryFactory entryFactory) {

        return new LoanScheduleCalculator(
                totalInstallments,
                provisionCalculator,
                amortizationCalculator,
                paymentDateCalculator,
                entryFactory
        );
    }
}