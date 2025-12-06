package com.personalFinance.personal_finance.transaction.domain.service;

import com.personalFinance.personal_finance.transaction.domain.entity.Transaction;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.exception.UnauthorizedAccessException;

import org.springframework.stereotype.Service;

/**
 * Domain Service para validar propriedade das transações.
 */
@Service
public class TransactionOwnershipService {

    public boolean belongsToUser(Transaction transaction, User user) {
        if (transaction == null || user == null) return false;

        return transaction.getUsuario() != null &&
                transaction.getUsuario().getId().equals(user.getId());
    }

    public void validateOwnership(Transaction transaction, User user) {
        if (!belongsToUser(transaction, user)) {
            throw new UnauthorizedAccessException("Você não tem permissão para acessar esta transação.");
        }
    }
}