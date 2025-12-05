package com.personalFinance.personal_finance.investment.api.mapper;

import com.personalFinance.personal_finance.investment.api.dto.response.InvestmentMovementResponseDTO;
import com.personalFinance.personal_finance.investment.domain.entity.InvestmentMovement;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter InvestmentMovement entre camadas.
 * Responsabilidade: transformação de dados (SRP).
 */
@Component
public class InvestmentMovementMapper {

    /**
     * Converte InvestmentMovement para DTO de resposta.
     */
    public InvestmentMovementResponseDTO toResponseDTO(InvestmentMovement movement) {
        if (movement == null) {
            return null;
        }

        return InvestmentMovementResponseDTO.builder()
                .id(movement.getId())
                .investmentId(movement.getInvestment().getId())
                .investmentNomeAtivo(movement.getInvestment().getNomeAtivo())
                .investmentSimbolo(movement.getInvestment().getSimbolo())
                .tipoMovimentacao(movement.getTipoMovimentacao())
                .tipoMovimentacaoDescricao(movement.getTipoMovimentacao().getDescricao())
                .quantidade(movement.getQuantidade())
                .valorUnitario(movement.getValorUnitario())
                .valorTotal(movement.getValorTotal())
                .taxas(movement.getTaxas())
                .dataMovimentacao(movement.getDataMovimentacao())
                .observacoes(movement.getObservacoes())
                .build();
    }
}
