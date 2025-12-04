package com.personalFinance.personal_finance.transaction.application.service;

import com.personalFinance.personal_finance.transaction.api.dto.response.TransactionResponseDTO;
import com.personalFinance.personal_finance.transaction.api.mapper.TransactionMapper;
import com.personalFinance.personal_finance.transaction.domain.entity.Transaction;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;
import com.personalFinance.personal_finance.transaction.domain.port.TransactionRepositoryPort;
import com.personalFinance.personal_finance.transaction.domain.service.TransactionOwnershipService;

import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionFinder {

    private final TransactionRepositoryPort transactionRepository;
    private final UserRepository userRepository;
    private final TransactionOwnershipService ownershipService;
    private final TransactionMapper mapper;

    @Transactional(readOnly = true)
    public TransactionResponseDTO findById(String keycloakId, Long id) {
        log.info("Buscando transação {} do usuário {}", id, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));

        ownershipService.validateOwnership(tx, user);

        return mapper.toResponseDTO(tx);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> findAllByUser(String keycloakId) {
        log.info("Buscando todas transações do usuário {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Transaction> list = transactionRepository.findByUsuarioId(user.getId());
        return mapper.toResponseDTOList(list);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> findByDate(String keycloakId, LocalDate data) {
        log.info("Buscando transações do dia {} para {}", data, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Transaction> list = transactionRepository.findByUsuarioIdAndData(data, user.getId());
        return mapper.toResponseDTOList(list);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> findByPeriodo(String keycloakId, LocalDate inicio, LocalDate fim) {
        log.info("Buscando transações entre {} e {} para {}", inicio, fim, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Transaction> list = transactionRepository.findByUsuarioIdAndDataBetween(user.getId(), inicio, fim);
        return mapper.toResponseDTOList(list);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> findByTipo(String keycloakId, TipoTransacao tipo) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Transaction> list = transactionRepository.findByUsuarioIdAndTipo(user.getId(), tipo);
        return mapper.toResponseDTOList(list);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumReceitas(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return transactionRepository.sumReceitas(user.getId());
    }

    @Transactional(readOnly = true)
    public BigDecimal sumDespesas(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return transactionRepository.sumDespesas(user.getId());
    }
}