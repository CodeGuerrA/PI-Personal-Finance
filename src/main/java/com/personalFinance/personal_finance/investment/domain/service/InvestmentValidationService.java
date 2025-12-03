package com.personalFinance.personal_finance.investment.domain.service;

import com.personalFinance.personal_finance.investment.domain.exception.InvalidInvestmentDataException;
import com.personalFinance.personal_finance.investment.domain.service.validation.InvestmentNomeAtivoValidator;
import com.personalFinance.personal_finance.investment.domain.service.validation.InvestmentQuantidadeValidator;
import com.personalFinance.personal_finance.investment.domain.service.validation.InvestmentSimboloValidator;
import com.personalFinance.personal_finance.investment.domain.service.validation.InvestmentValorValidator;
import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Domain Service para validação de investimentos.
 * Coordena múltiplos validators seguindo SRP e OCP.
 */
@Service
@RequiredArgsConstructor
public class InvestmentValidationService {

    private final InvestmentNomeAtivoValidator nomeAtivoValidator;
    private final InvestmentSimboloValidator simboloValidator;
    private final InvestmentQuantidadeValidator quantidadeValidator;
    private final InvestmentValorValidator valorValidator;

    /**
     * Valida os dados de criação de um investimento.
     */
    public void validateCreation(String nomeAtivo, String simbolo, BigDecimal quantidade,
                                BigDecimal valorCompra, BigDecimal valorTotalInvestido,
                                LocalDate dataCompra) {
        validateNomeAtivo(nomeAtivo);
        validateSimbolo(simbolo);
        validateQuantidade(quantidade);
        validateValorCompra(valorCompra);
        validateValorTotalInvestido(valorTotalInvestido);
        validateDataCompra(dataCompra);
    }

    /**
     * Valida os dados de atualização de um investimento.
     */
    public void validateUpdate(BigDecimal quantidade, BigDecimal valorCompra, BigDecimal valorTotalInvestido) {
        if (quantidade != null) {
            validateQuantidade(quantidade);
        }
        if (valorCompra != null) {
            validateValorCompra(valorCompra);
        }
        if (valorTotalInvestido != null) {
            validateValorTotalInvestido(valorTotalInvestido);
        }
    }

    private void validateNomeAtivo(String nomeAtivo) {
        ValidationResult result = nomeAtivoValidator.validate(nomeAtivo);
        if (!result.isValid()) {
            throw new InvalidInvestmentDataException(result.errorMessage());
        }
    }

    private void validateSimbolo(String simbolo) {
        ValidationResult result = simboloValidator.validate(simbolo);
        if (!result.isValid()) {
            throw new InvalidInvestmentDataException(result.errorMessage());
        }
    }

    private void validateQuantidade(BigDecimal quantidade) {
        ValidationResult result = quantidadeValidator.validate(quantidade);
        if (!result.isValid()) {
            throw new InvalidInvestmentDataException(result.errorMessage());
        }
    }

    private void validateValorCompra(BigDecimal valorCompra) {
        ValidationResult result = valorValidator.validate(valorCompra);
        if (!result.isValid()) {
            throw new InvalidInvestmentDataException("Valor de compra inválido: " + result.errorMessage());
        }
    }

    private void validateValorTotalInvestido(BigDecimal valorTotalInvestido) {
        ValidationResult result = valorValidator.validate(valorTotalInvestido);
        if (!result.isValid()) {
            throw new InvalidInvestmentDataException("Valor total investido inválido: " + result.errorMessage());
        }
    }

    private void validateDataCompra(LocalDate dataCompra) {
        if (dataCompra == null) {
            throw new InvalidInvestmentDataException("Data de compra não pode ser nula");
        }
        if (dataCompra.isAfter(LocalDate.now())) {
            throw new InvalidInvestmentDataException("Data de compra não pode ser futura");
        }
    }
}
