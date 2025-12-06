package com.personalFinance.personal_finance.user.application.auth;

import com.personalFinance.personal_finance.user.api.dto.request.UserSetPasswordRequestDTO;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para UserPasswordManager")
class UserPasswordManagerTest {

    @Mock
    private KeycloakFacade keycloakFacade;

    @InjectMocks
    private UserPasswordManager userPasswordManager;

    private UserSetPasswordRequestDTO setPasswordDTO;

    @BeforeEach
    void setUp() {
        setPasswordDTO = new UserSetPasswordRequestDTO();
        setPasswordDTO.setNewPassword("NewPass@456");
    }

    @Test
    @DisplayName("Deve definir senha permanente com sucesso")
    void deveDefinirSenhaPermanenteComSucesso() {
        // Arrange
        Long localId = 1L;
        doNothing().when(keycloakFacade).setPermanentPasswordByLocalId(anyLong(), any(UserSetPasswordRequestDTO.class));

        // Act
        userPasswordManager.setPermanentPassword(localId, setPasswordDTO);

        // Assert
        verify(keycloakFacade).setPermanentPasswordByLocalId(localId, setPasswordDTO);
    }

    @Test
    @DisplayName("Deve chamar KeycloakFacade com os parâmetros corretos")
    void deveChamarKeycloakFacadeComParametrosCorretos() {
        // Arrange
        Long localId = 2L;
        doNothing().when(keycloakFacade).setPermanentPasswordByLocalId(anyLong(), any(UserSetPasswordRequestDTO.class));

        // Act
        userPasswordManager.setPermanentPassword(localId, setPasswordDTO);

        // Assert
        verify(keycloakFacade, times(1)).setPermanentPasswordByLocalId(eq(localId), eq(setPasswordDTO));
    }
}
