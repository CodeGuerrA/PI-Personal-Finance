package com.personalFinance.personal_finance.objective.domain.service;

import com.personalFinance.personal_finance.objective.domain.entity.Objective;
import com.personalFinance.personal_finance.user.domain.entity.User;
import org.springframework.stereotype.Service;

/**
 * Domain Service para validar propriedade de objetivos.
 * Segue SRP - garante que usuário só acessa seus próprios objetivos.
 */
@Service
public class ObjectiveOwnershipService {

    /**
     * Verifica se o objetivo pertence ao usuário.
     * @param objective Objetivo a verificar
     * @param user Usuário a validar
     * @return true se o objetivo pertence ao usuário
     */
    public boolean belongsToUser(Objective objective, User user) {
        if (objective == null || user == null) {
            return false;
        }

        return objective.getUsuario() != null
                && objective.getUsuario().getId().equals(user.getId());
    }

    /**
     * Valida que o objetivo pertence ao usuário, lançando exceção caso contrário.
     * @param objective Objetivo a verificar
     * @param user Usuário a validar
     * @throws IllegalArgumentException se o objetivo não pertencer ao usuário
     */
    public void validateOwnership(Objective objective, User user) {
        if (!belongsToUser(objective, user)) {
            throw new IllegalArgumentException("Objetivo não pertence ao usuário");
        }
    }
}
