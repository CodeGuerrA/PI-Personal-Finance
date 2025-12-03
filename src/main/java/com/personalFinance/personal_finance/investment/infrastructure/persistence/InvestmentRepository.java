package com.personalFinance.personal_finance.investment.infrastructure.persistence;

import com.personalFinance.personal_finance.investment.domain.entity.Investment;
import com.personalFinance.personal_finance.investment.domain.entity.TipoInvestimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository JPA para Investment.
 * Utiliza JPQL explícito para todas as queries.
 */
@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    /**
     * Busca todos os investimentos de um usuário.
     */
    @Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId")
    List<Investment> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca investimentos ativos de um usuário.
     */
    @Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND i.ativo = :ativo")
    List<Investment> findByUsuarioIdAndAtivo(@Param("usuarioId") Long usuarioId, @Param("ativo") Boolean ativo);

    /**
     * Busca investimentos de um usuário por tipo.
     */
    @Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND i.tipoInvestimento = :tipoInvestimento")
    List<Investment> findByUsuarioIdAndTipoInvestimento(@Param("usuarioId") Long usuarioId, @Param("tipoInvestimento") TipoInvestimento tipoInvestimento);

    /**
     * Busca investimentos de um usuário por tipo e status ativo.
     */
    @Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND i.tipoInvestimento = :tipoInvestimento AND i.ativo = :ativo")
    List<Investment> findByUsuarioIdAndTipoInvestimentoAndAtivo(@Param("usuarioId") Long usuarioId, @Param("tipoInvestimento") TipoInvestimento tipoInvestimento, @Param("ativo") Boolean ativo);

    /**
     * Busca investimentos de um usuário por corretora.
     */
    @Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND LOWER(i.corretora) LIKE LOWER(CONCAT('%', :corretora, '%'))")
    List<Investment> findByUsuarioIdAndCorretora(@Param("usuarioId") Long usuarioId, @Param("corretora") String corretora);

    /**
     * Busca investimentos de um usuário por período (data de compra).
     */
    @Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND i.dataCompra BETWEEN :dataInicio AND :dataFim")
    List<Investment> findByUsuarioIdAndDataCompraBetween(@Param("usuarioId") Long usuarioId, @Param("dataInicio") java.time.LocalDate dataInicio, @Param("dataFim") java.time.LocalDate dataFim);

    /**
     * Busca investimentos de um usuário por símbolo (busca parcial).
     */
    @Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND LOWER(i.simbolo) LIKE LOWER(CONCAT('%', :simbolo, '%'))")
    List<Investment> findByUsuarioIdAndSimboloContaining(@Param("usuarioId") Long usuarioId, @Param("simbolo") String simbolo);

    /**
     * Busca investimentos de um usuário ordenados por valor total investido (decrescente).
     */
    @Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId ORDER BY i.valorTotalInvestido DESC")
    List<Investment> findByUsuarioIdOrderByValorTotalInvestidoDesc(@Param("usuarioId") Long usuarioId);

    /**
     * Busca investimentos de um usuário ordenados por data de compra (mais recentes primeiro).
     */
    @Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND i.ativo = true ORDER BY i.dataCompra DESC")
    List<Investment> findByUsuarioIdAndAtivoTrueOrderByDataCompraDesc(@Param("usuarioId") Long usuarioId);

    /**
     * Conta total de investimentos ativos de um usuário.
     */
    @Query("SELECT COUNT(i) FROM Investment i WHERE i.usuario.id = :usuarioId AND i.ativo = true")
    Long countByUsuarioIdAndAtivoTrue(@Param("usuarioId") Long usuarioId);

    /**
     * Calcula valor total investido por um usuário (apenas investimentos ativos).
     * Usa SQL nativo para melhor performance.
     */
    @Query(value = "SELECT COALESCE(SUM(valor_total_investido), 0) FROM investments WHERE usuario_id = :usuarioId AND ativo = true", nativeQuery = true)
    java.math.BigDecimal calcularValorTotalInvestidoByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca resumo de investimentos por tipo para um usuário (SQL nativo).
     */
    @Query(value = "SELECT tipo_investimento as tipo, COUNT(*) as quantidade, SUM(valor_total_investido) as total_investido " +
                   "FROM investments " +
                   "WHERE usuario_id = :usuarioId AND ativo = true " +
                   "GROUP BY tipo_investimento " +
                   "ORDER BY total_investido DESC",
           nativeQuery = true)
    List<Object[]> findInvestmentSummaryByTipo(@Param("usuarioId") Long usuarioId);
}
