package com.personalFinance.personal_finance.objective.domain.service.validation;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.shared.validator.Validator;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validador de formato Mês/Ano seguindo SRP.
 */
@Component
public class MonthYearValidator implements Validator<String> {

    private static final DateTimeFormatter MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    public ValidationResult validate(String mesAno) {
        if (mesAno == null || mesAno.isBlank()) {
            return ValidationResult.invalid("Mês/Ano não pode ser nulo ou vazio");
        }

        try {
            YearMonth.parse(mesAno, MONTH_YEAR_FORMATTER);
            return ValidationResult.valid();
        } catch (DateTimeParseException e) {
            return ValidationResult.invalid("Formato de Mês/Ano inválido. Use 'yyyy-MM' (Ex: '2024-12')");
        }
    }
}
