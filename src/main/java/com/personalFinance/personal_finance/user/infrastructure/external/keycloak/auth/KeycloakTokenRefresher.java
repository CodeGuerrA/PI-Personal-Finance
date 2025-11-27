package com.personalFinance.personal_finance.user.infrastructure.external.keycloak.auth;

import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.config.KeycloakPropertiesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakTokenRefresher {
    private final KeycloakPropertiesClient keycloakPropertiesClient;

    public AccessTokenResponse refresh(String refreshToken) {
        // Valida que o refresh token foi fornecido
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("Refresh token inválido. Faça login novamente.");
        }

        try {
            // Monta a URL do endpoint de token do Keycloak
            // Formato: https://keycloak.server/realms/nome-realm/protocol/openid-connect/token
            String tokenUrl = keycloakPropertiesClient.getServerUrl() + "/realms/"
                    + keycloakPropertiesClient.getRealm() + "/protocol/openid-connect/token";

            // Cria o request HTTP com os parâmetros do refresh token
            HttpEntity<MultiValueMap<String, String>> request = buildRefreshTokenRequest(refreshToken);

            // Faz a requisição POST ao Keycloak
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<AccessTokenResponse> response = restTemplate.postForEntity(
                    tokenUrl, request, AccessTokenResponse.class);

            // Verifica se a resposta foi bem-sucedida
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Token renovado com sucesso");
                // Retorna novo token ao cliente - NÃO armazena internamente
                return response.getBody();
            } else {
                throw new RuntimeException("Falha ao renovar token: resposta inválida");
            }
        } catch (Exception e) {
            log.error("Erro ao renovar token: {}", e.getMessage());
            throw new RuntimeException("Refresh token expirado ou inválido. Faça login novamente.", e);
        }
    }

    private HttpEntity<MultiValueMap<String, String>> buildRefreshTokenRequest(String refreshToken) {
        // Configura headers da requisição
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Monta os parâmetros da requisição no formato x-www-form-urlencoded
        // Obrigatórios: grant_type, client_id, client_secret, refresh_token
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", OAuth2Constants.REFRESH_TOKEN); // "refresh_token"
        formData.add("client_id", keycloakPropertiesClient.getClientId());
        formData.add("client_secret", keycloakPropertiesClient.getClientSecret());
        formData.add("refresh_token", refreshToken); // Token fornecido pelo cliente

        return new HttpEntity<>(formData, headers);
    }
}
