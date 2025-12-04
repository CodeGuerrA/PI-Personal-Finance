package com.personalFinance.personal_finance.transaction.domain.service;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;

import com.personalFinance.personal_finance.transaction.domain.entity.FrequenciaRecorrencia;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;
import com.personalFinance.personal_finance.transaction.domain.service.validation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RecurringTransactionValidationService {

    private final RecurringValorValidator valorValidator;
    private final RecurringDescricaoValidator descricaoValidator;
    private final RecurringDataInicioValidator dataInicioValidator;
    private final RecurringFrequenciaValidator frequenciaValidator;
    private final RecurringTipoValidator tipoValidator;

    /**
     * Validação para criação
     */
    public void validateCreation(BigDecimal valor, String descricao,
                                 LocalDate dataInicio,
                                 FrequenciaRecorrencia frequencia,
                                 TipoTransacao tipo) {

        validate(valorValidator.validate(valor));
        validate(descricaoValidator.validate(descricao));
        validate(dataInicioValidator.validate(dataInicio));
        validate(frequenciaValidator.validate(frequencia));
        validate(tipoValidator.validate(tipo));
    }

    /**
     * Validação para atualização parcial
     */
    public void validateUpdate(BigDecimal valor, String descricao,
                               LocalDate dataInicio,
                               FrequenciaRecorrencia frequencia) {

        if (valor != null) validate(valorValidator.validate(valor));
        if (descricao != null) validate(descricaoValidator.validate(descricao));
        if (dataInicio != null) validate(dataInicioValidator.validate(dataInicio));
        if (frequencia != null) validate(frequenciaValidator.validate(frequencia));
    }

    private void validate(ValidationResult result) {
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.errorMessage());
        }
    }
}