package com.personalFinance.personal_finance.transaction.api.mapper;

import com.personalFinance.personal_finance.transaction.api.dto.request.RecurringTransactionCreateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.request.RecurringTransactionUpdateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.response.RecurringTransactionResponseDTO;
import com.personalFinance.personal_finance.transaction.domain.entity.RecurringTransaction;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.category.domain.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão entre RecurringTransaction e DTOs.
 */
@Component
public class RecurringTransactionMapper {

    // --- ENTITY -> RESPONSE DTO ---
    public RecurringTransactionResponseDTO toResponseDTO(RecurringTransaction rt) {
        return RecurringTransactionResponseDTO.builder()
                .id(rt.getId())
                .usuarioId(rt.getUsuario() != null ? rt.getUsuario().getId() : null)
                .valor(rt.getValor())
                .tipo(rt.getTipo())
                .categoriaId(rt.getCategoria() != null ? rt.getCategoria().getId() : null)
                .descricao(rt.getDescricao())
                .dataInicio(rt.getDataInicio())
                .dataFim(rt.getDataFim())
                .frequencia(rt.getFrequencia())
                .diaVencimento(rt.getDiaVencimento())
                .proximaData(rt.getProximaData())
                .ativa(rt.getAtiva())
                .observacoes(rt.getObservacoes())
                .dataCriacao(rt.getDataCriacao())
                .build();
    }

    public List<RecurringTransactionResponseDTO> toResponseDTOList(List<RecurringTransaction> list) {
        return list.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // --- DTO -> ENTITY ---
    public RecurringTransaction toEntity(RecurringTransactionCreateRequestDTO dto, User usuario, Category categoria) {
        return RecurringTransaction.create(
                usuario,
                categoria,
                dto.getValor(),
                dto.getDescricao(),
                dto.getDataInicio(),
                dto.getFrequencia(),
                dto.getTipo(),
                dto.getDiaVencimento(),
                dto.getObservacoes()
        );
    }

    // --- UPDATE ---
    public void applyUpdate(RecurringTransaction rt, RecurringTransactionUpdateRequestDTO dto) {
        if (dto.getValor() != null) rt.updateValor(dto.getValor());
        if (dto.getDescricao() != null) rt.updateDescricao(dto.getDescricao());
        if (dto.getFrequencia() != null) rt.updateFrequencia(dto.getFrequencia());
        if (dto.getDataInicio() != null) rt.updateDataInicio(dto.getDataInicio());
        if (dto.getDataFim() != null) rt.updateDataFim(dto.getDataFim());
        if (dto.getDiaVencimento() != null) rt.updateDiaVencimento(dto.getDiaVencimento());
        if (dto.getObservacoes() != null) rt.updateObservacoes(dto.getObservacoes());
        if (dto.getAtiva() != null) {
            if (dto.getAtiva()) {
                rt.ativar();
            } else {
                rt.desativar();
            }
        }
    }
}
