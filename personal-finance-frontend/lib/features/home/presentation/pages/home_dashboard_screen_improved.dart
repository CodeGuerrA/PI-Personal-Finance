import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sgfi/core/routes/app_routes.dart';
import 'package:sgfi/core/utils/responsive_helper.dart';
import 'package:sgfi/features/transactions/domain/entities/transaction_entity.dart';
import 'package:sgfi/features/transactions/presentation/providers/transaction_provider.dart';
import 'package:sgfi/features/goals/domain/entities/goal_entity.dart';
import 'package:sgfi/features/goals/domain/repositories/goal_repository.dart';
import 'package:sgfi/features/goals/data/repositories/goal_repository_impl.dart';
import 'package:sgfi/features/goals/data/datasources/goal_remote_datasource_impl.dart';
import 'package:sgfi/features/recurrences/domain/entities/recurrence_entity.dart';
import 'package:sgfi/features/recurrences/domain/repositories/recurrence_repository.dart';
import 'package:sgfi/features/recurrences/data/repositories/recurrence_repository_impl.dart';
import 'package:sgfi/features/recurrences/data/datasources/recurrence_remote_datasource_impl.dart';

/// Dashboard principal MELHORADO com Provider e Design Responsivo
class HomeDashboardScreenImproved extends StatefulWidget {
  const HomeDashboardScreenImproved({super.key});

  @override
  State<HomeDashboardScreenImproved> createState() =>
      _HomeDashboardScreenImprovedState();
}

