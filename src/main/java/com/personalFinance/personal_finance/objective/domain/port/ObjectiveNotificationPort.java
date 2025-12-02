package com.personalFinance.personal_finance.objective.domain.port;

import com.personalFinance.personal_finance.objective.domain.entity.Objective;

/**
 * Port (Hexagonal Architecture) para notificações de alertas de objetivos.
 * Inverte a dependência - Domain não conhece Infrastructure.
 */
public interface ObjectiveNotificationPort {

    /**
     * Envia notificação de alerta amarelo (80% atingido).
     * @param objective Objetivo que atingiu 80%
     * @param userEmail Email do usuário
     */
    void sendYellowAlert(Objective objective, String userEmail);

    /**
     * Envia notificação de alerta vermelho (100% atingido/ultrapassado).
     * @param objective Objetivo que atingiu 100%
     * @param userEmail Email do usuário
     */
    void sendRedAlert(Objective objective, String userEmail);

    /**
     * Envia notificação de meta cumprida.
     * @param objective Objetivo cumprido
     * @param userEmail Email do usuário
     */
    void sendGoalAchievedNotification(Objective objective, String userEmail);
}
