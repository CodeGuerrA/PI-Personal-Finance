package com.personalFinance.personal_finance.transaction.api.controller;

import com.personalFinance.personal_finance.shared.security.AuthenticatedUserProvider;
import com.personalFinance.personal_finance.transaction.api.dto.request.RecurringTransactionCreateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.request.RecurringTransactionUpdateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.response.RecurringTransactionResponseDTO;
import com.personalFinance.personal_finance.transaction.application.facade.RecurringTransactionFacade;
import com.personalFinance.personal_finance.transaction.domain.entity.FrequenciaRecorrencia;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/recurring-transactions")
@RequiredArgsConstructor
public class RecurringTransactionController {

    private final RecurringTransactionFacade recurringTransactionFacade;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    // =======================================================
    // CREATE
    // =======================================================

    @PostMapping
    public ResponseEntity<RecurringTransactionResponseDTO> createRecurringTransaction(
            @Valid @RequestBody RecurringTransactionCreateRequestDTO dto) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para criar transação recorrente. Usuário: {}", keycloakId);

        RecurringTransactionResponseDTO created = recurringTransactionFacade.create(keycloakId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =======================================================
    // READ BY ID
    // =======================================================

    @GetMapping("/{id}")
    public ResponseEntity<RecurringTransactionResponseDTO> getRecurringTransactionById(@PathVariable Long id) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para consultar transação recorrente {}. Usuário: {}", id, keycloakId);

        RecurringTransactionResponseDTO rt = recurringTransactionFacade.findById(keycloakId, id);
        return ResponseEntity.ok(rt);
    }

    // =======================================================
    // LIST ALL BY USER
    // =======================================================

    @GetMapping
    public ResponseEntity<List<RecurringTransactionResponseDTO>> getAllRecurringTransactions() {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para listar transações recorrentes. Usuário: {}", keycloakId);

        List<RecurringTransactionResponseDTO> list = recurringTransactionFacade.findAllByUser(keycloakId);
        return ResponseEntity.ok(list);
    }

    // =======================================================
    // FILTER BY FREQUENCIA
    // =======================================================

    @GetMapping("/frequencia/{frequencia}")
    public ResponseEntity<List<RecurringTransactionResponseDTO>> getByFrequencia(
            @PathVariable FrequenciaRecorrencia frequencia) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para listar transações recorrentes por frequência {}. Usuário: {}", frequencia, keycloakId);

        List<RecurringTransactionResponseDTO> list = recurringTransactionFacade.findByFrequencia(keycloakId, frequencia);
        return ResponseEntity.ok(list);
    }

    // =======================================================
    // FILTER BY CATEGORIA
    // =======================================================

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<RecurringTransactionResponseDTO>> getByCategoria(@PathVariable Long categoriaId) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para listar transações recorrentes por categoria {}. Usuário: {}", categoriaId, keycloakId);

        List<RecurringTransactionResponseDTO> list = recurringTransactionFacade.findByCategoria(keycloakId, categoriaId);
        return ResponseEntity.ok(list);
    }

    // =======================================================
    // FILTER BY TIPO
    // =======================================================

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<RecurringTransactionResponseDTO>> getByTipo(@PathVariable TipoTransacao tipo) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para listar transações recorrentes por tipo {}. Usuário: {}", tipo, keycloakId);

        List<RecurringTransactionResponseDTO> list = recurringTransactionFacade.findByTipo(keycloakId, tipo);
        return ResponseEntity.ok(list);
    }

    // =======================================================
    // LIST ATIVAS (active recurring transactions)
    // =======================================================

    @GetMapping("/ativas")
    public ResponseEntity<List<RecurringTransactionResponseDTO>> getAtivas() {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para listar transações recorrentes ativas. Usuário: {}", keycloakId);

        List<RecurringTransactionResponseDTO> list = recurringTransactionFacade.findAtivas(keycloakId);
        return ResponseEntity.ok(list);
    }

    // =======================================================
    // UPDATE
    // =======================================================

    @PutMapping("/{id}")
    public ResponseEntity<RecurringTransactionResponseDTO> updateRecurringTransaction(
            @PathVariable Long id,
            @Valid @RequestBody RecurringTransactionUpdateRequestDTO dto) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para atualizar transação recorrente {}. Usuário: {}", id, keycloakId);

        RecurringTransactionResponseDTO updated = recurringTransactionFacade.update(keycloakId, id, dto);
        return ResponseEntity.ok(updated);
    }

    // =======================================================
    // DELETE
    // =======================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecurringTransaction(@PathVariable Long id) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para deletar transação recorrente {}. Usuário: {}", id, keycloakId);

        recurringTransactionFacade.delete(keycloakId, id);
        return ResponseEntity.noContent().build();
    }
}
