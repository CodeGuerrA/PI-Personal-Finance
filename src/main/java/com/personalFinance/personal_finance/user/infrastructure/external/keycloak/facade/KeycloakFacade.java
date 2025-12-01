package com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade;

import com.personalFinance.personal_finance.user.api.dto.request.ChangePasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserSetPasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.response.KeycloakUserResponseDTO;
import org.keycloak.representations.AccessTokenResponse;

/**
 * Facade para operações do Keycloak.
 * Não depende de DTOs da camada API - usa parâmetros primitivos.
 * Segue Clean Architecture: Infrastructure não deve conhecer API layer.
 */
public interface KeycloakFacade {

    AccessTokenResponse login(String username, String password);

    AccessTokenResponse refreshToken(String refreshToken);

    KeycloakUserResponseDTO createUser(String userName, String email, String firstName, String lastName);

    void setPermanentPasswordByLocalId(Long localId, UserSetPasswordRequestDTO dto);

    void changePassword(String keycloakId, ChangePasswordRequestDTO dto);

    void resetPassword(String keycloakId, String newPassword);

    void updateUser(String keycloakId, String email);

    void deleteUser(String keycloakId);

    boolean hasTemporaryPassword(String username);
}
