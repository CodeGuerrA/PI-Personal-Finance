package com.personalFinance.personal_finance.investment.domain.service;

import com.personalFinance.personal_finance.investment.domain.entity.InvestmentMovement;
import com.personalFinance.personal_finance.user.domain.exception.UnauthorizedAccessException;
import org.springframework.stereotype.Service;

/**
 * Serviço de domínio para validar propriedade de movimentações.
 * Garante que apenas o dono possa acessar suas movimentações (SRP).
 */
@Service
public class InvestmentMovementOwnershipService {

    /**
     * Valida se a movimentação pertence ao usuário.
     * @throws UnauthorizedAccessException se não pertencer.
     */
    public void validateOwnership(InvestmentMovement movement, Long usuarioId) {
        if (!movement.pertenceAoUsuario(usuarioId)) {
            throw new UnauthorizedAccessException(
                "Você não tem permissão para acessar esta movimentação de investimento"
            );
        }
    }
}
