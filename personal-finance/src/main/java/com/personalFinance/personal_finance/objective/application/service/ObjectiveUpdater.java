package com.personalFinance.personal_finance.objective.application.service;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.infrastructure.persistence.CategoryRepository;
import com.personalFinance.personal_finance.objective.api.dto.request.ObjectiveUpdateRequestDTO;
import com.personalFinance.personal_finance.objective.api.dto.response.ObjectiveResponseDTO;
import com.personalFinance.personal_finance.objective.api.mapper.ObjectiveMapper;
import com.personalFinance.personal_finance.objective.domain.entity.Objective;
import com.personalFinance.personal_finance.objective.domain.service.ObjectiveOwnershipService;
import com.personalFinance.personal_finance.objective.domain.service.validation.ObjectiveValueValidator;
import com.personalFinance.personal_finance.objective.infrastructure.persistence.ObjectiveRepository;
import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service seguindo SRP: Responsável apenas por ATUALIZAR objetivos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ObjectiveUpdater {

    private final ObjectiveRepository objectiveRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectiveMapper objectiveMapper;
    private final ObjectiveOwnershipService ownershipService;
    private final ObjectiveValueValidator valueValidator;

    @Transactional
    public void updateValue(String keycloakId, Long objectiveId, BigDecimal novoValor) {
        log.info("Atualizando valor do objetivo ID: {} para: {}", objectiveId, novoValor);

        // 1. Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar objetivo
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new IllegalArgumentException("Objetivo não encontrado"));

        // 3. Validar propriedade usando Domain Service
        ownershipService.validateOwnership(objective, user);

        // 4. Validar novo valor usando Domain Service
        ValidationResult validation = valueValidator.validate(novoValor);
        if (!validation.isValid()) {
            throw new IllegalArgumentException(validation.errorMessage());
        }

        // 5. Atualizar VALOR ATUAL (progresso), não o objetivo
        objective.setValorAtual(novoValor);
        objectiveRepository.save(objective);
        log.info("Valor atual do objetivo atualizado com sucesso (progresso)");
    }

    @Transactional
    public ObjectiveResponseDTO update(String keycloakId, Long objectiveId, ObjectiveUpdateRequestDTO dto) {
        log.info("Atualizando objetivo ID: {}", objectiveId);

        // 1. Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar objetivo
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new IllegalArgumentException("Objetivo não encontrado"));

        // 3. Validar propriedade
        ownershipService.validateOwnership(objective, user);

        // 4. Validar novo valor objetivo
        ValidationResult validationValorObjetivo = valueValidator.validate(dto.getValorObjetivo());
        if (!validationValorObjetivo.isValid()) {
            throw new IllegalArgumentException(validationValorObjetivo.errorMessage());
        }

        // 5. Validar novo valor atual
        ValidationResult validationValorAtual = valueValidator.validate(dto.getValorAtual());
        if (!validationValorAtual.isValid()) {
            throw new IllegalArgumentException(validationValorAtual.errorMessage());
        }

        // 6. Atualizar campos
        objective.atualizarDescricao(dto.getDescricao());
        objective.atualizarValorObjetivo(dto.getValorObjetivo());
        objective.setValorAtual(dto.getValorAtual());

        // 7. Salvar
        Objective saved = objectiveRepository.save(objective);
        log.info("Objetivo atualizado com sucesso");

        // 8. Buscar nome da categoria
        String categoriaNome = saved.getCategoriaId() != null
                ? categoryRepository.findById(saved.getCategoriaId())
                .map(Category::getNome)
                .orElse(null)
                : null;

        // 9. Converter para DTO e retornar
        return objectiveMapper.toObjectiveResponseDTO(saved, categoriaNome);
    }
}
