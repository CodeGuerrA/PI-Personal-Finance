import 'dart:convert';
import '../../../../core/network/api_client.dart';
import '../../domain/entities/goal_entity.dart';
import '../models/goal_model.dart';
import 'goal_remote_datasource.dart';

class GoalRemoteDataSourceImpl implements GoalRemoteDataSource {
  final ApiClient _apiClient;

  GoalRemoteDataSourceImpl({ApiClient? apiClient})
      : _apiClient = apiClient ?? ApiClient();

  @override
  Future<GoalModel> createGoal({
    required String descricao,
    required double valorObjetivo,
    required int categoriaId,
    required ObjectiveType tipo,
    String? mesAno,
  }) async {
    final response = await _apiClient.post(
      '/objectives',
      body: {
        'descricao': descricao,
        'valorObjetivo': valorObjetivo,
        'categoriaId': categoriaId,
        'tipo': _serializeObjectiveType(tipo),
        if (mesAno != null) 'mesAno': mesAno,
      },
    );

    final goal = _apiClient.parseResponse<GoalModel>(
      response,
      (json) => GoalModel.fromJson(json),
    );

    if (goal != null) {
      return goal;
    }

    throw Exception('Erro ao criar objetivo');
  }

  @override
  Future<GoalModel> getGoalById(int id) async {
    final response = await _apiClient.get('/objectives/$id');

    final goal = _apiClient.parseResponse<GoalModel>(
      response,
      (json) => GoalModel.fromJson(json),
    );

    if (goal != null) {
      return goal;
    }

    throw Exception('Objetivo não encontrado');
  }

  @override
  Future<List<GoalModel>> getAllGoals() async {
    final response = await _apiClient.get('/objectives');

    return _apiClient.parseListResponse<GoalModel>(
      response,
      (json) => GoalModel.fromJson(json),
    );
  }

  @override
  Future<List<GoalModel>> getGoalsByMonth(String mesAno) async {
    final response = await _apiClient.get('/objectives/month/$mesAno');

    return _apiClient.parseListResponse<GoalModel>(
      response,
      (json) => GoalModel.fromJson(json),
    );
  }

  @override
  Future<List<GoalModel>> getGoalsByTipo(String tipo) async {
    final response = await _apiClient.get('/objectives/tipo/$tipo');

    return _apiClient.parseListResponse<GoalModel>(
      response,
      (json) => GoalModel.fromJson(json),
    );
  }

  @override
  Future<void> updateGoalValue({
    required int id,
    required double novoValor,
  }) async {
    await _apiClient.patch(
      '/objectives/$id/value',
      queryParameters: {'novoValor': novoValor.toString()},
    );
  }

  @override
  Future<GoalModel> updateGoal({
    required int id,
    required String descricao,
    required double valorObjetivo,
    required double valorAtual,
  }) async {
    final response = await _apiClient.put(
      '/objectives/$id',
      body: {
        'descricao': descricao,
        'valorObjetivo': valorObjetivo,
        'valorAtual': valorAtual,
      },
    );

    final goal = _apiClient.parseResponse<GoalModel>(
      response,
      (json) => GoalModel.fromJson(json),
    );

    if (goal != null) {
      return goal;
    }

    throw Exception('Erro ao atualizar objetivo');
  }

  @override
  Future<void> deactivateGoal(int id) async {
    await _apiClient.delete('/objectives/$id');
  }

  @override
  Future<List<GoalModel>> getGoalsCloseToCompletion() async {
    final response = await _apiClient.get('/objectives/proximos-de-cumprir');

    return _apiClient.parseListResponse<GoalModel>(
      response,
      (json) => GoalModel.fromJson(json),
    );
  }

  @override
  Future<List<GoalModel>> getOverLimitGoals() async {
    final response = await _apiClient.get('/objectives/limites-ultrapassados');

    return _apiClient.parseListResponse<GoalModel>(
      response,
      (json) => GoalModel.fromJson(json),
    );
  }

  @override
  Future<Map<String, dynamic>> getStatistics() async {
    final response = await _apiClient.get('/objectives/estatisticas');

    if (response.statusCode >= 200 && response.statusCode < 300) {
      final json = jsonDecode(response.body);
      return json as Map<String, dynamic>;
    }

    throw Exception('Erro ao buscar estatísticas');
  }

  String _serializeObjectiveType(ObjectiveType type) {
    switch (type) {
      case ObjectiveType.limiteCategoria:
        return 'LIMITE_CATEGORIA';
      case ObjectiveType.metaEconomiaMes:
        return 'META_ECONOMIA_MES';
      case ObjectiveType.metaInvestimento:
        return 'META_INVESTIMENTO';
    }
  }
}
