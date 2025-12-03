package com.personalFinance.personal_finance.investment.application.service;

import com.personalFinance.personal_finance.investment.api.dto.response.InvestmentResponseDTO;
import com.personalFinance.personal_finance.investment.api.mapper.InvestmentMapper;
import com.personalFinance.personal_finance.investment.domain.entity.Investment;
import com.personalFinance.personal_finance.investment.domain.entity.TipoInvestimento;
import com.personalFinance.personal_finance.investment.domain.exception.InvestmentNotFoundException;
import com.personalFinance.personal_finance.investment.domain.port.InvestmentRepositoryPort;
import com.personalFinance.personal_finance.investment.domain.service.InvestmentOwnershipService;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Application Service seguindo SRP: Responsável apenas por BUSCAR investimentos.
 * Garante que usuário só vê seus próprios investimentos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentFinder {

    private final InvestmentRepositoryPort investmentRepository;
    private final UserRepository userRepository;
    private final InvestmentOwnershipService ownershipService;
    private final InvestmentMapper investmentMapper;

    @Transactional(readOnly = true)
    public InvestmentResponseDTO findById(String keycloakId, Long investmentId) {
        log.info("Buscando investimento {} para usuário {}", investmentId, keycloakId);

        // 1. Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar investimento
        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new InvestmentNotFoundException(investmentId));

        // 3. Validar propriedade (Security: usuário só pode ver seus próprios investimentos)
        ownershipService.validateOwnership(investment, user);

        // 4. Retornar DTO
        return investmentMapper.toInvestmentResponseDTO(investment);
    }

    @Transactional(readOnly = true)
    public List<InvestmentResponseDTO> findAllByUser(String keycloakId) {
        log.info("Buscando todos investimentos do usuário {}", keycloakId);

        // 1. Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar investimentos (Security: busca apenas do usuário logado)
        List<Investment> investments = investmentRepository.findByUsuarioId(user.getId());

        // 3. Retornar DTOs
        return investmentMapper.toInvestmentResponseDTOList(investments);
    }

    @Transactional(readOnly = true)
    public List<InvestmentResponseDTO> findByUserAndAtivo(String keycloakId, Boolean ativo) {
        log.info("Buscando investimentos ativos={} do usuário {}", ativo, keycloakId);

        // 1. Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar investimentos (Security: busca apenas do usuário logado)
        List<Investment> investments = investmentRepository.findByUsuarioIdAndAtivo(user.getId(), ativo);

        // 3. Retornar DTOs
        return investmentMapper.toInvestmentResponseDTOList(investments);
    }

    @Transactional(readOnly = true)
    public List<InvestmentResponseDTO> findByUserAndTipo(String keycloakId, TipoInvestimento tipo) {
        log.info("Buscando investimentos tipo={} do usuário {}", tipo, keycloakId);

        // 1. Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar investimentos (Security: busca apenas do usuário logado)
        List<Investment> investments = investmentRepository.findByUsuarioIdAndTipoInvestimento(user.getId(), tipo);

        // 3. Retornar DTOs
        return investmentMapper.toInvestmentResponseDTOList(investments);
    }

    /**
     * FILTRO: Busca investimentos por corretora (busca parcial case-insensitive).
     */
    @Transactional(readOnly = true)
    public List<InvestmentResponseDTO> findByUserAndCorretora(String keycloakId, String corretora) {
        log.info("Buscando investimentos corretora={} do usuário {}", corretora, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Investment> investments = investmentRepository.findByUsuarioIdAndCorretora(user.getId(), corretora);
        return investmentMapper.toInvestmentResponseDTOList(investments);
    }

    /**
     * FILTRO: Busca investimentos por período de compra.
     */
    @Transactional(readOnly = true)
    public List<InvestmentResponseDTO> findByUserAndDataCompraBetween(String keycloakId, LocalDate dataInicio, LocalDate dataFim) {
        log.info("Buscando investimentos entre {} e {} do usuário {}", dataInicio, dataFim, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Investment> investments = investmentRepository.findByUsuarioIdAndDataCompraBetween(user.getId(), dataInicio, dataFim);
        return investmentMapper.toInvestmentResponseDTOList(investments);
    }

    /**
     * FILTRO: Busca investimentos por símbolo (busca parcial case-insensitive).
     */
    @Transactional(readOnly = true)
    public List<InvestmentResponseDTO> findByUserAndSimboloContaining(String keycloakId, String simbolo) {
        log.info("Buscando investimentos símbolo={} do usuário {}", simbolo, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Investment> investments = investmentRepository.findByUsuarioIdAndSimboloContaining(user.getId(), simbolo);
        return investmentMapper.toInvestmentResponseDTOList(investments);
    }

    /**
     * FILTRO/ORDENAÇÃO: Busca investimentos ordenados por valor investido (maior para menor).
     */
    @Transactional(readOnly = true)
    public List<InvestmentResponseDTO> findByUserOrderByValorDesc(String keycloakId) {
        log.info("Buscando investimentos ordenados por valor do usuário {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Investment> investments = investmentRepository.findByUsuarioIdOrderByValorTotalInvestidoDesc(user.getId());
        return investmentMapper.toInvestmentResponseDTOList(investments);
    }

    /**
     * FILTRO/ORDENAÇÃO: Busca investimentos ativos ordenados por data de compra (mais recentes primeiro).
     */
    @Transactional(readOnly = true)
    public List<InvestmentResponseDTO> findActiveOrderByDataCompraDesc(String keycloakId) {
        log.info("Buscando investimentos ativos ordenados por data do usuário {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Investment> investments = investmentRepository.findByUsuarioIdAndAtivoTrueOrderByDataCompraDesc(user.getId());
        return investmentMapper.toInvestmentResponseDTOList(investments);
    }

    /**
     * ESTATÍSTICA: Conta total de investimentos ativos do usuário.
     */
    @Transactional(readOnly = true)
    public Long countActiveInvestments(String keycloakId) {
        log.info("Contando investimentos ativos do usuário {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return investmentRepository.countByUsuarioIdAndAtivoTrue(user.getId());
    }

    /**
     * ESTATÍSTICA: Calcula valor total investido (soma de todos investimentos ativos).
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalInvested(String keycloakId) {
        log.info("Calculando valor total investido do usuário {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return investmentRepository.calculateTotalInvested(user.getId());
    }

    /**
     * ESTATÍSTICA: Retorna resumo de investimentos por tipo (quantidade, total investido).
     */
    @Transactional(readOnly = true)
    public List<Object[]> findInvestmentSummaryByTipo(String keycloakId) {
        log.info("Buscando resumo de investimentos por tipo do usuário {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return investmentRepository.findInvestmentSummaryByTipo(user.getId());
    }
}
