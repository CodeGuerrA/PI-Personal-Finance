import '../../domain/entities/transaction_entity.dart';
import '../models/transaction_model.dart';

abstract class TransactionRemoteDataSource {
  Future<TransactionModel> createTransaction({
    required double valor,
    required String tipo,
    required PaymentMethod metodoPagamento,
    required int categoriaId,
    required String descricao,
    required DateTime data,
    String? observacoes,
  });

  Future<TransactionModel> getTransactionById(int id);

  Future<List<TransactionModel>> getAllTransactions();

  Future<TransactionModel> updateTransaction({
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
