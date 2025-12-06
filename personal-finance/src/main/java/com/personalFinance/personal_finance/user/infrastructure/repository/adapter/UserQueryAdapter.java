package com.personalFinance.personal_finance.user.infrastructure.repository.adapter;

import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.port.UserFindPort;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Adapter responsável por operações de consulta (leitura).
 * Implementa ports relacionados a find operations.
 * Segue Interface Segregation Principle do SOLID.
 */
@Service
@RequiredArgsConstructor
public class UserQueryAdapter implements UserFindPort {

    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<String> findKeycloakIdByLocalId(Long localId) {
        return userRepository.findKeycloakIdByLocalId(localId);
    }
}
