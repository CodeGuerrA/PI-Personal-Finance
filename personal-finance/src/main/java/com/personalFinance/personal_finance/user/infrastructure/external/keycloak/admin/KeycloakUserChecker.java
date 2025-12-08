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

            // CORREÇÃO: A forma correta de verificar senha temporária no Keycloak
            // é através dos Required Actions, não das credenciais
            List<String> requiredActions = user.getRequiredActions();

            // Se tem a action UPDATE_PASSWORD, significa que tem senha temporária
            boolean hasTemp = requiredActions != null &&
                             requiredActions.contains("UPDATE_PASSWORD");

            log.info("Usuário '{}' {} senha temporária (RequiredActions: {})",
                     username,
                     hasTemp ? "TEM" : "NÃO TEM",
                     requiredActions);

            return hasTemp;

        } catch (Exception e) {
            log.error("Erro ao verificar senha temporária do usuário '{}': {}", username, e.getMessage(), e);
            return false;
        }
    }
}
