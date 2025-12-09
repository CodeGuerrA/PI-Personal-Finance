package com.personalFinance.personal_finance.transaction.domain.port;

import com.personalFinance.personal_finance.transaction.domain.entity.RecurringTransaction;
import com.personalFinance.personal_finance.transaction.domain.entity.FrequenciaRecorrencia;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Port (interface) para repositório de transações recorrentes.
 * Segue arquitetura hexagonal.
 */
public interface RecurringTransactionRepositoryPort {

    /**
     * Salva uma transação recorrente.
     */
    RecurringTransaction save(RecurringTransaction recurringTransaction);

    /**
     * Busca por ID.
     */
    Optional<RecurringTransaction> findById(Long id);

    /**
     * Lista todas as recorrências do usuário.
     */
    List<RecurringTransaction> findByUsuarioId(Long usuarioId);

    /**
     * Filtra por frequência.
     */
    List<RecurringTransaction> findByUsuarioIdAndFrequencia(Long usuarioId, FrequenciaRecorrencia frequencia);

    /**
     * Filtra por categoria.
     */
    List<RecurringTransaction> findByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId);

    /**
     * Filtra por tipo (RECEITA / DESPESA).
     */
    List<RecurringTransaction> findByUsuarioIdAndTipo(Long usuarioId, TipoTransacao tipo);

    /**
     * Filtra por recorrências ativas (sem data fim).
     */
    List<RecurringTransaction> findByUsuarioIdAndDataFimIsNull(Long usuarioId);

    /**
     * Filtra por recorrências ativas (ativa = true).
     */
    List<RecurringTransaction> findByUsuarioIdAndAtiva(Long usuarioId, Boolean ativa);

    /**
     * Deleta.
     */
    void delete(RecurringTransaction recurringTransaction);

    /**
     * Verifica se existe.
     */
    boolean existsById(Long id);

    /**
     * Busca recorrências que precisam gerar nova transação hoje.
     */
    List<RecurringTransaction> findDueForExecution(LocalDate hoje);
}