package com.personalFinance.personal_finance.user.application.auth;

import com.personalFinance.personal_finance.user.api.dto.response.UserLoginResponseDTO;
import com.personalFinance.personal_finance.user.api.mapper.AuthResponseMapper;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRefreshToken {
    private final AuthResponseMapper authResponseMapper;
    private final KeycloakFacade keycloakFacade;

    public UserLoginResponseDTO refreshToken(String refreshToken) {
        try {
            AccessTokenResponse tokenResponse = keycloakFacade.refreshToken(refreshToken);
            return authResponseMapper.toUserLoginResponseDTO(tokenResponse);
        } catch (Exception e) {
            throw new RuntimeException("Refresh token inválido ou expirado");
        }
    }
}
