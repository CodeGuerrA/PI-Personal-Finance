package com.personalFinance.personal_finance.objective.application.service;

import com.personalFinance.personal_finance.category.domain.entity.Category;
import com.personalFinance.personal_finance.category.infrastructure.persistence.CategoryRepository;
import com.personalFinance.personal_finance.objective.api.dto.request.ObjectiveCreateRequestDTO;
import com.personalFinance.personal_finance.objective.api.dto.response.ObjectiveResponseDTO;
import com.personalFinance.personal_finance.objective.api.mapper.ObjectiveMapper;
import com.personalFinance.personal_finance.objective.domain.entity.Objective;
import com.personalFinance.personal_finance.objective.domain.entity.ObjectiveType;
import com.personalFinance.personal_finance.objective.domain.service.ObjectiveAlertService;
import com.personalFinance.personal_finance.objective.domain.service.ObjectiveCalculationService;
import com.personalFinance.personal_finance.objective.domain.service.validation.*;
import com.personalFinance.personal_finance.objective.infrastructure.persistence.ObjectiveRepository;
import com.personalFinance.personal_finance.shared.validator.ValidationResult;
import com.personalFinance.personal_finance.user.domain.entity.User;
import com.personalFinance.personal_finance.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service seguindo SRP: Responsável apenas por CRIAR objetivos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ObjectiveCreator {

    private final ObjectiveRepository objectiveRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectiveMapper objectiveMapper;

    // Domain Services
    private final ObjectiveDescriptionValidator descriptionValidator;
    private final ObjectiveValueValidator valueValidator;
    private final MonthYearValidator monthYearValidator;
    private final ObjectiveTypeValidator typeValidator;
    private final ObjectiveCalculationService calculationService;
    private final ObjectiveAlertService alertService;

    @Transactional
    public ObjectiveResponseDTO create(String keycloakId, ObjectiveCreateRequestDTO dto) {
        log.info("Criando objetivo para usuário: {}", keycloakId);

        // 1. Buscar usuário
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // 2. Validações usando Domain Services (SRP)
        validateObjective(dto);

        // 3. Verificar duplicidade
        validateUniqueness(user.getId(), dto);

        // 4. Criar entidade de domínio
        Objective objective = Objective.create(
                user,
                dto.getCategoriaId(),
                dto.getDescricao(),
                dto.getValorObjetivo(),
                dto.getMesAno(),
                dto.getTipo()
        );

        // 5. Salvar
        Objective savedObjective = objectiveRepository.save(objective);
        log.info("Objetivo criado com sucesso. ID: {}", savedObjective.getId());

        // 6. Calcular valor atual (inicialmente zero)
        savedObjective.setValorAtual(BigDecimal.ZERO);

        // 7. Buscar nome da categoria
        String categoriaNome = dto.getCategoriaId() != null
                ? categoryRepository.findById(dto.getCategoriaId())
                .map(Category::getNome)
                .orElse(null)
                : null;

        return objectiveMapper.toObjectiveResponseDTO(savedObjective, categoriaNome);
    }

    private void validateObjective(ObjectiveCreateRequestDTO dto) {
        // Validar descrição
        ValidationResult descValidation = descriptionValidator.validate(dto.getDescricao());
        if (!descValidation.isValid()) {
            throw new IllegalArgumentException(descValidation.errorMessage());
        }

        // Validar valor
        ValidationResult valueValidation = valueValidator.validate(dto.getValorObjetivo());
        if (!valueValidation.isValid()) {
            throw new IllegalArgumentException(valueValidation.errorMessage());
        }

        // Validar mês/ano
        ValidationResult monthValidation = monthYearValidator.validate(dto.getMesAno());
        if (!monthValidation.isValid()) {
            throw new IllegalArgumentException(monthValidation.errorMessage());
        }

        // Validar tipo e categoria
        ValidationResult typeValidation = typeValidator.validate(dto.getTipo(), dto.getCategoriaId());
        if (!typeValidation.isValid()) {
            throw new IllegalArgumentException(typeValidation.errorMessage());
        }
    }

    private void validateUniqueness(Long userId, ObjectiveCreateRequestDTO dto) {
        if (dto.getCategoriaId() != null) {
            objectiveRepository.findByUsuarioIdAndCategoriaIdAndMesAnoAndTipoAndAtivaTrue(
                    userId, dto.getCategoriaId(), dto.getMesAno(), dto.getTipo()
            ).ifPresent(obj -> {
                throw new IllegalArgumentException("Já existe um objetivo ativo para esta categoria/mês/tipo");
            });
        }
    }
}
