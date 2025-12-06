package com.personalFinance.personal_finance.investment.application.service;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.entity.CategoryType;
import com.personalFinance.personal_finance.category.domain.port.CategoryRepositoryPort;
import com.personalFinance.personal_finance.investment.domain.entity.InvestmentMovement;
import com.personalFinance.personal_finance.transaction.domain.entity.MetodoPagamento;
import com.personalFinance.personal_finance.transaction.domain.entity.TipoTransacao;
import com.personalFinance.personal_finance.transaction.domain.entity.Transaction;
import com.personalFinance.personal_finance.transaction.domain.port.TransactionRepositoryPort;
import com.personalFinance.personal_finance.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Serviço de integração entre movimentações de investimento e transações.
 * Responsabilidade: criar transações automaticamente quando movimentações são criadas.
 *
 * Regras de negócio:
 * - COMPRA → Transação tipo DESPESA
 * - VENDA → Transação tipo RECEITA
 * - DIVIDENDO → Transação tipo RECEITA
 * - RENDIMENTO → Transação tipo RECEITA
 * - AJUSTE → Não gera transação
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentMovementTransactionIntegrationService {

    private final TransactionRepositoryPort transactionRepository;
    private final CategoryRepositoryPort categoryRepository;

    private static final String CATEGORY_INVESTIMENTOS = "Investimentos";

    /**
     * Cria transação automaticamente a partir de uma movimentação de investimento.
     */
    @Transactional
    public void createTransactionFromMovement(InvestmentMovement movement, User usuario) {
        // AJUSTE não gera transação
        if (movement.getTipoMovimentacao().name().equals("AJUSTE")) {
            log.info("Movimentação tipo AJUSTE não gera transação. ID: {}", movement.getId());
            return;
        }

        // Determinar tipo de transação
        TipoTransacao tipoTransacao = movement.geraDespesa() ? TipoTransacao.DESPESA : TipoTransacao.RECEITA;

        // Buscar ou criar categoria de investimentos
        Category categoria = getOrCreateInvestmentCategory(usuario, tipoTransacao);

        // Calcular valor total (incluindo taxas se for despesa)
        BigDecimal valorTotal = movement.getValorTotal();
        if (movement.geraDespesa() && movement.getTaxas() != null) {
            valorTotal = valorTotal.add(movement.getTaxas());
        }

        // Criar descrição
        String descricao = buildTransactionDescription(movement);

        // Criar transação
        Transaction transaction = Transaction.create(
                usuario,
                categoria,
                valorTotal,
                movement.getDataMovimentacao(),
                descricao,
                tipoTransacao,
                MetodoPagamento.PIX, // Padrão para investimentos
                movement.getObservacoes()
        );

        // Vincular investimento à transação
        transaction.linkInvestment(movement.getInvestment());

        // Salvar transação
        transactionRepository.save(transaction);

        log.info("Transação criada automaticamente. Tipo: {}, Valor: {}, Movimentação ID: {}",
                tipoTransacao, valorTotal, movement.getId());
    }

    /**
     * Constrói descrição da transação baseada na movimentação.
     */
    private String buildTransactionDescription(InvestmentMovement movement) {
        String tipo = movement.getTipoMovimentacao().getDescricao();
        String ativo = movement.getInvestment().getNomeAtivo();
        String simbolo = movement.getInvestment().getSimbolo();

        return String.format("%s - %s (%s)", tipo, ativo, simbolo);
    }

    /**
     * Busca ou cria categoria padrão de investimentos.
     */
    private Category getOrCreateInvestmentCategory(User usuario, TipoTransacao tipoTransacao) {
        CategoryType categoryType = tipoTransacao == TipoTransacao.DESPESA
                ? CategoryType.DESPESA
                : CategoryType.RECEITA;

        // Buscar categorias disponíveis para o usuário
        List<Category> availableCategories = categoryRepository.findAllAvailableForUserByTipo(
                usuario.getId(),
                categoryType
        );

        // Procurar categoria "Investimentos"
        Category investmentCategory = availableCategories.stream()
                .filter(cat -> cat.getNome().equalsIgnoreCase(CATEGORY_INVESTIMENTOS))
                .findFirst()
                .orElse(null);

        // Se não encontrar, criar uma nova
        if (investmentCategory == null) {
            log.info("Categoria '{}' não encontrada. Criando nova categoria para usuário ID: {}",
                    CATEGORY_INVESTIMENTOS, usuario.getId());

            investmentCategory = Category.createUserCategory(
                    usuario,
                    CATEGORY_INVESTIMENTOS,
                    "#4CAF50", // Verde para investimentos
                    categoryType,
                    "trending_up" // Ícone de investimentos
            );

            investmentCategory = categoryRepository.save(investmentCategory);
        }

        return investmentCategory;
    }
}
