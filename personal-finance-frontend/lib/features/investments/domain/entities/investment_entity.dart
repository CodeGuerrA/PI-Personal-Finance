import 'package:equatable/equatable.dart';

enum InvestmentType {
  cripto,        // CRIPTO - Criptomoedas
  acao,          // ACAO - Ações
  fundo,         // FUNDO - Fundos de Investimento
  rendaFixa,     // RENDA_FIXA - Renda Fixa
  tesouroDireto, // TESOURO_DIRETO - Tesouro Direto
  cdb,           // CDB - Certificado de Depósito Bancário
}

class InvestmentEntity extends Equatable {
  final String id;
  final InvestmentType type;
  final String name;
  final String symbol;
  final double quantity;
  final double buyPrice;
  final double? currentPrice;
  final String broker;
  final bool isActive;

  // Valores calculados que vêm do backend
  final double? totalInvested;
  final double? currentValue;
  final double? profit;
  final double? profitPercent;

  const InvestmentEntity({
    required this.id,
    required this.type,
    required this.name,
    required this.symbol,
    required this.quantity,
    required this.buyPrice,
    required this.broker,
    this.currentPrice,
    this.isActive = true,
    this.totalInvested,
    this.currentValue,
    this.profit,
    this.profitPercent,
  });

  // Getters que usam os valores do backend ou fazem fallback para cálculo local
  double get valorTotalInvestido => totalInvested ?? (quantity * buyPrice);

  double get valorAtual => currentValue ?? ((currentPrice ?? buyPrice) * quantity);

  double get lucro => profit ?? (valorAtual - valorTotalInvestido);

  double get rentabilidade => profitPercent ??
      (valorTotalInvestido == 0 ? 0 : (lucro / valorTotalInvestido) * 100);

  @override
  List<Object?> get props => [
        id,
        type,
        name,
        symbol,
        quantity,
        buyPrice,
        currentPrice,
        broker,
        isActive,
        totalInvested,
        currentValue,
        profit,
        profitPercent,
      ];
}
