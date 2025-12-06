package com.personalFinance.personal_finance.objective.application.facade;

import com.personalFinance.personal_finance.objective.api.dto.request.ObjectiveCreateRequestDTO;
import com.personalFinance.personal_finance.objective.api.dto.request.ObjectiveUpdateRequestDTO;
import com.personalFinance.personal_finance.objective.api.dto.response.ObjectiveResponseDTO;
import com.personalFinance.personal_finance.objective.application.service.*;
import com.personalFinance.personal_finance.objective.domain.entity.ObjectiveType;
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
    public ObjectiveResponseDTO updateObjective(String keycloakId, Long objectiveId, ObjectiveUpdateRequestDTO dto) {
        log.info("Facade: Atualizando objetivo completo ID: {}", objectiveId);
        return objectiveUpdater.update(keycloakId, objectiveId, dto);
    }

    @Override
    public void deactivateObjective(String keycloakId, Long objectiveId) {
        log.info("Facade: Desativando objetivo ID: {}", objectiveId);
        objectiveDeleter.deactivate(keycloakId, objectiveId);
    }

    // ============ NOVOS MÉTODOS DE FILTRO E ESTATÍSTICAS ============

    @Override
    public List<ObjectiveResponseDTO> findObjectivesCloseToCompletion(String keycloakId) {
        log.info("Facade: Buscando objectives próximos de cumprir do usuário: {}", keycloakId);
        return objectiveFinder.findObjectivesCloseToCompletion(keycloakId);
    }

    @Override
    public List<ObjectiveResponseDTO> findOverLimitObjectives(String keycloakId) {
        log.info("Facade: Buscando objectives com limite ultrapassado do usuário: {}", keycloakId);
        return objectiveFinder.findOverLimitObjectives(keycloakId);
    }

    @Override
    public List<ObjectiveResponseDTO> findByUserAndTipo(String keycloakId, ObjectiveType tipo) {
        log.info("Facade: Buscando objectives tipo={} do usuário: {}", tipo, keycloakId);
        return objectiveFinder.findByUserAndTipo(keycloakId, tipo);
    }

    @Override
    public Long countActiveObjectives(String keycloakId) {
        log.info("Facade: Contando objectives ativos do usuário: {}", keycloakId);
        return objectiveFinder.countActiveObjectives(keycloakId);
    }

    @Override
    public Long countCompletedGoals(String keycloakId) {
        log.info("Facade: Contando objectives cumpridos do usuário: {}", keycloakId);
        return objectiveFinder.countCompletedGoals(keycloakId);
    }

    @Override
    public List<Object[]> findObjectiveSummaryByTipo(String keycloakId) {
        log.info("Facade: Buscando resumo de objectives por tipo do usuário: {}", keycloakId);
        return objectiveFinder.findObjectiveSummaryByTipo(keycloakId);
    }
}
