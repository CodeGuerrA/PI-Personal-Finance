package com.personalFinance.personal_finance.objective.application.facade;

import com.personalFinance.personal_finance.objective.api.dto.request.ObjectiveCreateRequestDTO;
import com.personalFinance.personal_finance.objective.api.dto.response.ObjectiveResponseDTO;

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

    void deactivateObjective(String keycloakId, Long objectiveId);
}
