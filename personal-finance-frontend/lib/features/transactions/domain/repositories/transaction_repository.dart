import '../entities/transaction_entity.dart';

abstract class TransactionRepository {
  Future<TransactionEntity> createTransaction({
    required double valor,
    required String tipo,
    required PaymentMethod metodoPagamento,
    required int categoriaId,
    required String descricao,
    required DateTime data,
    String? observacoes,
  });

  Future<TransactionEntity> getTransactionById(int id);

  Future<List<TransactionEntity>> getAllTransactions();

  Future<TransactionEntity> updateTransaction({
    required int id,
    required double valor,
    required String tipo,
    required PaymentMethod metodoPagamento,
    required int categoriaId,
    required String descricao,
    required DateTime data,
    String? observacoes,
  });

  Future<void> deleteTransaction(int id);
}
