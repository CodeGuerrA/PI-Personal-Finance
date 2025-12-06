package com.personalFinance.personal_finance.objective.domain.service;

import com.personalFinance.personal_finance.objective.domain.entity.Objective;
import com.personalFinance.personal_finance.objective.domain.entity.ObjectiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Domain Service para determinar alertas de objetivos.
 * Segue SRP - responsável apenas por lógica de alertas.
 */
@Service
@RequiredArgsConstructor
public class ObjectiveAlertService {

    private final ObjectiveCalculationService calculationService;

    private static final BigDecimal ALERT_YELLOW_THRESHOLD = BigDecimal.valueOf(80);
    private static final BigDecimal ALERT_RED_THRESHOLD = BigDecimal.valueOf(100);

    /**
     * Verifica se a meta atingiu 80% (alerta amarelo).
     */
    public boolean isAlertaAmarelo(Objective objective) {
        BigDecimal percentual = calculationService.calculatePercentualAtingido(objective);
        return percentual.compareTo(ALERT_YELLOW_THRESHOLD) >= 0
                && percentual.compareTo(ALERT_RED_THRESHOLD) < 0;
    }

    /**
     * Verifica se a meta atingiu ou ultrapassou 100% (alerta vermelho).
     */
    public boolean isAlertaVermelho(Objective objective) {
        BigDecimal percentual = calculationService.calculatePercentualAtingido(objective);
        return percentual.compareTo(ALERT_RED_THRESHOLD) >= 0;
    }

    /**
     * Verifica se a meta foi cumprida (para metas de economia/investimento).
     */
    public boolean isMetaCumprida(Objective objective) {
        if (objective.getTipo() == ObjectiveType.LIMITE_CATEGORIA) {
            return false; // Limite não tem "meta cumprida"
        }
        BigDecimal percentual = calculationService.calculatePercentualAtingido(objective);
        return percentual.compareTo(ALERT_RED_THRESHOLD) >= 0;
    }

    /**
     * Determina o status de alerta do objetivo.
     */
    public String determineAlertStatus(Objective objective) {
        if (isMetaCumprida(objective)) {
            return "CUMPRIDA";
        } else if (isAlertaVermelho(objective)) {
            return "VERMELHO";
        } else if (isAlertaAmarelo(objective)) {
            return "AMARELO";
        } else {
            return "NENHUM";
        }
    }
}
