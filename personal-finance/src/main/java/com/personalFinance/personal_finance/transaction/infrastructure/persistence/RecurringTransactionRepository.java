package com.personalFinance.personal_finance.transaction.infrastructure.persistence;

import com.personalFinance.personal_finance.transaction.domain.entity.RecurringTransaction;
import com.personalFinance.personal_finance.transaction.domain.entity.FrequenciaRecorrencia;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {

    @Query("SELECT r FROM RecurringTransaction r WHERE r.usuario.id = :usuarioId")
    List<RecurringTransaction> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT r FROM RecurringTransaction r WHERE r.usuario.id = :usuarioId AND r.frequencia = :frequencia")
    List<RecurringTransaction> findByUsuarioIdAndFrequencia(@Param("usuarioId") Long usuarioId,
                                                            @Param("frequencia") FrequenciaRecorrencia frequencia);

    @Query("SELECT r FROM RecurringTransaction r WHERE r.usuario.id = :usuarioId AND r.categoria.id = :categoriaId")
    List<RecurringTransaction> findByUsuarioIdAndCategoriaId(@Param("usuarioId") Long usuarioId,
                                                             @Param("categoriaId") Long categoriaId);

    @Query("SELECT r FROM RecurringTransaction r WHERE r.usuario.id = :usuarioId AND r.tipo = :tipo")
    List<RecurringTransaction> findByUsuarioIdAndTipo(@Param("usuarioId") Long usuarioId,
                                                      @Param("tipo") TipoTransacao tipo);

    @Query("SELECT r FROM RecurringTransaction r WHERE r.usuario.id = :usuarioId AND r.dataFim IS NULL")
    List<RecurringTransaction> findByUsuarioIdAndDataFimIsNull(@Param("usuarioId") Long usuarioId);

    /**
     * Busca recorrências que precisam executar HOJE.
     * A regra no domínio será criada depois (service).
     */
    @Query("""
           SELECT r FROM RecurringTransaction r
           WHERE r.dataFim IS NULL
             AND r.dataInicio <= :hoje
           """)
    List<RecurringTransaction> findDueForExecution(@Param("hoje") LocalDate hoje);
}