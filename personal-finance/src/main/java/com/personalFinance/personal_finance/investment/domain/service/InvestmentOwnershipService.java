package com.personalFinance.personal_finance.investment.domain.service;

import com.personalFinance.personal_finance.investment.domain.entity.Investment;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.exception.UnauthorizedAccessException;
import org.springframework.stereotype.Service;

/**
 * Domain Service para validar propriedade de investimentos.
 * Segue SRP - garante que usuário só acessa seus próprios investimentos.
 */
@Service
public class InvestmentOwnershipService {

    /**
     * Verifica se o investimento pertence ao usuário.
     * @param investment Investimento a verificar
     * @param user Usuário a validar
     * @return true se o investimento pertence ao usuário
     */
    public boolean belongsToUser(Investment investment, User user) {
        if (investment == null || user == null) {
            return false;
        }

        return investment.getUsuario() != null
                && investment.getUsuario().getId().equals(user.getId());
    }

    /**
     * Valida que o investimento pertence ao usuário, lançando exceção caso contrário.
     * @param investment Investimento a verificar
     * @param user Usuário a validar
     * @throws UnauthorizedAccessException se o investimento não pertencer ao usuário
     */
    public void validateOwnership(Investment investment, User user) {
        if (!belongsToUser(investment, user)) {
            throw new UnauthorizedAccessException("Você não tem permissão para acessar este investimento");
        }
    }
}
