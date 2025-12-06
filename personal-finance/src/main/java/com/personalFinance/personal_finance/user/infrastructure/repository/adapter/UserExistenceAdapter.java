package com.personalFinance.personal_finance.user.infrastructure.repository.adapter;

import com.personalFinance.personal_finance.user.domain.port.UserExistencePort;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Adapter responsável por verificações de existência.
 * Implementa port relacionado a exists operations.
 * Segue Interface Segregation Principle do SOLID.
 */
@Service
@RequiredArgsConstructor
public class UserExistenceAdapter implements UserExistencePort {

    private final UserRepository userRepository;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return userRepository.existsByCpf(cpf);
    }
}
