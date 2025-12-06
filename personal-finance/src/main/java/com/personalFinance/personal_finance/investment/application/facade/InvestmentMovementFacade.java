package com.personalFinance.personal_finance.investment.application.facade;

import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentMovementCreateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentMovementUpdateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.response.InvestmentMovementResponseDTO;

import java.util.List;

/**
 * Facade para operações de movimentações de investimento.
 * Interface que define o contrato para a camada de aplicação.
 */
public interface InvestmentMovementFacade {

    /**
     * Cria uma nova movimentação de investimento.
     */
    InvestmentMovementResponseDTO create(InvestmentMovementCreateRequestDTO dto, String keycloakId);

    /**
     * Busca movimentação por ID.
     */
    InvestmentMovementResponseDTO findById(Long id, String keycloakId);

    /**
     * Busca todas as movimentações do usuário.
     */
    List<InvestmentMovementResponseDTO> findAllByUser(String keycloakId);

    /**
     * Busca todas as movimentações de um investimento específico.
     */
    List<InvestmentMovementResponseDTO> findAllByUserAndInvestment(String keycloakId, Long investmentId);

    /**
     * Atualiza uma movimentação.
     */
    InvestmentMovementResponseDTO update(Long id, InvestmentMovementUpdateRequestDTO dto, String keycloakId);

    /**
     * Deleta uma movimentação.
     */
    void delete(Long id, String keycloakId);
}
