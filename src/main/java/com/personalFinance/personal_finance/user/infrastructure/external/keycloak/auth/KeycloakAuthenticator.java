package com.personalFinance.personal_finance.user.infrastructure.external.keycloak.auth;

import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.config.KeycloakPropertiesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakAuthenticator {
    private final KeycloakPropertiesClient keycloakPropertiesClient;

    /**
     * Autentica um usuário no Keycloak e retorna os tokens de acesso.
     * <p>
     * MUDANÇA: Antes armazenava a instância Keycloak em campo de classe (keycloakUser).
     * Agora cria uma instância local que é fechada após o uso.
     *
     * @param username Nome de usuário
     * @param password Senha do usuário
     * @return AccessTokenResponse contendo access_token, refresh_token e expires_in
     * @throws RuntimeException se a autenticação falhar
     */
    public AccessTokenResponse authenticate(String username, String password) {
        // Cria instância LOCAL do Keycloak (não armazena em campo de classe)
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakPropertiesClient.getServerUrl())
                .realm(keycloakPropertiesClient.getRealm())
                .clientId(keycloakPropertiesClient.getClientId())
                .clientSecret(keycloakPropertiesClient.getClientSecret())
                .username(username)
                .password(password)
                .grantType(OAuth2Constants.PASSWORD)
                .build();

        // Obtém o token e fecha a conexão imediatamente
        AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
        keycloak.close(); // Libera recursos (conexões HTTP)

        // Retorna token para o cliente - NUNCA armazena internamente
        return tokenResponse;
    }
}