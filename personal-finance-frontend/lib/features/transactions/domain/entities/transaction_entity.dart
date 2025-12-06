enum TransactionType {
  income,  // receita
  expense, // despesa
}

enum PaymentMethod {
  dinheiro,      // DINHEIRO - Dinheiro
  cartaoCredito, // CARTAO_CREDITO - Cartão de Crédito
  cartaoDebito,  // CARTAO_DEBITO - Cartão de Débito
  pix,           // PIX - PIX
}

class TransactionEntity {
  final String id;
  final String description;
  final double amount;
  final DateTime date;
  final String category;
  final int? categoriaId;
  final TransactionType type;
  final PaymentMethod paymentMethod;
  final String? observacoes;

  TransactionEntity({
    required this.id,
    required this.description,
    required this.amount,
    required this.date,
    required this.category,
    this.categoriaId,
    required this.type,
    required this.paymentMethod,
    this.observacoes,
  });
}
