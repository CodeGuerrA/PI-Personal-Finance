package com.personalFinance.personal_finance.user.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gerenciador de códigos de recuperação de senha.
 * Armazena códigos temporariamente em memória.
 * Para produção em larga escala, considere usar Redis.
 */
@Slf4j
@Service
public class PasswordRecoveryCodeManager {

    private final Map<String, RecoveryCode> codes = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private static final int CODE_EXPIRATION_MINUTES = 15;

    /**
     * Gera e armazena um código de 6 dígitos para o email.
     */
    public String generateCode(String email) {
        String code = String.format("%06d", random.nextInt(1000000));
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES);

        codes.put(email.toLowerCase(), new RecoveryCode(code, expiresAt));

        log.info("Código de recuperação gerado para email: {} (expira em {} minutos)", email, CODE_EXPIRATION_MINUTES);
        return code;
    }

    /**
     * Valida o código para o email informado.
     */
    public boolean validateCode(String email, String code) {
        String normalizedEmail = email.toLowerCase();
        RecoveryCode recoveryCode = codes.get(normalizedEmail);

        if (recoveryCode == null) {
            log.warn("Nenhum código encontrado para o email: {}", email);
            return false;
        }

        if (LocalDateTime.now().isAfter(recoveryCode.expiresAt)) {
            log.warn("Código expirado para o email: {}", email);
            codes.remove(normalizedEmail);
            return false;
        }

        if (!recoveryCode.code.equals(code)) {
            log.warn("Código inválido para o email: {}", email);
            return false;
        }

        log.info("Código validado com sucesso para o email: {}", email);
        return true;
    }

    /**
     * Remove o código após uso bem-sucedido.
     */
    public void removeCode(String email) {
        codes.remove(email.toLowerCase());
        log.info("Código removido para o email: {}", email);
    }

    /**
     * Limpa códigos expirados (pode ser chamado por um scheduled task).
     */
    public void cleanExpiredCodes() {
        LocalDateTime now = LocalDateTime.now();
        codes.entrySet().removeIf(entry -> now.isAfter(entry.getValue().expiresAt));
        log.info("Códigos expirados removidos");
    }

    private record RecoveryCode(String code, LocalDateTime expiresAt) {}
}
