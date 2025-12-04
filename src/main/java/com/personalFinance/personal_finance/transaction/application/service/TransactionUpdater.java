package com.personalFinance.personal_finance.transaction.application.service;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.infrastructure.persistence.CategoryRepository;
import com.personalFinance.personal_finance.transaction.api.dto.request.TransactionUpdateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.response.TransactionResponseDTO;
import com.personalFinance.personal_finance.transaction.api.mapper.TransactionMapper;

import com.personalFinance.personal_finance.transaction.domain.entity.Transaction;
import com.personalFinance.personal_finance.transaction.domain.port.TransactionRepositoryPort;
import com.personalFinance.personal_finance.transaction.domain.service.TransactionOwnershipService;
import com.personalFinance.personal_finance.transaction.domain.service.TransactionValidationService;

import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionUpdater {

    private final TransactionRepositoryPort transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionOwnershipService ownershipService;
    private final TransactionValidationService validationService;
    private final TransactionMapper mapper;

    @Transactional
    public TransactionResponseDTO update(String keycloakId, Long id, TransactionUpdateRequestDTO dto) {

        log.info("Atualizando transação {} do usuário {}", id, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));

        ownershipService.validateOwnership(tx, user);

        validationService.validateUpdate(
                dto.getDescricao(),
                dto.getValor(),
                dto.getData(),
                dto.getTipo() != null ? dto.getTipo().name() : null,
                dto.getMetodoPagamento() != null ? dto.getMetodoPagamento().name() : null
        );

        if (dto.getValor() != null) tx.updateValor(dto.getValor());
        if (dto.getDescricao() != null) tx.updateDescricao(dto.getDescricao());
        if (dto.getTipo() != null) tx.updateTipo(dto.getTipo());
        if (dto.getMetodoPagamento() != null) tx.updateMetodoPagamento(dto.getMetodoPagamento());
        if (dto.getData() != null) tx.updateData(dto.getData());

        if (dto.getCategoriaId() != null) {
            Category category = categoryRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
            tx.updateCategoria(category);
        }

        Transaction updated = transactionRepository.save(tx);
        return mapper.toResponseDTO(updated);
    }
}