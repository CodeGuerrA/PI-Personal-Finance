package com.personalFinance.personal_finance.user.application.orchestrator;

import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.domain.exception.UserPersistenceException;
import com.personalFinance.personal_finance.user.domain.port.UserUpdatePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Orquestra a operação de atualizar usuário com tratamento de erros.
 * Coordena a consistência transacional na atualização.
 * Agora trabalha apenas com entidades de domínio (não mais DTOs).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateUserOrchestrator {

    private final UserUpdatePort userUpdatePort;

    /**
     * Atualiza usuário com consistência transacional.
     *
     * @param user Entidade de domínio com dados atualizados
     */
    public void updateWithConsistency(User user) {
        try {
            userUpdatePort.update(user);
            log.info("Usuário '{}' atualizado com sucesso no banco de dados.", user.getUserName());

        } catch (DataIntegrityViolationException e) {
            log.error("Erro ao atualizar usuário '{}'. Violação de integridade.", user.getUserName(), e);
            throw new UserPersistenceException(
                    "Falha ao atualizar usuário com ID '" + user.getKeycloakId() + "'. Violação de integridade.", e);

        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar usuário '{}'.", user.getUserName(), e);
            throw new UserPersistenceException("Erro inesperado ao atualizar usuário: " + e.getMessage(), e);
        }
    }
}
