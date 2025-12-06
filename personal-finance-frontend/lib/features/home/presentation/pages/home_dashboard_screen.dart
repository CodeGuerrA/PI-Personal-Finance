import 'package:flutter/material.dart';
import 'package:sgfi/core/routes/app_routes.dart';
import 'package:sgfi/features/transactions/domain/entities/transaction_entity.dart';
import 'package:sgfi/features/transactions/domain/repositories/transaction_repository.dart';
import 'package:sgfi/features/transactions/data/repositories/transaction_repository_impl.dart';
import 'package:sgfi/features/transactions/data/datasources/transaction_remote_datasource_impl.dart';
import 'package:sgfi/features/goals/domain/entities/goal_entity.dart';
import 'package:sgfi/features/goals/domain/repositories/goal_repository.dart';
import 'package:sgfi/features/goals/data/repositories/goal_repository_impl.dart';
import 'package:sgfi/features/goals/data/datasources/goal_remote_datasource_impl.dart';
import 'package:sgfi/features/recurrences/domain/entities/recurrence_entity.dart';
import 'package:sgfi/features/recurrences/domain/repositories/recurrence_repository.dart';
import 'package:sgfi/features/recurrences/data/repositories/recurrence_repository_impl.dart';
import 'package:sgfi/features/recurrences/data/datasources/recurrence_remote_datasource_impl.dart';



class HomeDashboardScreen extends StatefulWidget {
  const HomeDashboardScreen({super.key});

  @override
  State<HomeDashboardScreen> createState() => _HomeDashboardScreenState();
}

class _HomeDashboardScreenState extends State<HomeDashboardScreen> {
  int _currentIndex = 0;

  // Repositórios
  late final TransactionRepository _transactionRepository;
  late final GoalRepository _goalRepository;
  late final RecurrenceRepository _recurrenceRepository;

  // Estado
  List<TransactionEntity> _transactions = [];
  List<GoalEntity> _goals = [];
  List<RecurrenceEntity> _recurrences = [];
  bool _isLoading = true;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    // Inicializa repositórios
    _transactionRepository = TransactionRepositoryImpl(
      remoteDataSource: TransactionRemoteDataSourceImpl(),
    );
    _goalRepository = GoalRepositoryImpl(
      remoteDataSource: GoalRemoteDataSourceImpl(),
    );
    _recurrenceRepository = RecurrenceRepositoryImpl(
      remoteDataSource: RecurrenceRemoteDataSourceImpl(),
    );

