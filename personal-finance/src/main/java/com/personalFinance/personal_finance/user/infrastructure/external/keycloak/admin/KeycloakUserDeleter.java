package com.personalFinance.personal_finance.user.infrastructure.external.keycloak.admin;

import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.config.KeycloakPropertiesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakUserDeleter {
    private final Keycloak keycloakAdminClient;
    private final KeycloakPropertiesClient keycloakPropertiesClient;

    public void deleteUser(String keycloakId) {
        log.info("Removendo usuário com ID '{}' do Keycloak.", keycloakId);
        try {
            keycloakAdminClient.realm(keycloakPropertiesClient.getRealm())
                    .users()
                    .get(keycloakId)
                    .remove();

            log.info("Usuário '{}' removido com sucesso do Keycloak.", keycloakId);
        } catch (Exception e) {
            log.error("Falha ao remover usuário '{}' do Keycloak: {}", keycloakId, e.getMessage(), e);
            throw new RuntimeException("Falha ao remover usuário com ID '" + keycloakId + "' do Keycloak: " + e.getMessage(), e);
        }
    }
}