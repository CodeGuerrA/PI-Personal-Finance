package com.personalFinance.personal_finance.transaction.application.facade;

import com.personalFinance.personal_finance.transaction.api.dto.request.RecurringTransactionCreateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.request.RecurringTransactionUpdateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.response.RecurringTransactionResponseDTO;

import com.personalFinance.personal_finance.transaction.domain.entity.FrequenciaRecorrencia;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecurringTransactionFacadeImpl implements RecurringTransactionFacade {

    private final com.personalFinance.personal_finance.transaction.application.service.RecurringTransactionCreator creator;
    private final com.personalFinance.personal_finance.transaction.application.service.RecurringTransactionFinder finder;
    private final com.personalFinance.personal_finance.transaction.application.service.RecurringTransactionUpdater updater;
    private final com.personalFinance.personal_finance.transaction.application.service.RecurringTransactionDeleter deleter;

    @Override
    public RecurringTransactionResponseDTO create(String keycloakId, RecurringTransactionCreateRequestDTO dto) {
        return creator.create(keycloakId, dto);
    }

    @Override
    public RecurringTransactionResponseDTO findById(String keycloakId, Long id) {
        return finder.findById(keycloakId, id);
    }

    @Override
    public List<RecurringTransactionResponseDTO> findAllByUser(String keycloakId) {
        return finder.findAllByUser(keycloakId);
    }

    @Override
    public List<RecurringTransactionResponseDTO> findByFrequencia(String keycloakId, FrequenciaRecorrencia frequencia) {
        return finder.findByFrequencia(keycloakId, frequencia);
    }

    @Override
    public List<RecurringTransactionResponseDTO> findByCategoria(String keycloakId, Long categoriaId) {
        return finder.findByCategoria(keycloakId, categoriaId);
    }

    @Override
    public List<RecurringTransactionResponseDTO> findByTipo(String keycloakId, TipoTransacao tipo) {
        return finder.findByTipo(keycloakId, tipo);
    }

    @Override
    public List<RecurringTransactionResponseDTO> findAtivas(String keycloakId) {
        return finder.findAtivas(keycloakId);
    }

    @Override
    public RecurringTransactionResponseDTO update(String keycloakId, Long id, RecurringTransactionUpdateRequestDTO dto) {
        return updater.update(keycloakId, id, dto);
    }

    @Override
    public void delete(String keycloakId, Long id) {
        deleter.delete(keycloakId, id);
    }
}