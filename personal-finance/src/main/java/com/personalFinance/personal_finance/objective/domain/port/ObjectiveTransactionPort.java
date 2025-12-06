package com.personalFinance.personal_finance.objective.domain.port;

import java.math.BigDecimal;

/**
 * Port (Hexagonal Architecture) para buscar valor atual de transações.
 * Inverte a dependência - Domain não conhece Infrastructure.
 */
public interface ObjectiveTransactionPort {

    /**
     * Calcula o valor total de transações de uma categoria em um período.
     * @param categoriaId ID da categoria
     * @param mesAno Período no formato yyyy-MM
     * @return Valor total das transações
     */
    BigDecimal calculateCategoryValueForPeriod(Long categoriaId, String mesAno);

    /**
     * Calcula o valor total de economia (receitas - despesas) em um período.
     * @param usuarioId ID do usuário
     * @param mesAno Período no formato yyyy-MM
     * @return Valor economizado
     */
    BigDecimal calculateSavingsForPeriod(Long usuarioId, String mesAno);

    /**
     * Calcula o valor total investido em um período.
     * @param usuarioId ID do usuário
     * @param mesAno Período no formato yyyy-MM
     * @return Valor investido
     */
    BigDecimal calculateInvestmentsForPeriod(Long usuarioId, String mesAno);
}
