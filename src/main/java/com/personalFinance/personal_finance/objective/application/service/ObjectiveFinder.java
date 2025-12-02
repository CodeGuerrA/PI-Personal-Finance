package com.personalFinance.personal_finance.objective.application.service;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.infrastructure.persistence.CategoryRepository;
import com.personalFinance.personal_finance.objective.api.dto.response.ObjectiveResponseDTO;
import com.personalFinance.personal_finance.objective.api.mapper.ObjectiveMapper;
import com.personalFinance.personal_finance.objective.domain.entity.Objective;
import com.personalFinance.personal_finance.objective.domain.service.ObjectiveOwnershipService;
import com.personalFinance.personal_finance.objective.infrastructure.persistence.ObjectiveRepository;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service seguindo SRP: Responsável apenas por BUSCAR objetivos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ObjectiveFinder {

    private final ObjectiveRepository objectiveRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectiveMapper objectiveMapper;
    private final ObjectiveOwnershipService ownershipService;

    @Transactional(readOnly = true)
    public List<ObjectiveResponseDTO> findAllByUser(String keycloakId) {
        log.info("Buscando objetivos do usuário: {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Objective> objectives = objectiveRepository.findByUsuarioIdAndAtivaTrue(user.getId());

        // Calcular valores atuais (simulado)
        objectives.forEach(obj -> obj.setValorAtual(calculateValorAtual(obj)));

        log.info("Encontrados {} objetivos", objectives.size());

        return objectiveMapper.toObjectiveResponseDTOList(
                objectives,
                categoriaId -> getCategoryName(categoriaId)
        );
    }

    @Transactional(readOnly = true)
    public List<ObjectiveResponseDTO> findByUserAndMonth(String keycloakId, String mesAno) {
        log.info("Buscando objetivos do usuário: {} para o mês: {}", keycloakId, mesAno);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Objective> objectives = objectiveRepository.findByUsuarioIdAndMesAnoAndAtivaTrue(
                user.getId(), mesAno
        );

        // Calcular valores atuais
        objectives.forEach(obj -> obj.setValorAtual(calculateValorAtual(obj)));

        return objectiveMapper.toObjectiveResponseDTOList(
                objectives,
                categoriaId -> getCategoryName(categoriaId)
        );
    }

    @Transactional(readOnly = true)
    public ObjectiveResponseDTO findById(String keycloakId, Long objectiveId) {
        log.info("Buscando objetivo ID: {} do usuário: {}", objectiveId, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new IllegalArgumentException("Objetivo não encontrado"));

        // Validar propriedade usando Domain Service
        ownershipService.validateOwnership(objective, user);

        // Calcular valor atual
        objective.setValorAtual(calculateValorAtual(objective));

        String categoriaNome = objective.getCategoriaId() != null
                ? getCategoryName(objective.getCategoriaId())
                : null;

        return objectiveMapper.toObjectiveResponseDTO(objective, categoriaNome);
    }

    private BigDecimal calculateValorAtual(Objective objective) {
        // TODO: Integrar com ObjectiveTransactionPort quando Transaction estiver implementado
        return BigDecimal.ZERO;
    }

    private String getCategoryName(Long categoriaId) {
        return categoryRepository.findById(categoriaId)
                .map(Category::getNome)
                .orElse(null);
    }
}
