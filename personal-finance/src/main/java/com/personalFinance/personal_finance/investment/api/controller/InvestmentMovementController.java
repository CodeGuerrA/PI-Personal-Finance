package com.personalFinance.personal_finance.investment.api.controller;

import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentMovementCreateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentMovementUpdateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.response.InvestmentMovementResponseDTO;
import com.personalFinance.personal_finance.investment.application.facade.InvestmentMovementFacade;
import com.personalFinance.personal_finance.shared.security.AuthenticatedUserProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações de Movimentações de Investimento.
 * Camada de API - responsável apenas por receber requisições HTTP.
 * Não contém lógica de negócio (delegada para Facade).
 * Garante que usuário só acessa suas próprias movimentações.
 */
@Slf4j
@RestController
@RequestMapping("/investment-movements")
@RequiredArgsConstructor
public class InvestmentMovementController {

    private final InvestmentMovementFacade movementFacade;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    /**
     * Cria uma nova movimentação de investimento.
     * POST /investment-movements
     */
    @PostMapping
    public ResponseEntity<InvestmentMovementResponseDTO> createMovement(
            @Valid @RequestBody InvestmentMovementCreateRequestDTO dto) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para criar movimentação de investimento. Usuário: {}, Tipo: {}",
                keycloakId, dto.getTipoMovimentacao());

        InvestmentMovementResponseDTO response = movementFacade.create(dto, keycloakId);
        log.info("Movimentação criada com sucesso. ID: {}", response.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca movimentação por ID (somente do usuário logado).
     * GET /investment-movements/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvestmentMovementResponseDTO> getMovementById(@PathVariable Long id) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar movimentação ID: {}. Usuário: {}", id, keycloakId);

        InvestmentMovementResponseDTO movement = movementFacade.findById(id, keycloakId);
        log.info("Movimentação encontrada: {}", movement.getTipoMovimentacao());

        return ResponseEntity.ok(movement);
    }

    /**
     * Busca todas as movimentações do usuário logado.
     * GET /investment-movements
     */
    @GetMapping
    public ResponseEntity<List<InvestmentMovementResponseDTO>> getAllMovements(
            @RequestParam(required = false) Long investmentId) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();

        List<InvestmentMovementResponseDTO> movements;

        if (investmentId != null) {
            log.info("Requisição para buscar movimentações do investimento ID: {}. Usuário: {}",
                    investmentId, keycloakId);
            movements = movementFacade.findAllByUserAndInvestment(keycloakId, investmentId);
        } else {
            log.info("Requisição para buscar todas as movimentações. Usuário: {}", keycloakId);
            movements = movementFacade.findAllByUser(keycloakId);
        }

        log.info("Retornando {} movimentações", movements.size());
        return ResponseEntity.ok(movements);
    }

    /**
     * Atualiza uma movimentação (somente do usuário logado).
     * PUT /investment-movements/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvestmentMovementResponseDTO> updateMovement(
            @PathVariable Long id,
            @Valid @RequestBody InvestmentMovementUpdateRequestDTO dto) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para atualizar movimentação ID: {}. Usuário: {}", id, keycloakId);

        InvestmentMovementResponseDTO response = movementFacade.update(id, dto, keycloakId);
        log.info("Movimentação atualizada com sucesso. ID: {}", response.getId());

        return ResponseEntity.ok(response);
    }

    /**
     * Deleta uma movimentação (somente do usuário logado).
     * DELETE /investment-movements/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovement(@PathVariable Long id) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para deletar movimentação ID: {}. Usuário: {}", id, keycloakId);

        movementFacade.delete(id, keycloakId);
        log.info("Movimentação deletada com sucesso. ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}
