import 'package:flutter/material.dart';
import 'package:sgfi/features/investments/domain/entities/investment_entity.dart';
import 'package:sgfi/features/investments/domain/repositories/investment_repository.dart';
import 'package:sgfi/core/cache/cache_service.dart';

/// Provider para gerenciar estado de investimentos
class InvestmentProvider extends ChangeNotifier {
  final InvestmentRepository _repository;

  InvestmentProvider(this._repository);

  // Estado
  List<InvestmentEntity> _investments = [];
  bool _isLoading = false;
  String? _errorMessage;

  // Getters
  List<InvestmentEntity> get investments => _investments;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  /// Investimentos ativos
  List<InvestmentEntity> get activeInvestments =>
      _investments.where((i) => i.isActive ?? true).toList();

  /// Valor total investido
  double get totalInvested =>
      _investments.fold(0.0, (sum, i) => sum + (i.totalInvested ?? 0));

  /// Valor atual da carteira
  double get currentValue =>
      _investments.fold(0.0, (sum, i) => sum + (i.currentValue ?? 0));

  /// Lucro total
  double get totalProfit => currentValue - totalInvested;

  /// Rentabilidade percentual total
  double get totalProfitPercent {
    if (totalInvested <= 0) return 0;
    return (totalProfit / totalInvested) * 100;
  }

  /// Investimentos com lucro
  List<InvestmentEntity> get profitableInvestments =>
      _investments.where((i) => (i.profit ?? 0) > 0).toList();

  /// Investimentos com prejuízo
  List<InvestmentEntity> get losingInvestments =>
      _investments.where((i) => (i.profit ?? 0) < 0).toList();

