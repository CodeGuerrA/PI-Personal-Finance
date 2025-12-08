import 'package:flutter/material.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:sgfi/features/transactions/domain/entities/transaction_entity.dart';
import 'package:sgfi/features/transactions/domain/repositories/transaction_repository.dart';
import 'package:sgfi/features/transactions/data/repositories/transaction_repository_impl.dart';
import 'package:sgfi/features/transactions/data/datasources/transaction_remote_datasource_impl.dart';
import 'package:sgfi/features/categories/domain/entities/category_entity.dart';
import 'package:sgfi/features/categories/domain/repositories/category_repository.dart';
import 'package:sgfi/features/categories/data/repositories/category_repository_impl.dart';
import 'package:sgfi/features/categories/data/datasources/category_remote_datasource_impl.dart';

class ReportsOverviewScreen extends StatefulWidget {
  const ReportsOverviewScreen({super.key});

  @override
  State<ReportsOverviewScreen> createState() => _ReportsOverviewScreenState();
}

class _ReportsOverviewScreenState extends State<ReportsOverviewScreen> {
  late final TransactionRepository _transactionRepository;
  late final CategoryRepository _categoryRepository;

  List<TransactionEntity> _allTransactions = [];
  List<CategoryEntity> _allCategories = [];
  bool _isLoading = true;
  String? _errorMessage;

  // FILTROS
  DateTimeRange? _selectedDateRange;
  Set<String> _selectedCategories = {};
  Set<TransactionType> _selectedTypes = {TransactionType.income, TransactionType.expense};
  Set<PaymentMethod> _selectedPaymentMethods = {};
  bool _showFilters = false;

  @override
  void initState() {
    super.initState();
    _transactionRepository = TransactionRepositoryImpl(
      remoteDataSource: TransactionRemoteDataSourceImpl(),
    );
    _categoryRepository = CategoryRepositoryImpl(
      remoteDataSource: CategoryRemoteDataSourceImpl(),
    );
    _loadData();
  }

