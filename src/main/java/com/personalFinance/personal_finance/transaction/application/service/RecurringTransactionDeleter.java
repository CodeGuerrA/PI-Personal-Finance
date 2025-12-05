package com.personalFinance.personal_finance.transaction.application.service;

import com.personalFinance.personal_finance.transaction.domain.entity.RecurringTransaction;
import com.personalFinance.personal_finance.transaction.domain.port.RecurringTransactionRepositoryPort;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecurringTransactionDeleter {

    private final RecurringTransactionRepositoryPort recurringTransactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void delete(String keycloakId, Long id) {

        log.info("Deletando transação recorrente {} para usuário {}", id, keycloakId);

        // Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Buscar transação recorrente
        RecurringTransaction recurringTransaction = recurringTransactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação recorrente não encontrada"));

        // Validar que pertence ao usuário
        if (!recurringTransaction.belongsToUser(user.getId())) {
            throw new IllegalArgumentException("Transação recorrente não pertence ao usuário");
        }

        // Deletar
        recurringTransactionRepository.delete(recurringTransaction);

        log.info("Transação recorrente {} deletada com sucesso", id);
    }
}
