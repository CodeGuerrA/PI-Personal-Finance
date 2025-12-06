import '../entities/recurrence_entity.dart';

abstract class RecurrenceRepository {
  Future<RecurrenceEntity> createRecurrence({
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

  Future<RecurrenceEntity> getRecurrenceById(int id);

  Future<List<RecurrenceEntity>> getAllRecurrences();

  Future<List<RecurrenceEntity>> getRecurrencesByFrequencia(String frequencia);

  Future<List<RecurrenceEntity>> getRecurrencesByCategoria(int categoriaId);

  Future<List<RecurrenceEntity>> getRecurrencesByTipo(String tipo);

  Future<List<RecurrenceEntity>> getActiveRecurrences();

  Future<RecurrenceEntity> updateRecurrence({
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
