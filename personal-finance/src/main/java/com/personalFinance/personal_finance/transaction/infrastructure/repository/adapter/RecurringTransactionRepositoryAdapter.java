package com.personalFinance.personal_finance.transaction.infrastructure.repository.adapter;

import com.personalFinance.personal_finance.transaction.domain.entity.RecurringTransaction;
import com.personalFinance.personal_finance.transaction.domain.entity.FrequenciaRecorrencia;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;
import com.personalFinance.personal_finance.transaction.domain.port.RecurringTransactionRepositoryPort;
import com.personalFinance.personal_finance.transaction.infrastructure.persistence.RecurringTransactionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Adapter que implementa o Port de transações recorrentes.
 * Segue Hexagonal Architecture e Adapter Pattern.
 */
@Component
@RequiredArgsConstructor
public class RecurringTransactionRepositoryAdapter implements RecurringTransactionRepositoryPort {

    private final RecurringTransactionRepository repository;

    @Override
    public RecurringTransaction save(RecurringTransaction recurringTransaction) {
        return repository.save(recurringTransaction);
    }

    @Override
    public Optional<RecurringTransaction> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<RecurringTransaction> findByUsuarioId(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<RecurringTransaction> findByUsuarioIdAndFrequencia(Long usuarioId, FrequenciaRecorrencia frequencia) {
        return repository.findByUsuarioIdAndFrequencia(usuarioId, frequencia);
    }

    @Override
    public List<RecurringTransaction> findByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId) {
        return repository.findByUsuarioIdAndCategoriaId(usuarioId, categoriaId);
    }

    @Override
    public List<RecurringTransaction> findByUsuarioIdAndTipo(Long usuarioId, TipoTransacao tipo) {
        return repository.findByUsuarioIdAndTipo(usuarioId, tipo);
    }

    @Override
    public List<RecurringTransaction> findByUsuarioIdAndDataFimIsNull(Long usuarioId) {
        return repository.findByUsuarioIdAndDataFimIsNull(usuarioId);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public void delete(RecurringTransaction recurringTransaction) {
        repository.delete(recurringTransaction);
    }

    @Override
    public List<RecurringTransaction> findDueForExecution(LocalDate hoje) {
        return repository.findDueForExecution(hoje);
    }
}
