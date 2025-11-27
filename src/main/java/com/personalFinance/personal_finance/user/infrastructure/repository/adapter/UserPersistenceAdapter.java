package com.personalFinance.personal_finance.user.infrastructure.repository.adapter;

import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.port.UserDeletePort;
import com.personalFinance.personal_finance.user.domain.port.UserSavePort;
import com.personalFinance.personal_finance.user.domain.port.UserUpdatePort;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Adapter responsável por operações de persistência (escrita).
 * Implementa ports relacionados a save, update e delete.
 * Segue Interface Segregation Principle do SOLID.
 */
@Service
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserSavePort, UserUpdatePort, UserDeletePort {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
