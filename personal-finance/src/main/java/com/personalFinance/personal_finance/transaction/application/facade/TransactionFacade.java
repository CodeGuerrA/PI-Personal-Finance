package com.personalFinance.personal_finance.transaction.application.facade;

import com.personalFinance.personal_finance.transaction.api.dto.request.TransactionCreateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.request.TransactionUpdateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.response.TransactionResponseDTO;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Facade Pattern para Transaction.
 * Interface simplificada para o Controller.
 */
public interface TransactionFacade {

    TransactionResponseDTO createTransaction(String keycloakId, TransactionCreateRequestDTO dto);

    TransactionResponseDTO findById(String keycloakId, Long id);

    List<TransactionResponseDTO> findAllByUser(String keycloakId);

    List<TransactionResponseDTO> findByDate(String keycloakId, LocalDate data);

    List<TransactionResponseDTO> findByPeriodo(String keycloakId, LocalDate inicio, LocalDate fim);

    List<TransactionResponseDTO> findByTipo(String keycloakId, TipoTransacao tipo);

    TransactionResponseDTO updateTransaction(String keycloakId, Long id, TransactionUpdateRequestDTO dto);

    void deleteTransaction(String keycloakId, Long id);

    // ---------- Estatísticas ----------

    BigDecimal sumReceitas(String keycloakId);

    BigDecimal sumDespesas(String keycloakId);
}