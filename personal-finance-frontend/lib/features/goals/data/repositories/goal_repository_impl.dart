import '../../domain/entities/goal_entity.dart';
import '../../domain/repositories/goal_repository.dart';
import '../datasources/goal_remote_datasource.dart';

class GoalRepositoryImpl implements GoalRepository {
  final GoalRemoteDataSource _remoteDataSource;

  GoalRepositoryImpl({required GoalRemoteDataSource remoteDataSource})
      : _remoteDataSource = remoteDataSource;

  @override
  Future<GoalEntity> createGoal({
    required String descricao,
    required double valorObjetivo,
    required int categoriaId,
    required ObjectiveType tipo,
    String? mesAno,
  }) async {
    return await _remoteDataSource.createGoal(
      descricao: descricao,
      valorObjetivo: valorObjetivo,
      categoriaId: categoriaId,
      tipo: tipo,
      mesAno: mesAno,
    );
  }

  @override
  Future<GoalEntity> getGoalById(int id) async {
    return await _remoteDataSource.getGoalById(id);
  }

  @override
  Future<List<GoalEntity>> getAllGoals() async {
    final models = await _remoteDataSource.getAllGoals();
    return models.cast<GoalEntity>();
  }

  @override
  Future<List<GoalEntity>> getGoalsByMonth(String mesAno) async {
    final models = await _remoteDataSource.getGoalsByMonth(mesAno);
    return models.cast<GoalEntity>();
  }

  @override
  Future<List<GoalEntity>> getGoalsByTipo(String tipo) async {
    final models = await _remoteDataSource.getGoalsByTipo(tipo);
    return models.cast<GoalEntity>();
  }

  @override
  Future<void> updateGoalValue({
    required int id,
    required double novoValor,
  }) async {
    return await _remoteDataSource.updateGoalValue(
      id: id,
      novoValor: novoValor,
    );
  }

  @override
  Future<GoalEntity> updateGoal({
    required int id,
    required String descricao,
    required double valorObjetivo,
    required double valorAtual,
  }) async {
    return await _remoteDataSource.updateGoal(
      id: id,
      descricao: descricao,
      valorObjetivo: valorObjetivo,
      valorAtual: valorAtual,
    );
  }

  @override
  Future<void> deactivateGoal(int id) async {
    return await _remoteDataSource.deactivateGoal(id);
  }

  @override
  Future<List<GoalEntity>> getGoalsCloseToCompletion() async {
    final models = await _remoteDataSource.getGoalsCloseToCompletion();
    return models.cast<GoalEntity>();
  }

  @override
  Future<List<GoalEntity>> getOverLimitGoals() async {
    final models = await _remoteDataSource.getOverLimitGoals();
    return models.cast<GoalEntity>();
  }
}
