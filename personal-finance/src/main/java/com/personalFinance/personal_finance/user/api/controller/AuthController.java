package com.personalFinance.personal_finance.user.api.controller;

import com.personalFinance.personal_finance.user.api.dto.request.ForgotPasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.ResetPasswordWithCodeRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserLoginRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.response.UserLoginResponseDTO;
import com.personalFinance.personal_finance.user.application.facade.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    //entrar em todas as entidades e corrigir para solid
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody UserLoginRequestDTO dto) {
        log.info("Requisição de login recebida para usuário: {}", dto.getUsername());
        UserLoginResponseDTO responseDTO = authService.login(dto);
        log.info("Login realizado com sucesso para usuário: {}", dto.getUsername());
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserLoginResponseDTO> refresh(@RequestBody String refreshToken) {
        log.info("Requisição de refresh token recebida");
        UserLoginResponseDTO responseDTO = authService.token(refreshToken);
        log.info("Refresh token realizado com sucesso");
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO dto) {
        log.info("Solicitação de recuperação de senha para o email: {}", dto.getEmail());
        authService.requestPasswordRecovery(dto);
        log.info("Código de recuperação enviado para o email: {}", dto.getEmail());
        return ResponseEntity.ok("Código de recuperação enviado para o email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordWithCodeRequestDTO dto) {
        log.info("Tentativa de redefinição de senha com código para o email: {}", dto.getEmail());
        authService.resetPasswordWithCode(dto);
        log.info("Senha redefinida com sucesso para o email: {}", dto.getEmail());
        return ResponseEntity.ok("Senha redefinida com sucesso");
    }
}
