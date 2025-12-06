package com.personalFinance.personal_finance.transaction.application.facade;

import com.personalFinance.personal_finance.transaction.api.dto.request.TransactionCreateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.request.TransactionUpdateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.response.TransactionResponseDTO;

import com.personalFinance.personal_finance.transaction.application.service.TransactionCreator;
import com.personalFinance.personal_finance.transaction.application.service.TransactionDeleter;
import com.personalFinance.personal_finance.transaction.application.service.TransactionFinder;
import com.personalFinance.personal_finance.transaction.application.service.TransactionUpdater;

import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementação do Facade de Transaction.
 * Delegação direta para os Application Services.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {

    private final TransactionCreator creator;
    private final TransactionFinder finder;
    private final TransactionUpdater updater;
    private final TransactionDeleter deleter;

    @Override
    public TransactionResponseDTO createTransaction(String keycloakId, TransactionCreateRequestDTO dto) {
        log.info("Facade: Criando transação para usuário {}", keycloakId);
        return creator.create(keycloakId, dto);
    }

    @Override
    public TransactionResponseDTO findById(String keycloakId, Long id) {
        log.info("Facade: Buscando transação {} do usuário {}", id, keycloakId);
        return finder.findById(keycloakId, id);
    }

    @Override
    public List<TransactionResponseDTO> findAllByUser(String keycloakId) {
        log.info("Facade: Buscando todas transações do usuário {}", keycloakId);
        return finder.findAllByUser(keycloakId);
    }

    @Override
    public List<TransactionResponseDTO> findByDate(String keycloakId, LocalDate data) {
        log.info("Facade: Buscando transações do dia {} do usuário {}", data, keycloakId);
        return finder.findByDate(keycloakId, data);
    }

    @Override
    public List<TransactionResponseDTO> findByPeriodo(String keycloakId, LocalDate inicio, LocalDate fim) {
        log.info("Facade: Buscando transações do período {} até {} para usuário {}", inicio, fim, keycloakId);
        return finder.findByPeriodo(keycloakId, inicio, fim);
    }

    @Override
    public List<TransactionResponseDTO> findByTipo(String keycloakId, TipoTransacao tipo) {
        log.info("Facade: Buscando transações tipo={} do usuário {}", tipo, keycloakId);
        return finder.findByTipo(keycloakId, tipo);
    }

    @Override
    public TransactionResponseDTO updateTransaction(String keycloakId, Long id, TransactionUpdateRequestDTO dto) {
        log.info("Facade: Atualizando transação {} do usuário {}", id, keycloakId);
        return updater.update(keycloakId, id, dto);
    }

    @Override
    public void deleteTransaction(String keycloakId, Long id) {
        log.info("Facade: Deletando transação {} do usuário {}", id, keycloakId);
        deleter.delete(keycloakId, id);
    }

    @Override
    public BigDecimal sumReceitas(String keycloakId) {
        log.info("Facade: Calculando total de receitas do usuário {}", keycloakId);
        return finder.sumReceitas(keycloakId);
    }

    @Override
    public BigDecimal sumDespesas(String keycloakId) {
        log.info("Facade: Calculando total de despesas do usuário {}", keycloakId);
        return finder.sumDespesas(keycloakId);
    }
}