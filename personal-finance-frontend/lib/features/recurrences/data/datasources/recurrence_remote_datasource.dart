import '../models/recurrence_model.dart';

abstract class RecurrenceRemoteDataSource {
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
  });

  Future<RecurrenceModel> getRecurrenceById(int id);

  Future<List<RecurrenceModel>> getAllRecurrences();

  Future<List<RecurrenceModel>> getRecurrencesByFrequencia(String frequencia);

  Future<List<RecurrenceModel>> getRecurrencesByCategoria(int categoriaId);

  Future<List<RecurrenceModel>> getRecurrencesByTipo(String tipo);

  Future<List<RecurrenceModel>> getActiveRecurrences();

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
  });

  Future<void> deleteRecurrence(int id);
}
