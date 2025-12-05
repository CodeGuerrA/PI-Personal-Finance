package com.personalFinance.personal_finance.investment.domain.port;

import com.personalFinance.personal_finance.investment.domain.entity.InvestmentMovement;

import java.util.List;
import java.util.Optional;

/**
 * Porta para repositório de movimentações de investimento.
 * Define o contrato de acesso a dados (Dependency Inversion Principle).
 */
public interface InvestmentMovementRepositoryPort {

    /**
     * Salva uma movimentação de investimento.
     */
    InvestmentMovement save(InvestmentMovement movement);

    /**
     * Busca movimentação por ID.
     */
    Optional<InvestmentMovement> findById(Long id);

    /**
     * Busca todas as movimentações de um usuário.
     */
    List<InvestmentMovement> findAllByUsuarioId(Long usuarioId);

    /**
     * Busca todas as movimentações de um investimento específico de um usuário.
     */
    List<InvestmentMovement> findAllByUsuarioIdAndInvestmentId(Long usuarioId, Long investmentId);

    /**
     * Deleta uma movimentação.
     */
    void delete(InvestmentMovement movement);

    /**
     * Verifica se uma movimentação existe.
     */
    boolean existsById(Long id);
}
