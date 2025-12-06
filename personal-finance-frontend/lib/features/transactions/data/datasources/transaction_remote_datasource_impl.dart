import '../../../../core/network/api_client.dart';
import '../../domain/entities/transaction_entity.dart';
import '../models/transaction_model.dart';
import 'transaction_remote_datasource.dart';

class TransactionRemoteDataSourceImpl implements TransactionRemoteDataSource {
  final ApiClient _apiClient;

  TransactionRemoteDataSourceImpl({ApiClient? apiClient})
      : _apiClient = apiClient ?? ApiClient();

  @override
  Future<TransactionModel> createTransaction({
    required double valor,
    required String tipo,
    required PaymentMethod metodoPagamento,
    required int categoriaId,
    required String descricao,
    required DateTime data,
    String? observacoes,
  }) async {
    final response = await _apiClient.post(
      '/transactions',
      body: {
        'valor': valor,
        'tipo': tipo,
        'metodoPagamento': _serializePaymentMethod(metodoPagamento),
        'categoriaId': categoriaId,
        'descricao': descricao,
        'data': data.toIso8601String().split('T')[0],
        if (observacoes != null) 'observacoes': observacoes,
      },
    );

    final transaction = _apiClient.parseResponse<TransactionModel>(
      response,
      (json) => TransactionModel.fromJson(json),
    );

    if (transaction != null) {
      return transaction;
    }

    throw Exception('Erro ao criar transação');
  }

  @override
  Future<TransactionModel> getTransactionById(int id) async {
    final response = await _apiClient.get('/transactions/$id');

    final transaction = _apiClient.parseResponse<TransactionModel>(
      response,
      (json) => TransactionModel.fromJson(json),
    );

    if (transaction != null) {
      return transaction;
    }

    throw Exception('Transação não encontrada');
  }

  @override
  Future<List<TransactionModel>> getAllTransactions() async {
    final response = await _apiClient.get('/transactions');

    return _apiClient.parseListResponse<TransactionModel>(
      response,
      (json) => TransactionModel.fromJson(json),
    );
  }

  @override
  Future<TransactionModel> updateTransaction({
    required int id,
    required double valor,
    required String tipo,
    required PaymentMethod metodoPagamento,
    required int categoriaId,
    required String descricao,
    required DateTime data,
    String? observacoes,
  }) async {
    final response = await _apiClient.put(
      '/transactions/$id',
      body: {
        'valor': valor,
        'tipo': tipo,
        'metodoPagamento': _serializePaymentMethod(metodoPagamento),
        'categoriaId': categoriaId,
        'descricao': descricao,
        'data': data.toIso8601String().split('T')[0],
        if (observacoes != null) 'observacoes': observacoes,
      },
    );

    final transaction = _apiClient.parseResponse<TransactionModel>(
      response,
      (json) => TransactionModel.fromJson(json),
    );

    if (transaction != null) {
      return transaction;
    }

    throw Exception('Erro ao atualizar transação');
  }

  @override
  Future<void> deleteTransaction(int id) async {
    await _apiClient.delete('/transactions/$id');
  }

  String _serializePaymentMethod(PaymentMethod method) {
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
