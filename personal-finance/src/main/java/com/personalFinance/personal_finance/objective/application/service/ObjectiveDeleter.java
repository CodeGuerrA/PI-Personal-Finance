package com.personalFinance.personal_finance.objective.application.service;

import com.personalFinance.personal_finance.objective.domain.entity.Objective;
import com.personalFinance.personal_finance.objective.domain.service.ObjectiveOwnershipService;
import com.personalFinance.personal_finance.objective.infrastructure.persistence.ObjectiveRepository;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service seguindo SRP: Responsável apenas por DESATIVAR objetivos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ObjectiveDeleter {

    private final ObjectiveRepository objectiveRepository;
    private final UserRepository userRepository;
    private final ObjectiveOwnershipService ownershipService;

    @Transactional
    public void deactivate(String keycloakId, Long objectiveId) {
        log.info("Desativando objetivo ID: {}", objectiveId);

        // 1. Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar objetivo
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new IllegalArgumentException("Objetivo não encontrado"));

        // 3. Validar propriedade usando Domain Service
        ownershipService.validateOwnership(objective, user);

        // 4. Desativar
        objective.desativar();
        objectiveRepository.save(objective);
        log.info("Objetivo desativado com sucesso");
    }
}
