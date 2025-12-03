package com.personalFinance.personal_finance.investment.application.facade;

import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentCreateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentUpdateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.response.InvestmentResponseDTO;
import com.personalFinance.personal_finance.investment.domain.entity.TipoInvestimento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Facade (Facade Pattern) para operações de Investment.
 * Abstrai a complexidade dos Application Services internos.
 * Fornece uma interface simplificada para o Controller.
 */
public interface InvestmentFacade {

    InvestmentResponseDTO createInvestment(String keycloakId, InvestmentCreateRequestDTO dto);

    InvestmentResponseDTO findById(String keycloakId, Long investmentId);

    List<InvestmentResponseDTO> findAllByUser(String keycloakId);

    List<InvestmentResponseDTO> findByUserAndAtivo(String keycloakId, Boolean ativo);

    List<InvestmentResponseDTO> findByUserAndTipo(String keycloakId, TipoInvestimento tipo);

    InvestmentResponseDTO updateInvestment(String keycloakId, Long investmentId, InvestmentUpdateRequestDTO dto);

    void deleteInvestment(String keycloakId, Long investmentId);

    // ============ NOVOS MÉTODOS DE FILTRO E ESTATÍSTICAS ============

    /**
     * FILTRO: Busca investimentos por corretora.
     */
    List<InvestmentResponseDTO> findByUserAndCorretora(String keycloakId, String corretora);

    /**
     * FILTRO: Busca investimentos por período de compra.
     */
    List<InvestmentResponseDTO> findByUserAndDataCompraBetween(String keycloakId, LocalDate dataInicio, LocalDate dataFim);

    /**
     * FILTRO: Busca investimentos por símbolo.
     */
    List<InvestmentResponseDTO> findByUserAndSimboloContaining(String keycloakId, String simbolo);

    /**
     * FILTRO/ORDENAÇÃO: Busca investimentos ordenados por valor investido.
     */
    List<InvestmentResponseDTO> findByUserOrderByValorDesc(String keycloakId);

    /**
     * FILTRO/ORDENAÇÃO: Busca investimentos ativos ordenados por data de compra.
     */
    List<InvestmentResponseDTO> findActiveOrderByDataCompraDesc(String keycloakId);

    /**
     * ESTATÍSTICA: Conta total de investimentos ativos.
     */
    Long countActiveInvestments(String keycloakId);

    /**
     * ESTATÍSTICA: Calcula valor total investido.
     */
    BigDecimal calculateTotalInvested(String keycloakId);

    /**
     * ESTATÍSTICA: Retorna resumo de investimentos por tipo.
     */
    List<Object[]> findInvestmentSummaryByTipo(String keycloakId);
}
