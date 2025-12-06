package com.personalFinance.personal_finance.user.infrastructure.external.keycloak.admin;

import com.personalFinance.personal_finance.user.api.dto.request.ChangePasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserSetPasswordRequestDTO;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.exception.UserNotFoundException;
import com.personalFinance.personal_finance.user.domain.port.UserFindPort;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.config.KeycloakPropertiesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

/**
 * Gerenciador de senhas do Keycloak.
 * Define senha permanente (não temporária) para usuários.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakPasswordManager {
    private final Keycloak keycloakAdminClient;
    private final KeycloakPropertiesClient keycloakPropertiesClient;
    private final UserFindPort userFindPort;

    /**
     * Define senha permanente para o usuário.
     * A senha não é marcada como temporária, permitindo login imediato.
     */
    public void setPermanentPasswordByLocalId(Long localId, UserSetPasswordRequestDTO dto) {
        String keycloakId = userFindPort.findKeycloakIdByLocalId(localId)
                .orElseThrow(() -> new UserNotFoundException(localId.toString()));
        setPermanentPassword(keycloakId, dto);
    }

    public void setPermanentPassword(String keycloakId, UserSetPasswordRequestDTO userSetPasswordRequestDTO) {
        log.info("Definindo senha permanente para o usuário com ID: {}", keycloakId);

        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(userSetPasswordRequestDTO.getNewPassword());
            credential.setTemporary(false); // Senha permanente, não temporária

            keycloakAdminClient.realm(keycloakPropertiesClient.getRealm())
                    .users()
                    .get(keycloakId)
                    .resetPassword(credential);

            log.info("Senha permanente definida com sucesso para o usuário com ID: {}", keycloakId);
        } catch (Exception e) {
            log.error("Erro ao definir senha permanente para usuário com ID {}: {}", keycloakId, e.getMessage(), e);
            throw new RuntimeException("Erro ao definir senha permanente: " + e.getMessage(), e);
        }
    }

    /**
     * Altera a senha do usuário após validar a senha atual.
     * Valida a senha atual fazendo login no Keycloak.
     */
    public void changePassword(String keycloakId, ChangePasswordRequestDTO dto) {
        log.info("Alterando senha para o usuário com ID: {}", keycloakId);

        try {
            // Busca o usuário no Keycloak para obter o username
            UserRepresentation user = keycloakAdminClient.realm(keycloakPropertiesClient.getRealm())
                    .users()
                    .get(keycloakId)
                    .toRepresentation();

            String username = user.getUsername();
            log.debug("Username do usuário: {}", username);

            // Valida a senha atual fazendo login
            validateCurrentPassword(username, dto.getCurrentPassword());

            // Se chegou aqui, a senha atual está correta
            // Agora define a nova senha
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(dto.getNewPassword());
            credential.setTemporary(false);

            keycloakAdminClient.realm(keycloakPropertiesClient.getRealm())
                    .users()
                    .get(keycloakId)
                    .resetPassword(credential);

            log.info("Senha alterada com sucesso para o usuário com ID: {}", keycloakId);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao alterar senha para usuário com ID {}: {}", keycloakId, e.getMessage(), e);
            throw new RuntimeException("Erro ao alterar senha: " + e.getMessage(), e);
        }
    }

    /**
     * Redefine a senha do usuário sem validação (usado na recuperação de senha).
     */
    public void resetPassword(String keycloakId, String newPassword) {
        log.info("Redefinindo senha para o usuário com ID: {}", keycloakId);

        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(newPassword);
            credential.setTemporary(false);

            keycloakAdminClient.realm(keycloakPropertiesClient.getRealm())
                    .users()
                    .get(keycloakId)
                    .resetPassword(credential);

            log.info("Senha redefinida com sucesso para o usuário com ID: {}", keycloakId);
        } catch (Exception e) {
            log.error("Erro ao redefinir senha para usuário com ID {}: {}", keycloakId, e.getMessage(), e);
            throw new RuntimeException("Erro ao redefinir senha: " + e.getMessage(), e);
        }
    }

    /**
     * Valida a senha atual do usuário fazendo login no Keycloak.
     */
    private void validateCurrentPassword(String username, String currentPassword) {
        log.debug("Validando senha atual para o usuário: {}", username);

        try (Keycloak keycloakTest = KeycloakBuilder.builder()
                .serverUrl(keycloakPropertiesClient.getServerUrl())
                .realm(keycloakPropertiesClient.getRealm())
                .clientId(keycloakPropertiesClient.getClientId())
                .clientSecret(keycloakPropertiesClient.getClientSecret())
                .grantType("password")
                .username(username)
                .password(currentPassword)
                .build()) {

            // Se conseguir obter o token, a senha está correta
            keycloakTest.tokenManager().getAccessToken();
            log.debug("Senha atual validada com sucesso para o usuário: {}", username);

        } catch (Exception e) {
            log.warn("Senha atual inválida para o usuário: {}", username);
            throw new RuntimeException("Senha atual incorreta");
        }
    }
}

