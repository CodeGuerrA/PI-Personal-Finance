package com.personalFinance.personal_finance.investment.infrastructure.persistence;

import com.personalFinance.personal_finance.investment.domain.entity.InvestmentMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para InvestmentMovement.
 */
@Repository
public interface InvestmentMovementRepository extends JpaRepository<InvestmentMovement, Long> {

    /**
     * Busca movimentação por ID com investment carregado.
     */
    @Query("SELECT m FROM InvestmentMovement m JOIN FETCH m.investment WHERE m.id = :id")
    Optional<InvestmentMovement> findByIdWithInvestment(@Param("id") Long id);

    /**
     * Busca todas as movimentações de um usuário com investment carregado.
     */
    @Query("SELECT m FROM InvestmentMovement m JOIN FETCH m.investment WHERE m.usuario.id = :usuarioId")
    List<InvestmentMovement> findAllByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca todas as movimentações de um investimento específico de um usuário.
     */
    @Query("SELECT m FROM InvestmentMovement m JOIN FETCH m.investment WHERE m.usuario.id = :usuarioId AND m.investment.id = :investmentId")
    List<InvestmentMovement> findAllByUsuarioIdAndInvestmentId(@Param("usuarioId") Long usuarioId, @Param("investmentId") Long investmentId);
}