  /// Carrega investimentos
  Future<void> loadInvestments({bool refresh = false}) async {
    if (_isLoading) return;

    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      // Tentar cache primeiro se não for refresh forçado
      if (!refresh) {
        final cachedData = CacheService.getCachedInvestments();
        if (cachedData != null && CacheService.isInvestmentsCacheValid()) {
          _investments = _convertCachedToEntities(cachedData);
          _isLoading = false;
          notifyListeners();
          return;
        }
      }

      // Buscar do backend
      final investments = await _repository.getAllInvestments();

      // Salvar no cache
      await CacheService.cacheInvestments(investments);

      _investments = investments;
      _isLoading = false;
      notifyListeners();
    } catch (e) {
      _errorMessage = 'Erro ao carregar investimentos: ${e.toString()}';
      _isLoading = false;

      // Fallback: tentar usar cache expirado em caso de erro de rede
      if (!refresh) {
        final cachedData = CacheService.getCachedInvestments();
        if (cachedData != null) {
          _investments = _convertCachedToEntities(cachedData);
        }
      }

      notifyListeners();
    }
  }

  /// Cria novo investimento
  Future<bool> createInvestment({
    required String tipoInvestimento,
    required String nomeAtivo,
    required String simbolo,
    required double quantidade,
    required double valorCompra,
    required DateTime dataCompra,
    required String corretora,
    String? observacoes,
  }) async {
    try {
      await _repository.createInvestment(
        tipoInvestimento: tipoInvestimento,
        nomeAtivo: nomeAtivo,
        simbolo: simbolo,
        quantidade: quantidade,
        valorCompra: valorCompra,
        dataCompra: dataCompra,
        corretora: corretora,
        observacoes: observacoes,
      );

      await loadInvestments(refresh: true);
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao criar investimento: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Atualiza investimento existente
  Future<bool> updateInvestment({
    required int id,
    required String nomeAtivo,
    required String simbolo,
    required double quantidade,
    required double valorCompra,
    required DateTime dataCompra,
    required String corretora,
    String? observacoes,
  }) async {
    try {
      await _repository.updateInvestment(
        id: id,
        nomeAtivo: nomeAtivo,
        simbolo: simbolo,
        quantidade: quantidade,
        valorCompra: valorCompra,
        dataCompra: dataCompra,
        corretora: corretora,
        observacoes: observacoes,
      );

      await loadInvestments(refresh: true);
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao atualizar investimento: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Deleta investimento
  Future<bool> deleteInvestment(int id) async {
    try {
      await _repository.deleteInvestment(id);

      await loadInvestments(refresh: true);
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao deletar investimento: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Filtra investimentos por tipo
  List<InvestmentEntity> filterByType(InvestmentType type) {
    return _investments.where((i) => i.type == type).toList();
  }

  /// Filtra investimentos por corretora
  List<InvestmentEntity> filterByBroker(String broker) {
    return _investments
        .where((i) => i.broker.toLowerCase().contains(broker.toLowerCase()))
        .toList();
  }

  /// Busca investimentos por nome ou símbolo
  List<InvestmentEntity> search(String query) {
    if (query.isEmpty) return _investments;
    final lowerQuery = query.toLowerCase();
    return _investments.where((i) {
      return i.name.toLowerCase().contains(lowerQuery) ||
          i.symbol.toLowerCase().contains(lowerQuery);
    }).toList();
  }

  /// Obtém investimento por ID
  InvestmentEntity? getInvestmentById(String id) {
    try {
      return _investments.firstWhere((i) => i.id == id);
    } catch (e) {
      return null;
    }
  }

  /// Agrupa investimentos por tipo
  Map<InvestmentType, List<InvestmentEntity>> groupByType() {
    final map = <InvestmentType, List<InvestmentEntity>>{};

    for (var investment in _investments) {
      if (!map.containsKey(investment.type)) {
        map[investment.type] = [];
      }
      map[investment.type]!.add(investment);
    }

    return map;
  }

  /// Calcula diversificação da carteira (percentual por tipo)
  Map<InvestmentType, double> getDiversification() {
    if (totalInvested <= 0) return {};

    final diversification = <InvestmentType, double>{};
    final grouped = groupByType();

    grouped.forEach((type, investments) {
      final typeTotal = investments.fold(
        0.0,
        (sum, i) => sum + (i.totalInvested ?? 0),
      );
      diversification[type] = (typeTotal / totalInvested) * 100;
    });

    return diversification;
  }

  /// Melhor investimento (maior rentabilidade percentual)
  InvestmentEntity? get bestPerformer {
    if (_investments.isEmpty) return null;

    return _investments.reduce((a, b) {
      final aProfit = a.profitPercent ?? 0;
      final bProfit = b.profitPercent ?? 0;
      return aProfit > bProfit ? a : b;
    });
  }

  /// Pior investimento (menor rentabilidade percentual)
  InvestmentEntity? get worstPerformer {
    if (_investments.isEmpty) return null;

    return _investments.reduce((a, b) {
      final aProfit = a.profitPercent ?? 0;
      final bProfit = b.profitPercent ?? 0;
      return aProfit < bProfit ? a : b;
    });
  }

  /// Limpa erro
  void clearError() {
    _errorMessage = null;
    notifyListeners();
  }

  /// Invalida todos os dados (força recarregamento na próxima requisição)
  void invalidate() {
    print('InvestmentProvider - Invalidando dados...');
    _investments = [];
    _errorMessage = null;
    notifyListeners();
  }

  /// Converte dados do cache para entidades
  List<InvestmentEntity> _convertCachedToEntities(List<Map<String, dynamic>> cachedData) {
    return cachedData.map((data) {
      return InvestmentEntity(
        id: data['id'].toString(),
        type: _parseInvestmentType(data['type']),
        name: data['name'] ?? '',
        symbol: data['symbol'] ?? '',
        quantity: (data['quantity'] as num?)?.toDouble() ?? 0.0,
        buyPrice: (data['buyPrice'] as num?)?.toDouble() ?? 0.0,
        broker: data['broker'] ?? '',
        currentPrice: (data['currentPrice'] as num?)?.toDouble(),
        isActive: data['isActive'] ?? true,
        totalInvested: (data['totalInvested'] as num?)?.toDouble(),
        currentValue: (data['currentValue'] as num?)?.toDouble(),
        profit: (data['profit'] as num?)?.toDouble(),
        profitPercent: (data['profitPercent'] as num?)?.toDouble(),
      );
    }).toList();
  }

  /// Parse de tipo de investimento
  InvestmentType _parseInvestmentType(String? typeString) {
    if (typeString == null) return InvestmentType.rendaFixa;
    if (typeString.contains('cripto')) return InvestmentType.cripto;
    if (typeString.contains('acao')) return InvestmentType.acao;
    if (typeString.contains('fundo')) return InvestmentType.fundo;
    if (typeString.contains('rendaFixa')) return InvestmentType.rendaFixa;
    if (typeString.contains('tesouroDireto')) return InvestmentType.tesouroDireto;
    if (typeString.contains('cdb')) return InvestmentType.cdb;
    return InvestmentType.rendaFixa;
  }
}
