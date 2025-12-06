import '../../../../core/network/api_client.dart';
import '../models/investment_movement_model.dart';
import 'investment_movement_remote_datasource.dart';

class InvestmentMovementRemoteDataSourceImpl
    implements InvestmentMovementRemoteDataSource {
  final ApiClient _apiClient;

  InvestmentMovementRemoteDataSourceImpl({ApiClient? apiClient})
      : _apiClient = apiClient ?? ApiClient();

  @override
  Future<InvestmentMovementModel> createMovement({
    required int investmentId,
    required String tipoMovimentacao,
    required double quantidade,
    required double valorUnitario,
    required double valorTotal,
    required double taxas,
    required DateTime dataMovimentacao,
    String? observacoes,
  }) async {
    final response = await _apiClient.post(
      '/investment-movements',
      body: {
        'investmentId': investmentId,
        'tipoMovimentacao': tipoMovimentacao,
        'quantidade': quantidade,
        'valorUnitario': valorUnitario,
        'valorTotal': valorTotal,
        'taxas': taxas,
        'dataMovimentacao': dataMovimentacao.toIso8601String().split('T')[0],
        if (observacoes != null) 'observacoes': observacoes,
      },
    );

    final movement = _apiClient.parseResponse<InvestmentMovementModel>(
      response,
      (json) => InvestmentMovementModel.fromJson(json),
    );

    if (movement != null) {
      return movement;
    }

    throw Exception('Erro ao criar movimentação');
  }

  @override
  Future<InvestmentMovementModel> getMovementById(int id) async {
    final response = await _apiClient.get('/investment-movements/$id');

    final movement = _apiClient.parseResponse<InvestmentMovementModel>(
      response,
      (json) => InvestmentMovementModel.fromJson(json),
    );

    if (movement != null) {
      return movement;
    }

    throw Exception('Movimentação não encontrada');
  }

  @override
  Future<List<InvestmentMovementModel>> getAllMovements() async {
    final response = await _apiClient.get('/investment-movements');

    return _apiClient.parseListResponse<InvestmentMovementModel>(
      response,
      (json) => InvestmentMovementModel.fromJson(json),
    );
  }

  @override
  Future<List<InvestmentMovementModel>> getMovementsByInvestment(
      int investmentId) async {
    final response = await _apiClient.get(
      '/investment-movements',
      queryParameters: {'investmentId': investmentId.toString()},
    );

    return _apiClient.parseListResponse<InvestmentMovementModel>(
      response,
      (json) => InvestmentMovementModel.fromJson(json),
    );
  }

  @override
  Future<InvestmentMovementModel> updateMovement({
    required int id,
    required double quantidade,
    required double valorUnitario,
    required double valorTotal,
    required double taxas,
    required DateTime dataMovimentacao,
    String? observacoes,
  }) async {
    final response = await _apiClient.put(
      '/investment-movements/$id',
      body: {
        'quantidade': quantidade,
        'valorUnitario': valorUnitario,
        'valorTotal': valorTotal,
        'taxas': taxas,
        'dataMovimentacao': dataMovimentacao.toIso8601String().split('T')[0],
        if (observacoes != null) 'observacoes': observacoes,
      },
    );

    final movement = _apiClient.parseResponse<InvestmentMovementModel>(
      response,
      (json) => InvestmentMovementModel.fromJson(json),
    );

    if (movement != null) {
      return movement;
    }

    throw Exception('Erro ao atualizar movimentação');
  }

  @override
  Future<void> deleteMovement(int id) async {
    await _apiClient.delete('/investment-movements/$id');
  }
}
