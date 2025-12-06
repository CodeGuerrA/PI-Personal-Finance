package com.personalFinance.personal_finance.objective.application.facade;

import com.personalFinance.personal_finance.objective.api.dto.request.ObjectiveCreateRequestDTO;
import com.personalFinance.personal_finance.objective.api.dto.request.ObjectiveUpdateRequestDTO;
import com.personalFinance.personal_finance.objective.api.dto.response.ObjectiveResponseDTO;
import com.personalFinance.personal_finance.objective.domain.entity.ObjectiveType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Facade (Facade Pattern) para operações de Objective.
 * Abstrai a complexidade dos services internos.
 */
public interface ObjectiveFacade {

    ObjectiveResponseDTO createObjective(String keycloakId, ObjectiveCreateRequestDTO dto);

    List<ObjectiveResponseDTO> findAllByUser(String keycloakId);

    List<ObjectiveResponseDTO> findByUserAndMonth(String keycloakId, String mesAno);

    ObjectiveResponseDTO findById(String keycloakId, Long objectiveId);

    void updateObjectiveValue(String keycloakId, Long objectiveId, BigDecimal novoValor);

    ObjectiveResponseDTO updateObjective(String keycloakId, Long objectiveId, ObjectiveUpdateRequestDTO dto);

    void deactivateObjective(String keycloakId, Long objectiveId);

    // ============ NOVOS MÉTODOS DE FILTRO E ESTATÍSTICAS ============

    /**
     * FILTRO: Busca objectives próximos de serem cumpridos (>= 80%).
     */
    List<ObjectiveResponseDTO> findObjectivesCloseToCompletion(String keycloakId);

    /**
     * FILTRO: Busca objectives que ultrapassaram o limite.
     */
    List<ObjectiveResponseDTO> findOverLimitObjectives(String keycloakId);

    /**
     * FILTRO: Busca objectives por tipo.
     */
    List<ObjectiveResponseDTO> findByUserAndTipo(String keycloakId, ObjectiveType tipo);

    /**
     * ESTATÍSTICA: Conta total de objectives ativos.
     */
    Long countActiveObjectives(String keycloakId);

    /**
     * ESTATÍSTICA: Conta objectives cumpridos.
     */
    Long countCompletedGoals(String keycloakId);

    /**
     * ESTATÍSTICA: Retorna resumo de objectives por tipo.
     */
    List<Object[]> findObjectiveSummaryByTipo(String keycloakId);
}
