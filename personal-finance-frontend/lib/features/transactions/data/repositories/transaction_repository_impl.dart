import '../../domain/entities/transaction_entity.dart';
import '../../domain/repositories/transaction_repository.dart';
import '../datasources/transaction_remote_datasource.dart';

class TransactionRepositoryImpl implements TransactionRepository {
  final TransactionRemoteDataSource _remoteDataSource;

  TransactionRepositoryImpl({required TransactionRemoteDataSource remoteDataSource})
      : _remoteDataSource = remoteDataSource;

  @override
  Future<TransactionEntity> createTransaction({
    required double valor,
    required String tipo,
    required PaymentMethod metodoPagamento,
    required int categoriaId,
    required String descricao,
    required DateTime data,
    String? observacoes,
  }) async {
    return await _remoteDataSource.createTransaction(
      valor: valor,
      tipo: tipo,
      metodoPagamento: metodoPagamento,
      categoriaId: categoriaId,
      descricao: descricao,
      data: data,
      observacoes: observacoes,
    );
  }

  @override
  Future<TransactionEntity> getTransactionById(int id) async {
    return await _remoteDataSource.getTransactionById(id);
  }

  @override
  Future<List<TransactionEntity>> getAllTransactions() async {
    final models = await _remoteDataSource.getAllTransactions();
    return models.cast<TransactionEntity>();
  }

  @override
  Future<TransactionEntity> updateTransaction({
    required int id,
    required double valor,
    required String tipo,
    required PaymentMethod metodoPagamento,
    required int categoriaId,
    required String descricao,
    required DateTime data,
    String? observacoes,
  }) async {
    return await _remoteDataSource.updateTransaction(
      id: id,
      valor: valor,
      tipo: tipo,
      metodoPagamento: metodoPagamento,
      categoriaId: categoriaId,
      descricao: descricao,
      data: data,
      observacoes: observacoes,
    );
  }

  @override
  Future<void> deleteTransaction(int id) async {
    return await _remoteDataSource.deleteTransaction(id);
  }
}