    _loadData();
  }

  Future<void> _loadData() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      final results = await Future.wait([
        _transactionRepository.getAllTransactions(),
        _goalRepository.getAllGoals(),
        _recurrenceRepository.getActiveRecurrences(),
      ]);

      setState(() {
        _transactions = results[0] as List<TransactionEntity>;
        _goals = results[1] as List<GoalEntity>;
        _recurrences = results[2] as List<RecurrenceEntity>;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _errorMessage = 'Erro ao carregar dados: ${e.toString()}';
        _isLoading = false;
      });
    }
  }

  // 🔹 Transações do mês atual
  List<TransactionEntity> get _currentMonthTransactions {
    final now = DateTime.now();
    return _transactions.where((t) {
      return t.date.year == now.year && t.date.month == now.month;
    }).toList();
  }

  // 🔹 Total de receitas do mês
  double get _totalIncomeMonth => _currentMonthTransactions
      .where((t) => t.type == TransactionType.income)
      .fold(0.0, (sum, t) => sum + t.amount);

  // 🔹 Total de despesas do mês
  double get _totalExpensesMonth => _currentMonthTransactions
      .where((t) => t.type == TransactionType.expense)
      .fold(0.0, (sum, t) => sum + t.amount);

  // 🔹 Saldo do mês
  double get _balance => _totalIncomeMonth - _totalExpensesMonth;

  List<TransactionEntity> get _recentTransactions {
    final sorted = [..._transactions]
      ..sort((a, b) => b.date.compareTo(a.date));
    return sorted.take(5).toList();
  }

  // ==========================
  // METAS - PROGRESSO GERAL
  // ==========================

  double get _goalsOverallProgressPercent {
    if (_goals.isEmpty) return 0;

    final totalTarget = _goals.fold<double>(0, (sum, g) => sum + g.targetAmount);
    final totalCurrent =
        _goals.fold<double>(0, (sum, g) => sum + g.currentAmount);

    if (totalTarget <= 0) return 0;

    final ratio = totalCurrent / totalTarget;
    // limita entre 0% e 100%
    return (ratio.clamp(0.0, 1.0)) * 100;
  }

    // ==========================
  // RECORRÊNCIAS PRÓXIMAS
  // ==========================

  /// Recorrências com próxima data dentro dos próximos 7 dias
  List<RecurrenceEntity> get _upcomingRecurrences {
    final now = DateTime.now();
    final today = DateTime(now.year, now.month, now.day);
    final limit = today.add(const Duration(days: 7));

    final list = _recurrences.where((r) {
      final d = r.nextDueDate; // <-- vem da extension
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



  void _onTabTapped(int index) async {
  if (index == 0) {
    setState(() => _currentIndex = 0);
  } else if (index == 1) {
    // espera a tela de Transações ser fechada (voltar)
    await Navigator.of(context).pushNamed(AppRoutes.transactions);
    _loadData(); // recarrega dados do backend
  } else if (index == 2) {
    await Navigator.of(context).pushNamed(AppRoutes.investments);
    _loadData(); // recarrega dados do backend
  } else if (index == 3) {
    await Navigator.of(context).pushNamed(AppRoutes.goals);
    _loadData(); // recarrega dados do backend
  }
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

    @override
    Widget build(BuildContext context) {
      final isNegativeBalance = _balance < 0;
      final upcomingRecurrences = _upcomingRecurrences;



    return Scaffold(
      appBar: AppBar(
        title: const Text('Resumo financeiro'),
        actions: [
          IconButton(
            icon: const Icon(Icons.bar_chart_outlined),
            tooltip: 'Relatórios',
            onPressed: () {
              Navigator.of(context).pushNamed(AppRoutes.reportsOverview);
            },
          ),
          IconButton(
            icon: const Icon(Icons.category_outlined),
            tooltip: 'Categorias',
            onPressed: () {
              Navigator.of(context).pushNamed(AppRoutes.categories);
            },
          ),
          IconButton(
            icon: const Icon(Icons.person_outline),
            tooltip: 'Meu perfil',
            onPressed: () {
              Navigator.of(context).pushNamed(AppRoutes.profile);
            },
          ),
        ],
      ),

      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _errorMessage != null
              ? Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Icon(Icons.error_outline, size: 48, color: Colors.red),
                      const SizedBox(height: 16),
                      Text(_errorMessage!, textAlign: TextAlign.center),
                      const SizedBox(height: 16),
                      ElevatedButton(
                        onPressed: _loadData,
                        child: const Text('Tentar novamente'),
                      ),
                    ],
                  ),
                )
              : Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Text(
              'Visão geral',
              style: Theme.of(context).textTheme.titleMedium,
            ),
            const SizedBox(height: 12),
                        Row(
              children: [
                Expanded(
                  child: _SummaryCard(
                    title: 'Saldo do mês',
                    value: _formatCurrency(_balance),
                    highlightColor:
                        isNegativeBalance ? Colors.red : Colors.green,
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: _SummaryCard(
                    title: 'Receitas do mês',
                    value: _formatCurrency(_totalIncomeMonth),
                    highlightColor: Colors.green,
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: _SummaryCard(
                    title: 'Despesas do mês',
                    value: _formatCurrency(_totalExpensesMonth),
                    highlightColor: Colors.red,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 24),

            // ==========================
            // PROGRESSO DE METAS
            // ==========================
            if (_goals.isNotEmpty) ...[
              Text(
                'Progresso de metas',
                style: Theme.of(context).textTheme.titleMedium,
              ),
              const SizedBox(height: 8),
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(12.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      LinearProgressIndicator(
                        value: _goalsOverallProgressPercent / 100,
                        minHeight: 10,
                        borderRadius: BorderRadius.circular(8),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        '${_goalsOverallProgressPercent.toStringAsFixed(1)}% do valor total das metas já atingido',
                        style: const TextStyle(fontSize: 12),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 24),
            ],

            // ==========================
            // ALERTAS DE RECORRÊNCIAS
            // ==========================
            if (upcomingRecurrences.isNotEmpty) ...[
              Text(
                'Pagamentos recorrentes próximos (7 dias)',
                style: Theme.of(context).textTheme.titleMedium,
              ),
              const SizedBox(height: 8),
              Card(
                child: ListView.separated(
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  itemCount: upcomingRecurrences.length > 3
                      ? 3
                      : upcomingRecurrences.length,
                  separatorBuilder: (_, __) => const Divider(height: 0),
                  itemBuilder: (context, index) {
                    final r = upcomingRecurrences[index];

                    final isIncome = r.type == TransactionType.income;
                    final color = isIncome ? Colors.green : Colors.red;
                    final sign = isIncome ? '+' : '-';
                    final nextDate = r.nextDueDate;

                    return ListTile(
                      leading: Icon(
                        Icons.notifications_active,
                        color: color,
                      ),
                      title: Text(r.name), // <-- usa name
                      subtitle: Text(
                        nextDate != null
                            ? 'Próxima em ${_formatDate(nextDate)}'
                            : 'Sem próxima data',
                      ),
                      trailing: Text(
                        '$sign R\$ ${r.amount.toStringAsFixed(2)}',
                        style: TextStyle(
                          color: color,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      onTap: () {
                        Navigator.of(context)
                            .pushNamed(AppRoutes.recurrences);
                      },
                    );
                  },
                ),
              ),
              const SizedBox(height: 24),
            ],



            Text(
              'Últimas transações',
              style: Theme.of(context).textTheme.titleMedium,
            ),
            const SizedBox(height: 8),
            Expanded(
              child: _recentTransactions.isEmpty
                  ? const Center(
                      child: Text('Nenhuma transação registrada.'),
                    )
                  : ListView.separated(
                      itemCount: _recentTransactions.length,
                      separatorBuilder: (_, __) => const Divider(height: 0),
                      itemBuilder: (context, index) {
                        final t = _recentTransactions[index];
                        final isIncome =
                            t.type == TransactionType.income;
                        final color =
                            isIncome ? Colors.green : Colors.red;
                        final sign = isIncome ? '+' : '-';

                        return ListTile(
                          leading: CircleAvatar(
                            backgroundColor: color.withOpacity(0.1),
                            child: Icon(
                              isIncome
                                  ? Icons.arrow_upward
                                  : Icons.arrow_downward,
                              color: color,
                            ),
                          ),
                          title: Text(t.description),
                          subtitle: Text(
                            '${t.category} • ${_formatDate(t.date)}',
                          ),
                          trailing: Text(
                            '$sign R\$ ${t.amount.toStringAsFixed(2)}',
                            style: TextStyle(
                              color: color,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        );
                      },
                    ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _currentIndex,
        onTap: _onTabTapped,
        items: const [
          BottomNavigationBarItem(
            icon: Icon(Icons.home_outlined),
            label: 'Início',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.swap_vert),
            label: 'Transações',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.savings_outlined),
            label: 'Investimentos',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.flag_outlined),
            label: 'Metas',
          ),
        ],
      ),
    );
  }
}

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
        padding: const EdgeInsets.all(12.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              title,
              style: const TextStyle(fontSize: 12, color: Colors.grey),
            ),
            const SizedBox(height: 4),
            Text(
              value,
              style: TextStyle(
                fontSize: 18,
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

