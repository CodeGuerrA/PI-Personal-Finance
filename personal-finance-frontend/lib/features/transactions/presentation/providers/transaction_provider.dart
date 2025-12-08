import 'package:flutter/material.dart';
import 'package:sgfi/features/transactions/domain/entities/transaction_entity.dart';
import 'package:sgfi/features/transactions/domain/repositories/transaction_repository.dart';
import 'package:sgfi/core/cache/cache_service.dart';

/// Provider para gerenciar estado de transações
/// Usa cache local para melhor performance e suporte offline
class TransactionProvider extends ChangeNotifier {
  final TransactionRepository _repository;

  TransactionProvider(this._repository);

  // Estado
  List<TransactionEntity> _transactions = [];
  bool _isLoading = false;
  String? _errorMessage;
  int _currentPage = 1;
  static const int _pageSize = 20;
  bool _hasMore = true;

  // Getters
  List<TransactionEntity> get transactions => _transactions;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;
  bool get hasMore => _hasMore;
  int get currentPage => _currentPage;

  /// Carrega transações com cache e paginação
  Future<void> loadTransactions({bool refresh = false}) async {
    if (_isLoading) return;

    // Se refresh, resetar paginação
    if (refresh) {
      _currentPage = 1;
      _transactions = [];
      _hasMore = true;
    }

    // Se não tem mais dados, não carregar
    if (!_hasMore && !refresh) return;

    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      // Tentar carregar do cache primeiro (apenas na primeira página)
      if (_currentPage == 1 && !refresh) {
        final cachedData = CacheService.getCachedTransactions();
        if (cachedData != null && CacheService.isTransactionsCacheValid()) {
          _transactions = _convertCachedToEntities(cachedData);
          _isLoading = false;
          notifyListeners();
          return;
        }
      }

      // Buscar do backend
      final newTransactions = await _repository.getAllTransactions();

      // Salvar no cache (apenas primeira página)
      if (_currentPage == 1) {
        await CacheService.cacheTransactions(newTransactions);
      }

      // Implementar paginação local
      final startIndex = (_currentPage - 1) * _pageSize;
      final endIndex = startIndex + _pageSize;

      if (startIndex >= newTransactions.length) {
        _hasMore = false;
      } else {
        final pageTransactions = newTransactions.sublist(
          startIndex,
          endIndex > newTransactions.length ? newTransactions.length : endIndex,
        );

        if (refresh) {
          _transactions = pageTransactions;
        } else {
          _transactions.addAll(pageTransactions);
        }

        _hasMore = endIndex < newTransactions.length;
        _currentPage++;
      }

      _isLoading = false;
      notifyListeners();
    } catch (e) {
      _errorMessage = 'Erro ao carregar transações: ${e.toString()}';
      _isLoading = false;
      notifyListeners();

      // Se falhar, tentar carregar do cache mesmo que expirado
      final cachedData = CacheService.getCachedTransactions();
      if (cachedData != null && _transactions.isEmpty) {
        _transactions = _convertCachedToEntities(cachedData);
        _errorMessage = 'Mostrando dados em cache (sem conexão)';
        notifyListeners();
      }
    }
  }

  /// Cria nova transação
  Future<bool> createTransaction({
    required double valor,
    required String tipo,
    required PaymentMethod metodoPagamento,
    required int categoriaId,
    required String descricao,
    required DateTime data,
    String? observacoes,
  }) async {
    try {
      await _repository.createTransaction(
        valor: valor,
        tipo: tipo,
        metodoPagamento: metodoPagamento,
        categoriaId: categoriaId,
        descricao: descricao,
        data: data,
        observacoes: observacoes,
      );

      // Limpar cache e recarregar
      await CacheService.clearTransactionsCache();
      await loadTransactions(refresh: true);

      return true;
    } catch (e) {
      _errorMessage = 'Erro ao criar transação: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Atualiza transação existente
  Future<bool> updateTransaction({
    required int id,
    required double valor,
    required String tipo,
    required PaymentMethod metodoPagamento,
    required int categoriaId,
    required String descricao,
    required DateTime data,
    String? observacoes,
  }) async {
    try {
      await _repository.updateTransaction(
        id: id,
        valor: valor,
        tipo: tipo,
        metodoPagamento: metodoPagamento,
        categoriaId: categoriaId,
        descricao: descricao,
        data: data,
        observacoes: observacoes,
      );

      // Limpar cache e recarregar
      await CacheService.clearTransactionsCache();
      await loadTransactions(refresh: true);

      return true;
    } catch (e) {
      _errorMessage = 'Erro ao atualizar transação: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Deleta transação
  Future<bool> deleteTransaction(int id) async {
    try {
      await _repository.deleteTransaction(id);

      // Limpar cache e recarregar
      await CacheService.clearTransactionsCache();
      await loadTransactions(refresh: true);

      return true;
    } catch (e) {
      _errorMessage = 'Erro ao deletar transação: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Filtra transações por tipo
  List<TransactionEntity> filterByType(TransactionType? type) {
    if (type == null) return _transactions;
    return _transactions.where((t) => t.type == type).toList();
  }

  /// Filtra transações por período
  List<TransactionEntity> filterByDateRange(DateTimeRange? range) {
    if (range == null) return _transactions;
    return _transactions.where((t) {
      return (t.date.isAtSameMomentAs(range.start) ||
              t.date.isAfter(range.start)) &&
          (t.date.isAtSameMomentAs(range.end) ||
              t.date.isBefore(range.end.add(const Duration(days: 1))));
    }).toList();
  }

  /// Busca transações por texto
  List<TransactionEntity> search(String query) {
    if (query.isEmpty) return _transactions;
    final lowerQuery = query.toLowerCase();
    return _transactions.where((t) {
      return t.description.toLowerCase().contains(lowerQuery) ||
          t.category.toLowerCase().contains(lowerQuery);
    }).toList();
  }

  /// Limpa erro
  void clearError() {
    _errorMessage = null;
    notifyListeners();
  }

  /// Converte dados do cache para entidades
  List<TransactionEntity> _convertCachedToEntities(
      List<Map<String, dynamic>> cachedData) {
    return cachedData.map((data) {
      return TransactionEntity(
        id: data['id'],
        description: data['description'],
        amount: data['amount'],
        date: DateTime.parse(data['date']),
        category: data['category'],
        type: _parseTransactionType(data['type']),
        paymentMethod: _parsePaymentMethod(data['paymentMethod']),
        observacoes: data['observacoes'],
        categoriaId: data['categoriaId'],
      );
    }).toList();
  }

  TransactionType _parseTransactionType(String type) {
    return type.contains('income')
        ? TransactionType.income
        : TransactionType.expense;
  }

  PaymentMethod _parsePaymentMethod(String method) {
    if (method.contains('dinheiro')) return PaymentMethod.dinheiro;
    if (method.contains('pix')) return PaymentMethod.pix;
    if (method.contains('cartaoDebito')) return PaymentMethod.cartaoDebito;
    if (method.contains('cartaoCredito')) return PaymentMethod.cartaoCredito;
    return PaymentMethod.dinheiro;
  }
}
