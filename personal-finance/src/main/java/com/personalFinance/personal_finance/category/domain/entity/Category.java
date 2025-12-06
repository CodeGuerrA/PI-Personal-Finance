package com.personalFinance.personal_finance.category.domain.entity;

import com.personalFinance.personal_finance.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.regex.Pattern;

/**
 * Rich Domain Model: Entidade Category (Categoria).
 * Representa categorias de receitas e despesas.
 */
@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"id", "nome", "tipo"})
public class Category {

    private static final Pattern COLOR_HEX_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cor", nullable = false, length = 7)
    private String cor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private CategoryType tipo;

    @Column(name = "icone", length = 50)
    private String icone;

    @Column(name = "padrao", nullable = false)
    private Boolean padrao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @Column(name = "ativa", nullable = false)
    private Boolean ativa;

    /**
     * Factory method para criar categoria personalizada do usuário.
     */
    public static Category createUserCategory(User usuario, String nome, String cor,
                                             CategoryType tipo, String icone) {
        validateUsuario(usuario);
        validateNome(nome);
        validateCor(cor);
        validateTipo(tipo);

        return Category.builder()
                .usuario(usuario)
                .nome(nome)
                .cor(cor)
                .tipo(tipo)
                .icone(icone)
                .padrao(false)
                .ativa(true)
                .build();
    }

    /**
     * Factory method para criar categoria padrão do sistema.
     */
    public static Category createDefaultCategory(String nome, String cor,
                                                 CategoryType tipo, String icone) {
        validateNome(nome);
        validateCor(cor);
        validateTipo(tipo);

        return Category.builder()
                .usuario(null) // Categoria padrão não tem usuário
                .nome(nome)
                .cor(cor)
                .tipo(tipo)
                .icone(icone)
                .padrao(true)
                .ativa(true)
                .build();
    }

    /**
     * Verifica se a categoria pertence ao usuário.
     */
    public boolean pertenceAoUsuario(User usuario) {
        if (this.padrao) {
            return true; // Categorias padrão são acessíveis por todos
        }
        return this.usuario != null && this.usuario.getId().equals(usuario.getId());
    }

    /**
     * Desativa a categoria.
     */
    public void desativar() {
        if (this.padrao) {
            throw new IllegalStateException("Não é possível desativar categoria padrão");
        }
        this.ativa = false;
    }

    /**
     * Reativa a categoria.
     */
    public void reativar() {
        this.ativa = true;
    }

    /**
     * Atualiza informações da categoria.
     */
    public void atualizar(String nome, String cor, String icone) {
        if (this.padrao) {
            throw new IllegalStateException("Não é possível alterar categoria padrão");
        }
        validateNome(nome);
        validateCor(cor);

        this.nome = nome;
        this.cor = cor;
        this.icone = icone;
    }

    // Validações privadas
    private static void validateUsuario(User usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo para categoria personalizada");
        }
    }

    private static void validateNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio");
        }
        if (nome.length() > 100) {
            throw new IllegalArgumentException("Nome da categoria não pode ter mais de 100 caracteres");
        }
    }

    private static void validateCor(String cor) {
        if (cor == null || cor.isBlank()) {
            throw new IllegalArgumentException("Cor não pode ser nula ou vazia");
        }
        if (!COLOR_HEX_PATTERN.matcher(cor).matches()) {
            throw new IllegalArgumentException("Cor deve estar no formato hexadecimal (#RRGGBB). Ex: #FF0000");
        }
    }

    private static void validateTipo(CategoryType tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de categoria não pode ser nulo");
        }
    }
}
