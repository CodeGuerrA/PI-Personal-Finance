package com.personalFinance.personal_finance.transaction.domain.port;

import com.personalFinance.personal_finance.transaction.domain.entity.Transaction;
import com.personalFinance.personal_finance.transaction.domain.entity.MetodoPagamento;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Port (interface) para repositório de transações.
 * Segue Hexagonal Architecture e DIP.
 */
public interface TransactionRepositoryPort {

    /**
     * Salva uma transação.
     */
    Transaction save(Transaction transaction);

    /**
     * Busca por ID.
     */
    Optional<Transaction> findById(Long id);

    /**
     * Busca transações de um usuário.
     */
    List<Transaction> findByUsuarioId(Long usuarioId);

    /**
     * Busca transações por data.
     */
    List<Transaction> findByUsuarioIdAndData(LocalDate data, Long usuarioId);

    /**
     * Busca por intervalo de datas.
     */
    List<Transaction> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDate inicio, LocalDate fim);

    /**
     * Busca por categoria.
     */
    List<Transaction> findByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId);

    /**
     * Busca por tipo (RECEITA / DESPESA).
     */
    List<Transaction> findByUsuarioIdAndTipo(Long usuarioId, TipoTransacao tipo);

    /**
     * Busca por método de pagamento.
     */
    List<Transaction> findByUsuarioIdAndMetodoPagamento(Long usuarioId, MetodoPagamento metodo);

    /**
     * Busca transações por valor mínimo/máximo.
     */
    List<Transaction> findByUsuarioIdAndValorBetween(Long usuarioId, BigDecimal minimo, BigDecimal maximo);

    /**
     * Delete.
     */
    void delete(Transaction transaction);

    /**
     * Verifica se existe uma transação com ID.
     */
    boolean existsById(Long id);

    // ----------- ESTATÍSTICAS / AGREGAÇÕES -----------

    /**
     * Soma total de receitas do usuário.
     */
    BigDecimal sumReceitas(Long usuarioId);

    /**
     * Soma total de despesas do usuário.
     */
    BigDecimal sumDespesas(Long usuarioId);

    /**
     * Quantidade de transações do usuário.
     */
    Long countByUsuarioId(Long usuarioId);

    /**
     * Retorna resumo agrupado por categoria:
     * [categoria_id, total]
     */
    List<Object[]> findResumoPorCategoria(Long usuarioId);

    /**
     * Retorna resumo por tipo:
     * [tipo, total]
     */
    List<Object[]> findResumoPorTipo(Long usuarioId);
}