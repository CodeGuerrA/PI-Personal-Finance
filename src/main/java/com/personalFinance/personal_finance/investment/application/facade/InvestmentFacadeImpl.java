package com.personalFinance.personal_finance.investment.application.facade;

import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentCreateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentUpdateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.response.InvestmentResponseDTO;
import com.personalFinance.personal_finance.investment.application.service.InvestmentCreator;
import com.personalFinance.personal_finance.investment.application.service.InvestmentDeleter;
import com.personalFinance.personal_finance.investment.application.service.InvestmentFinder;
import com.personalFinance.personal_finance.investment.application.service.InvestmentUpdater;
import com.personalFinance.personal_finance.investment.domain.entity.TipoInvestimento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementação do Facade Pattern para Investment.
 * Coordena os Application Services seguindo SRP.
 * Cada operação delega para o service especializado.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InvestmentFacadeImpl implements InvestmentFacade {

    private final InvestmentCreator investmentCreator;
    private final InvestmentFinder investmentFinder;
    private final InvestmentUpdater investmentUpdater;
    private final InvestmentDeleter investmentDeleter;

    @Override
    public InvestmentResponseDTO createInvestment(String keycloakId, InvestmentCreateRequestDTO dto) {
        log.info("Facade: Criando investimento para usuário {}", keycloakId);
        return investmentCreator.create(keycloakId, dto);
    }

    @Override
    public InvestmentResponseDTO findById(String keycloakId, Long investmentId) {
        log.info("Facade: Buscando investimento {} do usuário {}", investmentId, keycloakId);
        return investmentFinder.findById(keycloakId, investmentId);
    }

    @Override
    public List<InvestmentResponseDTO> findAllByUser(String keycloakId) {
        log.info("Facade: Buscando todos investimentos do usuário {}", keycloakId);
        return investmentFinder.findAllByUser(keycloakId);
    }

    @Override
    public List<InvestmentResponseDTO> findByUserAndAtivo(String keycloakId, Boolean ativo) {
        log.info("Facade: Buscando investimentos ativos={} do usuário {}", ativo, keycloakId);
        return investmentFinder.findByUserAndAtivo(keycloakId, ativo);
    }

    @Override
    public List<InvestmentResponseDTO> findByUserAndTipo(String keycloakId, TipoInvestimento tipo) {
        log.info("Facade: Buscando investimentos tipo={} do usuário {}", tipo, keycloakId);
        return investmentFinder.findByUserAndTipo(keycloakId, tipo);
    }

    @Override
    public InvestmentResponseDTO updateInvestment(String keycloakId, Long investmentId, InvestmentUpdateRequestDTO dto) {
        log.info("Facade: Atualizando investimento {} do usuário {}", investmentId, keycloakId);
        return investmentUpdater.update(keycloakId, investmentId, dto);
    }

    @Override
    public void deleteInvestment(String keycloakId, Long investmentId) {
        log.info("Facade: Deletando investimento {} do usuário {}", investmentId, keycloakId);
        investmentDeleter.delete(keycloakId, investmentId);
    }

    // ============ NOVOS MÉTODOS DE FILTRO E ESTATÍSTICAS ============

    @Override
    public List<InvestmentResponseDTO> findByUserAndCorretora(String keycloakId, String corretora) {
        log.info("Facade: Buscando investimentos por corretora={} do usuário {}", corretora, keycloakId);
        return investmentFinder.findByUserAndCorretora(keycloakId, corretora);
    }

    @Override
    public List<InvestmentResponseDTO> findByUserAndDataCompraBetween(String keycloakId, LocalDate dataInicio, LocalDate dataFim) {
        log.info("Facade: Buscando investimentos entre {} e {} do usuário {}", dataInicio, dataFim, keycloakId);
        return investmentFinder.findByUserAndDataCompraBetween(keycloakId, dataInicio, dataFim);
    }

    @Override
    public List<InvestmentResponseDTO> findByUserAndSimboloContaining(String keycloakId, String simbolo) {
        log.info("Facade: Buscando investimentos por símbolo={} do usuário {}", simbolo, keycloakId);
        return investmentFinder.findByUserAndSimboloContaining(keycloakId, simbolo);
    }

    @Override
    public List<InvestmentResponseDTO> findByUserOrderByValorDesc(String keycloakId) {
        log.info("Facade: Buscando investimentos ordenados por valor do usuário {}", keycloakId);
        return investmentFinder.findByUserOrderByValorDesc(keycloakId);
    }

    @Override
    public List<InvestmentResponseDTO> findActiveOrderByDataCompraDesc(String keycloakId) {
        log.info("Facade: Buscando investimentos ativos ordenados por data do usuário {}", keycloakId);
        return investmentFinder.findActiveOrderByDataCompraDesc(keycloakId);
    }

    @Override
    public Long countActiveInvestments(String keycloakId) {
        log.info("Facade: Contando investimentos ativos do usuário {}", keycloakId);
        return investmentFinder.countActiveInvestments(keycloakId);
    }

    @Override
    public BigDecimal calculateTotalInvested(String keycloakId) {
        log.info("Facade: Calculando valor total investido do usuário {}", keycloakId);
        return investmentFinder.calculateTotalInvested(keycloakId);
    }

    @Override
    public List<Object[]> findInvestmentSummaryByTipo(String keycloakId) {
        log.info("Facade: Buscando resumo de investimentos por tipo do usuário {}", keycloakId);
        return investmentFinder.findInvestmentSummaryByTipo(keycloakId);
    }
}
