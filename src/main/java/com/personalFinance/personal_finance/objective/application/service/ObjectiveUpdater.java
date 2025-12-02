package com.personalFinance.personal_finance.objective.application.service;

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

        // 5. Atualizar
        objective.atualizarValorObjetivo(novoValor);
        objectiveRepository.save(objective);
        log.info("Valor do objetivo atualizado com sucesso");
    }
}
