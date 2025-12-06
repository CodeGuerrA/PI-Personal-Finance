package com.personalFinance.personal_finance.user.application.orchestrator;

/**
 * Strategy para rollback de operações.
 * Segue Open/Closed Principle: Aberto para extensão, fechado para modificação.
 *
 * Permite adicionar novas estratégias de rollback sem modificar código existente.
 */
public interface RollbackStrategy {
    /**
     * Executa rollback de uma operação.
     *
     * @param keycloakId ID do usuário no Keycloak
     * @param username Nome do usuário (para logging)
     */
    void performRollback(String keycloakId, String username);
}
