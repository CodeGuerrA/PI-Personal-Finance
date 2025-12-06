package com.personalFinance.personal_finance.user.domain.service.validation;

import com.personalFinance.personal_finance.user.domain.port.UserExistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsernameGenerator {

    private final UserExistencePort userExistencePort;

    private static final int MAX_GENERATION_ATTEMPTS = 1000;
    private static final String USERNAME_SEPARATOR = ".";

    public String generateUsername(String firstName, String lastName) {
        log.info("Gerando username para o usuário: {} {}", firstName, lastName);

        String baseUsername = buildBaseUsername(firstName, lastName);
        String uniqueUsername = generateUniqueUsername(baseUsername);

        log.info("Username gerado com sucesso: {}", uniqueUsername);
        return uniqueUsername;
    }

    private String buildBaseUsername(String firstName, String lastName) {
        String sanitizedFirstName = sanitizeNamePart(firstName);
        String sanitizedLastName = sanitizeNamePart(lastName);

        return (sanitizedFirstName + USERNAME_SEPARATOR + sanitizedLastName).toLowerCase();
    }

    private String sanitizeNamePart(String namePart) {
        if (namePart == null) return "";

        // Remove espaços extras e decomponhe caracteres acentuados
        String normalized = Normalizer.normalize(namePart.trim(), Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", ""); // remove acentos
        normalized = normalized.replaceAll("[^a-zA-Z0-9]", ""); // remove símbolos

        return normalized;
    }

    private String generateUniqueUsername(String baseUsername) {
        String candidateUsername = baseUsername;
        int attempt = 0;

        while (attempt < MAX_GENERATION_ATTEMPTS) {
            // Verifica apenas no banco de dados local
            if (!userExistencePort.existsByUserName(candidateUsername)) {
                if (attempt > 0) {
                    log.info("Username único '{}' encontrado após {} tentativa(s)", candidateUsername, attempt);
                }
                return candidateUsername;
            }

            log.debug("Username '{}' já existe no banco de dados. Tentando próximo sufixo...", candidateUsername);

            attempt++;
            candidateUsername = baseUsername + (attempt + 1);
        }

        log.error("Atingido limite de {} tentativas para gerar username único. Usando fallback com timestamp.", MAX_GENERATION_ATTEMPTS);
        String fallbackUsername = baseUsername + "_" + System.currentTimeMillis();

        if (userExistencePort.existsByUserName(fallbackUsername)) {
            throw new RuntimeException("Não foi possível gerar um username único após " + MAX_GENERATION_ATTEMPTS + " tentativas.");
        }

        return fallbackUsername;
    }
}

