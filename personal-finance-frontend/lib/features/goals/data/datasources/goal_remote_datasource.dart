import '../../domain/entities/goal_entity.dart';
import '../models/goal_model.dart';

abstract class GoalRemoteDataSource {
  Future<GoalModel> createGoal({
    required String descricao,
    required double valorObjetivo,
    required int categoriaId,
    required ObjectiveType tipo,
    String? mesAno,
  });

  Future<GoalModel> getGoalById(int id);

  Future<List<GoalModel>> getAllGoals();

  Future<List<GoalModel>> getGoalsByMonth(String mesAno);

  Future<List<GoalModel>> getGoalsByTipo(String tipo);

  Future<void> updateGoalValue({
    required int id,
    required double novoValor,
  });

  Future<GoalModel> updateGoal({
    required int id,
    required String descricao,
    required double valorObjetivo,
    required double valorAtual,
  });

  Future<void> deactivateGoal(int id);

  Future<List<GoalModel>> getGoalsCloseToCompletion();

  Future<List<GoalModel>> getOverLimitGoals();

  Future<Map<String, dynamic>> getStatistics();
}
