package com.personalFinance.personal_finance.objective.domain.service;

import com.personalFinance.personal_finance.objective.domain.entity.Objective;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Domain Service para cálculos relacionados a objetivos.
 * Segue SRP - responsável apenas por cálculos.
 */
@Service
public class ObjectiveCalculationService {

    /**
     * Calcula o percentual atingido da meta.
     * @return Percentual de 0 a 100+
     */
    public BigDecimal calculatePercentualAtingido(BigDecimal valorAtual, BigDecimal valorObjetivo) {
        if (valorObjetivo.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal valorAtualCalc = valorAtual != null ? valorAtual : BigDecimal.ZERO;

        return valorAtualCalc
                .divide(valorObjetivo, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula o saldo restante para atingir a meta.
     * @return Valor restante (negativo se ultrapassou)
     */
    public BigDecimal calculateSaldoRestante(BigDecimal valorAtual, BigDecimal valorObjetivo) {
        BigDecimal valorAtualCalc = valorAtual != null ? valorAtual : BigDecimal.ZERO;
        return valorObjetivo.subtract(valorAtualCalc);
    }

    /**
     * Calcula o percentual atingido de um objetivo.
     */
    public BigDecimal calculatePercentualAtingido(Objective objective) {
        return calculatePercentualAtingido(objective.getValorAtual(), objective.getValorObjetivo());
    }

    /**
     * Calcula o saldo restante de um objetivo.
     */
    public BigDecimal calculateSaldoRestante(Objective objective) {
        return calculateSaldoRestante(objective.getValorAtual(), objective.getValorObjetivo());
    }
}
