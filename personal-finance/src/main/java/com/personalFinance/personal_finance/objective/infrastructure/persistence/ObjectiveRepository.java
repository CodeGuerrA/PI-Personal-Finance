package com.personalFinance.personal_finance.objective.infrastructure.persistence;

import com.personalFinance.personal_finance.objective.domain.entity.Objective;
import com.personalFinance.personal_finance.objective.domain.entity.ObjectiveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA para Objective.
 * Utiliza JPQL explícito para todas as queries.
 */
@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {

    /**
     * Busca todos os objetivos ativos de um usuário.
     */
    @Query("SELECT o FROM Objective o WHERE o.usuario.id = :usuarioId AND o.ativa = true")
    List<Objective> findByUsuarioIdAndAtivaTrue(@Param("usuarioId") Long usuarioId);

    /**
     * Busca objetivos ativos de um usuário por mês/ano.
     */
    @Query("SELECT o FROM Objective o WHERE o.usuario.id = :usuarioId AND o.mesAno = :mesAno AND o.ativa = true")
    List<Objective> findByUsuarioIdAndMesAnoAndAtivaTrue(@Param("usuarioId") Long usuarioId, @Param("mesAno") String mesAno);

    /**
     * Busca um objetivo específico ativo de um usuário por categoria, mês/ano e tipo.
     */
    @Query("SELECT o FROM Objective o WHERE o.usuario.id = :usuarioId AND o.categoriaId = :categoriaId AND o.mesAno = :mesAno AND o.tipo = :tipo AND o.ativa = true")
    Optional<Objective> findByUsuarioIdAndCategoriaIdAndMesAnoAndTipoAndAtivaTrue(
            @Param("usuarioId") Long usuarioId,
            @Param("categoriaId") Long categoriaId,
            @Param("mesAno") String mesAno,
            @Param("tipo") ObjectiveType tipo);

    /**
     * Busca objetivos ativos de um usuário por tipo.
     */
    @Query("SELECT o FROM Objective o WHERE o.usuario.id = :usuarioId AND o.tipo = :tipo AND o.ativa = true")
    List<Objective> findByUsuarioIdAndTipoAndAtivaTrue(@Param("usuarioId") Long usuarioId, @Param("tipo") ObjectiveType tipo);

    /**
     * Busca objetivos ativos que estão perto de serem cumpridos (percentual >= 80%).
     * Usa SQL nativo para cálculo.
     */
    @Query(value = "SELECT * FROM objectives o " +
                   "WHERE o.usuario_id = :usuarioId " +
                   "AND o.ativa = true " +
                   "AND (o.valor_atual / NULLIF(o.valor_objetivo, 0) * 100) >= 80 " +
                   "ORDER BY (o.valor_atual / NULLIF(o.valor_objetivo, 0) * 100) DESC",
           nativeQuery = true)
    List<Objective> findObjectivesCloseToCompletion(@Param("usuarioId") Long usuarioId);

    /**
     * Busca objetivos do tipo LIMITE_CATEGORIA que já ultrapassaram o limite (valor_atual > valor_objetivo).
     * Usa SQL nativo para comparação.
     */
    @Query(value = "SELECT * FROM objectives o " +
                   "WHERE o.usuario_id = :usuarioId " +
                   "AND o.tipo = :tipo " +
                   "AND o.ativa = true " +
                   "AND o.valor_atual > o.valor_objetivo " +
                   "ORDER BY (o.valor_atual - o.valor_objetivo) DESC",
           nativeQuery = true)
    List<Objective> findOverLimitObjectives(@Param("usuarioId") Long usuarioId, @Param("tipo") String tipo);

    /**
     * Conta total de objetivos ativos de um usuário.
     */
    @Query("SELECT COUNT(o) FROM Objective o WHERE o.usuario.id = :usuarioId AND o.ativa = true")
    Long countByUsuarioIdAndAtivaTrue(@Param("usuarioId") Long usuarioId);

    /**
     * Conta objetivos de metas de um usuário.
     * Nota: Não é possível calcular cumpridos pois valor_atual é @Transient.
     */
    @Query("SELECT COUNT(o) FROM Objective o " +
           "WHERE o.usuario.id = :usuarioId " +
           "AND o.ativa = true " +
           "AND o.tipo IN ('META_ECONOMIA_MES', 'META_INVESTIMENTO')")
    Long countCompletedGoals(@Param("usuarioId") Long usuarioId);

    /**
     * Busca resumo de objetivos por tipo para um usuário (SQL nativo).
     * Nota: valor_atual é @Transient, então não pode ser usado em queries SQL.
     */
    @Query(value = "SELECT tipo, COUNT(*) as quantidade, " +
                   "SUM(valor_objetivo) as total_objetivo " +
                   "FROM objectives " +
                   "WHERE usuario_id = :usuarioId AND ativa = true " +
                   "GROUP BY tipo " +
                   "ORDER BY COUNT(*) DESC",
           nativeQuery = true)
    List<Object[]> findObjectiveSummaryByTipo(@Param("usuarioId") Long usuarioId);

    /**
     * Busca objetivos ativos ordenados por mês/ano (mais recentes primeiro).
     */
    @Query("SELECT o FROM Objective o WHERE o.usuario.id = :usuarioId AND o.ativa = true ORDER BY o.mesAno DESC")
    List<Objective> findByUsuarioIdAndAtivaTrueOrderByMesAnoDesc(@Param("usuarioId") Long usuarioId);
}
