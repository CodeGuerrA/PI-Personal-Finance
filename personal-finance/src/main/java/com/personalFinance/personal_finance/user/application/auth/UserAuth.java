package com.personalFinance.personal_finance.user.application.auth;

import com.personalFinance.personal_finance.user.api.dto.request.UserLoginRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.response.UserLoginResponseDTO;
import com.personalFinance.personal_finance.user.api.mapper.AuthResponseMapper;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuth {
    private final AuthResponseMapper authResponseMapper;
    private final KeycloakFacade keycloakFacade;

    public UserLoginResponseDTO loginUser(UserLoginRequestDTO userLoginRequestDTO) {
        try {
            AccessTokenResponse tokenResponse = keycloakFacade.login(userLoginRequestDTO.getUsername(), userLoginRequestDTO.getPassword());
            UserLoginResponseDTO responseDTO = authResponseMapper.toUserLoginResponseDTO(tokenResponse);

            // Verifica se o usuário tem senha temporária
            boolean hasTemporaryPassword = keycloakFacade.hasTemporaryPassword(userLoginRequestDTO.getUsername());
            responseDTO.setRequiresPasswordChange(hasTemporaryPassword);
            responseDTO.setUsername(userLoginRequestDTO.getUsername());

            if (hasTemporaryPassword) {
                log.warn("Usuário '{}' fez login com senha temporária e precisa alterar a senha", userLoginRequestDTO.getUsername());
            }

            return responseDTO;
        } catch (Exception e) {
            log.error("ERRO NO LOGIN - Exceção: {}, Mensagem: {}", e.getClass().getName(), e.getMessage());
            throw new RuntimeException("Credenciais inválidas ou erro ao realizar login.", e);
        }
    }
}
