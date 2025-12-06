package com.personalFinance.personal_finance.user.domain.port;

/**
 * Port para verificação de existência de usuários.
 * Segrega a responsabilidade de checagem de existência.
 */
public interface UserExistencePort {
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByCpf(String cpf);
}
