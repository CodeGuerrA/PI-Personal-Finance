package com.personalFinance.personal_finance.investment.api.dto.response;

import com.personalFinance.personal_finance.investment.domain.entity.TipoMovimentacao;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta para movimentação de investimento.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentMovementResponseDTO {

    private Long id;
    private Long investmentId;
    private String investmentNomeAtivo;
    private String investmentSimbolo;
    private TipoMovimentacao tipoMovimentacao;
    private String tipoMovimentacaoDescricao;
    private BigDecimal quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
    private BigDecimal taxas;
    private LocalDate dataMovimentacao;
    private String observacoes;
}
