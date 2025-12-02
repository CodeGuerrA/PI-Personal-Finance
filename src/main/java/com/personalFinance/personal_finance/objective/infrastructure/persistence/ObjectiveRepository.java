package com.personalFinance.personal_finance.objective.infrastructure.persistence;

import com.personalFinance.personal_finance.objective.domain.entity.Objective;
import com.personalFinance.personal_finance.objective.domain.entity.ObjectiveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {

    List<Objective> findByUsuarioIdAndAtivaTrue(Long usuarioId);

    List<Objective> findByUsuarioIdAndMesAnoAndAtivaTrue(Long usuarioId, String mesAno);

    Optional<Objective> findByUsuarioIdAndCategoriaIdAndMesAnoAndTipoAndAtivaTrue(
            Long usuarioId, Long categoriaId, String mesAno, ObjectiveType tipo);

    List<Objective> findByUsuarioIdAndTipoAndAtivaTrue(Long usuarioId, ObjectiveType tipo);
}
