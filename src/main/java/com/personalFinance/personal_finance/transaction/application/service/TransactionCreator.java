package com.personalFinance.personal_finance.transaction.application.service;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.infrastructure.persistence.CategoryRepository;
import com.personalFinance.personal_finance.transaction.api.dto.request.TransactionCreateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.response.TransactionResponseDTO;
import com.personalFinance.personal_finance.transaction.api.mapper.TransactionMapper;
import com.personalFinance.personal_finance.transaction.domain.entity.Transaction;
import com.personalFinance.personal_finance.transaction.domain.port.TransactionRepositoryPort;
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
public class TransactionCreator {

    private final TransactionRepositoryPort transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionValidationService validationService;
    private final TransactionMapper mapper;

    @Transactional
    public TransactionResponseDTO create(String keycloakId, TransactionCreateRequestDTO dto) {

        log.info("Criando transação para usuário {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Category category = categoryRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

        validationService.validateCreation(
                dto.getDescricao(),
                dto.getValor(),
                dto.getData(),
                dto.getTipo() != null ? dto.getTipo().name() : null,
                dto.getMetodoPagamento() != null ? dto.getMetodoPagamento().name() : null
        );

        Transaction tx = mapper.toEntity(dto, user, category);
        Transaction saved = transactionRepository.save(tx);

        return mapper.toResponseDTO(saved);
    }
}