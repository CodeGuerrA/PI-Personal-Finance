enum MovementType {
  compra,      // COMPRA
  venda,       // VENDA
  dividendo,   // DIVIDENDO
  rendimento,  // RENDIMENTO
  ajuste,      // AJUSTE
}

class InvestmentMovementEntity {
  final String id;
  final String investmentId;
  final String investmentName;
  final String investmentSymbol;
  final MovementType type;
  final String typeDescription;
  final double quantity;
  final double unitPrice;
  final double totalValue;
  final double fees;
  final DateTime date;
  final String? observations;

  const InvestmentMovementEntity({
    required this.id,
    required this.investmentId,
    required this.investmentName,
    required this.investmentSymbol,
    required this.type,
    required this.typeDescription,
    required this.quantity,
    required this.unitPrice,
    required this.totalValue,
    required this.fees,
    required this.date,
    this.observations,
  });

  double get netValue => totalValue - fees;

  bool get isIncome =>
      type == MovementType.venda ||
      type == MovementType.dividendo ||
      type == MovementType.rendimento;

  bool get isExpense => type == MovementType.compra;
}
