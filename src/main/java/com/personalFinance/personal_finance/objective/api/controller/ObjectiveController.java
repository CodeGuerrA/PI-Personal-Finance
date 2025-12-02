package com.personalFinance.personal_finance.objective.api.controller;

import com.personalFinance.personal_finance.objective.api.dto.request.ObjectiveCreateRequestDTO;
import com.personalFinance.personal_finance.objective.api.dto.response.ObjectiveResponseDTO;
import com.personalFinance.personal_finance.objective.application.facade.ObjectiveFacade;
import com.personalFinance.personal_finance.shared.security.AuthenticatedUserProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
}
