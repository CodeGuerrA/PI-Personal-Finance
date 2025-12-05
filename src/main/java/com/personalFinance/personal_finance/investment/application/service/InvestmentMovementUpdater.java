package com.personalFinance.personal_finance.investment.application.service;

import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentMovementUpdateRequestDTO;
import com.personalFinance.personal_finance.investment.domain.entity.InvestmentMovement;
import com.personalFinance.personal_finance.investment.domain.exception.InvestmentMovementNotFoundException;
import com.personalFinance.personal_finance.investment.domain.port.InvestmentMovementRepositoryPort;
import com.personalFinance.personal_finance.investment.domain.service.InvestmentMovementOwnershipService;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço de aplicação para atualizar movimentações de investimento.
 * Responsabilidade: orquestrar atualização de movimentação (SRP).
 */
@Service
@RequiredArgsConstructor
public class InvestmentMovementUpdater {

    private final InvestmentMovementRepositoryPort movementRepository;
    private final UserRepository userRepository;
    private final InvestmentMovementOwnershipService ownershipService;

    /**
     * Atualiza uma movimentação de investimento.
     */
    @Transactional
    public InvestmentMovement update(Long id, InvestmentMovementUpdateRequestDTO dto, String keycloakId) {
        // 1. Buscar usuário
        User usuario = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar movimentação
        InvestmentMovement movement = movementRepository.findById(id)
                .orElseThrow(() -> InvestmentMovementNotFoundException.withId(id));

        // 3. Validar propriedade
        ownershipService.validateOwnership(movement, usuario.getId());

        // 4. Atualizar campos (apenas os fornecidos)
        if (dto.getQuantidade() != null) {
            movement.atualizarQuantidade(dto.getQuantidade());
        }
        if (dto.getValorUnitario() != null) {
            movement.atualizarValorUnitario(dto.getValorUnitario());
        }
        if (dto.getValorTotal() != null) {
            movement.atualizarValorTotal(dto.getValorTotal());
        }
        if (dto.getTaxas() != null) {
            movement.atualizarTaxas(dto.getTaxas());
        }
        if (dto.getObservacoes() != null) {
            movement.atualizarObservacoes(dto.getObservacoes());
        }

        // 5. Salvar
        return movementRepository.save(movement);
    }
}
