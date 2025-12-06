package com.personalFinance.personal_finance.objective.domain.entity;

import com.personalFinance.personal_finance.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Rich Domain Model: Entidade Objective (Objetivo Financeiro).
 * Refatorada seguindo SOLID e Clean Code.
 * Responsabilidade: Manter estado do objetivo financeiro.
 * Validações e cálculos delegados para Domain Services (SRP).
 */
@Entity
@Table(name = "objectives")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"id", "descricao", "tipo", "mesAno"})
public class Objective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(name = "categoria_id")
    private Long categoriaId;

    @Column(name = "descricao", nullable = false, length = 500)
    private String descricao;

    @Column(name = "valor_objetivo", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorObjetivo;

    @Column(name = "mes_ano", nullable = false, length = 7)
    private String mesAno;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 50)
    private ObjectiveType tipo;

    @Column(name = "ativa", nullable = false)
    private Boolean ativa;

    /**
     * Valor atual do progresso do objetivo.
     * Pode ser atualizado manualmente ou calculado.
     */
    @Column(name = "valor_atual", precision = 15, scale = 2)
    private BigDecimal valorAtual;

    /**
     * Factory method simplificado.
     * Validações delegadas para Validators (SRP).
     */
    public static Objective create(User usuario, Long categoriaId, String descricao,
                                  BigDecimal valorObjetivo, String mesAno, ObjectiveType tipo) {
        return Objective.builder()
                .usuario(usuario)
                .categoriaId(categoriaId)
                .descricao(descricao)
                .valorObjetivo(valorObjetivo)
                .valorAtual(BigDecimal.ZERO)
                .mesAno(mesAno)
                .tipo(tipo)
                .ativa(true)
                .build();
    }

    /**
     * Define o valor atual (calculado externamente por Domain Service).
     */
    public void setValorAtual(BigDecimal valorAtual) {
        this.valorAtual = valorAtual != null ? valorAtual : BigDecimal.ZERO;
    }

    /**
     * Desativa o objetivo.
     */
    public void desativar() {
        this.ativa = false;
    }

    /**
     * Reativa o objetivo.
     */
    public void reativar() {
        this.ativa = true;
    }

    /**
     * Atualiza o valor objetivo.
     * Validação delegada para ObjectiveValueValidator (SRP).
     */
    public void atualizarValorObjetivo(BigDecimal novoValor) {
        this.valorObjetivo = novoValor;
    }

    /**
     * Atualiza a descrição.
     * Validação delegada para ObjectiveDescriptionValidator (SRP).
     */
    public void atualizarDescricao(String novaDescricao) {
        this.descricao = novaDescricao;
    }

    /**
     * Verifica se o objetivo está no período especificado.
     */
    public boolean isInPeriod(String mesAno) {
        return this.mesAno.equals(mesAno);
    }

    /**
     * Verifica se o objetivo está ativo.
     */
    public boolean isAtivo() {
        return this.ativa;
    }

    /**
     * Verifica se o objetivo é de um tipo específico.
     */
    public boolean isTipo(ObjectiveType tipo) {
        return this.tipo == tipo;
    }
}
