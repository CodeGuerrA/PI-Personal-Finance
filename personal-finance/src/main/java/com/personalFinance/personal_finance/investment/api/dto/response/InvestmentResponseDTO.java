package com.personalFinance.personal_finance.investment.api.dto.response;

import com.personalFinance.personal_finance.investment.domain.entity.TipoInvestimento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resposta para investimento.
 * Inclui todos os dados do investimento e cálculos (valor atual, lucro, rentabilidade).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentResponseDTO {

    private Long id;
    private Long usuarioId;
    private TipoInvestimento tipoInvestimento;
    private String nomeAtivo;
    private String simbolo;
    private BigDecimal quantidade;
    private BigDecimal valorCompra;
    private BigDecimal valorTotalInvestido;
    private LocalDate dataCompra;
    private String corretora;
    private String observacoes;
    private Boolean ativo;

    // Campos calculados
    private BigDecimal cotacaoAtual;
    private BigDecimal valorAtual;
    private BigDecimal lucro;
    private BigDecimal rentabilidade;
}
