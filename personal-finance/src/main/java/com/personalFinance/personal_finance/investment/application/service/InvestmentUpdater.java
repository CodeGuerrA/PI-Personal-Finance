package com.personalFinance.personal_finance.investment.application.service;

import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentUpdateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.response.InvestmentResponseDTO;
import com.personalFinance.personal_finance.investment.api.mapper.InvestmentMapper;
import com.personalFinance.personal_finance.investment.domain.entity.Investment;
import com.personalFinance.personal_finance.investment.domain.exception.InvestmentNotFoundException;
import com.personalFinance.personal_finance.investment.domain.port.InvestmentRepositoryPort;
import com.personalFinance.personal_finance.investment.domain.service.InvestmentOwnershipService;
import com.personalFinance.personal_finance.investment.domain.service.InvestmentValidationService;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service seguindo SRP: Responsável apenas por ATUALIZAR investimentos.
 * Garante que usuário só atualiza seus próprios investimentos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentUpdater {

    private final InvestmentRepositoryPort investmentRepository;
    private final UserRepository userRepository;
    private final InvestmentOwnershipService ownershipService;
    private final InvestmentValidationService validationService;
    private final InvestmentMapper investmentMapper;

    @Transactional
    public InvestmentResponseDTO update(String keycloakId, Long investmentId, InvestmentUpdateRequestDTO dto) {
        log.info("Atualizando investimento {} para usuário {}", investmentId, keycloakId);

        // 1. Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Buscar investimento
        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new InvestmentNotFoundException(investmentId));

        // 3. Validar propriedade (Security: usuário só pode atualizar seus próprios investimentos)
        ownershipService.validateOwnership(investment, user);

        // 4. Validar dados de atualização
        validationService.validateUpdate(
                dto.getQuantidade(),
                dto.getValorCompra(),
                dto.getValorTotalInvestido()
        );

        // 5. Atualizar usando métodos do Rich Domain Model
        if (dto.getQuantidade() != null) {
            investment.atualizarQuantidade(dto.getQuantidade());
        }
        if (dto.getValorCompra() != null) {
            investment.atualizarValorCompra(dto.getValorCompra());
        }
        if (dto.getValorTotalInvestido() != null) {
            investment.atualizarValorTotalInvestido(dto.getValorTotalInvestido());
        }
        if (dto.getObservacoes() != null) {
            investment.atualizarObservacoes(dto.getObservacoes());
        }
        if (dto.getAtivo() != null) {
            if (dto.getAtivo()) {
                investment.reativar();
            } else {
                investment.desativar();
            }
        }

        // 6. Salvar
        Investment updatedInvestment = investmentRepository.save(investment);
        log.info("Investimento {} atualizado com sucesso", investmentId);

        // 7. Retornar DTO
        return investmentMapper.toInvestmentResponseDTO(updatedInvestment);
    }
}
