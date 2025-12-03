package com.personalFinance.personal_finance.investment.api.controller;

import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentCreateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.request.InvestmentUpdateRequestDTO;
import com.personalFinance.personal_finance.investment.api.dto.response.InvestmentResponseDTO;
import com.personalFinance.personal_finance.investment.application.facade.InvestmentFacade;
import com.personalFinance.personal_finance.investment.domain.entity.TipoInvestimento;
import com.personalFinance.personal_finance.shared.security.AuthenticatedUserProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST para operações de Investimentos.
 * Camada de API - responsável apenas por receber requisições HTTP.
 * Não contém lógica de negócio (delegada para Facade).
 * Garante que usuário só acessa seus próprios investimentos.
 */
@Slf4j
@RestController
@RequestMapping("/investments")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentFacade investmentFacade;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    /**
     * Cria um novo investimento.
     * POST /investments
     */
    @PostMapping
    public ResponseEntity<InvestmentResponseDTO> createInvestment(
            @Valid @RequestBody InvestmentCreateRequestDTO dto) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para criar investimento. Usuário: {}", keycloakId);

        InvestmentResponseDTO response = investmentFacade.createInvestment(keycloakId, dto);
        log.info("Investimento criado com sucesso. ID: {}", response.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca investimento por ID (somente do usuário logado).
     * GET /investments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvestmentResponseDTO> getInvestmentById(@PathVariable Long id) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar investimento ID: {}. Usuário: {}", id, keycloakId);

        InvestmentResponseDTO investment = investmentFacade.findById(keycloakId, id);
        log.info("Investimento encontrado: {}", investment.getNomeAtivo());

        return ResponseEntity.ok(investment);
    }

    /**
     * Busca todos os investimentos do usuário logado.
     * GET /investments
     */
    @GetMapping
    public ResponseEntity<List<InvestmentResponseDTO>> getAllInvestments() {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar todos os investimentos. Usuário: {}", keycloakId);

        List<InvestmentResponseDTO> investments = investmentFacade.findAllByUser(keycloakId);
        log.info("Retornando {} investimentos", investments.size());

        return ResponseEntity.ok(investments);
    }

    /**
     * Busca investimentos por status ativo/inativo.
     * GET /investments/ativo?status=true
     */
    @GetMapping("/ativo")
    public ResponseEntity<List<InvestmentResponseDTO>> getInvestmentsByAtivo(
            @RequestParam Boolean status) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar investimentos ativo={}. Usuário: {}", status, keycloakId);

        List<InvestmentResponseDTO> investments = investmentFacade.findByUserAndAtivo(keycloakId, status);
        log.info("Retornando {} investimentos", investments.size());

        return ResponseEntity.ok(investments);
    }

    /**
     * Busca investimentos por tipo.
     * GET /investments/tipo/{tipo}
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<InvestmentResponseDTO>> getInvestmentsByTipo(
            @PathVariable TipoInvestimento tipo) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar investimentos tipo={}. Usuário: {}", tipo, keycloakId);

        List<InvestmentResponseDTO> investments = investmentFacade.findByUserAndTipo(keycloakId, tipo);
        log.info("Retornando {} investimentos", investments.size());

        return ResponseEntity.ok(investments);
    }

    /**
     * Atualiza um investimento (somente do usuário logado).
     * PUT /investments/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvestmentResponseDTO> updateInvestment(
            @PathVariable Long id,
            @Valid @RequestBody InvestmentUpdateRequestDTO dto) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para atualizar investimento ID: {}. Usuário: {}", id, keycloakId);

        InvestmentResponseDTO response = investmentFacade.updateInvestment(keycloakId, id, dto);
        log.info("Investimento atualizado com sucesso");

        return ResponseEntity.ok(response);
    }

    /**
     * Deleta um investimento (somente do usuário logado).
     * DELETE /investments/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestment(@PathVariable Long id) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para deletar investimento ID: {}. Usuário: {}", id, keycloakId);

        investmentFacade.deleteInvestment(keycloakId, id);
        log.info("Investimento deletado com sucesso");

        return ResponseEntity.noContent().build();
    }

    // ============ NOVOS ENDPOINTS DE FILTRO E ESTATÍSTICAS ============

    /**
     * FILTRO: Busca investimentos por corretora (busca parcial case-insensitive).
     * GET /investments/corretora?nome=Clear
     */
    @GetMapping("/corretora")
    public ResponseEntity<List<InvestmentResponseDTO>> getInvestmentsByCorretora(
            @RequestParam String nome) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar investimentos por corretora={}. Usuário: {}", nome, keycloakId);

        List<InvestmentResponseDTO> investments = investmentFacade.findByUserAndCorretora(keycloakId, nome);
        log.info("Retornando {} investimentos", investments.size());

        return ResponseEntity.ok(investments);
    }

    /**
     * FILTRO: Busca investimentos por período de compra.
     * GET /investments/periodo?dataInicio=2025-01-01&dataFim=2025-12-31
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<InvestmentResponseDTO>> getInvestmentsByPeriodo(
            @RequestParam LocalDate dataInicio,
            @RequestParam LocalDate dataFim) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar investimentos entre {} e {}. Usuário: {}", dataInicio, dataFim, keycloakId);

        List<InvestmentResponseDTO> investments = investmentFacade.findByUserAndDataCompraBetween(keycloakId, dataInicio, dataFim);
        log.info("Retornando {} investimentos", investments.size());

        return ResponseEntity.ok(investments);
    }

    /**
     * FILTRO: Busca investimentos por símbolo (busca parcial case-insensitive).
     * GET /investments/simbolo?codigo=PETR
     */
    @GetMapping("/simbolo")
    public ResponseEntity<List<InvestmentResponseDTO>> getInvestmentsBySimbolo(
            @RequestParam String codigo) {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar investimentos por símbolo={}. Usuário: {}", codigo, keycloakId);

        List<InvestmentResponseDTO> investments = investmentFacade.findByUserAndSimboloContaining(keycloakId, codigo);
        log.info("Retornando {} investimentos", investments.size());

        return ResponseEntity.ok(investments);
    }

    /**
     * FILTRO/ORDENAÇÃO: Busca investimentos ordenados por valor investido (maior para menor).
     * GET /investments/ordenar/valor
     */
    @GetMapping("/ordenar/valor")
    public ResponseEntity<List<InvestmentResponseDTO>> getInvestmentsOrderByValor() {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar investimentos ordenados por valor. Usuário: {}", keycloakId);

        List<InvestmentResponseDTO> investments = investmentFacade.findByUserOrderByValorDesc(keycloakId);
        log.info("Retornando {} investimentos", investments.size());

        return ResponseEntity.ok(investments);
    }

    /**
     * FILTRO/ORDENAÇÃO: Busca investimentos ativos ordenados por data de compra (mais recentes primeiro).
     * GET /investments/ordenar/data
     */
    @GetMapping("/ordenar/data")
    public ResponseEntity<List<InvestmentResponseDTO>> getInvestmentsOrderByData() {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar investimentos ordenados por data. Usuário: {}", keycloakId);

        List<InvestmentResponseDTO> investments = investmentFacade.findActiveOrderByDataCompraDesc(keycloakId);
        log.info("Retornando {} investimentos", investments.size());

        return ResponseEntity.ok(investments);
    }

    /**
     * ESTATÍSTICA: Retorna estatísticas completas dos investimentos do usuário.
     * GET /investments/estatisticas
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> getInvestmentStatistics() {
        String keycloakId = authenticatedUserProvider.getAuthenticatedKeycloakId();
        log.info("Requisição para buscar estatísticas de investimentos. Usuário: {}", keycloakId);

        Long countAtivos = investmentFacade.countActiveInvestments(keycloakId);
        BigDecimal totalInvestido = investmentFacade.calculateTotalInvested(keycloakId);
        List<Object[]> resumoPorTipo = investmentFacade.findInvestmentSummaryByTipo(keycloakId);

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalAtivos", countAtivos);
        statistics.put("valorTotalInvestido", totalInvestido);
        statistics.put("resumoPorTipo", resumoPorTipo);

        return ResponseEntity.ok(statistics);
    }
}
