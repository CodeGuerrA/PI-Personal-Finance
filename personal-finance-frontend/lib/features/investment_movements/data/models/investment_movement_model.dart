import '../../domain/entities/investment_movement_entity.dart';

class InvestmentMovementModel extends InvestmentMovementEntity {
  const InvestmentMovementModel({
    required super.id,
    required super.investmentId,
    required super.investmentName,
    required super.investmentSymbol,
    required super.type,
    required super.typeDescription,
    required super.quantity,
    required super.unitPrice,
    required super.totalValue,
    required super.fees,
    required super.date,
    super.observations,
  });

  factory InvestmentMovementModel.fromJson(Map<String, dynamic> json) {
    return InvestmentMovementModel(
      id: json['id'].toString(),
      investmentId: json['investmentId'].toString(),
      investmentName: json['investmentNomeAtivo'] ?? '',
      investmentSymbol: json['investmentSimbolo'] ?? '',
      type: _parseMovementType(json['tipoMovimentacao']),
      typeDescription: json['tipoMovimentacaoDescricao'] ?? '',
      quantity: (json['quantidade'] as num?)?.toDouble() ?? 0.0,
      unitPrice: (json['valorUnitario'] as num?)?.toDouble() ?? 0.0,
      totalValue: (json['valorTotal'] as num?)?.toDouble() ?? 0.0,
      fees: (json['taxas'] as num?)?.toDouble() ?? 0.0,
      date: json['dataMovimentacao'] != null
          ? DateTime.parse(json['dataMovimentacao'])
          : DateTime.now(),
      observations: json['observacoes'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'investmentId': investmentId,
      'investmentNomeAtivo': investmentName,
      'investmentSimbolo': investmentSymbol,
      'tipoMovimentacao': _serializeMovementType(type),
      'tipoMovimentacaoDescricao': typeDescription,
      'quantidade': quantity,
      'valorUnitario': unitPrice,
      'valorTotal': totalValue,
      'taxas': fees,
      'dataMovimentacao': date.toIso8601String().split('T')[0],
      'observacoes': observations,
    };
  }

  static MovementType _parseMovementType(String? tipo) {
    if (tipo == null) return MovementType.compra;
    switch (tipo.toUpperCase()) {
      case 'COMPRA':
        return MovementType.compra;
      case 'VENDA':
        return MovementType.venda;
      case 'DIVIDENDO':
        return MovementType.dividendo;
      case 'RENDIMENTO':
        return MovementType.rendimento;
      case 'AJUSTE':
        return MovementType.ajuste;
      default:
        return MovementType.compra;
    }
  }

  static String _serializeMovementType(MovementType type) {
    switch (type) {
      case MovementType.compra:
        return 'COMPRA';
      case MovementType.venda:
        return 'VENDA';
      case MovementType.dividendo:
        return 'DIVIDENDO';
      case MovementType.rendimento:
        return 'RENDIMENTO';
      case MovementType.ajuste:
        return 'AJUSTE';
    }
  }
}
