import 'package:sgfi/features/transactions/domain/entities/transaction_entity.dart';

enum RecurrenceFrequency {
  daily,
  weekly,
  monthly,
  yearly,
}

class RecurrenceEntity {
  final String id;
  final String name;
  final double amount;
  final TransactionType type;
  final String categoryName;
  final int? categoriaId;
  final RecurrenceFrequency frequency;
  final DateTime startDate;
  final DateTime? endDate;
  final int? diaVencimento;
  final String? observacoes;
  final bool isActive;

  RecurrenceEntity({
    required this.id,
    required this.name,
    required this.amount,
    required this.type,
    required this.categoryName,
    this.categoriaId,
    required this.frequency,
    required this.startDate,
    required this.isActive,
    this.endDate,
    this.diaVencimento,
    this.observacoes,
  });

  /// Próxima data de cobrança, calculada com base na frequência.
  /// Retorna null se a recorrência não estiver mais ativa
  /// ou se passou da data final (quando houver).
  DateTime? get nextDueDate {
    if (!isActive) return null;

    final now = DateTime.now();
    final today = DateTime(now.year, now.month, now.day);

    // Começa a partir da data de início (normalizada pro dia)
    DateTime next =
        DateTime(startDate.year, startDate.month, startDate.day);

    // Avança até ser hoje ou depois de hoje
    while (next.isBefore(today)) {
      switch (frequency) {
        case RecurrenceFrequency.daily:
          next = next.add(const Duration(days: 1));
          break;
        case RecurrenceFrequency.weekly:
          next = next.add(const Duration(days: 7));
          break;
        case RecurrenceFrequency.monthly:
          next = DateTime(next.year, next.month + 1, next.day);
          break;
        case RecurrenceFrequency.yearly:
          next = DateTime(next.year + 1, next.month, next.day);
          break;
      }

      // Se passou da data final (quando existir), não tem mais próxima
      if (endDate != null && next.isAfter(endDate!)) {
        return null;
      }
    }

    return next;
  }
}
