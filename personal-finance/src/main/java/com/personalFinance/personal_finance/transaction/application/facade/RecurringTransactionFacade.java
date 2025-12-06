package com.personalFinance.personal_finance.transaction.application.facade;

import com.personalFinance.personal_finance.transaction.api.dto.request.RecurringTransactionCreateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.request.RecurringTransactionUpdateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.response.RecurringTransactionResponseDTO;
import com.personalFinance.personal_finance.transaction.domain.entity.FrequenciaRecorrencia;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import java.util.List;

public interface RecurringTransactionFacade {

    RecurringTransactionResponseDTO create(String keycloakId, RecurringTransactionCreateRequestDTO dto);

    RecurringTransactionResponseDTO findById(String keycloakId, Long id);

    List<RecurringTransactionResponseDTO> findAllByUser(String keycloakId);

    List<RecurringTransactionResponseDTO> findByFrequencia(String keycloakId, FrequenciaRecorrencia frequencia);

    List<RecurringTransactionResponseDTO> findByCategoria(String keycloakId, Long categoriaId);

    List<RecurringTransactionResponseDTO> findByTipo(String keycloakId, TipoTransacao tipo);

    List<RecurringTransactionResponseDTO> findAtivas(String keycloakId);

    RecurringTransactionResponseDTO update(String keycloakId, Long id, RecurringTransactionUpdateRequestDTO dto);

    void delete(String keycloakId, Long id);
}