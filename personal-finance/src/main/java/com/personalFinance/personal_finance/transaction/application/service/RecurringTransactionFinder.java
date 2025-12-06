package com.personalFinance.personal_finance.transaction.application.service;

import com.personalFinance.personal_finance.transaction.api.dto.response.RecurringTransactionResponseDTO;
import com.personalFinance.personal_finance.transaction.api.mapper.RecurringTransactionMapper;
import com.personalFinance.personal_finance.transaction.domain.entity.FrequenciaRecorrencia;
import com.personalFinance.personal_finance.transaction.domain.entity.RecurringTransaction;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;
import com.personalFinance.personal_finance.transaction.domain.port.RecurringTransactionRepositoryPort;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecurringTransactionFinder {

    private final RecurringTransactionRepositoryPort recurringTransactionRepository;
    private final UserRepository userRepository;
    private final RecurringTransactionMapper mapper;

    @Transactional(readOnly = true)
    public RecurringTransactionResponseDTO findById(String keycloakId, Long id) {

        log.info("Buscando transação recorrente {} para usuário {}", id, keycloakId);

        // Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Buscar transação recorrente
        RecurringTransaction recurringTransaction = recurringTransactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação recorrente não encontrada"));

        // Validar que pertence ao usuário
        if (!recurringTransaction.belongsToUser(user.getId())) {
            throw new IllegalArgumentException("Transação recorrente não pertence ao usuário");
        }

        log.info("Transação recorrente {} encontrada com sucesso", id);

        return mapper.toResponseDTO(recurringTransaction);
    }

    @Transactional(readOnly = true)
    public List<RecurringTransactionResponseDTO> findAllByUser(String keycloakId) {

        log.info("Buscando todas as transações recorrentes do usuário {}", keycloakId);

        // Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Buscar todas as transações recorrentes
        List<RecurringTransaction> recurringTransactions =
                recurringTransactionRepository.findByUsuarioId(user.getId());

        log.info("Encontradas {} transações recorrentes para o usuário {}",
                recurringTransactions.size(), keycloakId);

        return mapper.toResponseDTOList(recurringTransactions);
    }

    @Transactional(readOnly = true)
    public List<RecurringTransactionResponseDTO> findByFrequencia(
            String keycloakId,
            FrequenciaRecorrencia frequencia) {

        log.info("Buscando transações recorrentes por frequência {} para usuário {}",
                frequencia, keycloakId);

        // Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Buscar transações recorrentes por frequência
        List<RecurringTransaction> recurringTransactions =
                recurringTransactionRepository.findByUsuarioIdAndFrequencia(user.getId(), frequencia);

        log.info("Encontradas {} transações recorrentes com frequência {} para o usuário {}",
                recurringTransactions.size(), frequencia, keycloakId);

        return mapper.toResponseDTOList(recurringTransactions);
    }

    @Transactional(readOnly = true)
    public List<RecurringTransactionResponseDTO> findByCategoria(
            String keycloakId,
            Long categoriaId) {

        log.info("Buscando transações recorrentes por categoria {} para usuário {}",
                categoriaId, keycloakId);

        // Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Buscar transações recorrentes por categoria
        List<RecurringTransaction> recurringTransactions =
                recurringTransactionRepository.findByUsuarioIdAndCategoriaId(user.getId(), categoriaId);

        log.info("Encontradas {} transações recorrentes na categoria {} para o usuário {}",
                recurringTransactions.size(), categoriaId, keycloakId);

        return mapper.toResponseDTOList(recurringTransactions);
    }

    @Transactional(readOnly = true)
    public List<RecurringTransactionResponseDTO> findByTipo(
            String keycloakId,
            TipoTransacao tipo) {

        log.info("Buscando transações recorrentes por tipo {} para usuário {}",
                tipo, keycloakId);

        // Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Buscar transações recorrentes por tipo
        List<RecurringTransaction> recurringTransactions =
                recurringTransactionRepository.findByUsuarioIdAndTipo(user.getId(), tipo);

        log.info("Encontradas {} transações recorrentes do tipo {} para o usuário {}",
                recurringTransactions.size(), tipo, keycloakId);

        return mapper.toResponseDTOList(recurringTransactions);
    }

    @Transactional(readOnly = true)
    public List<RecurringTransactionResponseDTO> findAtivas(String keycloakId) {

        log.info("Buscando transações recorrentes ativas para usuário {}", keycloakId);

        // Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Buscar transações recorrentes ativas (sem data fim)
        List<RecurringTransaction> recurringTransactions =
                recurringTransactionRepository.findByUsuarioIdAndDataFimIsNull(user.getId());

        log.info("Encontradas {} transações recorrentes ativas para o usuário {}",
                recurringTransactions.size(), keycloakId);

        return mapper.toResponseDTOList(recurringTransactions);
    }
}
