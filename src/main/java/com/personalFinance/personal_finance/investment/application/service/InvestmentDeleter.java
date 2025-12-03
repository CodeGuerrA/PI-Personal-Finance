package com.personalFinance.personal_finance.investment.application.service;

import com.personalFinance.personal_finance.investment.domain.entity.Investment;
import com.personalFinance.personal_finance.investment.domain.exception.InvestmentNotFoundException;
import com.personalFinance.personal_finance.investment.domain.port.InvestmentRepositoryPort;
import com.personalFinance.personal_finance.investment.domain.service.InvestmentOwnershipService;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service seguindo SRP: Responsável apenas por DELETAR investimentos.
 * Garante que usuário só deleta seus próprios investimentos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentDeleter {

    private final InvestmentRepositoryPort investmentRepository;
    private final UserRepository userRepository;
    private final InvestmentOwnershipService ownershipService;

    @Transactional
    public void delete(String keycloakId, Long investmentId) {
        log.info("Deletando investimento {} para usuário {}", investmentId, keycloakId);

        // 1. Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar investimento
        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new InvestmentNotFoundException(investmentId));

        // 3. Validar propriedade (Security: usuário só pode deletar seus próprios investimentos)
        ownershipService.validateOwnership(investment, user);

        // 4. Deletar
        investmentRepository.delete(investment);
        log.info("Investimento {} deletado com sucesso", investmentId);
    }
}
