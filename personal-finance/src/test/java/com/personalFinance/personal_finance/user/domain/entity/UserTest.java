package com.personalFinance.personal_finance.user.domain.entity;

import com.personalFinance.personal_finance.user.domain.exception.InvalidCpfException;
import com.personalFinance.personal_finance.user.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para User Entity (Rich Domain Model)")
class UserTest {

    @Test
    @DisplayName("Deve criar usuário com dados válidos")
    void deveCriarUsuarioComDadosValidos() {
        // Arrange & Act
        User user = User.create(
                "joao.silva",
                "João",
                "Silva",
                "joao@example.com",
                "11144477735",
                "keycloak-123"
        );

        // Assert
        assertNotNull(user, "Usuário não deve ser nulo");
        assertEquals("joao.silva", user.getUserName(), "Username deve estar correto");
        assertEquals("João", user.getFirstName(), "Primeiro nome deve estar correto");
        assertEquals("Silva", user.getLastName(), "Sobrenome deve estar correto");
        assertEquals("joao@example.com", user.getEmail(), "Email deve estar correto");
        assertEquals("11144477735", user.getCpf(), "CPF deve estar correto");
        assertEquals("keycloak-123", user.getKeycloakId(), "Keycloak ID deve estar correto");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar usuário com email inválido")
    void deveLancarExcecaoAoCriarUsuarioComEmailInvalido() {
        // Act & Assert
        assertThrows(InvalidEmailException.class, () ->
                User.create(
                        "joao.silva",
                        "João",
                        "Silva",
                        "email-invalido",
                        "11144477735",
                        "keycloak-123"
                ),
                "Deve lançar InvalidEmailException para email inválido"
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar usuário com CPF inválido")
    void deveLancarExcecaoAoCriarUsuarioComCpfInvalido() {
        // Act & Assert
        assertThrows(InvalidCpfException.class, () ->
                User.create(
                        "joao.silva",
                        "João",
                        "Silva",
                        "joao@example.com",
                        "111.111.111-11", // CPF com todos dígitos iguais
                        "keycloak-123"
                ),
                "Deve lançar InvalidCpfException para CPF com dígitos iguais"
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar usuário com campos nulos ou vazios")
    void deveLancarExcecaoAoCriarUsuarioComCamposNulos() {
        // Act & Assert - Username nulo
        assertThrows(IllegalArgumentException.class, () ->
                User.create(
                        null,
                        "João",
                        "Silva",
                        "joao@example.com",
                        "11144477735",
                        "keycloak-123"
                ),
                "Deve lançar IllegalArgumentException para username nulo"
        );

        // Act & Assert - FirstName vazio
        assertThrows(IllegalArgumentException.class, () ->
                User.create(
                        "joao.silva",
                        "",
                        "Silva",
                        "joao@example.com",
                        "11144477735",
                        "keycloak-123"
                ),
                "Deve lançar IllegalArgumentException para firstName vazio"
        );

        // Act & Assert - LastName nulo
        assertThrows(IllegalArgumentException.class, () ->
                User.create(
                        "joao.silva",
                        "João",
                        null,
                        "joao@example.com",
                        "11144477735",
                        "keycloak-123"
                ),
                "Deve lançar IllegalArgumentException para lastName nulo"
        );
    }

    @Test
    @DisplayName("Deve atualizar email com sucesso quando email é válido")
    void deveAtualizarEmailComSucessoQuandoEmailEhValido() {
        // Arrange
        User user = User.create(
                "joao.silva",
                "João",
                "Silva",
                "joao@example.com",
                "11144477735",
                "keycloak-123"
        );

        String novoEmail = "joao.silva@newdomain.com";

        // Act
        user.updateEmail(novoEmail);

        // Assert
        assertEquals(novoEmail, user.getEmail(), "Email deve ter sido atualizado");
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar email com formato inválido")
    void deveLancarExcecaoAoAtualizarEmailComFormatoInvalido() {
        // Arrange
        User user = User.create(
                "joao.silva",
                "João",
                "Silva",
                "joao@example.com",
                "11144477735",
                "keycloak-123"
        );

        // Act & Assert
        assertThrows(InvalidEmailException.class, () ->
                user.updateEmail("email-invalido-sem-arroba"),
                "Deve lançar InvalidEmailException ao tentar atualizar com email inválido"
        );
    }

    @Test
    @DisplayName("Deve validar CPF com dígitos verificadores corretos")
    void deveValidarCpfComDigitosVerificadoresCorretos() {
        // Act - CPF válido com dígitos verificadores corretos
        User user = User.create(
                "maria.santos",
                "Maria",
                "Santos",
                "maria@example.com",
                "52998224725", // CPF válido
                "keycloak-456"
        );

        // Assert
        assertNotNull(user, "Usuário deve ser criado com CPF válido");
        assertEquals("52998224725", user.getCpf(), "CPF válido deve ser aceito");
    }

    @Test
    @DisplayName("Deve lançar exceção para CPF com dígitos verificadores incorretos")
    void deveLancarExcecaoParaCpfComDigitosVerificadoresIncorretos() {
        // Act & Assert
        assertThrows(InvalidCpfException.class, () ->
                User.create(
                        "maria.santos",
                        "Maria",
                        "Santos",
                        "maria@example.com",
                        "12345678901", // CPF com dígitos verificadores incorretos
                        "keycloak-456"
                ),
                "Deve lançar InvalidCpfException para CPF com dígitos verificadores incorretos"
        );
    }
}
