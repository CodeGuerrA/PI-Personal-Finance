package com.personalFinance.personal_finance.investment.infrastructure.repository.adapter;

import com.personalFinance.personal_finance.investment.domain.entity.InvestmentMovement;
import com.personalFinance.personal_finance.investment.domain.port.InvestmentMovementRepositoryPort;
import com.personalFinance.personal_finance.investment.infrastructure.persistence.InvestmentMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter para repositório de movimentações de investimento.
 * Implementa a porta definida no domínio (Hexagonal Architecture).
 */
@Component
@RequiredArgsConstructor
public class InvestmentMovementRepositoryAdapter implements InvestmentMovementRepositoryPort {

    private final InvestmentMovementRepository repository;

    @Override
    public InvestmentMovement save(InvestmentMovement movement) {
        return repository.save(movement);
    }

    @Override
    public Optional<InvestmentMovement> findById(Long id) {
        return repository.findByIdWithInvestment(id);
    }

    @Override
    public List<InvestmentMovement> findAllByUsuarioId(Long usuarioId) {
        return repository.findAllByUsuarioId(usuarioId);
    }

    @Override
    public List<InvestmentMovement> findAllByUsuarioIdAndInvestmentId(Long usuarioId, Long investmentId) {
        return repository.findAllByUsuarioIdAndInvestmentId(usuarioId, investmentId);
    }

    @Override
    public void delete(InvestmentMovement movement) {
        repository.delete(movement);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
