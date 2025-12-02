package com.personalFinance.personal_finance.objective.application.facade;

import com.personalFinance.personal_finance.objective.api.dto.request.ObjectiveCreateRequestDTO;
import com.personalFinance.personal_finance.objective.api.dto.response.ObjectiveResponseDTO;
import com.personalFinance.personal_finance.objective.application.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementação do Facade para Objective.
 * Orquestra os services de aplicação.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ObjectiveFacadeImpl implements ObjectiveFacade {

    private final ObjectiveCreator objectiveCreator;
    private final ObjectiveFinder objectiveFinder;
    private final ObjectiveUpdater objectiveUpdater;
    private final ObjectiveDeleter objectiveDeleter;

    @Override
    public ObjectiveResponseDTO createObjective(String keycloakId, ObjectiveCreateRequestDTO dto) {
        log.info("Facade: Criando objetivo para usuário: {}", keycloakId);
        return objectiveCreator.create(keycloakId, dto);
    }

    @Override
    public List<ObjectiveResponseDTO> findAllByUser(String keycloakId) {
        log.info("Facade: Buscando todos objetivos do usuário: {}", keycloakId);
        return objectiveFinder.findAllByUser(keycloakId);
    }

    @Override
    public List<ObjectiveResponseDTO> findByUserAndMonth(String keycloakId, String mesAno) {
        log.info("Facade: Buscando objetivos do mês {} para usuário: {}", mesAno, keycloakId);
        return objectiveFinder.findByUserAndMonth(keycloakId, mesAno);
    }

    @Override
    public ObjectiveResponseDTO findById(String keycloakId, Long objectiveId) {
        log.info("Facade: Buscando objetivo ID: {} do usuário: {}", objectiveId, keycloakId);
        return objectiveFinder.findById(keycloakId, objectiveId);
    }

    @Override
    public void updateObjectiveValue(String keycloakId, Long objectiveId, BigDecimal novoValor) {
        log.info("Facade: Atualizando valor do objetivo ID: {}", objectiveId);
        objectiveUpdater.updateValue(keycloakId, objectiveId, novoValor);
    }

    @Override
    public void deactivateObjective(String keycloakId, Long objectiveId) {
        log.info("Facade: Desativando objetivo ID: {}", objectiveId);
        objectiveDeleter.deactivate(keycloakId, objectiveId);
    }
}
