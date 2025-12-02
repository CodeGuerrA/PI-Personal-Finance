package com.personalFinance.personal_finance.objective.api.dto.response;

import com.personalFinance.personal_finance.objective.domain.entity.ObjectiveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectiveResponseDTO {

    private Long id;
    private Long categoriaId;
    private String categoriaNome;
    private String descricao;
    private BigDecimal valorObjetivo;
    private BigDecimal valorAtual;
    private BigDecimal percentualAtingido;
    private BigDecimal saldoRestante;
    private String mesAno;
    private ObjectiveType tipo;
    private Boolean ativa;
    private String statusAlerta; // NENHUM, AMARELO, VERMELHO, CUMPRIDA
}
