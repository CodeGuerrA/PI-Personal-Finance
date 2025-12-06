package com.personalFinance.personal_finance.investment.application.service;

import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentMovementCreateRequestDTO;
import com.personalFinance.personal_finance.investment.domain.entity.Investment;
import com.personalFinance.personal_finance.investment.domain.entity.InvestmentMovement;
import com.personalFinance.personal_finance.investment.domain.port.InvestmentMovementRepositoryPort;
import com.personalFinance.personal_finance.investment.domain.port.InvestmentRepositoryPort;
import com.personalFinance.personal_finance.investment.domain.exception.InvestmentNotFoundException;
import com.personalFinance.personal_finance.investment.domain.service.InvestmentOwnershipService;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço de aplicação para criar movimentações de investimento.
 * Responsabilidade: orquestrar criação de movimentação (SRP).
 */
@Service
@RequiredArgsConstructor
public class InvestmentMovementCreator {

    private final InvestmentMovementRepositoryPort movementRepository;
    private final InvestmentRepositoryPort investmentRepository;
    private final UserRepository userRepository;
    private final InvestmentOwnershipService ownershipService;
    private final InvestmentMovementTransactionIntegrationService transactionIntegrationService;

    /**
     * Cria uma nova movimentação de investimento.
     */
    @Transactional
    public InvestmentMovement create(InvestmentMovementCreateRequestDTO dto, String keycloakId) {
        // 1. Buscar usuário
        User usuario = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar investimento
        Investment investment = investmentRepository.findById(dto.getInvestmentId())
                .orElseThrow(() -> new InvestmentNotFoundException(dto.getInvestmentId()));

        // 3. Validar propriedade do investimento
        ownershipService.validateOwnership(investment, usuario);

        // 4. Criar movimentação
        InvestmentMovement movement = InvestmentMovement.create(
                investment,
                usuario,
                dto.getTipoMovimentacao(),
                dto.getQuantidade(),
                dto.getValorUnitario(),
                dto.getValorTotal(),
                dto.getTaxas(),
                dto.getDataMovimentacao(),
                dto.getObservacoes()
        );

        // 5. Salvar
        movement = movementRepository.save(movement);

        // 6. Criar transação automaticamente (se aplicável)
        transactionIntegrationService.createTransactionFromMovement(movement, usuario);

        return movement;
    }
}
