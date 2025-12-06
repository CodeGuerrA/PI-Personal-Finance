package com.personalFinance.personal_finance.objective.api.controller;

import com.personalFinance.personal_finance.objective.api.dto.request.ObjectiveCreateRequestDTO;
import com.personalFinance.personal_finance.objective.api.dto.request.ObjectiveUpdateRequestDTO;
import com.personalFinance.personal_finance.objective.api.dto.response.ObjectiveResponseDTO;
import com.personalFinance.personal_finance.objective.application.facade.ObjectiveFacade;
import com.personalFinance.personal_finance.objective.domain.entity.ObjectiveType;
import com.personalFinance.personal_finance.shared.security.AuthenticatedUserProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST para operações de Objetivos Financeiros.
 * Camada de API - responsável apenas por receber requisições HTTP.
 * Não contém lógica de negócio (delegada para Facade).
 */
@Slf4j
@RestController
@RequestMapping("/objectives")
@RequiredArgsConstructor
public class ObjectiveController {

    private final ObjectiveFacade objectiveFacade;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @PostMapping
    public ResponseEntity<ObjectiveResponseDTO> createObjective(
            @Valid @RequestBody ObjectiveCreateRequestDTO dto) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para criar objetivo. Usuário: {}", keycloakId);

        ObjectiveResponseDTO response = objectiveFacade.createObjective(keycloakId, dto);
        log.info("Objetivo criado com sucesso. ID: {}", response.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ObjectiveResponseDTO>> getAllObjectives() {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar todos os objetivos. Usuário: {}", keycloakId);

        List<ObjectiveResponseDTO> objectives = objectiveFacade.findAllByUser(keycloakId);
        log.info("Retornando {} objetivos", objectives.size());

        return ResponseEntity.ok(objectives);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjectiveResponseDTO> getObjectiveById(@PathVariable Long id) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar objetivo ID: {}. Usuário: {}", id, keycloakId);

        ObjectiveResponseDTO objective = objectiveFacade.findById(keycloakId, id);
        log.info("Objetivo encontrado: {}", objective.getDescricao());

        return ResponseEntity.ok(objective);
    }

    @GetMapping("/month/{mesAno}")
    public ResponseEntity<List<ObjectiveResponseDTO>> getObjectivesByMonth(@PathVariable String mesAno) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar objetivos do mês: {}. Usuário: {}", mesAno, keycloakId);

        List<ObjectiveResponseDTO> objectives = objectiveFacade.findByUserAndMonth(keycloakId, mesAno);
        log.info("Retornando {} objetivos para o mês {}", objectives.size(), mesAno);

        return ResponseEntity.ok(objectives);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObjectiveResponseDTO> updateObjective(
            @PathVariable Long id,
            @Valid @RequestBody ObjectiveUpdateRequestDTO dto) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para atualizar objetivo ID: {}", id);

        ObjectiveResponseDTO response = objectiveFacade.updateObjective(keycloakId, id, dto);
        log.info("Objetivo atualizado com sucesso");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/value")
    public ResponseEntity<Void> updateObjectiveValue(
            @PathVariable Long id,
            @RequestParam BigDecimal novoValor) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para atualizar valor do objetivo ID: {}. Novo valor: {}", id, novoValor);

        objectiveFacade.updateObjectiveValue(keycloakId, id, novoValor);
        log.info("Valor do objetivo atualizado com sucesso");

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateObjective(@PathVariable Long id) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para desativar objetivo ID: {}. Usuário: {}", id, keycloakId);

        objectiveFacade.deactivateObjective(keycloakId, id);
        log.info("Objetivo desativado com sucesso");

        return ResponseEntity.noContent().build();
    }

    // ============ NOVOS ENDPOINTS DE FILTRO E ESTATÍSTICAS ============

    /**
     * FILTRO: Busca objectives próximos de serem cumpridos (>= 80%).
     * GET /objectives/proximos-de-cumprir
     */
    @GetMapping("/proximos-de-cumprir")
    public ResponseEntity<List<ObjectiveResponseDTO>> getObjectivesCloseToCompletion() {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar objectives próximos de cumprir. Usuário: {}", keycloakId);

        List<ObjectiveResponseDTO> objectives = objectiveFacade.findObjectivesCloseToCompletion(keycloakId);
        log.info("Retornando {} objectives", objectives.size());

        return ResponseEntity.ok(objectives);
    }

    /**
     * FILTRO: Busca objectives que ultrapassaram o limite (tipo LIMITE_CATEGORIA).
     * GET /objectives/limites-ultrapassados
     */
    @GetMapping("/limites-ultrapassados")
    public ResponseEntity<List<ObjectiveResponseDTO>> getOverLimitObjectives() {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar objectives com limite ultrapassado. Usuário: {}", keycloakId);

        List<ObjectiveResponseDTO> objectives = objectiveFacade.findOverLimitObjectives(keycloakId);
        log.info("Retornando {} objectives", objectives.size());

        return ResponseEntity.ok(objectives);
    }

    /**
     * FILTRO: Busca objectives por tipo.
     * GET /objectives/tipo/{tipo}
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ObjectiveResponseDTO>> getObjectivesByTipo(@PathVariable ObjectiveType tipo) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar objectives tipo={}. Usuário: {}", tipo, keycloakId);

        List<ObjectiveResponseDTO> objectives = objectiveFacade.findByUserAndTipo(keycloakId, tipo);
        log.info("Retornando {} objectives", objectives.size());

        return ResponseEntity.ok(objectives);
    }

    /**
     * ESTATÍSTICA: Retorna estatísticas completas dos objectives do usuário.
     * GET /objectives/estatisticas
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> getObjectiveStatistics() {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar estatísticas de objectives. Usuário: {}", keycloakId);

        Long countAtivos = objectiveFacade.countActiveObjectives(keycloakId);
        Long countCumpridos = objectiveFacade.countCompletedGoals(keycloakId);
        List<Object[]> resumoPorTipo = objectiveFacade.findObjectiveSummaryByTipo(keycloakId);

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalAtivos", countAtivos);
        statistics.put("totalCumpridos", countCumpridos);
        statistics.put("resumoPorTipo", resumoPorTipo);

        return ResponseEntity.ok(statistics);
    }
}
