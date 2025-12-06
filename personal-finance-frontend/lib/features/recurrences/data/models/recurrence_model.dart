import '../../../transactions/domain/entities/transaction_entity.dart';
import '../../domain/entities/recurrence_entity.dart';

class RecurrenceModel extends RecurrenceEntity {
  RecurrenceModel({
    required super.id,
    required super.name,
    required super.amount,
    required super.type,
    required super.categoryName,
    super.categoriaId,
    required super.frequency,
    required super.startDate,
    required super.isActive,
    super.endDate,
    super.diaVencimento,
    super.observacoes,
    this.proximaData,
    this.dataCriacao,
    this.usuarioId,
  });

  final DateTime? proximaData;
  final DateTime? dataCriacao;
  final int? usuarioId;

  factory RecurrenceModel.fromJson(Map<String, dynamic> json) {
    return RecurrenceModel(
      id: json['id'].toString(),
      name: json['descricao'] ?? '',
      amount: (json['valor'] as num?)?.toDouble() ?? 0.0,
      type: _parseTransactionType(json['tipo']),
      categoryName: '', // será preenchido via categoriaId
      frequency: _parseFrequency(json['frequencia']),
      startDate: json['dataInicio'] != null
          ? DateTime.parse(json['dataInicio'])
          : DateTime.now(),
      isActive: json['ativa'] ?? true,
      endDate: json['dataFim'] != null ? DateTime.parse(json['dataFim']) : null,
      categoriaId: json['categoriaId'],
      diaVencimento: json['diaVencimento'],
      proximaData: json['proximaData'] != null
          ? DateTime.parse(json['proximaData'])
          : null,
      observacoes: json['observacoes'],
      dataCriacao: json['dataCriacao'] != null
          ? DateTime.parse(json['dataCriacao'])
          : null,
      usuarioId: json['usuarioId'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'descricao': name,
      'valor': amount,
      'tipo': type == TransactionType.income ? 'RECEITA' : 'DESPESA',
      'categoriaId': categoriaId,
      'dataInicio': startDate.toIso8601String().split('T')[0],
      'dataFim': endDate?.toIso8601String().split('T')[0],
      'frequencia': _serializeFrequency(frequency),
      'diaVencimento': diaVencimento,
      'proximaData': proximaData?.toIso8601String().split('T')[0],
      'ativa': isActive,
      'observacoes': observacoes,
    };
  }

  static TransactionType _parseTransactionType(String? tipo) {
    if (tipo == null) return TransactionType.expense;
    switch (tipo.toUpperCase()) {
      case 'RECEITA':
        return TransactionType.income;
      case 'DESPESA':
        return TransactionType.expense;
      default:
        return TransactionType.expense;
    }
  }

  static RecurrenceFrequency _parseFrequency(String? frequencia) {
    if (frequencia == null) return RecurrenceFrequency.monthly;
    switch (frequencia.toUpperCase()) {
      case 'DIARIA':
        return RecurrenceFrequency.daily;
      case 'SEMANAL':
        return RecurrenceFrequency.weekly;
      case 'MENSAL':
        return RecurrenceFrequency.monthly;
      case 'ANUAL':
        return RecurrenceFrequency.yearly;
      default:
        return RecurrenceFrequency.monthly;
    }
  }

  static String _serializeFrequency(RecurrenceFrequency frequency) {
    switch (frequency) {
      case RecurrenceFrequency.daily:
        return 'DIARIA';
      case RecurrenceFrequency.weekly:
        return 'SEMANAL';
      case RecurrenceFrequency.monthly:
        return 'MENSAL';
      case RecurrenceFrequency.yearly:
        return 'ANUAL';
    }
  }
}
