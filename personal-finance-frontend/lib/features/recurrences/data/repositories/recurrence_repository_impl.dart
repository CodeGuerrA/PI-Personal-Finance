import '../../domain/entities/recurrence_entity.dart';
import '../../domain/repositories/recurrence_repository.dart';
import '../datasources/recurrence_remote_datasource.dart';

class RecurrenceRepositoryImpl implements RecurrenceRepository {
  final RecurrenceRemoteDataSource _remoteDataSource;

  RecurrenceRepositoryImpl({required RecurrenceRemoteDataSource remoteDataSource})
      : _remoteDataSource = remoteDataSource;

  @override
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
  }) async {
    return await _remoteDataSource.createRecurrence(
      valor: valor,
      tipo: tipo,
      categoriaId: categoriaId,
      descricao: descricao,
      dataInicio: dataInicio,
      frequencia: frequencia,
      dataFim: dataFim,
      diaVencimento: diaVencimento,
      observacoes: observacoes,
    );
  }

  @override
  Future<RecurrenceEntity> getRecurrenceById(int id) async {
    return await _remoteDataSource.getRecurrenceById(id);
  }

  @override
  Future<List<RecurrenceEntity>> getAllRecurrences() async {
    final models = await _remoteDataSource.getAllRecurrences();
    return models.cast<RecurrenceEntity>();
  }

  @override
  Future<List<RecurrenceEntity>> getRecurrencesByFrequencia(
      String frequencia) async {
    final models = await _remoteDataSource.getRecurrencesByFrequencia(frequencia);
    return models.cast<RecurrenceEntity>();
  }

  @override
  Future<List<RecurrenceEntity>> getRecurrencesByCategoria(
      int categoriaId) async {
    final models = await _remoteDataSource.getRecurrencesByCategoria(categoriaId);
    return models.cast<RecurrenceEntity>();
  }

  @override
  Future<List<RecurrenceEntity>> getRecurrencesByTipo(String tipo) async {
    final models = await _remoteDataSource.getRecurrencesByTipo(tipo);
    return models.cast<RecurrenceEntity>();
  }

  @override
  Future<List<RecurrenceEntity>> getActiveRecurrences() async {
    final models = await _remoteDataSource.getActiveRecurrences();
    return models.cast<RecurrenceEntity>();
  }

  @override
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
  }) async {
    return await _remoteDataSource.updateRecurrence(
      id: id,
      valor: valor,
      tipo: tipo,
      categoriaId: categoriaId,
      descricao: descricao,
      dataInicio: dataInicio,
      frequencia: frequencia,
      dataFim: dataFim,
      diaVencimento: diaVencimento,
      observacoes: observacoes,
      ativa: ativa,
    );
  }

  @override
  Future<void> deleteRecurrence(int id) async {
    return await _remoteDataSource.deleteRecurrence(id);
  }
}
