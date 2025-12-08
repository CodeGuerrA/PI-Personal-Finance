package com.personalFinance.personal_finance.user.application.auth;

import com.personalFinance.personal_finance.user.api.dto.request.FirstAccessRequestDTO;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFirstAccess {
    private final KeycloakFacade keycloakFacade;
    private final UserRepository userRepository;

    public void handleFirstAccess(FirstAccessRequestDTO dto) {
        log.info("Processando primeiro acesso para usuário: {}", dto.getUsername());

        try {
            // 1. Buscar usuário no banco local pelo username
            var user = userRepository.findByUserName(dto.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + dto.getUsername()));

            // 2. Verificar se o usuário tem senha temporária no Keycloak
            log.debug("Verificando se usuário {} tem senha temporária", dto.getUsername());
            boolean hasTemporaryPassword = keycloakFacade.hasTemporaryPassword(dto.getUsername());

            if (!hasTemporaryPassword) {
                log.error("Usuário {} não tem senha temporária no Keycloak", dto.getUsername());
                throw new RuntimeException("Usuário não tem senha temporária. Use o fluxo de login normal.");
            }

            log.info("Usuário {} confirmado com senha temporária", dto.getUsername());

            // 3. Definir senha permanente no Keycloak (substitui a temporária)
            log.debug("Definindo senha permanente para usuário: {}", dto.getUsername());
            keycloakFacade.resetPassword(user.getKeycloakId(), dto.getNewPassword());

            log.info("Senha permanente definida com sucesso para usuário: {}. Senha temporária foi substituída.", dto.getUsername());

        } catch (Exception e) {
            log.error("Erro ao processar primeiro acesso para usuário {}: {}", dto.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Erro ao processar primeiro acesso: " + e.getMessage());
        }
    }
}
