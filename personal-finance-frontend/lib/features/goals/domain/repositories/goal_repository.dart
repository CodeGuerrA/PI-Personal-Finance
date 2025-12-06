import '../entities/goal_entity.dart';

abstract class GoalRepository {
  Future<GoalEntity> createGoal({
    required String descricao,
    required double valorObjetivo,
    required int categoriaId,
    required ObjectiveType tipo,
    String? mesAno,
  });

  Future<GoalEntity> getGoalById(int id);

  Future<List<GoalEntity>> getAllGoals();

  Future<List<GoalEntity>> getGoalsByMonth(String mesAno);

  Future<List<GoalEntity>> getGoalsByTipo(String tipo);

  Future<void> updateGoalValue({
    required int id,
    required double novoValor,
  });

  Future<GoalEntity> updateGoal({
    required int id,
    required String descricao,
    required double valorObjetivo,
    required double valorAtual,
  });

  Future<void> deactivateGoal(int id);

  Future<List<GoalEntity>> getGoalsCloseToCompletion();

  Future<List<GoalEntity>> getOverLimitGoals();
}
