package com.personalFinance.personal_finance.objective.application.service;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.infrastructure.persistence.CategoryRepository;
import com.personalFinance.personal_finance.objective.api.dto.response.ObjectiveResponseDTO;
import com.personalFinance.personal_finance.objective.api.mapper.ObjectiveMapper;
import com.personalFinance.personal_finance.objective.domain.entity.Objective;
import com.personalFinance.personal_finance.objective.domain.entity.ObjectiveType;
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

        // Garantir que valorAtual não seja null
        objectives.forEach(obj -> {
            if (obj.getValorAtual() == null) {
                obj.setValorAtual(BigDecimal.ZERO);
            }
        });

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

        // Garantir que valorAtual não seja null
        objectives.forEach(obj -> {
            if (obj.getValorAtual() == null) {
                obj.setValorAtual(BigDecimal.ZERO);
            }
        });

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

        // Garantir que valorAtual não seja null
        if (objective.getValorAtual() == null) {
            objective.setValorAtual(BigDecimal.ZERO);
        }

        String categoriaNome = objective.getCategoriaId() != null
                ? getCategoryName(objective.getCategoriaId())
                : null;

        return objectiveMapper.toObjectiveResponseDTO(objective, categoriaNome);
    }

    private String getCategoryName(Long categoriaId) {
        return categoryRepository.findById(categoriaId)
                .map(Category::getNome)
                .orElse(null);
    }

    // ============ NOVOS MÉTODOS DE FILTRO E ESTATÍSTICAS ============

    /**
     * FILTRO: Busca objectives próximos de serem cumpridos (>= 80%).
     */
    @Transactional(readOnly = true)
    public List<ObjectiveResponseDTO> findObjectivesCloseToCompletion(String keycloakId) {
        log.info("Buscando objectives próximos de cumprir do usuário: {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Objective> objectives = objectiveRepository.findObjectivesCloseToCompletion(user.getId());

        return objectiveMapper.toObjectiveResponseDTOList(
                objectives,
                categoriaId -> getCategoryName(categoriaId)
        );
    }

    /**
     * FILTRO: Busca objectives que ultrapassaram o limite (tipo LIMITE_CATEGORIA).
     */
    @Transactional(readOnly = true)
    public List<ObjectiveResponseDTO> findOverLimitObjectives(String keycloakId) {
        log.info("Buscando objectives com limite ultrapassado do usuário: {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Objective> objectives = objectiveRepository.findOverLimitObjectives(
                user.getId(),
                ObjectiveType.LIMITE_CATEGORIA.name()
        );

        return objectiveMapper.toObjectiveResponseDTOList(
                objectives,
                categoriaId -> getCategoryName(categoriaId)
        );
    }

    /**
     * FILTRO: Busca objectives por tipo.
     */
    @Transactional(readOnly = true)
    public List<ObjectiveResponseDTO> findByUserAndTipo(String keycloakId, ObjectiveType tipo) {
        log.info("Buscando objectives tipo={} do usuário: {}", tipo, keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        List<Objective> objectives = objectiveRepository.findByUsuarioIdAndTipoAndAtivaTrue(user.getId(), tipo);

        return objectiveMapper.toObjectiveResponseDTOList(
                objectives,
                categoriaId -> getCategoryName(categoriaId)
        );
    }

    /**
     * ESTATÍSTICA: Conta total de objectives ativos.
     */
    @Transactional(readOnly = true)
    public Long countActiveObjectives(String keycloakId) {
        log.info("Contando objectives ativos do usuário: {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return objectiveRepository.countByUsuarioIdAndAtivaTrue(user.getId());
    }

    /**
     * ESTATÍSTICA: Conta objectives cumpridos (>= 100%).
     */
    @Transactional(readOnly = true)
    public Long countCompletedGoals(String keycloakId) {
        log.info("Contando objectives cumpridos do usuário: {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return objectiveRepository.countCompletedGoals(user.getId());
    }

    /**
     * ESTATÍSTICA: Retorna resumo de objectives por tipo.
     */
    @Transactional(readOnly = true)
    public List<Object[]> findObjectiveSummaryByTipo(String keycloakId) {
        log.info("Buscando resumo de objectives por tipo do usuário: {}", keycloakId);

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return objectiveRepository.findObjectiveSummaryByTipo(user.getId());
    }
}
