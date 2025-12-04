package com.personalFinance.personal_finance.transaction.infrastructure.repository.adapter;

import com.personalFinance.personal_finance.transaction.domain.entity.Transaction;
import com.personalFinance.personal_finance.transaction.domain.entity.MetodoPagamento;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;
import com.personalFinance.personal_finance.transaction.domain.port.TransactionRepositoryPort;
import com.personalFinance.personal_finance.transaction.infrastructure.persistence.TransactionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Adapter que implementa o Port de transações.
 * Segue Hexagonal Architecture e Adapter Pattern.
 */
@Component
@RequiredArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final TransactionRepository repository;

    @Override
    public Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Transaction> findByUsuarioId(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Transaction> findByUsuarioIdAndData(LocalDate data, Long usuarioId) {
        return repository.findByUsuarioIdAndData(usuarioId, data);
    }

    @Override
    public List<Transaction> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDate inicio, LocalDate fim) {
        return repository.findByUsuarioIdAndDataBetween(usuarioId, inicio, fim);
    }

    @Override
    public List<Transaction> findByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId) {
        return repository.findByUsuarioIdAndCategoriaId(usuarioId, categoriaId);
    }

    @Override
    public List<Transaction> findByUsuarioIdAndTipo(Long usuarioId, TipoTransacao tipo) {
        return repository.findByUsuarioIdAndTipo(usuarioId, tipo);
    }

    @Override
    public List<Transaction> findByUsuarioIdAndMetodoPagamento(Long usuarioId, MetodoPagamento metodo) {
        return repository.findByUsuarioIdAndMetodoPagamento(usuarioId, metodo);
    }

    @Override
    public List<Transaction> findByUsuarioIdAndValorBetween(Long usuarioId, BigDecimal minimo, BigDecimal maximo) {
        return repository.findByUsuarioIdAndValorBetween(usuarioId, minimo, maximo);
    }

    @Override
    public void delete(Transaction transaction) {
        repository.delete(transaction);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    // ---------- ESTATÍSTICAS -----------

    @Override
    public BigDecimal sumReceitas(Long usuarioId) {
        return repository.sumReceitas(usuarioId);
    }

    @Override
    public BigDecimal sumDespesas(Long usuarioId) {
        return repository.sumDespesas(usuarioId);
    }

    @Override
    public Long countByUsuarioId(Long usuarioId) {
        return repository.countByUsuarioId(usuarioId);
    }

    @Override
    public List<Object[]> findResumoPorCategoria(Long usuarioId) {
        return repository.findResumoPorCategoria(usuarioId);
    }

    @Override
    public List<Object[]> findResumoPorTipo(Long usuarioId) {
        return repository.findResumoPorTipo(usuarioId);
    }
}
