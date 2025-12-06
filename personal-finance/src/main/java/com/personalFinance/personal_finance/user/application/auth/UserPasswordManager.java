package com.personalFinance.personal_finance.user.application.auth;

import com.personalFinance.personal_finance.user.api.dto.request.ChangePasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserSetPasswordRequestDTO;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPasswordManager {
    private final KeycloakFacade keycloakFacade;

    public void setPermanentPassword(Long localId, UserSetPasswordRequestDTO userSetPasswordRequestDTO) {
        log.info("Definindo senha permanente para o usuário com ID: {}", localId);
        keycloakFacade.setPermanentPasswordByLocalId(localId, userSetPasswordRequestDTO);
        log.info("Senha permanente definida com sucesso para o usuário com ID: {}", localId);
    }

    public void changePassword(String keycloakId, ChangePasswordRequestDTO dto) {
        log.info("Alterando senha para o usuário com ID: {}", keycloakId);
        keycloakFacade.changePassword(keycloakId, dto);
        log.info("Senha alterada com sucesso para o usuário com ID: {}", keycloakId);
    }
}
