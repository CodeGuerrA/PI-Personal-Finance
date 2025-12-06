package com.personalFinance.personal_finance.transaction.api.mapper;

import com.personalFinance.personal_finance.transaction.api.dto.request.TransactionCreateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.request.TransactionUpdateRequestDTO;
import com.personalFinance.personal_finance.transaction.api.dto.response.TransactionResponseDTO;
import com.personalFinance.personal_finance.transaction.domain.entity.Transaction;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.category.domain.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão entre Transaction e DTOs.
 * Observação: para criar Transaction no domínio usamos o factory Transaction.create(...)
 * que exige User e Category — portanto este mapper oferece um método overload que
 * recebe esses relacionamentos já resolvidos.
 */
@Component
public class TransactionMapper {

    // --- ENTITY -> RESPONSE DTO ---
    public TransactionResponseDTO toResponseDTO(Transaction tx) {
        return TransactionResponseDTO.builder()
                .id(tx.getId())
                .usuarioId(tx.getUsuario() != null ? tx.getUsuario().getId() : null)
                .valor(tx.getValor())
                .tipo(tx.getTipo())
                .metodoPagamento(tx.getMetodoPagamento())
                .categoriaId(tx.getCategoria() != null ? tx.getCategoria().getId() : null)
                .descricao(tx.getDescricao())
                .data(tx.getData())
                .observacoes(tx.getObservacoes())
                .transacaoRecorrenteId(tx.getTransacaoRecorrente() != null ? tx.getTransacaoRecorrente().getId() : null)
                .investimentoId(tx.getInvestimento() != null ? tx.getInvestimento().getId() : null)
                .dataCriacao(tx.getDataCriacao())
                .build();
    }

    public List<TransactionResponseDTO> toResponseDTOList(List<Transaction> list) {
        return list.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // --- DTO -> ENTITY ---
    // NÃO usa builder público; usa factory create que exige User e Category.
    public Transaction toEntity(TransactionCreateRequestDTO dto, User usuario, Category categoria) {
        // chama factory do domínio (assumindo que Transaction.create existe e aceita esses parâmetros)
        return Transaction.create(
                usuario,
                categoria,
                dto.getValor(),
                dto.getData(),
                dto.getDescricao(),
                dto.getTipo(),
                dto.getMetodoPagamento(),
                dto.getObservacoes()
        );
    }

    // Versão para update: não cria entidade nova — o service deve buscar a entidade e aplicar updates.
    // Aqui apenas disponibilizamos um helper opcional que copia valores (não obrigatório)
    public void applyUpdate(Transaction tx, TransactionUpdateRequestDTO dto) {
        if (dto.getValor() != null) tx.updateValor(dto.getValor());
        if (dto.getData() != null) tx.updateData(dto.getData());
        if (dto.getDescricao() != null) tx.updateDescricao(dto.getDescricao());
        if (dto.getTipo() != null) tx.updateTipo(dto.getTipo());
        if (dto.getMetodoPagamento() != null) tx.updateMetodoPagamento(dto.getMetodoPagamento());
        if (dto.getObservacoes() != null) tx.updateObservacoes(dto.getObservacoes());
        // categoria: se vier categoriaId, o service deve buscar Category e usar tx.updateCategoria(...)
    }
}
