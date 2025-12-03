package com.personalFinance.personal_finance.investment.api.mapper;

import com.personalFinance.personal_finance.investment.api.dto.response.InvestmentResponseDTO;
import com.personalFinance.personal_finance.investment.domain.entity.Investment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão de Investment para DTOs.
 * Utiliza métodos do Rich Domain Model para cálculos (seguindo SRP).
 */
@Component
public class InvestmentMapper {

    /**
     * Converte Investment para InvestmentResponseDTO.
     * Utiliza métodos de cálculo do próprio Investment (Rich Domain Model).
     */
    public InvestmentResponseDTO toInvestmentResponseDTO(Investment investment) {
        return InvestmentResponseDTO.builder()
                .id(investment.getId())
                .usuarioId(investment.getUsuario().getId())
                .tipoInvestimento(investment.getTipoInvestimento())
                .nomeAtivo(investment.getNomeAtivo())
                .simbolo(investment.getSimbolo())
                .quantidade(investment.getQuantidade())
                .valorCompra(investment.getValorCompra())
                .valorTotalInvestido(investment.getValorTotalInvestido())
                .dataCompra(investment.getDataCompra())
                .corretora(investment.getCorretora())
                .observacoes(investment.getObservacoes())
                .ativo(investment.getAtivo())
                .cotacaoAtual(investment.getCotacaoAtual())
                .valorAtual(investment.calcularValorAtual())
                .lucro(investment.calcularLucro())
                .rentabilidade(investment.calcularRentabilidade())
                .build();
    }

    /**
     * Converte lista de Investment para lista de InvestmentResponseDTO.
     */
    public List<InvestmentResponseDTO> toInvestmentResponseDTOList(List<Investment> investments) {
        return investments.stream()
                .map(this::toInvestmentResponseDTO)
                .collect(Collectors.toList());
    }
}
