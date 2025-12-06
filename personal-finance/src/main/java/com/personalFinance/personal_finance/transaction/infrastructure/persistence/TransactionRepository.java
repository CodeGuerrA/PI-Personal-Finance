package com.personalFinance.personal_finance.transaction.infrastructure.persistence;

import com.personalFinance.personal_finance.transaction.domain.entity.Transaction;
import com.personalFinance.personal_finance.transaction.domain.entity.MetodoPagamento;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // ======================= CONSULTAS BASE =======================

    @Query("SELECT t FROM Transaction t WHERE t.usuario.id = :usuarioId")
    List<Transaction> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT t FROM Transaction t WHERE t.usuario.id = :usuarioId AND t.data = :data")
    List<Transaction> findByUsuarioIdAndData(@Param("usuarioId") Long usuarioId,
                                             @Param("data") LocalDate data);

    @Query("SELECT t FROM Transaction t WHERE t.usuario.id = :usuarioId AND t.data BETWEEN :inicio AND :fim")
    List<Transaction> findByUsuarioIdAndDataBetween(@Param("usuarioId") Long usuarioId,
                                                    @Param("inicio") LocalDate inicio,
                                                    @Param("fim") LocalDate fim);

    @Query("SELECT t FROM Transaction t WHERE t.usuario.id = :usuarioId AND t.categoria.id = :categoriaId")
    List<Transaction> findByUsuarioIdAndCategoriaId(@Param("usuarioId") Long usuarioId,
                                                    @Param("categoriaId") Long categoriaId);

    @Query("SELECT t FROM Transaction t WHERE t.usuario.id = :usuarioId AND t.tipo = :tipo")
    List<Transaction> findByUsuarioIdAndTipo(@Param("usuarioId") Long usuarioId,
                                             @Param("tipo") TipoTransacao tipo);

    @Query("SELECT t FROM Transaction t WHERE t.usuario.id = :usuarioId AND t.metodoPagamento = :metodo")
    List<Transaction> findByUsuarioIdAndMetodoPagamento(@Param("usuarioId") Long usuarioId,
                                                        @Param("metodo") MetodoPagamento metodo);

    @Query("SELECT t FROM Transaction t WHERE t.usuario.id = :usuarioId AND t.valor BETWEEN :min AND :max")
    List<Transaction> findByUsuarioIdAndValorBetween(@Param("usuarioId") Long usuarioId,
                                                     @Param("min") BigDecimal min,
                                                     @Param("max") BigDecimal max);

    // ======================= ESTATÍSTICAS =======================

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transaction t WHERE t.usuario.id = :usuarioId AND t.tipo = com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao.RECEITA")
    BigDecimal sumReceitas(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transaction t WHERE t.usuario.id = :usuarioId AND t.tipo = com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao.DESPESA")
    BigDecimal sumDespesas(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.usuario.id = :usuarioId")
    Long countByUsuarioId(@Param("usuarioId") Long usuarioId);

    // ======================= AGRUPAMENTOS =======================

    @Query("""
           SELECT t.categoria.id AS categoriaId,
                  SUM(t.valor) AS total
           FROM Transaction t
           WHERE t.usuario.id = :usuarioId
           GROUP BY t.categoria.id
           ORDER BY total DESC
           """)
    List<Object[]> findResumoPorCategoria(@Param("usuarioId") Long usuarioId);

    @Query("""
           SELECT t.tipo AS tipo,
                  SUM(t.valor) AS total
           FROM Transaction t
           WHERE t.usuario.id = :usuarioId
           GROUP BY t.tipo
           """)
    List<Object[]> findResumoPorTipo(@Param("usuarioId") Long usuarioId);
}