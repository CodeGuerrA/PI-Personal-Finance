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

import java.util.List;

/**
 * Serviço de aplicação para buscar movimentações de investimento.
 * Responsabilidade: orquestrar busca de movimentações (SRP).
 */
@Service
@RequiredArgsConstructor
public class InvestmentMovementFinder {

    private final InvestmentMovementRepositoryPort movementRepository;
    private final UserRepository userRepository;
    private final InvestmentMovementOwnershipService ownershipService;

    /**
     * Busca movimentação por ID (validando propriedade).
     */
    @Transactional(readOnly = true)
    public InvestmentMovement findById(Long id, String keycloakId) {
        User usuario = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        InvestmentMovement movement = movementRepository.findById(id)
                .orElseThrow(() -> InvestmentMovementNotFoundException.withId(id));

        ownershipService.validateOwnership(movement, usuario.getId());

        return movement;
    }

    /**
     * Busca todas as movimentações do usuário.
     */
    @Transactional(readOnly = true)
    public List<InvestmentMovement> findAllByUser(String keycloakId) {
        User usuario = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return movementRepository.findAllByUsuarioId(usuario.getId());
    }

    /**
     * Busca todas as movimentações de um investimento específico do usuário.
     */
    @Transactional(readOnly = true)
    public List<InvestmentMovement> findAllByUserAndInvestment(String keycloakId, Long investmentId) {
        User usuario = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return movementRepository.findAllByUsuarioIdAndInvestmentId(usuario.getId(), investmentId);
    }
}
