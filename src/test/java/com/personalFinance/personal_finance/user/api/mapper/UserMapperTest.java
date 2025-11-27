package com.personalFinance.personal_finance.user.api.mapper;

import com.personalFinance.personal_finance.user.api.dto.response.UserResponseDTO;
import com.personalFinance.personal_finance.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para UserMapper")
class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("Deve mapear User entity para UserResponseDTO corretamente")
    void deveMappearUserEntityParaUserResponseDTO() {
        // Arrange - Usar factory method (Rich Domain Model)
        User user = User.create(
                "joao.silva",
                "João",
                "Silva",
                "teste@example.com",
                "11144477735",
                "keycloak-123"
        );

        // Act
        UserResponseDTO dto = userMapper.toUserResponseDTO(user);

        // Assert
        assertNotNull(dto, "UserResponseDTO não deve ser nulo");
        assertEquals(user.getEmail(), dto.getEmail(), "Email deve ser mapeado corretamente");
        assertEquals(user.getFirstName(), dto.getFirstName(), "FirstName deve ser mapeado corretamente");
        assertEquals(user.getLastName(), dto.getLastName(), "LastName deve ser mapeado corretamente");
        assertEquals(user.getUserName(), dto.getUserName(), "UserName deve ser mapeado corretamente");
    }

    @Test
    @DisplayName("Deve mapear lista de User entities para lista de UserResponseDTO")
    void deveMappearListaDeUsersParaListaDeUserResponseDTO() {
        // Arrange
        User user1 = User.create(
                "joao.silva",
                "João",
                "Silva",
                "user1@example.com",
                "11144477735",
                "keycloak-1"
        );

        User user2 = User.create(
                "maria.santos",
                "Maria",
                "Santos",
                "user2@example.com",
                "52998224725",
                "keycloak-2"
        );

        List<User> users = Arrays.asList(user1, user2);

        // Act
        List<UserResponseDTO> dtos = userMapper.toUserResponseDTOList(users);

        // Assert
        assertNotNull(dtos, "Lista de DTOs não deve ser nula");
        assertEquals(2, dtos.size(), "Lista deve conter 2 elementos");
        assertEquals(user1.getEmail(), dtos.get(0).getEmail(), "Primeiro elemento deve ser mapeado corretamente");
        assertEquals(user2.getEmail(), dtos.get(1).getEmail(), "Segundo elemento deve ser mapeado corretamente");
    }

    @Test
    @DisplayName("Deve retornar null ao mapear Entity nula")
    void deveRetornarNullAoMappearEntityNula() {
        // Act
        UserResponseDTO dto = userMapper.toUserResponseDTO(null);

        // Assert
        assertNull(dto, "Deve retornar null para Entity nula");
    }
}
