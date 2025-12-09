import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sgfi/features/transactions/presentation/providers/transaction_provider.dart';
import 'package:sgfi/features/categories/presentation/providers/category_provider.dart';
import 'package:sgfi/features/goals/presentation/providers/goal_provider.dart';
import 'package:sgfi/features/investments/presentation/providers/investment_provider.dart';
import 'package:sgfi/features/recurrences/presentation/providers/recurrence_provider.dart';
import 'package:sgfi/core/cache/cache_service.dart';

/// Serviço para invalidar todos os providers ao trocar de usuário
/// Garante que nenhum dado antigo de outro usuário seja exibido
class ProviderInvalidationService {
  /// Invalida TODOS os providers e limpa o cache do usuário anterior
  static Future<void> invalidateAllProviders(BuildContext context) async {
    print('==========================================');
    print('🔄 INVALIDANDO TODOS OS PROVIDERS');
    print('==========================================');

    try {
      // Limpar cache do usuário atual
      await CacheService.clearCurrentUserCache();
      print('✅ Cache do usuário anterior limpo');

      // Invalidar todos os providers
      final transactionProvider = Provider.of<TransactionProvider>(context, listen: false);
      transactionProvider.invalidate();

      final categoryProvider = Provider.of<CategoryProvider>(context, listen: false);
      categoryProvider.invalidate();

      final goalProvider = Provider.of<GoalProvider>(context, listen: false);
      goalProvider.invalidate();

      final investmentProvider = Provider.of<InvestmentProvider>(context, listen: false);
      investmentProvider.invalidate();

      final recurrenceProvider = Provider.of<RecurrenceProvider>(context, listen: false);
      recurrenceProvider.invalidate();

      print('✅ Todos os providers invalidados');
      print('==========================================');
    } catch (e) {
      print('❌ Erro ao invalidar providers: $e');
    }
  }

  /// Força recarregamento de todos os providers
  static Future<void> reloadAllProviders(BuildContext context) async {
    print('==========================================');
    print('📥 RECARREGANDO TODOS OS DADOS');
    print('==========================================');

    try {
      final transactionProvider = Provider.of<TransactionProvider>(context, listen: false);
      await transactionProvider.loadTransactions(refresh: true);
      print('✅ Transações recarregadas');

      final categoryProvider = Provider.of<CategoryProvider>(context, listen: false);
      await categoryProvider.loadCategories(refresh: true);
      print('✅ Categorias recarregadas');

      final goalProvider = Provider.of<GoalProvider>(context, listen: false);
      await goalProvider.loadGoals(refresh: true);
      print('✅ Metas recarregadas');

      final investmentProvider = Provider.of<InvestmentProvider>(context, listen: false);
      await investmentProvider.loadInvestments(refresh: true);
      print('✅ Investimentos recarregados');

      final recurrenceProvider = Provider.of<RecurrenceProvider>(context, listen: false);
      await recurrenceProvider.loadRecurrences(refresh: true);
      print('✅ Recorrências recarregadas');

      print('==========================================');
      print('✅ TODOS OS DADOS RECARREGADOS');
      print('==========================================');
    } catch (e) {
      print('❌ Erro ao recarregar providers: $e');
    }
  }
}
