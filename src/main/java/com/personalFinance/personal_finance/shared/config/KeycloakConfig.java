package com.personalFinance.personal_finance.shared.config;

import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.config.KeycloakPropertiesAdmin;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

    private final KeycloakPropertiesAdmin keycloakPropertiesClient;

    /**
     * Bean do Keycloak Admin Client para criar e gerenciar usuários.
     * Usa o admin do master realm, permitindo criar usuários em qualquer outro realm.
     */
    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakPropertiesClient.getServerUrl())
                .realm(keycloakPropertiesClient.getAdminRealm()) // geralmente 'master'
                .username(keycloakPropertiesClient.getAdminUsername())
                .password(keycloakPropertiesClient.getAdminPassword())
                .clientId(keycloakPropertiesClient.getAdminClientId())
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}
