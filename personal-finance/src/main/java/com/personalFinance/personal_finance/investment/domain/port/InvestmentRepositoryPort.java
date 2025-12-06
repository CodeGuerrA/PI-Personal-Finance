package com.personalFinance.personal_finance.investment.domain.port;

import com.personalFinance.personal_finance.investment.domain.entity.Investment;
import com.personalFinance.personal_finance.investment.domain.entity.TipoInvestimento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Port (interface) para repositório de investimentos.
 * Segue Hexagonal Architecture e Dependency Inversion Principle (DIP).
 * Domain define o contrato, Infrastructure implementa.
 */
public interface InvestmentRepositoryPort {

    /**
     * Salva um investimento.
     */
    Investment save(Investment investment);

    /**
     * Busca investimento por ID.
     */
    Optional<Investment> findById(Long id);

    /**
     * Busca todos os investimentos de um usuário.
     */
    List<Investment> findByUsuarioId(Long usuarioId);

    /**
     * Busca investimentos ativos de um usuário.
     */
    List<Investment> findByUsuarioIdAndAtivo(Long usuarioId, Boolean ativo);

    /**
     * Busca investimentos de um usuário por tipo.
     */
    List<Investment> findByUsuarioIdAndTipoInvestimento(Long usuarioId, TipoInvestimento tipo);

    /**
     * Busca investimentos de um usuário por tipo e status ativo.
     */
    List<Investment> findByUsuarioIdAndTipoInvestimentoAndAtivo(Long usuarioId, TipoInvestimento tipo, Boolean ativo);

    /**
     * Deleta um investimento.
     */
    void delete(Investment investment);

    /**
     * Verifica se existe investimento com o ID.
     */
    boolean existsById(Long id);

    // ============ NOVOS MÉTODOS DE FILTRO E ESTATÍSTICAS ============

    /**
     * FILTRO: Busca investimentos por corretora (busca parcial case-insensitive).
     */
    List<Investment> findByUsuarioIdAndCorretora(Long usuarioId, String corretora);

    /**
     * FILTRO: Busca investimentos por período de compra.
     */
    List<Investment> findByUsuarioIdAndDataCompraBetween(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);

    /**
     * FILTRO: Busca investimentos por símbolo (busca parcial case-insensitive).
     */
    List<Investment> findByUsuarioIdAndSimboloContaining(Long usuarioId, String simbolo);

    /**
     * FILTRO/ORDENAÇÃO: Busca investimentos ordenados por valor investido (maior para menor).
     */
    List<Investment> findByUsuarioIdOrderByValorTotalInvestidoDesc(Long usuarioId);

    /**
     * FILTRO/ORDENAÇÃO: Busca investimentos ativos ordenados por data de compra (mais recentes primeiro).
     */
    List<Investment> findByUsuarioIdAndAtivoTrueOrderByDataCompraDesc(Long usuarioId);

    /**
     * ESTATÍSTICA: Conta total de investimentos ativos do usuário.
     */
    Long countByUsuarioIdAndAtivoTrue(Long usuarioId);

    /**
     * ESTATÍSTICA: Calcula valor total investido (soma de todos investimentos ativos).
     */
    BigDecimal calculateTotalInvested(Long usuarioId);

    /**
     * ESTATÍSTICA: Retorna resumo de investimentos por tipo (quantidade, total investido).
     * Retorna List<Object[]> onde cada array contém [tipo, quantidade, total_investido].
     */
    List<Object[]> findInvestmentSummaryByTipo(Long usuarioId);
}
