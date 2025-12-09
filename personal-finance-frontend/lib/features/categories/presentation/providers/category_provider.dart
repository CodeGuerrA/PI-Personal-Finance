import 'package:flutter/material.dart';
import 'package:sgfi/features/categories/domain/entities/category_entity.dart';
import 'package:sgfi/features/categories/domain/repositories/category_repository.dart';
import 'package:sgfi/core/cache/cache_service.dart';

/// Provider para gerenciar estado de categorias
/// Usa cache local para melhor performance e suporte offline
class CategoryProvider extends ChangeNotifier {
  final CategoryRepository _repository;

  CategoryProvider(this._repository);

  // Estado
  List<CategoryEntity> _categories = [];
  bool _isLoading = false;
  String? _errorMessage;

  // Getters
  List<CategoryEntity> get categories => _categories;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  /// Categorias de receita
  List<CategoryEntity> get incomeCategories =>
      _categories.where((c) => c.isIncome).toList();

  /// Categorias de despesa
  List<CategoryEntity> get expenseCategories =>
      _categories.where((c) => !c.isIncome).toList();

  /// Categorias padrão do sistema
  List<CategoryEntity> get defaultCategories =>
      _categories.where((c) => c.isDefault).toList();

  /// Categorias personalizadas do usuário
  List<CategoryEntity> get customCategories =>
      _categories.where((c) => !c.isDefault).toList();

  /// Categorias ativas
  List<CategoryEntity> get activeCategories =>
      _categories.where((c) => c.ativa ?? true).toList();

  /// Carrega categorias com cache
  Future<void> loadCategories({bool refresh = false}) async {
    if (_isLoading) return;

    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      // Tentar carregar do cache primeiro
      if (!refresh) {
        final cachedData = CacheService.getCachedCategories();
        if (cachedData != null && CacheService.isCategoriesCacheValid()) {
          _categories = _convertCachedToEntities(cachedData);
          _isLoading = false;
          notifyListeners();
          return;
        }
      }

      // Buscar do backend
      final categories = await _repository.getAllCategories();

      // Salvar no cache
      await CacheService.cacheCategories(categories);

      _categories = categories;
      _isLoading = false;
      notifyListeners();
    } catch (e) {
      _errorMessage = 'Erro ao carregar categorias: ${e.toString()}';
      _isLoading = false;
      notifyListeners();

      // Se falhar, tentar carregar do cache mesmo que expirado
      final cachedData = CacheService.getCachedCategories();
      if (cachedData != null && _categories.isEmpty) {
        _categories = _convertCachedToEntities(cachedData);
        _errorMessage = 'Mostrando dados em cache (sem conexão)';
        notifyListeners();
      }
    }
  }

  /// Cria nova categoria
  Future<bool> createCategory({
    required String nome,
    required String tipo,
    String? cor,
    String? icone,
  }) async {
    try {
      await _repository.createCategory(
        nome: nome,
        tipo: tipo,
        cor: cor,
        icone: icone,
      );

      // Limpar cache e recarregar
      await CacheService.clearCategoriesCache();
      await loadCategories(refresh: true);

      return true;
    } catch (e) {
      _errorMessage = 'Erro ao criar categoria: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Atualiza categoria existente
  Future<bool> updateCategory({
    required int id,
    required String nome,
    String? cor,
    String? icone,
    bool? ativa,
  }) async {
    try {
      await _repository.updateCategory(
        id: id,
        nome: nome,
        cor: cor,
        icone: icone,
      );

      // Limpar cache e recarregar
      await CacheService.clearCategoriesCache();
      await loadCategories(refresh: true);

      return true;
    } catch (e) {
      _errorMessage = 'Erro ao atualizar categoria: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Deleta categoria (desativa)
  Future<bool> deleteCategory(int id) async {
    try {
      await _repository.deleteCategory(id);

      // Limpar cache e recarregar
      await CacheService.clearCategoriesCache();
      await loadCategories(refresh: true);

      return true;
    } catch (e) {
      _errorMessage = 'Erro ao deletar categoria: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Filtra categorias por tipo
  List<CategoryEntity> filterByType(bool isIncome) {
    return _categories.where((c) => c.isIncome == isIncome).toList();
  }

  /// Busca categorias por nome
  List<CategoryEntity> search(String query) {
    if (query.isEmpty) return _categories;
    final lowerQuery = query.toLowerCase();
    return _categories
        .where((c) => c.name.toLowerCase().contains(lowerQuery))
        .toList();
  }

  /// Obtém categoria por ID
  CategoryEntity? getCategoryById(String id) {
    try {
      return _categories.firstWhere((c) => c.id == id);
    } catch (e) {
      return null;
    }
  }

  /// Limpa erro
  void clearError() {
    _errorMessage = null;
    notifyListeners();
  }

  /// Invalida todos os dados (força recarregamento na próxima requisição)
  void invalidate() {
    print('CategoryProvider - Invalidando dados...');
    _categories = [];
    _errorMessage = null;
    notifyListeners();
  }

  /// Converte dados do cache para entidades
  List<CategoryEntity> _convertCachedToEntities(
      List<Map<String, dynamic>> cachedData) {
    return cachedData.map((data) {
      return CategoryEntity(
        id: data['id'],
        name: data['name'],
        isDefault: data['isDefault'],
        isIncome: data['isIncome'],
        cor: data['cor'],
        icone: data['icone'],
        ativa: data['ativa'],
      );
    }).toList();
  }
}
