package com.personalFinance.personal_finance.investment.application.facade;

import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentMovementCreateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentMovementUpdateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.response.InvestmentMovementResponseDTO;
import com.personalFinance.personal_finance.investment.api.mapper.InvestmentMovementMapper;
import com.personalFinance.personal_finance.investment.application.service.InvestmentMovementCreator;
import com.personalFinance.personal_finance.investment.application.service.InvestmentMovementDeleter;
import com.personalFinance.personal_finance.investment.application.service.InvestmentMovementFinder;
import com.personalFinance.personal_finance.investment.application.service.InvestmentMovementUpdater;
import com.personalFinance.personal_finance.investment.domain.entity.InvestmentMovement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação da Facade para movimentações de investimento.
 * Orquestra operações e conversões entre camadas.
 */
@Service
@RequiredArgsConstructor
public class InvestmentMovementFacadeImpl implements InvestmentMovementFacade {

    private final InvestmentMovementCreator creator;
    private final InvestmentMovementFinder finder;
    private final InvestmentMovementUpdater updater;
    private final InvestmentMovementDeleter deleter;
    private final InvestmentMovementMapper mapper;

    @Override
    public InvestmentMovementResponseDTO create(InvestmentMovementCreateRequestDTO dto, String keycloakId) {
        InvestmentMovement movement = creator.create(dto, keycloakId);
        return mapper.toResponseDTO(movement);
    }

    @Override
    public InvestmentMovementResponseDTO findById(Long id, String keycloakId) {
        InvestmentMovement movement = finder.findById(id, keycloakId);
        return mapper.toResponseDTO(movement);
    }

    @Override
    public List<InvestmentMovementResponseDTO> findAllByUser(String keycloakId) {
        return finder.findAllByUser(keycloakId).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvestmentMovementResponseDTO> findAllByUserAndInvestment(String keycloakId, Long investmentId) {
        return finder.findAllByUserAndInvestment(keycloakId, investmentId).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InvestmentMovementResponseDTO update(Long id, InvestmentMovementUpdateRequestDTO dto, String keycloakId) {
        InvestmentMovement movement = updater.update(id, dto, keycloakId);
        return mapper.toResponseDTO(movement);
    }

    @Override
    public void delete(Long id, String keycloakId) {
        deleter.delete(id, keycloakId);
    }
}
