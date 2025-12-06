import '../../domain/entities/investment_entity.dart';

class InvestmentModel extends InvestmentEntity {
  const InvestmentModel({
    required super.id,
    required super.type,
    required super.name,
    required super.symbol,
    required super.quantity,
    required super.buyPrice,
    required super.broker,
    super.currentPrice,
    super.isActive,
    super.totalInvested,
    super.currentValue,
    super.profit,
    super.profitPercent,
    this.dataCompra,
    this.observacoes,
    this.usuarioId,
  });

  final DateTime? dataCompra;
  final String? observacoes;
  final int? usuarioId;

  factory InvestmentModel.fromJson(Map<String, dynamic> json) {
    return InvestmentModel(
      id: json['id'].toString(),
      type: _parseInvestmentType(json['tipoInvestimento']),
      name: json['nomeAtivo'] ?? '',
      symbol: json['simbolo'] ?? '',
      quantity: (json['quantidade'] as num?)?.toDouble() ?? 0.0,
      buyPrice: (json['valorCompra'] as num?)?.toDouble() ?? 0.0,
      broker: json['corretora'] ?? '',
      currentPrice: (json['cotacaoAtual'] as num?)?.toDouble(),
      isActive: json['ativo'] ?? true,
      totalInvested: (json['valorTotalInvestido'] as num?)?.toDouble(),
      currentValue: (json['valorAtual'] as num?)?.toDouble(),
      profit: (json['lucro'] as num?)?.toDouble(),
      profitPercent: (json['rentabilidade'] as num?)?.toDouble(),
      dataCompra: json['dataCompra'] != null
          ? DateTime.parse(json['dataCompra'])
          : null,
      observacoes: json['observacoes'],
      usuarioId: json['usuarioId'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'tipoInvestimento': _serializeInvestmentType(type),
      'nomeAtivo': name,
      'simbolo': symbol,
      'quantidade': quantity,
      'valorCompra': buyPrice,
      'corretora': broker,
      'cotacaoAtual': currentPrice,
      'ativo': isActive,
      'dataCompra': dataCompra?.toIso8601String().split('T')[0],
      'observacoes': observacoes,
      'valorTotalInvestido': totalInvested,
      'valorAtual': currentValue,
      'lucro': profit,
      'rentabilidade': profitPercent,
      'usuarioId': usuarioId,
    };
  }

  static InvestmentType _parseInvestmentType(String? tipo) {
    if (tipo == null) return InvestmentType.rendaFixa;
    switch (tipo.toUpperCase()) {
      case 'CRIPTO':
        return InvestmentType.cripto;
      case 'ACAO':
        return InvestmentType.acao;
      case 'FUNDO':
        return InvestmentType.fundo;
      case 'RENDA_FIXA':
        return InvestmentType.rendaFixa;
      case 'TESOURO_DIRETO':
        return InvestmentType.tesouroDireto;
      case 'CDB':
        return InvestmentType.cdb;
      default:
        return InvestmentType.rendaFixa;
    }
  }

  static String _serializeInvestmentType(InvestmentType type) {
    switch (type) {
      case InvestmentType.cripto:
        return 'CRIPTO';
      case InvestmentType.acao:
        return 'ACAO';
      case InvestmentType.fundo:
        return 'FUNDO';
      case InvestmentType.rendaFixa:
        return 'RENDA_FIXA';
      case InvestmentType.tesouroDireto:
        return 'TESOURO_DIRETO';
      case InvestmentType.cdb:
        return 'CDB';
    }
  }
}