class _HomeDashboardScreenImprovedState
    extends State<HomeDashboardScreenImproved> {
  int _currentIndex = 0;

  // Repositórios (Goals e Recurrences ainda não têm Provider)
  late final GoalRepository _goalRepository;
  late final RecurrenceRepository _recurrenceRepository;

  // Estado local (apenas para Goals e Recurrences por enquanto)
  List<GoalEntity> _goals = [];
  List<RecurrenceEntity> _recurrences = [];
  bool _isLoadingExtras = false;

  @override
  void initState() {
    super.initState();

    // Inicializa repositórios que ainda não têm Provider
    _goalRepository = GoalRepositoryImpl(
      remoteDataSource: GoalRemoteDataSourceImpl(),
    );
    _recurrenceRepository = RecurrenceRepositoryImpl(
      remoteDataSource: RecurrenceRemoteDataSourceImpl(),
    );

    // Carrega transações usando Provider
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<TransactionProvider>().loadTransactions();
      _loadExtras();
    });
  }

  Future<void> _loadExtras() async {
    setState(() => _isLoadingExtras = true);

    try {
      final results = await Future.wait([
        _goalRepository.getAllGoals(),
        _recurrenceRepository.getActiveRecurrences(),
      ]);

      setState(() {
        _goals = results[0] as List<GoalEntity>;
        _recurrences = results[1] as List<RecurrenceEntity>;
        _isLoadingExtras = false;
      });
    } catch (e) {
      setState(() => _isLoadingExtras = false);
    }
  }

  // Métodos auxiliares (movidos para fora do build)
  List<TransactionEntity> _getCurrentMonthTransactions(
      List<TransactionEntity> transactions) {
    final now = DateTime.now();
    return transactions.where((t) {
      return t.date.year == now.year && t.date.month == now.month;
    }).toList();
  }

  double _getTotalIncomeMonth(List<TransactionEntity> monthTransactions) {
    return monthTransactions
        .where((t) => t.type == TransactionType.income)
        .fold(0.0, (sum, t) => sum + t.amount);
  }

  double _getTotalExpensesMonth(List<TransactionEntity> monthTransactions) {
    return monthTransactions
        .where((t) => t.type == TransactionType.expense)
        .fold(0.0, (sum, t) => sum + t.amount);
  }

  List<TransactionEntity> _getRecentTransactions(
      List<TransactionEntity> transactions) {
    final sorted = [...transactions]..sort((a, b) => b.date.compareTo(a.date));
    return sorted.take(5).toList();
  }

  double _getGoalsProgress() {
    if (_goals.isEmpty) return 0;

    final totalTarget = _goals.fold<double>(0, (sum, g) => sum + g.targetAmount);
    final totalCurrent =
        _goals.fold<double>(0, (sum, g) => sum + g.currentAmount);

    if (totalTarget <= 0) return 0;
    return (totalCurrent / totalTarget).clamp(0.0, 1.0) * 100;
  }

  List<RecurrenceEntity> _getUpcomingRecurrences() {
    final now = DateTime.now();
    final today = DateTime(now.year, now.month, now.day);
    final limit = today.add(const Duration(days: 7));

    final list = _recurrences.where((r) {
      final d = r.nextDueDate;
      if (d == null) return false;
      return !d.isBefore(today) && d.isBefore(limit);
    }).toList();

    list.sort((a, b) {
      final ad = a.nextDueDate;
      final bd = b.nextDueDate;
      if (ad == null && bd == null) return 0;
      if (ad == null) return 1;
      if (bd == null) return -1;
      return ad.compareTo(bd);
    });

    return list;
  }

  String _formatCurrency(double value) {
    final sign = value < 0 ? '-' : '';
    final abs = value.abs().toStringAsFixed(2);
    return '${sign}R\$ $abs';
  }

  String _formatDate(DateTime date) {
    return '${date.day.toString().padLeft(2, '0')}/'
        '${date.month.toString().padLeft(2, '0')}/'
        '${date.year}';
  }

  void _onTabTapped(int index) async {
    if (index == 0) {
      setState(() => _currentIndex = 0);
    } else if (index == 1) {
      await Navigator.of(context).pushNamed(AppRoutes.transactions);
      if (mounted) {
        context.read<TransactionProvider>().loadTransactions(refresh: true);
        _loadExtras();
      }
    } else if (index == 2) {
      await Navigator.of(context).pushNamed(AppRoutes.investments);
      if (mounted) {
        context.read<TransactionProvider>().loadTransactions(refresh: true);
        _loadExtras();
      }
    } else if (index == 3) {
      await Navigator.of(context).pushNamed(AppRoutes.goals);
      if (mounted) {
        context.read<TransactionProvider>().loadTransactions(refresh: true);
        _loadExtras();
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Resumo financeiro'),
        actions: [
          IconButton(
            icon: const Icon(Icons.category),
            tooltip: 'Categorias',
            onPressed: () =>
                Navigator.of(context).pushNamed(AppRoutes.categories),
          ),
          IconButton(
            icon: const Icon(Icons.bar_chart),
            tooltip: 'Relatórios',
            onPressed: () {
              // TODO: Adicionar rota de relatórios
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('Relatórios em desenvolvimento')),
              );
            },
          ),
          IconButton(
            icon: const Icon(Icons.person),
            tooltip: 'Perfil',
            onPressed: () =>
                Navigator.of(context).pushNamed(AppRoutes.profile),
          ),
        ],
      ),
      body: _buildBody(context),
      bottomNavigationBar: _buildBottomNav(),
    );
  }

  Widget _buildBody(BuildContext context) {
    // ✅ Usa Consumer para reconstruir apenas quando o Provider mudar
    return Consumer<TransactionProvider>(
      builder: (context, transactionProvider, child) {
        if (transactionProvider.isLoading && transactionProvider.transactions.isEmpty) {
          return const Center(child: CircularProgressIndicator());
        }

        if (transactionProvider.errorMessage != null &&
            transactionProvider.transactions.isEmpty) {
          return Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Icon(Icons.error_outline, size: 48, color: Colors.red),
                SizedBox(height: ResponsiveHelper.responsivePadding(context)),
                Text(
                  transactionProvider.errorMessage!,
                  textAlign: TextAlign.center,
                ),
                SizedBox(height: ResponsiveHelper.responsivePadding(context)),
                ElevatedButton(
                  onPressed: () =>
                      transactionProvider.loadTransactions(refresh: true),
                  child: const Text('Tentar novamente'),
                ),
              ],
            ),
          );
        }

        // Cálculos baseados nas transações do Provider
        final transactions = transactionProvider.transactions;
        final monthTransactions = _getCurrentMonthTransactions(transactions);
        final totalIncome = _getTotalIncomeMonth(monthTransactions);
        final totalExpenses = _getTotalExpensesMonth(monthTransactions);
        final balance = totalIncome - totalExpenses;
        final recentTransactions = _getRecentTransactions(transactions);
        final isNegativeBalance = balance < 0;
        final upcomingRecurrences = _getUpcomingRecurrences();
        final goalsProgress = _getGoalsProgress();

        // ✅ Layout responsivo usando ResponsiveHelper
        return RefreshIndicator(
          onRefresh: () async {
            await transactionProvider.loadTransactions(refresh: true);
            await _loadExtras();
          },
          child: SingleChildScrollView(
            physics: const AlwaysScrollableScrollPhysics(),
            padding: ResponsiveHelper.responsiveEdgeInsets(context),
            child: Center(
              child: ConstrainedBox(
                // ✅ Limita largura em telas grandes
                constraints: BoxConstraints(
                  maxWidth: ResponsiveHelper.maxContentWidth(context),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    // ✅ Cards de resumo com layout responsivo
                    ResponsiveHelper.responsive(
                      context: context,
                      mobile: _buildSummaryCardsColumn(
                        context,
                        balance,
                        totalIncome,
                        totalExpenses,
                        isNegativeBalance,
                      ),
                      tablet: _buildSummaryCardsRow(
                        context,
                        balance,
                        totalIncome,
                        totalExpenses,
                        isNegativeBalance,
                      ),
                      desktop: _buildSummaryCardsRow(
                        context,
                        balance,
                        totalIncome,
                        totalExpenses,
                        isNegativeBalance,
                      ),
                    ),

                    ResponsiveHelper.responsiveVerticalSpacing(context, factor: 2),

                    // Transações recentes
                    Text(
                      'Últimas transações',
                      style: Theme.of(context).textTheme.titleMedium?.copyWith(
                            fontSize: ResponsiveHelper.responsiveFontSize(context, 16),
                          ),
                    ),
                    ResponsiveHelper.responsiveVerticalSpacing(context),
                    _buildRecentTransactions(context, recentTransactions),

                    ResponsiveHelper.responsiveVerticalSpacing(context, factor: 2),

                    // Progresso de metas
                    _buildGoalsProgress(context, goalsProgress),

                    ResponsiveHelper.responsiveVerticalSpacing(context, factor: 2),

                    // Recorrências próximas
                    if (upcomingRecurrences.isNotEmpty)
                      _buildUpcomingRecurrences(context, upcomingRecurrences),
                  ],
                ),
              ),
            ),
          ),
        );
      },
    );
  }

  // ✅ Layout em coluna para mobile
  Widget _buildSummaryCardsColumn(
    BuildContext context,
    double balance,
    double totalIncome,
    double totalExpenses,
    bool isNegative,
  ) {
    return Column(
      children: [
        _SummaryCard(
          title: 'Saldo do mês',
          value: _formatCurrency(balance),
          highlightColor: isNegative ? Colors.red : Colors.green,
        ),
        ResponsiveHelper.responsiveVerticalSpacing(context),
        Row(
          children: [
            Expanded(
              child: _SummaryCard(
                title: 'Receitas',
                value: _formatCurrency(totalIncome),
                highlightColor: Colors.green,
              ),
            ),
            ResponsiveHelper.responsiveHorizontalSpacing(context),
            Expanded(
              child: _SummaryCard(
                title: 'Despesas',
                value: _formatCurrency(totalExpenses),
                highlightColor: Colors.red,
              ),
            ),
          ],
        ),
      ],
    );
  }

  // ✅ Layout em linha para tablet/desktop
  Widget _buildSummaryCardsRow(
    BuildContext context,
    double balance,
    double totalIncome,
    double totalExpenses,
    bool isNegative,
  ) {
    return Row(
      children: [
        Expanded(
          child: _SummaryCard(
            title: 'Saldo do mês',
            value: _formatCurrency(balance),
            highlightColor: isNegative ? Colors.red : Colors.green,
          ),
        ),
        ResponsiveHelper.responsiveHorizontalSpacing(context),
        Expanded(
          child: _SummaryCard(
            title: 'Receitas',
            value: _formatCurrency(totalIncome),
            highlightColor: Colors.green,
          ),
        ),
        ResponsiveHelper.responsiveHorizontalSpacing(context),
        Expanded(
          child: _SummaryCard(
            title: 'Despesas',
            value: _formatCurrency(totalExpenses),
            highlightColor: Colors.red,
          ),
        ),
      ],
    );
  }

  Widget _buildRecentTransactions(
    BuildContext context,
    List<TransactionEntity> transactions,
  ) {
    if (transactions.isEmpty) {
      return const Card(
        child: Padding(
          padding: EdgeInsets.all(16.0),
          child: Text('Nenhuma transação registrada.'),
        ),
      );
    }

    return Card(
      child: ListView.separated(
        shrinkWrap: true,
        physics: const NeverScrollableScrollPhysics(),
        itemCount: transactions.length,
        separatorBuilder: (_, __) => const Divider(height: 0),
        itemBuilder: (context, index) {
          final t = transactions[index];
          final isIncome = t.type == TransactionType.income;
          final color = isIncome ? Colors.green : Colors.red;
          final sign = isIncome ? '+' : '-';

          return ListTile(
            leading: CircleAvatar(
              backgroundColor: color.withValues(alpha: 0.1),
              child: Icon(
                isIncome ? Icons.arrow_upward : Icons.arrow_downward,
                color: color,
              ),
            ),
            title: Text(t.description),
            subtitle: Text('${t.category} • ${_formatDate(t.date)}'),
            trailing: Text(
              '$sign ${_formatCurrency(t.amount)}',
              style: TextStyle(
                color: color,
                fontWeight: FontWeight.bold,
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildGoalsProgress(BuildContext context, double progress) {
    return Card(
      child: Padding(
        padding: ResponsiveHelper.responsiveEdgeInsets(context),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'Progresso das metas',
              style: Theme.of(context).textTheme.titleMedium,
            ),
            ResponsiveHelper.responsiveVerticalSpacing(context),
            LinearProgressIndicator(
              value: progress / 100,
              minHeight: ResponsiveHelper.isMobile(context) ? 8 : 12,
            ),
            ResponsiveHelper.responsiveVerticalSpacing(context, factor: 0.5),
            Text(
              '${progress.toStringAsFixed(1)}% concluído',
              style: Theme.of(context).textTheme.bodySmall,
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildUpcomingRecurrences(
    BuildContext context,
    List<RecurrenceEntity> recurrences,
  ) {
    return Card(
      color: Colors.orange.shade50,
      child: Padding(
        padding: ResponsiveHelper.responsiveEdgeInsets(context),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                const Icon(Icons.warning_amber, color: Colors.orange),
                ResponsiveHelper.responsiveHorizontalSpacing(context, factor: 0.5),
                Text(
                  'Recorrências próximas (7 dias)',
                  style: Theme.of(context).textTheme.titleMedium,
                ),
              ],
            ),
            ResponsiveHelper.responsiveVerticalSpacing(context),
            ...recurrences.map((r) => Padding(
                  padding: const EdgeInsets.symmetric(vertical: 4),
                  child: Text(
                    '• ${r.name} - ${_formatDate(r.nextDueDate!)}',
                  ),
                )),
          ],
        ),
      ),
    );
  }

  BottomNavigationBar _buildBottomNav() {
    return BottomNavigationBar(
      type: BottomNavigationBarType.fixed,
      currentIndex: _currentIndex,
      onTap: _onTabTapped,
      items: const [
        BottomNavigationBarItem(
          icon: Icon(Icons.home),
          label: 'Início',
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.attach_money),
          label: 'Transações',
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.trending_up),
          label: 'Investimentos',
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.flag),
          label: 'Metas',
        ),
      ],
    );
  }
}

// Widget de card de resumo com padding responsivo
class _SummaryCard extends StatelessWidget {
  final String title;
  final String value;
  final Color highlightColor;

  const _SummaryCard({
    required this.title,
    required this.value,
    required this.highlightColor,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: ResponsiveHelper.responsiveEdgeInsets(context),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              title,
              style: TextStyle(
                fontSize: ResponsiveHelper.responsiveFontSize(context, 12),
                color: Colors.grey,
              ),
            ),
            ResponsiveHelper.responsiveVerticalSpacing(context, factor: 0.3),
            Text(
              value,
              style: TextStyle(
                fontSize: ResponsiveHelper.responsiveFontSize(context, 16),
                fontWeight: FontWeight.bold,
                color: highlightColor,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
