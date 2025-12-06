package com.personalFinance.personal_finance.shared.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Provedor para obter informações do usuário autenticado.
 * Extrai dados do JWT presente no contexto de segurança.
 */
@Slf4j
@Component
public class AuthenticatedUserProvider {

    /**
     * Obtém o keycloakId (subject) do usuário autenticado.
     *
     * @return keycloakId do usuário logado
     * @throws IllegalStateException se não houver usuário autenticado
     */
    public String getAuthenticatedKeycloakId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Tentativa de acesso sem autenticação");
            throw new IllegalStateException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            String keycloakId = jwt.getSubject();
            log.debug("Keycloak ID do usuário autenticado: {}", keycloakId);
            return keycloakId;
        }

        log.error("Principal não é do tipo Jwt: {}", principal.getClass().getName());
        throw new IllegalStateException("Tipo de autenticação inválido");
    }

    /**
     * Obtém o username do usuário autenticado.
     *
     * @return username do usuário logado
     */
    public String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            String username = jwt.getClaimAsString("preferred_username");
            return username != null ? username : authentication.getName();
        }

        return authentication.getName();
    }
}
