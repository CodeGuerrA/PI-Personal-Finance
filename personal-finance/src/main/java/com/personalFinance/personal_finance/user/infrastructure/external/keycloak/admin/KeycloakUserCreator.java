package com.personalFinance.personal_finance.user.infrastructure.external.keycloak.admin;

import com.personalFinance.personal_finance.user.api.dto.response.KeycloakUserResponseDTO;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.config.KeycloakPropertiesClient;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserCreator {
    private final Keycloak keycloakAdminClient;
    private final KeycloakPropertiesClient keycloakPropertiesClient;

    public KeycloakUserResponseDTO createUser(String username, String email, String firstName, String lastName) {
        log.info("Iniciando criação de usuário no Keycloak. Username: {}, Email: {}", username, email);
        try {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(email);
            user.setEmailVerified(true);
            user.setEnabled(true);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            String tempPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
            System.err.println("=================================================");
            System.err.println("SENHA TEMPORÁRIA GERADA: " + tempPassword);
            System.err.println("USERNAME: " + username);
            System.err.println("=================================================");
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(tempPassword);
            credential.setTemporary(true);
            user.setCredentials(List.of(credential));

            Response response = keycloakAdminClient.realm(keycloakPropertiesClient.getRealm()).users().create(user);
            if (response.getStatus() != 201) {
                log.error("Falha ao criar usuário no Keycloak. HTTP status: {}", response.getStatus());
                throw new RuntimeException("Falha ao criar usuário no Keycloak: HTTP " + response.getStatus());
            }

            String userId = CreatedResponseUtil.getCreatedId(response);
            log.info("Usuário '{}' criado com sucesso no Keycloak com ID: {}", username, userId);

            return new KeycloakUserResponseDTO(userId, tempPassword);

        } catch (RuntimeException e) {
            log.error("Erro de criação de usuário no Keycloak: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao criar usuário no Keycloak: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao criar usuário no Keycloak: " + e.getMessage(), e);
        }
    }

}

