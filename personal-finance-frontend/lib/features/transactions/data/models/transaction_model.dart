import '../../domain/entities/transaction_entity.dart';

class TransactionModel extends TransactionEntity {
  TransactionModel({
    required super.id,
    required super.description,
    required super.amount,
    required super.date,
    required super.category,
    super.categoriaId,
    required super.type,
    required super.paymentMethod,
    super.observacoes,
    this.transacaoRecorrenteId,
    this.investimentoId,
    this.dataCriacao,
    this.usuarioId,
  });

  final int? transacaoRecorrenteId;
  final int? investimentoId;
  final DateTime? dataCriacao;
  final int? usuarioId;

  factory TransactionModel.fromJson(Map<String, dynamic> json) {
    return TransactionModel(
      id: json['id'].toString(),
      description: json['descricao'] ?? '',
      amount: (json['valor'] as num?)?.toDouble() ?? 0.0,
      date: json['data'] != null ? DateTime.parse(json['data']) : DateTime.now(),
      category: '', // será preenchido via categoriaId
      type: _parseTransactionType(json['tipo']),
      paymentMethod: _parsePaymentMethod(json['metodoPagamento']),
      observacoes: json['observacoes'],
      categoriaId: json['categoriaId'],
      transacaoRecorrenteId: json['transacaoRecorrenteId'],
      investimentoId: json['investimentoId'],
      dataCriacao: json['dataCriacao'] != null
          ? DateTime.parse(json['dataCriacao'])
          : null,
      usuarioId: json['usuarioId'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'descricao': description,
      'valor': amount,
      'data': date.toIso8601String().split('T')[0],
      'tipo': type == TransactionType.income ? 'RECEITA' : 'DESPESA',
      'metodoPagamento': _serializePaymentMethod(paymentMethod),
      if (observacoes != null) 'observacoes': observacoes,
      if (categoriaId != null) 'categoriaId': categoriaId,
      if (transacaoRecorrenteId != null) 'transacaoRecorrenteId': transacaoRecorrenteId,
      if (investimentoId != null) 'investimentoId': investimentoId,
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

  static PaymentMethod _parsePaymentMethod(String? metodo) {
    if (metodo == null) return PaymentMethod.dinheiro;
    switch (metodo.toUpperCase()) {
      case 'DINHEIRO':
        return PaymentMethod.dinheiro;
      case 'CARTAO_CREDITO':
        return PaymentMethod.cartaoCredito;
      case 'CARTAO_DEBITO':
        return PaymentMethod.cartaoDebito;
      case 'PIX':
        return PaymentMethod.pix;
      default:
        return PaymentMethod.dinheiro;
    }
  }

  static String _serializePaymentMethod(PaymentMethod method) {
    switch (method) {
      case PaymentMethod.dinheiro:
        return 'DINHEIRO';
      case PaymentMethod.cartaoCredito:
        return 'CARTAO_CREDITO';
      case PaymentMethod.cartaoDebito:
        return 'CARTAO_DEBITO';
      case PaymentMethod.pix:
        return 'PIX';
    }
  }
}
