package com.personalFinance.personal_finance.category.infrastructure.config;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.entity.CategoryType;
import com.personalFinance.personal_finance.category.infrastructure.persistence.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Data Loader para criar categorias padrão do sistema.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryDataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        log.info("Verificando e criando categorias padrão...");

        List<Category> defaultCategories = categoryRepository.findByPadraoTrueAndAtivaTrue();

        if (defaultCategories.isEmpty()) {
            log.info("Nenhuma categoria padrão encontrada. Criando categorias padrão...");
            createDefaultCategories();
        } else {
            log.info("{} categorias padrão já existem no sistema.", defaultCategories.size());
        }
    }

    private void createDefaultCategories() {
        // Categorias de RECEITA
        List<CategoryData> receitas = Arrays.asList(
                new CategoryData("Salário", "#4CAF50", "salary"),
                new CategoryData("Freelance", "#8BC34A", "freelance"),
                new CategoryData("Investimentos", "#00BCD4", "investments"),
                new CategoryData("Dividendos", "#2196F3", "dividends"),
                new CategoryData("Outros", "#607D8B", "other")
        );

        // Categorias de DESPESA
        List<CategoryData> despesas = Arrays.asList(
                new CategoryData("Alimentação", "#FF5722", "food"),
                new CategoryData("Transporte", "#FF9800", "transport"),
                new CategoryData("Moradia", "#795548", "housing"),
                new CategoryData("Saúde", "#F44336", "health"),
                new CategoryData("Educação", "#3F51B5", "education"),
                new CategoryData("Lazer", "#E91E63", "entertainment"),
                new CategoryData("Investimentos", "#009688", "investments"),
                new CategoryData("Outros", "#9E9E9E", "other")
        );

        // Criar categorias de RECEITA
        for (CategoryData data : receitas) {
            Category category = Category.createDefaultCategory(
                    data.nome,
                    data.cor,
                    CategoryType.RECEITA,
                    data.icone
            );
            categoryRepository.save(category);
            log.info("Categoria padrão criada: {} (RECEITA)", data.nome);
        }

        // Criar categorias de DESPESA
        for (CategoryData data : despesas) {
            Category category = Category.createDefaultCategory(
                    data.nome,
                    data.cor,
                    CategoryType.DESPESA,
                    data.icone
            );
            categoryRepository.save(category);
            log.info("Categoria padrão criada: {} (DESPESA)", data.nome);
        }

        log.info("Todas as categorias padrão foram criadas com sucesso!");
    }

    /**
     * Classe auxiliar para armazenar dados de categoria.
     */
    private static class CategoryData {
        String nome;
        String cor;
        String icone;

        CategoryData(String nome, String cor, String icone) {
            this.nome = nome;
            this.cor = cor;
            this.icone = icone;
        }
    }
}
