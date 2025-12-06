package com.personalFinance.personal_finance.transaction.application.service;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.port.CategoryRepositoryPort;
import com.personalFinance.personal_finance.transaction.api.dto.request.RecurringTransactionCreateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.response.RecurringTransactionResponseDTO;
import com.personalFinance.personal_finance.transaction.api.mapper.RecurringTransactionMapper;
import com.personalFinance.personal_finance.transaction.domain.entity.RecurringTransaction;
import com.personalFinance.personal_finance.transaction.domain.port.RecurringTransactionRepositoryPort;
import com.personalFinance.personal_finance.transaction.domain.service.RecurringTransactionValidationService;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecurringTransactionCreator {

    private final RecurringTransactionRepositoryPort recurringTransactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepositoryPort categoryRepository;
    private final RecurringTransactionValidationService validationService;
    private final RecurringTransactionMapper mapper;

    @Transactional
    public RecurringTransactionResponseDTO create(String keycloakId, RecurringTransactionCreateRequestDTO dto) {

        log.info("Criando transação recorrente para usuário {}", keycloakId);

        // Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Validar categoria
        Category category = categoryRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

        if (!category.pertenceAoUsuario(user)) {
            throw new IllegalArgumentException("Categoria não pertence ao usuário");
        }

        // Validar dados
        validationService.validateCreation(
                dto.getValor(),
                dto.getDescricao(),
                dto.getDataInicio(),
                dto.getFrequencia(),
                dto.getTipo()
        );

        // Criar entidade
        RecurringTransaction recurringTransaction = mapper.toEntity(dto, user, category);

        // Se tem dataFim no DTO, atualizar
        if (dto.getDataFim() != null) {
            recurringTransaction.updateDataFim(dto.getDataFim());
        }

        // Salvar
        RecurringTransaction saved = recurringTransactionRepository.save(recurringTransaction);

        log.info("Transação recorrente criada com sucesso. ID: {}", saved.getId());

        return mapper.toResponseDTO(saved);
    }
}
