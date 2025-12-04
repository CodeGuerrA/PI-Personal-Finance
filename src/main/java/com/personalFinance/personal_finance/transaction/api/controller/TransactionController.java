package com.personalFinance.personal_finance.transaction.api.controller;

import com.personalFinance.personal_finance.shared.security.AuthenticatedUserProvider;
import com.personalFinance.personal_finance.transaction.api.dto.request.TransactionCreateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.request.TransactionUpdateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.response.TransactionResponseDTO;
import com.personalFinance.personal_finance.transaction.application.facade.TransactionFacade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionFacade transactionFacade;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    // =======================================================
    // CREATE
    // =======================================================

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Valid @RequestBody TransactionCreateRequestDTO dto) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para criar transação. Usuário: {}", keycloakId);

        TransactionResponseDTO created = transactionFacade.createTransaction(keycloakId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =======================================================
    // READ BY ID
    // =======================================================

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para consultar transação {}. Usuário: {}", id, keycloakId);

        TransactionResponseDTO tx = transactionFacade.findById(keycloakId, id);
        return ResponseEntity.ok(tx);
    }

    // =======================================================
    // LIST ALL BY USER
    // =======================================================

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para listar transações. Usuário: {}", keycloakId);

        List<TransactionResponseDTO> list = transactionFacade.findAllByUser(keycloakId);
        return ResponseEntity.ok(list);
    }

    // =======================================================
    // UPDATE
    // =======================================================

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionUpdateRequestDTO dto) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para atualizar transação {}. Usuário: {}", id, keycloakId);

        TransactionResponseDTO updated = transactionFacade.updateTransaction(keycloakId, id, dto);
        return ResponseEntity.ok(updated);
    }

    // =======================================================
    // DELETE
    // =======================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {

        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para deletar transação {}. Usuário: {}", id, keycloakId);

        transactionFacade.deleteTransaction(keycloakId, id);
        return ResponseEntity.noContent().build();
    }
}