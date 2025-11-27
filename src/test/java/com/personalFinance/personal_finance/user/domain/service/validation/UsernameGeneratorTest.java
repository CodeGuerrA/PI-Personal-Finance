package com.personalFinance.personal_finance.user.domain.service.validation;

import com.personalFinance.personal_finance.user.domain.port.UserExistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para UsernameGenerator")
class UsernameGeneratorTest {

    @Mock
    private UserExistencePort userExistencePort;

    @InjectMocks
    private UsernameGenerator usernameGenerator;

    @Test
    @DisplayName("Deve gerar username corretamente com firstName e lastName")
    void deveGerarUsernameCorretamenteComPrimeiroEUltimoNome() {
        // Arrange
        String firstName = "João";
        String lastName = "Silva";
        when(userExistencePort.existsByUserName(anyString())).thenReturn(false);

        // Act
        String username = usernameGenerator.generateUsername(firstName, lastName);

        // Assert
        assertNotNull(username, "Username não deve ser nulo");
        assertTrue(username.contains("joao"), "Username deve conter o primeiro nome normalizado");
        assertTrue(username.contains("silva"), "Username deve conter o último nome normalizado");
        assertEquals("joao.silva", username, "Username deve estar no formato correto");
    }

    @Test
    @DisplayName("Deve adicionar sufixo numérico quando username já existe")
    void deveAdicionarSufixoNumericoQuandoUsernameJaExiste() {
        // Arrange
        String firstName = "Carlos";
        String lastName = "Garcia";
        when(userExistencePort.existsByUserName("carlos.garcia")).thenReturn(true);
        when(userExistencePort.existsByUserName("carlos.garcia2")).thenReturn(false);

        // Act
        String username = usernameGenerator.generateUsername(firstName, lastName);

        // Assert
        assertNotNull(username, "Username não deve ser nulo");
        assertEquals("carlos.garcia2", username, "Username deve ter sufixo numérico quando já existe");
    }

    @Test
    @DisplayName("Deve normalizar caracteres especiais e acentos")
    void deveNormalizarCaracteresEspeciaisEAcentos() {
        // Arrange
        String firstName = "José";
        String lastName = "Ñoño";
        when(userExistencePort.existsByUserName(anyString())).thenReturn(false);

        // Act
        String username = usernameGenerator.generateUsername(firstName, lastName);

        // Assert
        assertNotNull(username, "Username não deve ser nulo");
        assertFalse(username.contains("é"), "Username não deve conter acentos");
        assertFalse(username.contains("ñ"), "Username não deve conter caracteres especiais");
        assertTrue(username.matches("[a-z.0-9]+"), "Username deve conter apenas letras minúsculas, pontos e números");
    }

    @Test
    @DisplayName("Deve lidar com firstName ou lastName nulos gerando username vazio")
    void deveLidarComNomesNulos() {
        // Arrange
        when(userExistencePort.existsByUserName(anyString())).thenReturn(false);

        // Act
        String usernameWithNullFirstName = usernameGenerator.generateUsername(null, "Silva");
        String usernameWithNullLastName = usernameGenerator.generateUsername("João", null);

        // Assert
        assertNotNull(usernameWithNullFirstName, "Username não deve ser nulo mesmo com firstName nulo");
        assertEquals(".silva", usernameWithNullFirstName, "Username deve conter apenas lastName quando firstName é nulo");

        assertNotNull(usernameWithNullLastName, "Username não deve ser nulo mesmo com lastName nulo");
        assertEquals("joao.", usernameWithNullLastName, "Username deve conter apenas firstName quando lastName é nulo");
    }

    @Test
    @DisplayName("Deve adicionar sufixos incrementais quando múltiplos usernames já existem")
    void deveAdicionarSufixosIncrementaisQuandoMultiplosUsernamesJaExistem() {
        // Arrange: Simula que carlos.garcia, carlos.garcia2 e carlos.garcia3 já existem
        String firstName = "Carlos";
        String lastName = "Garcia";

        when(userExistencePort.existsByUserName("carlos.garcia")).thenReturn(true);
        when(userExistencePort.existsByUserName("carlos.garcia2")).thenReturn(true);
        when(userExistencePort.existsByUserName("carlos.garcia3")).thenReturn(true);
        when(userExistencePort.existsByUserName("carlos.garcia4")).thenReturn(false);

        // Act
        String username = usernameGenerator.generateUsername(firstName, lastName);

        // Assert
        assertEquals("carlos.garcia4", username, "Username deve ter sufixo 4 quando 1, 2 e 3 já existem");
    }
}
