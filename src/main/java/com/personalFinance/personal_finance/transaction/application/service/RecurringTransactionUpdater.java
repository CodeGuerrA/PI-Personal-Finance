package com.personalFinance.personal_finance.transaction.application.service;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.port.CategoryRepositoryPort;
import com.personalFinance.personal_finance.transaction.api.dto.request.RecurringTransactionUpdateRequestDTO;
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
public class RecurringTransactionUpdater {

    private final RecurringTransactionRepositoryPort recurringTransactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepositoryPort categoryRepository;
    private final RecurringTransactionValidationService validationService;
    private final RecurringTransactionMapper mapper;

    @Transactional
    public RecurringTransactionResponseDTO update(
            String keycloakId,
            Long id,
            RecurringTransactionUpdateRequestDTO dto) {

        log.info("Atualizando transação recorrente {} para usuário {}", id, keycloakId);

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

        // Validar categoria se foi fornecida
        if (dto.getCategoriaId() != null) {
            Category category = categoryRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

            if (!category.pertenceAoUsuario(user)) {
                throw new IllegalArgumentException("Categoria não pertence ao usuário");
            }

            recurringTransaction.updateCategoria(category);
        }

        // Validar dados
        validationService.validateUpdate(
                dto.getValor(),
                dto.getDescricao(),
                dto.getDataInicio(),
                dto.getFrequencia()
        );

        // Aplicar atualizações
        mapper.applyUpdate(recurringTransaction, dto);

        // Salvar
        RecurringTransaction updated = recurringTransactionRepository.save(recurringTransaction);

        log.info("Transação recorrente {} atualizada com sucesso", id);

        return mapper.toResponseDTO(updated);
    }
}
