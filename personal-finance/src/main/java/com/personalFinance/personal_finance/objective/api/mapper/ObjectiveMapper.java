package com.personalFinance.personal_finance.objective.api.mapper;

import com.personalFinance.personal_finance.objective.api.dto.response.ObjectiveResponseDTO;
import com.personalFinance.personal_finance.objective.domain.entity.Objective;
import com.personalFinance.personal_finance.objective.domain.service.ObjectiveAlertService;
import com.personalFinance.personal_finance.objective.domain.service.ObjectiveCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão de Objective para DTOs.
 * Usa Domain Services para cálculos e alertas (seguindo SRP).
 */
@Component
@RequiredArgsConstructor
public class ObjectiveMapper {

    private final ObjectiveCalculationService calculationService;
    private final ObjectiveAlertService alertService;

    public ObjectiveResponseDTO toObjectiveResponseDTO(Objective objective, String categoriaNome) {
        return ObjectiveResponseDTO.builder()
                .id(objective.getId())
                .categoriaId(objective.getCategoriaId())
                .categoriaNome(categoriaNome)
                .descricao(objective.getDescricao())
                .valorObjetivo(objective.getValorObjetivo())
                .valorAtual(objective.getValorAtual())
                .percentualAtingido(calculationService.calculatePercentualAtingido(objective))
                .saldoRestante(calculationService.calculateSaldoRestante(objective))
                .mesAno(objective.getMesAno())
                .tipo(objective.getTipo())
                .ativa(objective.getAtiva())
                .statusAlerta(alertService.determineAlertStatus(objective))
                .build();
    }

    public List<ObjectiveResponseDTO> toObjectiveResponseDTOList(
            List<Objective> objectives,
            java.util.function.Function<Long, String> categoriaNomeProvider) {
        return objectives.stream()
                .map(obj -> {
                    String categoriaNome = obj.getCategoriaId() != null
                            ? categoriaNomeProvider.apply(obj.getCategoriaId())
                            : null;
                    return toObjectiveResponseDTO(obj, categoriaNome);
                })
                .collect(Collectors.toList());
    }
}
