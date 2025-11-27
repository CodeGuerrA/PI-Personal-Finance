package com.personalFinance.personal_finance.user.application.service;

import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.user.api.dto.request.UserCreateRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.response.KeycloakUserResponseDTO;
import com.personalFinance.personal_finance.user.application.orchestrator.SaveUserOrchestrator;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.port.UserNotificationPort;
import com.personalFinance.personal_finance.user.domain.service.validation.*;
import com.personalFinance.personal_finance.user.infrastructure.external.keycloak.facade.KeycloakFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para UserCreator")
class UserCreatorTest {

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private CPFValidator cpfValidator;

    @Mock
    private EmailNormalizer emailNormalizer;

    @Mock
    private CpfNormalizer cpfNormalizer;

    @Mock
    private UsernameGenerator usernameGenerator;

    @Mock
    private KeycloakFacade keycloakFacade;

    @Mock
    private SaveUserOrchestrator saveUserOrchestrator;

    @Mock
    private UserNotificationPort userNotificationPort;

    @InjectMocks
    private UserCreator userCreator;

    private UserCreateRequestDTO createRequestDTO;
    private KeycloakUserResponseDTO keycloakResponse;
    private User savedUser;

    @BeforeEach
    void setUp() {
        createRequestDTO = new UserCreateRequestDTO();
        createRequestDTO.setEmail("teste@example.com");
        createRequestDTO.setFirstName("João");
        createRequestDTO.setLastName("Silva");
        createRequestDTO.setCpf("111.444.777-35");

        keycloakResponse = new KeycloakUserResponseDTO("keycloak-123", "temp-password-123");

        // Usar factory method para criar User (Rich Domain Model)
        savedUser = User.create(
                "joao.silva",
                "João",
                "Silva",
                "teste@example.com",
                "11144477735",
                "keycloak-123"
        );
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso seguindo todo o fluxo")
    void deveCriarUsuarioComSucessoSeguindoTodoFluxo() {
        // Arrange
        String normalizedEmail = "teste@example.com";
        String normalizedCpf = "11144477735";
        String generatedUsername = "joao.silva";

        when(emailNormalizer.normalize(anyString())).thenReturn(normalizedEmail);
        when(emailValidator.validate(normalizedEmail)).thenReturn(ValidationResult.valid());
        when(cpfNormalizer.normalize(anyString())).thenReturn(normalizedCpf);
        when(cpfValidator.validate(normalizedCpf)).thenReturn(ValidationResult.valid());
        when(usernameGenerator.generateUsername(anyString(), anyString())).thenReturn(generatedUsername);
        when(keycloakFacade.createUser(anyString(), anyString(), anyString(), anyString())).thenReturn(keycloakResponse);
        when(saveUserOrchestrator.saveWithRollback(any(User.class), anyString())).thenReturn(savedUser);

        // Act
        User result = userCreator.createUser(createRequestDTO);

        // Assert
        assertNotNull(result, "Usuário criado não deve ser nulo");
        assertEquals(savedUser.getUserName(), result.getUserName(), "Username do usuário deve estar correto");
        assertEquals(savedUser.getEmail(), result.getEmail(), "Email do usuário deve estar correto");

        // Verify all method calls in correct order
        verify(emailNormalizer).normalize(createRequestDTO.getEmail());
        verify(emailValidator).validate(normalizedEmail);
        verify(cpfNormalizer).normalize(anyString());
        verify(cpfValidator).validate(normalizedCpf);
        verify(usernameGenerator).generateUsername(createRequestDTO.getFirstName(), createRequestDTO.getLastName());
        verify(keycloakFacade).createUser(generatedUsername, normalizedEmail,
            createRequestDTO.getFirstName(), createRequestDTO.getLastName());
        verify(saveUserOrchestrator).saveWithRollback(any(User.class), eq(keycloakResponse.getUserId()));
        verify(userNotificationPort).sendWelcomeEmail(normalizedEmail, generatedUsername,
                keycloakResponse.getTemporaryPassword());
    }

    @Test
    @DisplayName("Deve validar email antes de gerar username")
    void deveValidarEmailAntesDeGerarUsername() {
        // Arrange
        when(emailNormalizer.normalize(anyString())).thenReturn("teste@example.com");
        when(emailValidator.validate(anyString())).thenReturn(ValidationResult.valid());
        when(cpfNormalizer.normalize(anyString())).thenReturn("11144477735");
        when(cpfValidator.validate(anyString())).thenReturn(ValidationResult.valid());
        when(usernameGenerator.generateUsername(anyString(), anyString())).thenReturn("joao.silva");
        when(keycloakFacade.createUser(anyString(), anyString(), anyString(), anyString())).thenReturn(keycloakResponse);
        when(saveUserOrchestrator.saveWithRollback(any(User.class), anyString())).thenReturn(savedUser);

        // Act
        userCreator.createUser(createRequestDTO);

        // Assert - verify order of execution
        var inOrder = inOrder(emailValidator, usernameGenerator);
        inOrder.verify(emailValidator).validate(anyString());
        inOrder.verify(usernameGenerator).generateUsername(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve criar usuário no Keycloak antes de salvar no banco")
    void deveCriarUsuarioNoKeycloakAntesDeSalvarNoBanco() {
        // Arrange
        when(emailNormalizer.normalize(anyString())).thenReturn("teste@example.com");
        when(emailValidator.validate(anyString())).thenReturn(ValidationResult.valid());
        when(cpfNormalizer.normalize(anyString())).thenReturn("11144477735");
        when(cpfValidator.validate(anyString())).thenReturn(ValidationResult.valid());
        when(usernameGenerator.generateUsername(anyString(), anyString())).thenReturn("joao.silva");
        when(keycloakFacade.createUser(anyString(), anyString(), anyString(), anyString())).thenReturn(keycloakResponse);
        when(saveUserOrchestrator.saveWithRollback(any(User.class), anyString())).thenReturn(savedUser);

        // Act
        userCreator.createUser(createRequestDTO);

        // Assert - verify order of execution
        var inOrder = inOrder(keycloakFacade, saveUserOrchestrator);
        inOrder.verify(keycloakFacade).createUser(anyString(), anyString(), anyString(), anyString());
        inOrder.verify(saveUserOrchestrator).saveWithRollback(any(User.class), anyString());
    }

    @Test
    @DisplayName("Deve enviar email de boas-vindas após salvar usuário")
    void deveEnviarEmailDeBoasVindasAposSalvarUsuario() {
        // Arrange
        when(emailNormalizer.normalize(anyString())).thenReturn("teste@example.com");
        when(emailValidator.validate(anyString())).thenReturn(ValidationResult.valid());
        when(cpfNormalizer.normalize(anyString())).thenReturn("11144477735");
        when(cpfValidator.validate(anyString())).thenReturn(ValidationResult.valid());
        when(usernameGenerator.generateUsername(anyString(), anyString())).thenReturn("joao.silva");
        when(keycloakFacade.createUser(anyString(), anyString(), anyString(), anyString())).thenReturn(keycloakResponse);
        when(saveUserOrchestrator.saveWithRollback(any(User.class), anyString())).thenReturn(savedUser);

        // Act
        userCreator.createUser(createRequestDTO);

        // Assert - verify order of execution
        var inOrder = inOrder(saveUserOrchestrator, userNotificationPort);
        inOrder.verify(saveUserOrchestrator).saveWithRollback(any(User.class), anyString());
        inOrder.verify(userNotificationPort).sendWelcomeEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Deve propagar exceção quando validação de email falhar")
    void devePropararExcecaoQuandoValidacaoDeEmailFalhar() {
        // Arrange
        when(emailNormalizer.normalize(anyString())).thenReturn("teste@example.com");
        when(emailValidator.validate(anyString()))
            .thenReturn(ValidationResult.invalid("Email já existe"));

        // Act & Assert
        assertThrows(Exception.class,
            () -> userCreator.createUser(createRequestDTO),
            "Deve propagar exceção de validação de email");

        // Verify that no other operations were performed
        verify(usernameGenerator, never()).generateUsername(anyString(), anyString());
        verify(keycloakFacade, never()).createUser(anyString(), anyString(), anyString(), anyString());
        verify(saveUserOrchestrator, never()).saveWithRollback(any(User.class), anyString());
    }
}
