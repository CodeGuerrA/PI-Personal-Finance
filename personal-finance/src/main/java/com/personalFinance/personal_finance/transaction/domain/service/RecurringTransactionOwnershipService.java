package com.personalFinance.personal_finance.transaction.domain.service;

import com.personalFinance.personal_finance.transaction.domain.entity.RecurringTransaction;
import com.personalFinance.personal_finance.user.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public class RecurringTransactionOwnershipService {

    public boolean belongsToUser(RecurringTransaction rt, User user) {
        if (rt == null || user == null) return false;
        return rt.getUsuario().getId().equals(user.getId());
    }

    public void validateOwnership(RecurringTransaction rt, User user) {
        if (!belongsToUser(rt, user)) {
            throw new IllegalArgumentException("Você não tem permissão para acessar esta transação recorrente");
        }
    }
}