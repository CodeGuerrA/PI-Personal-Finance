package com.personalFinance.personal_finance.transaction.domain.service;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.transaction.domain.service.validation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Domain Service para validação de transações.
 * Coordena validadores seguindo SRP/OCP.
 */
@Service
@RequiredArgsConstructor
public class TransactionValidationService {

    private final TransactionDescricaoValidator descricaoValidator;
    private final TransactionValorValidator valorValidator;
    private final TransactionDataValidator dataValidator;
    private final TransactionTipoValidator tipoValidator;
    private final TransactionMetodoPagamentoValidator metodoPagamentoValidator;

    /**
     * Valida dados de criação.
     */
    public void validateCreation(
            String descricao,
            BigDecimal valor,
            LocalDate data,
            String tipo,
            String metodoPagamento
    ) {
        validateDescricao(descricao);
        validateValor(valor);
        validateData(data);
        validateTipo(tipo);
        validateMetodoPagamento(metodoPagamento);
    }

    /**
     * Valida dados de atualização (todos opcionais).
     */
    public void validateUpdate(
            String descricao,
            BigDecimal valor,
            LocalDate data,
            String tipo,
            String metodoPagamento
    ) {
        if (descricao != null) validateDescricao(descricao);
        if (valor != null) validateValor(valor);
        if (data != null) validateData(data);
        if (tipo != null) validateTipo(tipo);
        if (metodoPagamento != null) validateMetodoPagamento(metodoPagamento);
    }

    // ------------------ Private helpers ------------------

    private void validateDescricao(String descricao) {
        ValidationResult result = descricaoValidator.validate(descricao);
        if (!result.isValid()) throw new IllegalArgumentException(result.errorMessage());
    }

    private void validateValor(BigDecimal valor) {
        ValidationResult result = valorValidator.validate(valor);
        if (!result.isValid()) throw new IllegalArgumentException(result.errorMessage());
    }

    private void validateData(LocalDate data) {
        ValidationResult result = dataValidator.validate(data);
        if (!result.isValid()) throw new IllegalArgumentException(result.errorMessage());
    }

    private void validateTipo(String tipo) {
        ValidationResult result = tipoValidator.validate(tipo);
        if (!result.isValid()) throw new IllegalArgumentException(result.errorMessage());
    }

    private void validateMetodoPagamento(String metodo) {
        ValidationResult result = metodoPagamentoValidator.validate(metodo);
        if (!result.isValid()) throw new IllegalArgumentException(result.errorMessage());
    }
}