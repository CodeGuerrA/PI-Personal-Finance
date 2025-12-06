import '../../../../core/network/api_client.dart';
import '../models/recurrence_model.dart';
import 'recurrence_remote_datasource.dart';

class RecurrenceRemoteDataSourceImpl implements RecurrenceRemoteDataSource {
  final ApiClient _apiClient;

  RecurrenceRemoteDataSourceImpl({ApiClient? apiClient})
      : _apiClient = apiClient ?? ApiClient();

  @override
  Future<RecurrenceModel> createRecurrence({
    required double valor,
    required String tipo,
    required int categoriaId,
    required String descricao,
    required DateTime dataInicio,
    required String frequencia,
    DateTime? dataFim,
    int? diaVencimento,
    String? observacoes,
  }) async {
    final response = await _apiClient.post(
      '/recurring-transactions',
      body: {
        'valor': valor,
        'tipo': tipo,
        'categoriaId': categoriaId,
        'descricao': descricao,
        'dataInicio': dataInicio.toIso8601String().split('T')[0],
        'frequencia': frequencia,
        if (dataFim != null) 'dataFim': dataFim.toIso8601String().split('T')[0],
        if (diaVencimento != null) 'diaVencimento': diaVencimento,
        if (observacoes != null) 'observacoes': observacoes,
      },
    );

    final recurrence = _apiClient.parseResponse<RecurrenceModel>(
      response,
      (json) => RecurrenceModel.fromJson(json),
    );

    if (recurrence != null) {
      return recurrence;
    }

    throw Exception('Erro ao criar transação recorrente');
  }

  @override
  Future<RecurrenceModel> getRecurrenceById(int id) async {
    final response = await _apiClient.get('/recurring-transactions/$id');

    final recurrence = _apiClient.parseResponse<RecurrenceModel>(
      response,
      (json) => RecurrenceModel.fromJson(json),
    );

    if (recurrence != null) {
      return recurrence;
    }

    throw Exception('Transação recorrente não encontrada');
  }

  @override
  Future<List<RecurrenceModel>> getAllRecurrences() async {
    final response = await _apiClient.get('/recurring-transactions');

    return _apiClient.parseListResponse<RecurrenceModel>(
      response,
      (json) => RecurrenceModel.fromJson(json),
    );
  }

  @override
  Future<List<RecurrenceModel>> getRecurrencesByFrequencia(
      String frequencia) async {
    final response =
        await _apiClient.get('/recurring-transactions/frequencia/$frequencia');

    return _apiClient.parseListResponse<RecurrenceModel>(
      response,
      (json) => RecurrenceModel.fromJson(json),
    );
  }

  @override
  Future<List<RecurrenceModel>> getRecurrencesByCategoria(
      int categoriaId) async {
    final response =
        await _apiClient.get('/recurring-transactions/categoria/$categoriaId');

    return _apiClient.parseListResponse<RecurrenceModel>(
      response,
      (json) => RecurrenceModel.fromJson(json),
    );
  }

  @override
  Future<List<RecurrenceModel>> getRecurrencesByTipo(String tipo) async {
    final response = await _apiClient.get('/recurring-transactions/tipo/$tipo');

    return _apiClient.parseListResponse<RecurrenceModel>(
      response,
      (json) => RecurrenceModel.fromJson(json),
    );
  }

  @override
  Future<List<RecurrenceModel>> getActiveRecurrences() async {
    final response = await _apiClient.get('/recurring-transactions/ativas');

    return _apiClient.parseListResponse<RecurrenceModel>(
      response,
      (json) => RecurrenceModel.fromJson(json),
    );
  }

  @override
  Future<RecurrenceModel> updateRecurrence({
    required int id,
    required double valor,
    required String tipo,
    required int categoriaId,
    required String descricao,
    required DateTime dataInicio,
    required String frequencia,
    DateTime? dataFim,
    int? diaVencimento,
    String? observacoes,
    bool? ativa,
  }) async {
    final response = await _apiClient.put(
      '/recurring-transactions/$id',
      body: {
        'valor': valor,
        'tipo': tipo,
        'categoriaId': categoriaId,
        'descricao': descricao,
        'dataInicio': dataInicio.toIso8601String().split('T')[0],
        'frequencia': frequencia,
        if (dataFim != null) 'dataFim': dataFim.toIso8601String().split('T')[0],
        if (diaVencimento != null) 'diaVencimento': diaVencimento,
        if (observacoes != null) 'observacoes': observacoes,
        if (ativa != null) 'ativa': ativa,
      },
    );

    final recurrence = _apiClient.parseResponse<RecurrenceModel>(
      response,
      (json) => RecurrenceModel.fromJson(json),
    );

    if (recurrence != null) {
      return recurrence;
    }

    throw Exception('Erro ao atualizar transação recorrente');
  }

  @override
  Future<void> deleteRecurrence(int id) async {
    await _apiClient.delete('/recurring-transactions/$id');
  }
}
