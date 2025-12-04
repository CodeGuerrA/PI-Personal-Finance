package com.personalFinance.personal_finance.category.domain.service;

import com.personalFinance.personal_finance.category.domain.entity.CategoryType;
import com.personalFinance.personal_finance.category.domain.exception.InvalidCategoryDataException;
import com.personalFinance.personal_finance.category.domain.service.validation.CategoryCorValidator;
import com.personalFinance.personal_finance.category.domain.service.validation.CategoryIconeValidator;
import com.personalFinance.personal_finance.category.domain.service.validation.CategoryNomeValidator;
import com.personalFinance.personal_finance.category.domain.service.validation.CategoryTipoValidator;
import com.personalFinance.personal_finance.shared.validator.ValidationResult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Domain Service responsável por coordenar validações de Category.
 */
@Service
@RequiredArgsConstructor
public class CategoryValidationService {

    private final CategoryNomeValidator nomeValidator;
    private final CategoryCorValidator corValidator;
    private final CategoryTipoValidator tipoValidator;
    private final CategoryIconeValidator iconeValidator;

    /**
     * Valida dados de criação.
     */
    public void validateCreation(String nome, String cor, CategoryType tipo, String icone) {

        validateNome(nome);
        validateCor(cor);
        validateTipo(tipo);
        validateIcone(icone);
    }

    /**
     * Valida dados de atualização.
     */
    public void validateUpdate(String nome, String cor, String icone) {

        if (nome != null)
            validateNome(nome);
        if (cor != null)
            validateCor(cor);
        if (icone != null)
            validateIcone(icone);
    }

    private void validateNome(String nome) {
        ValidationResult result = nomeValidator.validate(nome);
        if (!result.isValid())
            throw new InvalidCategoryDataException(result.errorMessage());
    }

    private void validateCor(String cor) {
        ValidationResult result = corValidator.validate(cor);
        if (!result.isValid())
            throw new InvalidCategoryDataException(result.errorMessage());
    }

    private void validateTipo(CategoryType tipo) {
        ValidationResult result = tipoValidator.validate(tipo);
        if (!result.isValid())
            throw new InvalidCategoryDataException(result.errorMessage());
    }

    private void validateIcone(String icone) {
        ValidationResult result = iconeValidator.validate(icone);
        if (!result.isValid())
            throw new InvalidCategoryDataException(result.errorMessage());
    }
}