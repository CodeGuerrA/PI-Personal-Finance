package com.personalFinance.personal_finance.transaction.application.service;

import com.personalFinance.personal_finance.transaction.domain.entity.Transaction;
import com.personalFinance.personal_finance.transaction.domain.port.TransactionRepositoryPort;
import com.personalFinance.personal_finance.transaction.domain.service.TransactionOwnershipService;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionDeleter {

    private final TransactionRepositoryPort transactionRepository;
    private final UserRepository userRepository;
    private final TransactionOwnershipService ownershipService;

    @Transactional
    public void delete(String keycloakId, Long transactionId) {
        log.info("Deletando transação {} do usuário {}", transactionId, keycloakId);

        // 1. Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar transação
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));

        // 3. Validar dono
        ownershipService.validateOwnership(transaction, user);

        // 4. Deletar
        transactionRepository.delete(transaction);

        log.info("Transação {} deletada com sucesso", transactionId);
    }
}