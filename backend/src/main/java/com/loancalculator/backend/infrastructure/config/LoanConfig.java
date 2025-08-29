package com.loancalculator.backend.infrastructure.config;

import com.loancalculator.backend.domain.service.LoanScheduleCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanConfig {

    @Bean
    public LoanScheduleCalculator loanScheduleCalculator() {
        return new LoanScheduleCalculator();
    }
}
