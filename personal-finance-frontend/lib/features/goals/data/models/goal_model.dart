import '../../domain/entities/goal_entity.dart';

class GoalModel extends GoalEntity {
  GoalModel({
    required super.id,
    required super.name,
    required super.targetAmount,
    required super.currentAmount,
    required super.isActive,
    super.dueDate,
    super.type,
    super.percentualAtingido,
    super.saldoRestante,
    super.statusAlerta,
    this.categoriaId,
    this.categoriaNome,
    this.mesAno,
  });

  final int? categoriaId;
  final String? categoriaNome;
  final String? mesAno;

  factory GoalModel.fromJson(Map<String, dynamic> json) {
    return GoalModel(
      id: json['id'].toString(),
      name: json['descricao'] ?? '',
      targetAmount: (json['valorObjetivo'] as num?)?.toDouble() ?? 0.0,
      currentAmount: (json['valorAtual'] as num?)?.toDouble() ?? 0.0,
      isActive: json['ativa'] ?? true,
      dueDate: json['mesAno'] != null ? _parseMesAno(json['mesAno']) : null,
      type: _parseObjectiveType(json['tipo']),
      categoriaId: json['categoriaId'],
      categoriaNome: json['categoriaNome'],
      percentualAtingido: (json['percentualAtingido'] as num?)?.toDouble(),
      saldoRestante: (json['saldoRestante'] as num?)?.toDouble(),
      mesAno: json['mesAno'],
      statusAlerta: json['statusAlerta'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'descricao': name,
      'valorObjetivo': targetAmount,
      'valorAtual': currentAmount,
      'ativa': isActive,
      'categoriaId': categoriaId,
      'categoriaNome': categoriaNome,
      'percentualAtingido': percentualAtingido,
      'saldoRestante': saldoRestante,
      'mesAno': mesAno,
      'tipo': type != null ? _serializeObjectiveType(type!) : null,
      'statusAlerta': statusAlerta,
    };
  }

  static DateTime? _parseMesAno(String? mesAno) {
    if (mesAno == null || mesAno.isEmpty) return null;
    try {
      final parts = mesAno.split('-');
      if (parts.length == 2) {
        final year = int.parse(parts[0]);
        final month = int.parse(parts[1]);
        return DateTime(year, month);
      }
    } catch (e) {
      return null;
    }
    return null;
  }

  static ObjectiveType? _parseObjectiveType(String? tipo) {
    if (tipo == null) return null;
    switch (tipo.toUpperCase()) {
      case 'LIMITE_CATEGORIA':
        return ObjectiveType.limiteCategoria;
      case 'META_ECONOMIA_MES':
        return ObjectiveType.metaEconomiaMes;
      case 'META_INVESTIMENTO':
        return ObjectiveType.metaInvestimento;
      default:
        return null;
    }
  }

  static String _serializeObjectiveType(ObjectiveType type) {
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
