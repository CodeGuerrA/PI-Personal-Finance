package com.personalFinance.personal_finance.user.application.service;

import com.personalFinance.personal_finance.user.api.dto.response.UserResponseDTO;
import com.personalFinance.personal_finance.user.api.mapper.UserMapper;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.exception.UserNotFoundException;
import com.personalFinance.personal_finance.user.domain.port.UserFindPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFinder {

    private final UserMapper userMapper;
    private final UserFindPort userFindPort;

    public UserResponseDTO findUserByKeycloakId(String keycloakId) {
        log.info("Buscando usuário com Keycloak ID: {}", keycloakId);
        User user = userFindPort.findByKeycloakId(keycloakId)
                .orElseThrow(() -> {
                    log.warn("Usuário com Keycloak ID '{}' não encontrado.", keycloakId);
                    return new UserNotFoundException(keycloakId);
                });
        return userMapper.toUserResponseDTO(user);
    }

    public List<UserResponseDTO> findAllUsers() {
        log.info("Buscando todos os usuários");
        List<User> users = userFindPort.findAll();
        log.info("Encontrados {} usuários", users.size());
        return users.stream()
                .map(userMapper::toUserResponseDTO)
                .toList();
    }
}
