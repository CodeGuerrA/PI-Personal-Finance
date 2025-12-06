package com.personalFinance.personal_finance.user.api.controller;

import com.personalFinance.personal_finance.shared.security.AuthenticatedUserProvider;
import com.personalFinance.personal_finance.user.api.dto.request.ChangePasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserCreateRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserSetPasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserUpdateRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.response.UserResponseDTO;
import com.personalFinance.personal_finance.user.application.facade.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações de usuário.
 * Camada de API - responsável apenas por receber requisições e retornar respostas HTTP.
 * Não contém lógica de negócio (delegada para a camada de Application).
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateRequestDTO dto) {
        log.info("Requisição de criação de usuário recebida: {}", dto.getEmail());
        userService.createUser(dto);
        log.info("Usuário criado com sucesso: {}", dto.getEmail());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Usuário criado com sucesso! Verifique seu email para obter a senha temporária.");
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> setPassword(@PathVariable Long id, @RequestBody UserSetPasswordRequestDTO userSetPasswordRequestDTO) {
        userService.setPermanentPassword(id, userSetPasswordRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("Requisição para buscar todos os usuários");
        List<UserResponseDTO> users = userService.findAllUsers();
        log.info("Retornando {} usuários", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile() {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar perfil do usuário: {}", keycloakId);
        UserResponseDTO user = userService.findUserByKeycloakId(keycloakId);
        log.info("Perfil do usuário {} retornado com sucesso", keycloakId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateMyProfile(@Valid @RequestBody UserUpdateRequestDTO dto) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Usuário {} atualizando seu próprio perfil", keycloakId);
        userService.updateUser(keycloakId, dto);
        log.info("Perfil do usuário {} atualizado com sucesso", keycloakId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(@Valid @RequestBody ChangePasswordRequestDTO dto) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Usuário {} solicitando troca de senha", keycloakId);
        userService.changePassword(keycloakId, dto);
        log.info("Senha do usuário {} alterada com sucesso", keycloakId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount() {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Usuário {} solicitando exclusão da própria conta", keycloakId);
        userService.deleteUser(keycloakId);
        log.info("Conta do usuário {} deletada com sucesso", keycloakId);
        return ResponseEntity.noContent().build();
    }
}
