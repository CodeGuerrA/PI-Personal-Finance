package com.personalFinance.personal_finance.user.infrastructure.external.keycloak.admin;

import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.config.KeycloakPropertiesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

/**
 * Atualizador de usuários do Keycloak.
 * Não depende de DTOs da camada API - usa parâmetros primitivos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakUserUpdater {
    private final Keycloak keycloakAdminClient;
    private final KeycloakPropertiesClient keycloakPropertiesClient;

    public void updateUser(String keycloakId, String email) {
        log.info("Iniciando atualização de usuário no Keycloak. ID: {}", keycloakId);
        try {
            UserRepresentation user = keycloakAdminClient.realm(keycloakPropertiesClient.getRealm())
                    .users()
                    .get(keycloakId)
                    .toRepresentation();

            if (user == null) {
                log.error("Usuário '{}' não encontrado no Keycloak.", keycloakId);
                throw new RuntimeException("Usuário não encontrado no Keycloak: " + keycloakId);
            }

            user.setEmail(email);
            log.info("Atualizando e-mail do usuário '{}' para '{}'.", keycloakId, email);

            keycloakAdminClient.realm(keycloakPropertiesClient.getRealm())
                    .users()
                    .get(keycloakId)
                    .update(user);

            log.info("Usuário '{}' atualizado com sucesso no Keycloak.", keycloakId);

        } catch (RuntimeException e) {
            log.error("Erro ao atualizar usuário '{}' no Keycloak: {}", keycloakId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar usuário '{}' no Keycloak: {}", keycloakId, e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar usuário no Keycloak: " + e.getMessage(), e);
        }
    }
}
