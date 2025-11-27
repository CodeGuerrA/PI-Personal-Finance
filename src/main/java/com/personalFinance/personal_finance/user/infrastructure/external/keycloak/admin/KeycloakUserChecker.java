package com.personalFinance.personal_finance.user.infrastructure.external.keycloak.admin;

import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.config.KeycloakPropertiesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakUserChecker {
    private final Keycloak keycloakAdminClient;
    private final KeycloakPropertiesClient keycloakPropertiesClient;

    public boolean hasTemporaryPassword(String username) {
        log.info("Verificando se usuário '{}' tem senha temporária", username);
        try {
            List<UserRepresentation> users = keycloakAdminClient.realm(keycloakPropertiesClient.getRealm())
                    .users()
                    .search(username, true);

            if (users.isEmpty()) {
                log.warn("Usuário '{}' não encontrado", username);
                return false;
            }

            UserRepresentation user = users.get(0);
            String userId = user.getId();

            // Busca as credenciais do usuário
            List<CredentialRepresentation> credentials = keycloakAdminClient.realm(keycloakPropertiesClient.getRealm())
                    .users()
                    .get(userId)
                    .credentials();

            // Verifica se tem alguma credencial temporária
            boolean hasTemp = credentials.stream()
                    .anyMatch(cred -> "password".equals(cred.getType()) &&
                                     Boolean.TRUE.equals(cred.isTemporary()));

            log.info("Usuário '{}' {} senha temporária", username, hasTemp ? "TEM" : "NÃO TEM");
            return hasTemp;

        } catch (Exception e) {
            log.error("Erro ao verificar senha temporária do usuário '{}': {}", username, e.getMessage(), e);
            return false;
        }
    }
}
