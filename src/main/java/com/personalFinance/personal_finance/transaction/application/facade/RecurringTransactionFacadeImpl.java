package com.personalFinance.personal_finance.transaction.application.facade;

import com.personalFinance.personal_finance.transaction.api.dto.request.RecurringTransactionCreateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.request.RecurringTransactionUpdateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.response.RecurringTransactionResponseDTO;
import com.personalFinance.personal_finance.transaction.application.service.RecurringTransactionCreator;
import com.personalFinance.personal_finance.transaction.application.service.RecurringTransactionDeleter;
import com.personalFinance.personal_finance.transaction.application.service.RecurringTransactionFinder;
import com.personalFinance.personal_finance.transaction.application.service.RecurringTransactionUpdater;

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

    private final RecurringTransactionCreator creator;
    private final RecurringTransactionFinder finder;
    private final RecurringTransactionUpdater updater;
    private final RecurringTransactionDeleter deleter;

    @Override
    public RecurringTransactionResponseDTO create(String keycloakId, RecurringTransactionCreateRequestDTO dto) {
        log.info("Facade: Criando transação recorrente para usuário {}", keycloakId);
        return creator.create(keycloakId, dto);
    }

    @Override
    public RecurringTransactionResponseDTO findById(String keycloakId, Long id) {
        log.info("Facade: Buscando transação recorrente {} para usuário {}", id, keycloakId);
        return finder.findById(keycloakId, id);
    }

    @Override
    public List<RecurringTransactionResponseDTO> findAllByUser(String keycloakId) {
        log.info("Facade: Buscando todas transações recorrentes do usuário {}", keycloakId);
        return finder.findAllByUser(keycloakId);
    }

    @Override
    public List<RecurringTransactionResponseDTO> findByFrequencia(String keycloakId, FrequenciaRecorrencia frequencia) {
        log.info("Facade: Buscando transações recorrentes com frequência {} do usuário {}", frequencia, keycloakId);
        return finder.findByFrequencia(keycloakId, frequencia);
    }

    @Override
    public List<RecurringTransactionResponseDTO> findByCategoria(String keycloakId, Long categoriaId) {
        log.info("Facade: Buscando transações recorrentes da categoria {} do usuário {}", categoriaId, keycloakId);
        return finder.findByCategoria(keycloakId, categoriaId);
    }

    @Override
    public List<RecurringTransactionResponseDTO> findByTipo(String keycloakId, TipoTransacao tipo) {
        log.info("Facade: Buscando transações recorrentes do tipo {} do usuário {}", tipo, keycloakId);
        return finder.findByTipo(keycloakId, tipo);
    }

    @Override
    public List<RecurringTransactionResponseDTO> findAtivas(String keycloakId) {
        log.info("Facade: Buscando transações recorrentes ativas do usuário {}", keycloakId);
        return finder.findAtivas(keycloakId);
    }

    @Override
    public RecurringTransactionResponseDTO update(String keycloakId, Long id, RecurringTransactionUpdateRequestDTO dto) {
        log.info("Facade: Atualizando transação recorrente {} do usuário {}", id, keycloakId);
        return updater.update(keycloakId, id, dto);
    }

    @Override
    public void delete(String keycloakId, Long id) {
        log.info("Facade: Deletando transação recorrente {} do usuário {}", id, keycloakId);
        deleter.delete(keycloakId, id);
    }
}