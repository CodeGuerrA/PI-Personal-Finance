package com.personalFinance.personal_finance.investment.application.service;

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
 * Serviço de aplicação para deletar movimentações de investimento.
 * Responsabilidade: orquestrar deleção de movimentação (SRP).
 */
@Service
@RequiredArgsConstructor
public class InvestmentMovementDeleter {

    private final InvestmentMovementRepositoryPort movementRepository;
    private final UserRepository userRepository;
    private final InvestmentMovementOwnershipService ownershipService;

    /**
     * Deleta uma movimentação de investimento.
     */
    @Transactional
    public void delete(Long id, String keycloakId) {
        // 1. Buscar usuário
        User usuario = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar movimentação
        InvestmentMovement movement = movementRepository.findById(id)
                .orElseThrow(() -> InvestmentMovementNotFoundException.withId(id));

        // 3. Validar propriedade
        ownershipService.validateOwnership(movement, usuario.getId());

        // 4. Deletar
        movementRepository.delete(movement);
    }
}
