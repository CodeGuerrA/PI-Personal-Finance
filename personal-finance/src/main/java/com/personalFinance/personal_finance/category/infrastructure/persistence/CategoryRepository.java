package com.personalFinance.personal_finance.category.infrastructure.persistence;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.domain.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository JPA para Category.
 * Utiliza JPQL explícito para todas as queries.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Busca todas as categorias ativas de um usuário.
     */
    @Query("SELECT c FROM Category c WHERE c.usuario.id = :usuarioId AND c.ativa = true")
    List<Category> findByUsuarioIdAndAtivaTrue(@Param("usuarioId") Long usuarioId);

    /**
     * Busca todas as categorias padrão ativas.
     */
    @Query("SELECT c FROM Category c WHERE c.padrao = true AND c.ativa = true")
    List<Category> findByPadraoTrueAndAtivaTrue();

    /**
     * Busca categorias ativas de um usuário por tipo.
     */
    @Query("SELECT c FROM Category c WHERE c.usuario.id = :usuarioId AND c.tipo = :tipo AND c.ativa = true")
    List<Category> findByUsuarioIdAndTipoAndAtivaTrue(@Param("usuarioId") Long usuarioId, @Param("tipo") CategoryType tipo);

    /**
     * Busca categorias padrão ativas por tipo.
     */
    @Query("SELECT c FROM Category c WHERE c.padrao = true AND c.tipo = :tipo AND c.ativa = true")
    List<Category> findByPadraoTrueAndTipoAndAtivaTrue(@Param("tipo") CategoryType tipo);

    /**
     * Busca categorias de um usuário por nome (busca parcial case-insensitive).
     */
    @Query("SELECT c FROM Category c WHERE c.usuario.id = :usuarioId AND LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND c.ativa = true")
    List<Category> findByUsuarioIdAndNomeContaining(@Param("usuarioId") Long usuarioId, @Param("nome") String nome);

    /**
     * Busca todas as categorias disponíveis para um usuário por nome (padrão + próprias).
     */
    @Query("SELECT c FROM Category c WHERE (c.padrao = true OR c.usuario.id = :usuarioId) AND LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND c.ativa = true ORDER BY c.padrao DESC, c.nome ASC")
    List<Category> findAllAvailableForUserByNomeContaining(@Param("usuarioId") Long usuarioId, @Param("nome") String nome);

    /**
     * Busca todas as categorias disponíveis para um usuário (padrão + próprias).
     */
    @Query("SELECT c FROM Category c WHERE (c.padrao = true OR c.usuario.id = :usuarioId) AND c.ativa = true ORDER BY c.padrao DESC, c.nome ASC")
    List<Category> findAllAvailableForUser(@Param("usuarioId") Long usuarioId);

    /**
     * Busca todas as categorias disponíveis para um usuário por tipo (padrão + próprias).
     */
    @Query("SELECT c FROM Category c WHERE (c.padrao = true OR c.usuario.id = :usuarioId) AND c.tipo = :tipo AND c.ativa = true ORDER BY c.padrao DESC, c.nome ASC")
    List<Category> findAllAvailableForUserByTipo(@Param("usuarioId") Long usuarioId, @Param("tipo") CategoryType tipo);

    /**
     * Conta total de categorias ativas de um usuário (sem incluir padrão).
     */
    @Query("SELECT COUNT(c) FROM Category c WHERE c.usuario.id = :usuarioId AND c.ativa = true")
    Long countByUsuarioIdAndAtivaTrue(@Param("usuarioId") Long usuarioId);

    /**
     * Busca categorias ordenadas por nome.
     */
    @Query("SELECT c FROM Category c WHERE c.usuario.id = :usuarioId AND c.ativa = true ORDER BY c.nome ASC")
    List<Category> findByUsuarioIdAndAtivaTrueOrderByNomeAsc(@Param("usuarioId") Long usuarioId);

    /**
     * Busca categorias mais utilizadas por um usuário (com base em objectives).
     * Usa SQL nativo.
     */
    @Query(value = "SELECT c.id, c.nome, c.tipo, COUNT(o.id) as uso_count " +
                   "FROM categories c " +
                   "LEFT JOIN objectives o ON o.categoria_id = c.id AND o.ativa = true " +
                   "WHERE (c.padrao = true OR c.usuario_id = :usuarioId) AND c.ativa = true " +
                   "GROUP BY c.id, c.nome, c.tipo " +
                   "ORDER BY uso_count DESC " +
                   "LIMIT 10",
           nativeQuery = true)
    List<Object[]> findMostUsedCategories(@Param("usuarioId") Long usuarioId);

    /**
     * Verifica se existe categoria com o mesmo nome para o usuário.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.usuario.id = :usuarioId AND LOWER(c.nome) = LOWER(:nome) AND c.ativa = true")
    boolean existsByUsuarioIdAndNome(@Param("usuarioId") Long usuarioId, @Param("nome") String nome);
}
