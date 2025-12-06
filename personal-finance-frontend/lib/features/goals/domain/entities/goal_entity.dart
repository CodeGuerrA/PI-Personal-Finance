enum ObjectiveType {
  limiteCategoria,    // LIMITE_CATEGORIA - Orçamento máximo para uma categoria
  metaEconomiaMes,    // META_ECONOMIA_MES - Quanto quer economizar no mês
  metaInvestimento,   // META_INVESTIMENTO - Quanto quer investir no período
}

class GoalEntity {
  final String id;
  final String name;
  final double targetAmount;
  final double currentAmount;
  final DateTime? dueDate;
  final bool isActive;
  final ObjectiveType? type;

  // Valores calculados que vêm do backend
  final double? percentualAtingido;
  final double? saldoRestante;
  final String? statusAlerta;

  GoalEntity({
    required this.id,
    required this.name,
    required this.targetAmount,
    required this.currentAmount,
    required this.isActive,
    this.dueDate,
    this.type,
    this.percentualAtingido,
    this.saldoRestante,
    this.statusAlerta,
  });

  // Getter que usa o valor do backend ou faz fallback para cálculo local
  double get progress {
    if (percentualAtingido != null) {
      return (percentualAtingido! / 100).clamp(0, 2.0);
    }
    if (targetAmount <= 0) return 0;
    return (currentAmount / targetAmount).clamp(0, 2.0);
  }

  // Getter que usa o status do backend ou faz fallback para cálculo local
  String get statusLabel {
    if (statusAlerta != null) {
      switch (statusAlerta!.toUpperCase()) {
        case 'CUMPRIDA':
          return 'Atingida';
        case 'VERMELHO':
          return 'Ultrapassada';
        case 'AMARELO':
          return 'Em atenção (80%+)';
        case 'NENHUM':
        default:
          return 'Em progresso';
      }
    }

    // Fallback para cálculo local
    if (progress >= 1.0 && currentAmount > targetAmount) {
      return 'Ultrapassada';
    } else if (progress >= 1.0) {
      return 'Atingida';
    } else if (progress >= 0.8) {
      return 'Em atenção (80%+)';
    } else {
      return 'Em progresso';
    }
  }

  // Getter para saldo restante usando valor do backend ou calculando localmente
  double get remainingBalance {
    return saldoRestante ?? (targetAmount - currentAmount);
  }
}
