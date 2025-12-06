package com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade;

import com.personalFinance.personal_finance.user.api.dto.request.ChangePasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserSetPasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.response.KeycloakUserResponseDTO;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.admin.KeycloakPasswordManager;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.admin.KeycloakUserChecker;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.admin.KeycloakUserCreator;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.admin.KeycloakUserDeleter;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.admin.KeycloakUserUpdater;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.auth.KeycloakAuthenticator;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.auth.KeycloakTokenRefresher;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

/**
 * Implementação do facade Keycloak.
 * Não depende de DTOs da camada API - recebe parâmetros primitivos.
 * Segue Clean Architecture: Infrastructure não conhece API layer.
 */
@Service
@RequiredArgsConstructor
public class KeycloakFacadeImpl implements KeycloakFacade {
    private final KeycloakAuthenticator authenticator;
    private final KeycloakTokenRefresher tokenRefresher;
    private final KeycloakUserCreator userCreator;
    private final KeycloakPasswordManager passwordManager;
    private final KeycloakUserUpdater userUpdater;
    private final KeycloakUserDeleter userDeleter;
    private final KeycloakUserChecker userChecker;

    @Override
    public AccessTokenResponse login(String username, String password) {
        return authenticator.authenticate(username, password);
    }

    @Override
    public AccessTokenResponse refreshToken(String refreshToken) {
        return tokenRefresher.refresh(refreshToken);
    }

    @Override
    public KeycloakUserResponseDTO createUser(String userName, String email, String firstName, String lastName) {
        return userCreator.createUser(userName, email, firstName, lastName);
    }

    @Override
    public void setPermanentPasswordByLocalId(Long localId, UserSetPasswordRequestDTO dto) {
        passwordManager.setPermanentPasswordByLocalId(localId, dto);
    }


    @Override
    public void changePassword(String keycloakId, ChangePasswordRequestDTO dto) {
        passwordManager.changePassword(keycloakId, dto);
    }

    @Override
    public void resetPassword(String keycloakId, String newPassword) {
        passwordManager.resetPassword(keycloakId, newPassword);
    }

    @Override
    public void updateUser(String keycloakId, String email) {
        userUpdater.updateUser(keycloakId, email);
    }

    @Override
    public void deleteUser(String keycloakId) {
        userDeleter.deleteUser(keycloakId);
    }

    @Override
    public boolean hasTemporaryPassword(String username) {
        return userChecker.hasTemporaryPassword(username);
    }

}

