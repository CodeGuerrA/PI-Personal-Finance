package com.personalFinance.personal_finance.investment.application.service;

import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentCreateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.response.InvestmentResponseDTO;
import com.personalFinance.personal_finance.investment.api.mapper.InvestmentMapper;
import com.personalFinance.personal_finance.investment.domain.entity.Investment;
import com.personalFinance.personal_finance.investment.domain.port.InvestmentRepositoryPort;
import com.personalFinance.personal_finance.investment.domain.service.InvestmentValidationService;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service seguindo SRP: Responsável apenas por CRIAR investimentos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentCreator {

    private final InvestmentRepositoryPort investmentRepository;
    private final UserRepository userRepository;
    private final InvestmentValidationService validationService;
    private final InvestmentMapper investmentMapper;

    @Transactional
    public InvestmentResponseDTO create(String keycloakId, InvestmentCreateRequestDTO dto) {
        log.info("Criando investimento para usuário: {}", keycloakId);

        // 1. Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Validar dados usando Domain Service (SRP)
        validationService.validateCreation(
                dto.getNomeAtivo(),
                dto.getSimbolo(),
                dto.getQuantidade(),
                dto.getValorCompra(),
                dto.getValorTotalInvestido(),
                dto.getDataCompra()
        );

        // 3. Criar entidade de domínio usando Factory Method
        Investment investment = Investment.create(
                user,
                dto.getTipoInvestimento(),
                dto.getNomeAtivo(),
                dto.getSimbolo(),
                dto.getQuantidade(),
                dto.getValorCompra(),
                dto.getValorTotalInvestido(),
                dto.getDataCompra(),
                dto.getCorretora(),
                dto.getObservacoes()
        );

        // 4. Salvar
        Investment savedInvestment = investmentRepository.save(investment);
        log.info("Investimento criado com sucesso. ID: {}", savedInvestment.getId());

        // 5. Retornar DTO
        return investmentMapper.toInvestmentResponseDTO(savedInvestment);
    }
}
