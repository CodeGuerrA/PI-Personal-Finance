import 'dart:convert';
import '../../../../core/network/api_client.dart';
import '../models/category_model.dart';
import 'category_remote_datasource.dart';

class CategoryRemoteDataSourceImpl implements CategoryRemoteDataSource {
  final ApiClient _apiClient;

  CategoryRemoteDataSourceImpl({ApiClient? apiClient})
      : _apiClient = apiClient ?? ApiClient();

  @override
  Future<CategoryModel> createCategory({
    required String nome,
    required String tipo,
    String? cor,
    String? icone,
  }) async {
    // Converter tipo para o formato esperado pelo backend (RECEITA ou DESPESA)
    final tipoBackend = tipo.toLowerCase() == 'receita' ? 'RECEITA' : 'DESPESA';

    // Usar cor padrão se não fornecida
    final corFinal = cor ?? (tipoBackend == 'RECEITA' ? '#4CAF50' : '#F44336');

    final response = await _apiClient.post(
      '/categories',
      body: {
        'nome': nome,
        'tipo': tipoBackend,
        'cor': corFinal,
        if (icone != null) 'icone': icone,
      },
    );

    final category = _apiClient.parseResponse<CategoryModel>(
      response,
      (json) => CategoryModel.fromJson(json),
    );

    if (category != null) {
      return category;
    }

    throw Exception('Erro ao criar categoria');
  }

  @override
  Future<CategoryModel> getCategoryById(int id) async {
    final response = await _apiClient.get('/categories/$id');

    final category = _apiClient.parseResponse<CategoryModel>(
      response,
      (json) => CategoryModel.fromJson(json),
    );

    if (category != null) {
      return category;
    }

    throw Exception('Categoria não encontrada');
  }

  @override
  Future<List<CategoryModel>> getAllCategories() async {
    final response = await _apiClient.get('/categories');

    return _apiClient.parseListResponse<CategoryModel>(
      response,
      (json) => CategoryModel.fromJson(json),
    );
  }

  @override
  Future<List<CategoryModel>> getCategoriesByTipo(String tipo) async {
    // Converter tipo para o formato esperado pelo backend (RECEITA ou DESPESA)
    final tipoBackend = tipo.toLowerCase() == 'receita' ? 'RECEITA' : 'DESPESA';

    final response = await _apiClient.get('/categories/tipo/$tipoBackend');

    return _apiClient.parseListResponse<CategoryModel>(
      response,
      (json) => CategoryModel.fromJson(json),
    );
  }

  @override
  Future<CategoryModel> updateCategory({
    required int id,
    required String nome,
    String? cor,
    String? icone,
  }) async {
    final response = await _apiClient.put(
      '/categories/$id',
      body: {
        'nome': nome,
        if (cor != null) 'cor': cor,
        if (icone != null) 'icone': icone,
      },
    );

    final category = _apiClient.parseResponse<CategoryModel>(
      response,
      (json) => CategoryModel.fromJson(json),
    );

    if (category != null) {
      return category;
    }

    throw Exception('Erro ao atualizar categoria');
  }

  @override
  Future<void> deleteCategory(int id) async {
    await _apiClient.delete('/categories/$id');
  }

  @override
  Future<List<CategoryModel>> searchCategoriesByName(String nome) async {
    final response = await _apiClient.get(
      '/categories/buscar',
      queryParameters: {'nome': nome},
    );

    return _apiClient.parseListResponse<CategoryModel>(
      response,
      (json) => CategoryModel.fromJson(json),
    );
  }

  @override
  Future<int> countCategories() async {
    final response = await _apiClient.get('/categories/count');

    if (response.statusCode >= 200 && response.statusCode < 300) {
      return int.parse(response.body);
    }

    throw Exception('Erro ao contar categorias');
  }

  @override
  Future<List<Map<String, dynamic>>> getMostUsedCategories() async {
    final response = await _apiClient.get('/categories/mais-usadas');

    if (response.statusCode >= 200 && response.statusCode < 300) {
      final List<dynamic> jsonList = jsonDecode(response.body);
      return jsonList.map((item) => item as Map<String, dynamic>).toList();
    }

    throw Exception('Erro ao buscar categorias mais usadas');
  }
}