  Future<void> _loadData() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      final transactions = await _transactionRepository.getAllTransactions();
      final categories = await _categoryRepository.getAllCategories();
      setState(() {
        _allTransactions = transactions;
        _allCategories = categories;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _errorMessage = 'Erro ao carregar dados: ${e.toString()}';
        _isLoading = false;
      });
    }
  }

  List<TransactionEntity> get _filteredTransactions {
    return _allTransactions.where((t) {
      // Filtro de período
      if (_selectedDateRange != null) {
        if (t.date.isBefore(_selectedDateRange!.start) ||
            t.date.isAfter(_selectedDateRange!.end)) {
          return false;
        }
      }

      // Filtro de categoria
      if (_selectedCategories.isNotEmpty) {
        if (!_selectedCategories.contains(t.category)) {
          return false;
        }
      }

      // Filtro de tipo
      if (!_selectedTypes.contains(t.type)) {
        return false;
      }

      // Filtro de método de pagamento
      if (_selectedPaymentMethods.isNotEmpty) {
        if (!_selectedPaymentMethods.contains(t.paymentMethod)) {
          return false;
        }
      }

      return true;
    }).toList();
  }

  void _clearFilters() {
    setState(() {
      _selectedDateRange = null;
      _selectedCategories.clear();
      _selectedTypes = {TransactionType.income, TransactionType.expense};
      _selectedPaymentMethods.clear();
    });
  }

  Future<void> _selectDateRange() async {
    final DateTimeRange? picked = await showDateRangePicker(
      context: context,
      firstDate: DateTime(2020),
      lastDate: DateTime.now(),
      initialDateRange: _selectedDateRange,
      builder: (context, child) {
        return Theme(
          data: Theme.of(context).copyWith(
            colorScheme: Theme.of(context).colorScheme,
          ),
          child: child!,
        );
      },
    );

    if (picked != null) {
      setState(() => _selectedDateRange = picked);
    }
  }

  // ============================
  // 1) DESPESAS POR CATEGORIA
  // ============================
  Map<String, double> _expenseByCategory() {
    final Map<String, double> totals = {};

    for (final t in _filteredTransactions) {
      if (t.type != TransactionType.expense) continue;

      totals.update(
        t.category,
        (value) => value + t.amount,
        ifAbsent: () => t.amount,
      );
    }

    return totals;
  }

  // ============================
  // 1.1) RECEITAS POR CATEGORIA
  // ============================
  Map<String, double> _incomeByCategory() {
    final Map<String, double> totals = {};

    for (final t in _filteredTransactions) {
      if (t.type != TransactionType.income) continue;

      totals.update(
        t.category,
        (value) => value + t.amount,
        ifAbsent: () => t.amount,
      );
    }

    return totals;
  }

  // ============================
  // 1.2) DESPESAS POR MÉTODO DE PAGAMENTO
  // ============================
  Map<String, double> _expenseByPaymentMethod() {
    final Map<String, double> totals = {};

    for (final t in _filteredTransactions) {
      if (t.type != TransactionType.expense) continue;

      final methodLabel = _paymentMethodLabel(t.paymentMethod);
      totals.update(
        methodLabel,
        (value) => value + t.amount,
        ifAbsent: () => t.amount,
      );
    }

    return totals;
  }

  // ============================
  // 1.3) RECEITAS POR MÉTODO DE PAGAMENTO
  // ============================
  Map<String, double> _incomeByPaymentMethod() {
    final Map<String, double> totals = {};

    for (final t in _filteredTransactions) {
      if (t.type != TransactionType.income) continue;

      final methodLabel = _paymentMethodLabel(t.paymentMethod);
      totals.update(
        methodLabel,
        (value) => value + t.amount,
        ifAbsent: () => t.amount,
      );
    }

    return totals;
  }

  // ============================
  // 2) RECEITAS x DESPESAS / MÊS
  // (últimos 6 meses ou período filtrado)
  // ============================
  List<_MonthlySummary> _monthlySummary() {
    final now = DateTime.now();
    final List<_MonthlySummary> result = [];

    // Se tem filtro de período, usa o período filtrado
    if (_selectedDateRange != null) {
      final start = _selectedDateRange!.start;
      final end = _selectedDateRange!.end;

      DateTime current = DateTime(start.year, start.month, 1);
      final endMonth = DateTime(end.year, end.month, 1);

      while (current.isBefore(endMonth) || current.isAtSameMomentAs(endMonth)) {
        double incomes = 0;
        double expenses = 0;

        for (final t in _filteredTransactions) {
          if (t.date.year == current.year && t.date.month == current.month) {
            if (t.type == TransactionType.income) {
              incomes += t.amount;
            } else if (t.type == TransactionType.expense) {
              expenses += t.amount;
            }
          }
        }

        result.add(
          _MonthlySummary(
            year: current.year,
            month: current.month,
            incomes: incomes,
            expenses: expenses,
          ),
        );

        current = DateTime(current.year, current.month + 1, 1);
      }
    } else {
      // Últimos 6 meses
      for (int i = 5; i >= 0; i--) {
        final monthDate = DateTime(now.year, now.month - i, 1);
        final year = monthDate.year;
        final month = monthDate.month;

        double incomes = 0;
        double expenses = 0;

        for (final t in _filteredTransactions) {
          if (t.date.year == year && t.date.month == month) {
            if (t.type == TransactionType.income) {
              incomes += t.amount;
            } else if (t.type == TransactionType.expense) {
              expenses += t.amount;
            }
          }
        }

        result.add(
          _MonthlySummary(
            year: year,
            month: month,
            incomes: incomes,
            expenses: expenses,
          ),
        );
      }
    }

    return result;
  }

  String _monthLabel(int month) {
    const labels = [
      'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun',
      'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez',
    ];
    return labels[month - 1];
  }

  Widget _buildFilterChips() {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(
                  children: [
                    Icon(Icons.filter_list, size: 20, color: Theme.of(context).colorScheme.primary),
                    const SizedBox(width: 8),
                    Text(
                      'Filtros',
                      style: Theme.of(context).textTheme.titleMedium,
                    ),
                  ],
                ),
                Row(
                  children: [
                    if (_selectedDateRange != null ||
                        _selectedCategories.isNotEmpty ||
                        _selectedPaymentMethods.isNotEmpty ||
                        _selectedTypes.length != 2)
                      TextButton.icon(
                        onPressed: _clearFilters,
                        icon: const Icon(Icons.clear_all, size: 18),
                        label: const Text('Limpar'),
                      ),
                    IconButton(
                      icon: Icon(_showFilters ? Icons.expand_less : Icons.expand_more),
                      onPressed: () => setState(() => _showFilters = !_showFilters),
                    ),
                  ],
                ),
              ],
            ),
            if (_showFilters) ...[
              const Divider(),
              const SizedBox(height: 8),

              // Filtro de Período
              Text('Período', style: Theme.of(context).textTheme.bodySmall),
              const SizedBox(height: 8),
              Wrap(
                spacing: 8,
                children: [
                  FilterChip(
                    label: Text(_selectedDateRange == null
                      ? 'Selecionar período'
                      : '${_formatDate(_selectedDateRange!.start)} - ${_formatDate(_selectedDateRange!.end)}'),
                    selected: _selectedDateRange != null,
                    onSelected: (_) => _selectDateRange(),
                    avatar: const Icon(Icons.date_range, size: 18),
                  ),
                ],
              ),
              const SizedBox(height: 16),

              // Filtro de Tipo
              Text('Tipo de transação', style: Theme.of(context).textTheme.bodySmall),
              const SizedBox(height: 8),
              Wrap(
                spacing: 8,
                children: [
                  FilterChip(
                    label: const Text('Receitas'),
                    selected: _selectedTypes.contains(TransactionType.income),
                    onSelected: (selected) {
                      setState(() {
                        if (selected) {
                          _selectedTypes.add(TransactionType.income);
                        } else {
                          _selectedTypes.remove(TransactionType.income);
                        }
                      });
                    },
                    avatar: const Icon(Icons.arrow_upward, size: 18, color: Colors.green),
                  ),
                  FilterChip(
                    label: const Text('Despesas'),
                    selected: _selectedTypes.contains(TransactionType.expense),
                    onSelected: (selected) {
                      setState(() {
                        if (selected) {
                          _selectedTypes.add(TransactionType.expense);
                        } else {
                          _selectedTypes.remove(TransactionType.expense);
                        }
                      });
                    },
                    avatar: const Icon(Icons.arrow_downward, size: 18, color: Colors.red),
                  ),
                ],
              ),
              const SizedBox(height: 16),

              // Filtro de Categorias
              Text('Categorias', style: Theme.of(context).textTheme.bodySmall),
              const SizedBox(height: 8),
              Wrap(
                spacing: 8,
                runSpacing: 4,
                children: _allCategories.map((cat) {
                  return FilterChip(
                    label: Text(cat.name),
                    selected: _selectedCategories.contains(cat.name),
                    onSelected: (selected) {
                      setState(() {
                        if (selected) {
                          _selectedCategories.add(cat.name);
                        } else {
                          _selectedCategories.remove(cat.name);
                        }
                      });
                    },
                  );
                }).toList(),
              ),
              const SizedBox(height: 16),

              // Filtro de Método de Pagamento
              Text('Método de pagamento', style: Theme.of(context).textTheme.bodySmall),
              const SizedBox(height: 8),
              Wrap(
                spacing: 8,
                runSpacing: 4,
                children: PaymentMethod.values.map((method) {
                  return FilterChip(
                    label: Text(_paymentMethodLabel(method)),
                    selected: _selectedPaymentMethods.contains(method),
                    onSelected: (selected) {
                      setState(() {
                        if (selected) {
                          _selectedPaymentMethods.add(method);
                        } else {
                          _selectedPaymentMethods.remove(method);
                        }
                      });
                    },
                  );
                }).toList(),
              ),
            ],
          ],
        ),
      ),
    );
  }

  String _formatDate(DateTime date) {
    return '${date.day.toString().padLeft(2, '0')}/${date.month.toString().padLeft(2, '0')}/${date.year}';
  }

  String _paymentMethodLabel(PaymentMethod method) {
    switch (method) {
      case PaymentMethod.dinheiro:
        return 'Dinheiro';
      case PaymentMethod.pix:
        return 'PIX';
      case PaymentMethod.cartaoDebito:
        return 'Cartão Débito';
      case PaymentMethod.cartaoCredito:
        return 'Cartão Crédito';
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Relatórios'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            tooltip: 'Atualizar',
            onPressed: _loadData,
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
              : Builder(
                  builder: (context) {
                    final expenseByCategory = _expenseByCategory();
                    final incomeByCategory = _incomeByCategory();
                    final expenseByPaymentMethod = _expenseByPaymentMethod();
                    final incomeByPaymentMethod = _incomeByPaymentMethod();
                    final monthlySummary = _monthlySummary();

                    return SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            // FILTROS
            _buildFilterChips(),
            const SizedBox(height: 16),

            // Resumo dos filtros ativos
            if (_selectedDateRange != null ||
                _selectedCategories.isNotEmpty ||
                _selectedPaymentMethods.isNotEmpty ||
                _selectedTypes.length != 2)
              Card(
                color: theme.colorScheme.primaryContainer,
                child: Padding(
                  padding: const EdgeInsets.all(12),
                  child: Row(
                    children: [
                      Icon(Icons.info_outline, size: 16, color: theme.colorScheme.onPrimaryContainer),
                      const SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          'Exibindo ${_filteredTransactions.length} transações filtradas',
                          style: TextStyle(
                            fontSize: 12,
                            color: theme.colorScheme.onPrimaryContainer,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            if (_selectedDateRange != null ||
                _selectedCategories.isNotEmpty ||
                _selectedPaymentMethods.isNotEmpty ||
                _selectedTypes.length != 2)
              const SizedBox(height: 16),

            // ==============================
            // CARD 1: DESPESAS POR CATEGORIA
            // ==============================
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Text(
                      'Despesas por categoria',
                      style: theme.textTheme.titleMedium,
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Distribuição das despesas por categoria '
                      '(com base nas transações ${_selectedDateRange != null ? 'no período selecionado' : 'cadastradas'}).',
                      style: theme.textTheme.bodySmall,
                    ),
                    const SizedBox(height: 16),
                    if (expenseByCategory.isEmpty)
                      const Padding(
                        padding: EdgeInsets.all(8.0),
                        child: Text('Nenhuma despesa encontrada com os filtros aplicados.'),
                      )
                    else
                      SizedBox(
                        height: 220,
                        child: PieChart(
                          PieChartData(
                            sectionsSpace: 2,
                            centerSpaceRadius: 40,
                            sections: _buildPieSections(
                              context,
                              expenseByCategory,
                            ),
                          ),
                        ),
                      ),
                    const SizedBox(height: 16),
                    if (expenseByCategory.isNotEmpty)
                      Wrap(
                        spacing: 8,
                        runSpacing: 4,
                        children: expenseByCategory.entries
                            .map(
                              (e) => _LegendItem(
                                color: _colorForCategory(
                                  context,
                                  e.key,
                                ),
                                label:
                                    '${e.key} (R\$ ${e.value.toStringAsFixed(2)})',
                              ),
                            )
                            .toList(),
                      ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),

            // ==============================
            // CARD 2: RECEITAS POR CATEGORIA
            // ==============================
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Text(
                      'Receitas por categoria',
                      style: theme.textTheme.titleMedium,
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Distribuição das receitas por categoria '
                      '(com base nas transações ${_selectedDateRange != null ? 'no período selecionado' : 'cadastradas'}).',
                      style: theme.textTheme.bodySmall,
                    ),
                    const SizedBox(height: 16),
                    if (incomeByCategory.isEmpty)
                      const Padding(
                        padding: EdgeInsets.all(8.0),
                        child: Text('Nenhuma receita encontrada com os filtros aplicados.'),
                      )
                    else
                      SizedBox(
                        height: 220,
                        child: PieChart(
                          PieChartData(
                            sectionsSpace: 2,
                            centerSpaceRadius: 40,
                            sections: _buildPieSections(
                              context,
                              incomeByCategory,
                            ),
                          ),
                        ),
                      ),
                    const SizedBox(height: 16),
                    if (incomeByCategory.isNotEmpty)
                      Wrap(
                        spacing: 8,
                        runSpacing: 4,
                        children: incomeByCategory.entries
                            .map(
                              (e) => _LegendItem(
                                color: _colorForCategory(
                                  context,
                                  e.key,
                                ),
                                label:
                                    '${e.key} (R\$ ${e.value.toStringAsFixed(2)})',
                              ),
                            )
                            .toList(),
                      ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),

            // ======================================
            // CARD 3: DESPESAS POR MÉTODO DE PAGAMENTO
            // ======================================
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Text(
                      'Despesas por método de pagamento',
                      style: theme.textTheme.titleMedium,
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Distribuição das despesas por forma de pagamento '
                      '(com base nas transações ${_selectedDateRange != null ? 'no período selecionado' : 'cadastradas'}).',
                      style: theme.textTheme.bodySmall,
                    ),
                    const SizedBox(height: 16),
                    if (expenseByPaymentMethod.isEmpty)
                      const Padding(
                        padding: EdgeInsets.all(8.0),
                        child: Text('Nenhuma despesa encontrada com os filtros aplicados.'),
                      )
                    else
                      SizedBox(
                        height: 220,
                        child: PieChart(
                          PieChartData(
                            sectionsSpace: 2,
                            centerSpaceRadius: 40,
                            sections: _buildPieSectionsForPaymentMethod(
                              context,
                              expenseByPaymentMethod,
                            ),
                          ),
                        ),
                      ),
                    const SizedBox(height: 16),
                    if (expenseByPaymentMethod.isNotEmpty)
                      Wrap(
                        spacing: 8,
                        runSpacing: 4,
                        children: expenseByPaymentMethod.entries
                            .map(
                              (e) => _LegendItem(
                                color: _colorForPaymentMethod(
                                  context,
                                  e.key,
                                ),
                                label:
                                    '${e.key} (R\$ ${e.value.toStringAsFixed(2)})',
                              ),
                            )
                            .toList(),
                      ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),

            // ======================================
            // CARD 4: RECEITAS POR MÉTODO DE PAGAMENTO
            // ======================================
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Text(
                      'Receitas por método de pagamento',
                      style: theme.textTheme.titleMedium,
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Distribuição das receitas por forma de pagamento '
                      '(com base nas transações ${_selectedDateRange != null ? 'no período selecionado' : 'cadastradas'}).',
                      style: theme.textTheme.bodySmall,
                    ),
                    const SizedBox(height: 16),
                    if (incomeByPaymentMethod.isEmpty)
                      const Padding(
                        padding: EdgeInsets.all(8.0),
                        child: Text('Nenhuma receita encontrada com os filtros aplicados.'),
                      )
                    else
                      SizedBox(
                        height: 220,
                        child: PieChart(
                          PieChartData(
                            sectionsSpace: 2,
                            centerSpaceRadius: 40,
                            sections: _buildPieSectionsForPaymentMethod(
                              context,
                              incomeByPaymentMethod,
                            ),
                          ),
                        ),
                      ),
                    const SizedBox(height: 16),
                    if (incomeByPaymentMethod.isNotEmpty)
                      Wrap(
                        spacing: 8,
                        runSpacing: 4,
                        children: incomeByPaymentMethod.entries
                            .map(
                              (e) => _LegendItem(
                                color: _colorForPaymentMethod(
                                  context,
                                  e.key,
                                ),
                                label:
                                    '${e.key} (R\$ ${e.value.toStringAsFixed(2)})',
                              ),
                            )
                            .toList(),
                      ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),

            // ======================================
            // CARD 5: RECEITAS x DESPESAS (MENSAL)
            // ======================================
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Text(
                      'Receitas x Despesas ${_selectedDateRange != null ? '(período selecionado)' : '(últimos 6 meses)'}',
                      style: theme.textTheme.titleMedium,
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Comparativo mensal para análise de tendência.',
                      style: theme.textTheme.bodySmall,
                    ),
                    const SizedBox(height: 16),
                    if (monthlySummary.isEmpty)
                      const Padding(
                        padding: EdgeInsets.all(8.0),
                        child: Text('Nenhum dado encontrado para o período.'),
                      )
                    else
                      SizedBox(
                        height: 260,
                        child: BarChart(
                          BarChartData(
                            barGroups: _buildBarGroups(
                              context,
                              monthlySummary,
                            ),
                            gridData: const FlGridData(show: false),
                            borderData: FlBorderData(show: false),
                            titlesData: FlTitlesData(
                              leftTitles:
                                  const AxisTitles(sideTitles: SideTitles(showTitles: false)),
                              topTitles:
                                  const AxisTitles(sideTitles: SideTitles(showTitles: false)),
                              rightTitles:
                                  const AxisTitles(sideTitles: SideTitles(showTitles: false)),
                              bottomTitles: AxisTitles(
                                sideTitles: SideTitles(
                                  showTitles: true,
                                  getTitlesWidget: (value, meta) {
                                    final index = value.toInt();
                                    if (index < 0 ||
                                        index >= monthlySummary.length) {
                                      return const SizedBox.shrink();
                                    }
                                    final m = monthlySummary[index];
                                    return Padding(
                                      padding: const EdgeInsets.only(top: 4),
                                      child: Text(
                                        _monthLabel(m.month),
                                        style: const TextStyle(fontSize: 10),
                                      ),
                                    );
                                  },
                                ),
                              ),
                            ),
                            barTouchData: BarTouchData(
                              enabled: true,
                            ),
                          ),
                        ),
                      ),
                    const SizedBox(height: 12),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: const [
                        _LegendItem(
                          color: Colors.green,
                          label: 'Receitas',
                        ),
                        SizedBox(width: 12),
                        _LegendItem(
                          color: Colors.red,
                          label: 'Despesas',
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      );
                  },
                ),
    );
  }

  // ============= HELPERS PARA OS GRÁFICOS =============

  List<PieChartSectionData> _buildPieSections(
    BuildContext context,
    Map<String, double> data,
  ) {
    final total = data.values.fold<double>(0, (p, c) => p + c);

    return data.entries.map((e) {
      final value = e.value;
      final percentage = total == 0 ? 0 : (value / total) * 100;
      final color = _colorForCategory(context, e.key);

      return PieChartSectionData(
        color: color,
        value: value,
        title: '${percentage.toStringAsFixed(1)}%',
        radius: 60,
        titleStyle: const TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.bold,
          color: Colors.white,
        ),
      );
    }).toList();
  }

  Color _colorForCategory(BuildContext context, String category) {
    Theme.of(context);

    // Só uma forma simples de variar as cores a partir do hash do nome
    final hash = category.codeUnits.fold<int>(0, (p, c) => p + c);
    final shade = 100 * ((hash % 5) + 1); // 100, 200, 300, 400, 500

    return Colors.primaries[hash % Colors.primaries.length]
        [shade]!
        .withValues(alpha: 0.9);
  }

  List<PieChartSectionData> _buildPieSectionsForPaymentMethod(
    BuildContext context,
    Map<String, double> data,
  ) {
    final total = data.values.fold<double>(0, (p, c) => p + c);

    return data.entries.map((e) {
      final value = e.value;
      final percentage = total == 0 ? 0 : (value / total) * 100;
      final color = _colorForPaymentMethod(context, e.key);

      return PieChartSectionData(
        color: color,
        value: value,
        title: '${percentage.toStringAsFixed(1)}%',
        radius: 60,
        titleStyle: const TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.bold,
          color: Colors.white,
        ),
      );
    }).toList();
  }

  Color _colorForPaymentMethod(BuildContext context, String paymentMethod) {
    // Cores específicas para cada método de pagamento
    switch (paymentMethod) {
      case 'Dinheiro':
        return Colors.green[600]!;
      case 'PIX':
        return Colors.teal[600]!;
      case 'Cartão Débito':
        return Colors.blue[600]!;
      case 'Cartão Crédito':
        return Colors.orange[600]!;
      default:
        return Colors.grey[600]!;
    }
  }

  List<BarChartGroupData> _buildBarGroups(
    BuildContext context,
    List<_MonthlySummary> summary,
  ) {
    final List<BarChartGroupData> groups = [];

    for (int i = 0; i < summary.length; i++) {
      final m = summary[i];
      groups.add(
        BarChartGroupData(
          x: i,
          barRods: [
            BarChartRodData(
              toY: m.incomes,
              width: 8,
              color: Colors.green,
            ),
            BarChartRodData(
              toY: m.expenses,
              width: 8,
              color: Colors.red,
            ),
          ],
          barsSpace: 4,
        ),
      );
    }

    return groups;
  }
}

class _MonthlySummary {
  final int year;
  final int month;
  final double incomes;
  final double expenses;

  _MonthlySummary({
    required this.year,
    required this.month,
    required this.incomes,
    required this.expenses,
  });
}

class _LegendItem extends StatelessWidget {
  final Color color;
  final String label;

  const _LegendItem({
    required this.color,
    required this.label,
  });

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(
          width: 12,
          height: 12,
          decoration: BoxDecoration(
            color: color,
            borderRadius: BorderRadius.circular(4),
          ),
        ),
        const SizedBox(width: 4),
        Text(
          label,
          style: const TextStyle(fontSize: 11),
        ),
      ],
    );
  }
}
