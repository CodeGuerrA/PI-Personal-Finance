package com.personalFinance.personal_finance.investment.infrastructure.repository.adapter;

import com.personalFinance.personal_finance.investment.domain.entity.Investment;
import com.personalFinance.personal_finance.investment.domain.entity.TipoInvestimento;
import com.personalFinance.personal_finance.investment.domain.port.InvestmentRepositoryPort;
import com.personalFinance.personal_finance.investment.infrastructure.persistence.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Adapter que implementa o Port do repositório de investimentos.
 * Segue Hexagonal Architecture e Adapter Pattern.
 * Conecta o Domain (Port) com a Infrastructure (JPA Repository).
 */
@Component
@RequiredArgsConstructor
public class InvestmentRepositoryAdapter implements InvestmentRepositoryPort {

    private final InvestmentRepository investmentRepository;

    @Override
    public Investment save(Investment investment) {
        return investmentRepository.save(investment);
    }

    @Override
    public Optional<Investment> findById(Long id) {
        return investmentRepository.findById(id);
    }

    @Override
    public List<Investment> findByUsuarioId(Long usuarioId) {
        return investmentRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Investment> findByUsuarioIdAndAtivo(Long usuarioId, Boolean ativo) {
        return investmentRepository.findByUsuarioIdAndAtivo(usuarioId, ativo);
    }

    @Override
    public List<Investment> findByUsuarioIdAndTipoInvestimento(Long usuarioId, TipoInvestimento tipo) {
        return investmentRepository.findByUsuarioIdAndTipoInvestimento(usuarioId, tipo);
    }

    @Override
    public List<Investment> findByUsuarioIdAndTipoInvestimentoAndAtivo(Long usuarioId, TipoInvestimento tipo, Boolean ativo) {
        return investmentRepository.findByUsuarioIdAndTipoInvestimentoAndAtivo(usuarioId, tipo, ativo);
    }

    @Override
    public void delete(Investment investment) {
        investmentRepository.delete(investment);
    }

    @Override
    public boolean existsById(Long id) {
        return investmentRepository.existsById(id);
    }

    // ============ NOVOS MÉTODOS DE FILTRO E ESTATÍSTICAS ============

    @Override
    public List<Investment> findByUsuarioIdAndCorretora(Long usuarioId, String corretora) {
        return investmentRepository.findByUsuarioIdAndCorretora(usuarioId, corretora);
    }

    @Override
    public List<Investment> findByUsuarioIdAndDataCompraBetween(Long usuarioId, LocalDate dataInicio, LocalDate dataFim) {
        return investmentRepository.findByUsuarioIdAndDataCompraBetween(usuarioId, dataInicio, dataFim);
    }

    @Override
    public List<Investment> findByUsuarioIdAndSimboloContaining(Long usuarioId, String simbolo) {
        return investmentRepository.findByUsuarioIdAndSimboloContaining(usuarioId, simbolo);
    }

    @Override
    public List<Investment> findByUsuarioIdOrderByValorTotalInvestidoDesc(Long usuarioId) {
        return investmentRepository.findByUsuarioIdOrderByValorTotalInvestidoDesc(usuarioId);
    }

    @Override
    public List<Investment> findByUsuarioIdAndAtivoTrueOrderByDataCompraDesc(Long usuarioId) {
        return investmentRepository.findByUsuarioIdAndAtivoTrueOrderByDataCompraDesc(usuarioId);
    }

    @Override
    public Long countByUsuarioIdAndAtivoTrue(Long usuarioId) {
        return investmentRepository.countByUsuarioIdAndAtivoTrue(usuarioId);
    }

    @Override
    public BigDecimal calculateTotalInvested(Long usuarioId) {
        return investmentRepository.calcularValorTotalInvestidoByUsuarioId(usuarioId);
    }

    @Override
    public List<Object[]> findInvestmentSummaryByTipo(Long usuarioId) {
        return investmentRepository.findInvestmentSummaryByTipo(usuarioId);
    }
}
