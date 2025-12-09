import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sgfi/features/transactions/domain/entities/transaction_entity.dart';
import 'package:sgfi/features/transactions/presentation/providers/transaction_provider.dart';
import 'package:sgfi/features/categories/domain/entities/category_entity.dart';
import 'package:sgfi/features/categories/presentation/providers/category_provider.dart';
import 'package:sgfi/core/routes/app_routes.dart';
import 'package:sgfi/core/utils/format_utils.dart';

class TransactionsListScreen extends StatefulWidget {
  const TransactionsListScreen({super.key});

  @override
  State<TransactionsListScreen> createState() =>
      _TransactionsListScreenState();
}

class _TransactionsListScreenState extends State<TransactionsListScreen> {
  // Estado local para filtros (UI state)
  TransactionType? _filterType;
  String _searchText = '';
  DateTimeRange? _filterRange;

  @override
  void initState() {
    super.initState();
    // Carregar dados dos providers
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _loadData();
    });
  }

  Future<void> _loadData() async {
    final transactionProvider = context.read<TransactionProvider>();
    final categoryProvider = context.read<CategoryProvider>();

    await Future.wait([
      transactionProvider.loadTransactions(),
      categoryProvider.loadCategories(),
    ]);
  }

  Future<void> _pickDateRange() async {
    final now = DateTime.now();
    final firstDate = DateTime(now.year - 1, 1, 1);
    final lastDate = DateTime(now.year + 1, 12, 31);

    final initialRange = _filterRange ??
        DateTimeRange(
          start: DateTime(now.year, now.month, 1),
          end: now,
        );

    final picked = await showDateRangePicker(
      context: context,
      firstDate: firstDate,
      lastDate: lastDate,
      initialDateRange: initialRange,
    );

    if (picked != null) {
      setState(() {
        _filterRange = picked;
      });
    }
  }

  void _clearDateFilter() {
    setState(() {
      _filterRange = null;
    });
  }

  String _currentDateFilterLabel() {
    if (_filterRange == null) {
      return 'Todas as datas';
    }
    final start = _filterRange!.start;
    final end = _filterRange!.end;

    String fmt(DateTime d) =>
        '${d.day.toString().padLeft(2, '0')}/${d.month.toString().padLeft(2, '0')}/${d.year}';

    return '${fmt(start)} até ${fmt(end)}';
  }

  List<TransactionEntity> _getFilteredTransactions(List<TransactionEntity> allTransactions) {
    return allTransactions.where((t) {
      // 1) filtro por tipo
      final matchesType =
          _filterType == null ? true : t.type == _filterType;

      // 2) filtro por texto (descrição ou categoria)
      final text = _searchText.toLowerCase();
      final matchesSearch = text.isEmpty
          ? true
          : t.description.toLowerCase().contains(text) ||
              t.category.toLowerCase().contains(text);

      // 3) filtro por período (data)
      final matchesDate = _filterRange == null
          ? true
          : (t.date.isAtSameMomentAs(_filterRange!.start) ||
                  t.date.isAfter(_filterRange!.start)) &&
              (t.date.isAtSameMomentAs(_filterRange!.end) ||
                  t.date.isBefore(
                    _filterRange!.end.add(const Duration(days: 1)),
                  ));

      return matchesType && matchesSearch && matchesDate;
    }).toList();
  }

  void _setFilter(TransactionType? type) {
    setState(() {
      _filterType = type;
    });
  }

  void _openAddTransactionSheet() {
    final categoryProvider = context.read<CategoryProvider>();

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (ctx) {
        return Padding(
          padding: EdgeInsets.only(
            left: 16,
            right: 16,
            top: 16,
            bottom: MediaQuery.of(ctx).viewInsets.bottom + 16,
          ),
          child: _AddTransactionForm(
            categories: categoryProvider.categories,
            onSubmit: () {
              Navigator.of(ctx).pop();
              _loadData();
            },
          ),
        );
      },
    );
  }

  Future<void> _deleteTransaction(TransactionEntity t) async {
    final transactionProvider = context.read<TransactionProvider>();

    final success = await transactionProvider.deleteTransaction(int.parse(t.id));

    if (mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(success
              ? 'Transação "${t.description}" removida.'
              : 'Erro ao remover: ${transactionProvider.errorMessage ?? "Desconhecido"}'),
          backgroundColor: success ? null : Colors.red,
        ),
      );
    }
  }

  String _formatDate(DateTime date) {
    return '${date.day.toString().padLeft(2, '0')}/'
        '${date.month.toString().padLeft(2, '0')}/'
        '${date.year}';
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Transações'),
        actions: [
          IconButton(
            icon: const Icon(Icons.repeat),
            tooltip: 'Transações recorrentes',
            onPressed: () {
              Navigator.of(context).pushNamed(AppRoutes.recurrences);
            },
          ),
        ],
      ),
      body: Consumer2<TransactionProvider, CategoryProvider>(
        builder: (context, transactionProvider, categoryProvider, child) {
          final isLoading = transactionProvider.isLoading || categoryProvider.isLoading;
          final hasError = transactionProvider.errorMessage != null || categoryProvider.errorMessage != null;
          final errorMessage = transactionProvider.errorMessage ?? categoryProvider.errorMessage;

          if (isLoading) {
            return const Center(child: CircularProgressIndicator());
          }

          if (hasError) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Icon(Icons.error_outline, size: 48, color: Colors.red),
                  const SizedBox(height: 16),
                  Text(errorMessage!, textAlign: TextAlign.center),
                  const SizedBox(height: 16),
                  ElevatedButton(
                    onPressed: _loadData,
                    child: const Text('Tentar novamente'),
                  ),
                ],
              ),
            );
          }

          final transactions = _getFilteredTransactions(transactionProvider.transactions);
          return Column(
        children: [
          // Filtro + busca
          Padding(
            padding: const EdgeInsets.all(12.0),
            child: Column(
              children: [
                TextField(
                  decoration: const InputDecoration(
                    labelText: 'Buscar por descrição ou categoria',
                    prefixIcon: Icon(Icons.search),
                  ),
                  onChanged: (value) {
                    setState(() {
                      _searchText = value;
                    });
                  },
                ),
                const SizedBox(height: 8),
                Row(
                  children: [
                    _FilterChip(
                      label: 'Todos',
                      selected: _filterType == null,
                      onSelected: () => _setFilter(null),
                    ),
                    const SizedBox(width: 8),
                    _FilterChip(
                      label: 'Receitas',
                      selected: _filterType == TransactionType.income,
                      onSelected: () =>
                          _setFilter(TransactionType.income),
                      color: Colors.green,
                    ),
                    const SizedBox(width: 8),
                    _FilterChip(
                      label: 'Despesas',
                      selected: _filterType == TransactionType.expense,
                      onSelected: () =>
                          _setFilter(TransactionType.expense),
                      color: Colors.red,
                    ),
                  ],
                ),
                const SizedBox(height: 8),
                Row(
                  children: [
                    Expanded(
                      child: OutlinedButton.icon(
                        onPressed: _pickDateRange,
                        icon: const Icon(Icons.date_range),
                        label: Text(_currentDateFilterLabel()),
                      ),
                    ),
                    const SizedBox(width: 8),
                    if (_filterRange != null)
                      IconButton(
                        tooltip: 'Limpar filtro de datas',
                        onPressed: _clearDateFilter,
                        icon: const Icon(Icons.clear),
                      ),
                  ],
                ),
              ],
            ),
          ),
          const Divider(height: 0),
          Expanded(
            child: transactions.isEmpty
                ? const Center(
                    child: Text('Nenhuma transação encontrada.'),
                  )
                : ListView.separated(
                    itemCount: transactions.length,
                    separatorBuilder: (_, __) =>
                        const Divider(height: 0),
                    itemBuilder: (context, index) {
                      final t = transactions[index];
                      final isIncome =
                          t.type == TransactionType.income;
                      final color =
                          isIncome ? Colors.green : Colors.red;
                      final sign = isIncome ? '+' : '-';

                      return Dismissible(
                        key: ValueKey(t.id),
                        direction: DismissDirection.endToStart,
                        background: Container(
                          color: Colors.red,
                          alignment: Alignment.centerRight,
                          padding: const EdgeInsets.symmetric(
                            horizontal: 16,
                          ),
                          child: const Icon(
                            Icons.delete,
                            color: Colors.white,
                          ),
                        ),
                        confirmDismiss: (direction) async {
                          final result = await showDialog<bool>(
                                context: context,
                                builder: (ctx) {
                                  return AlertDialog(
                                    title: const Text(
                                        'Remover transação'),
                                    content: Text(
                                      'Tem certeza que deseja remover a transação "${t.description}"?',
                                    ),
                                    actions: [
                                      TextButton(
                                        onPressed: () =>
                                            Navigator.of(ctx)
                                                .pop(false),
                                        child:
                                            const Text('Cancelar'),
                                      ),
                                      TextButton(
                                        onPressed: () =>
                                            Navigator.of(ctx)
                                                .pop(true),
                                        child: const Text(
                                          'Remover',
                                          style: TextStyle(
                                            color: Colors.red,
                                          ),
                                        ),
                                      ),
                                    ],
                                  );
                                },
                              ) ??
                              false;
                          return result;
                        },
                        onDismissed: (direction) {
                          _deleteTransaction(t);
                        },
                        child: ListTile(
                          leading: CircleAvatar(
                            backgroundColor: color.withValues(alpha: 0.1),
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
                          onTap: () {
                            showModalBottomSheet(
                              context: context,
                              isScrollControlled: true,
                              shape:
                                  const RoundedRectangleBorder(
                                borderRadius:
                                    BorderRadius.vertical(
                                  top: Radius.circular(24),
                                ),
                              ),
                              builder: (ctx) {
                                return Padding(
                                  padding: EdgeInsets.only(
                                    left: 16,
                                    right: 16,
                                    top: 16,
                                    bottom: MediaQuery.of(ctx)
                                            .viewInsets
                                            .bottom +
                                        16,
                                  ),
                                  child: _AddTransactionForm(
                                    categories: categoryProvider.categories,
                                    initial: t,
                                    onSubmit: () {
                                      Navigator.of(ctx).pop();
                                      _loadData();
                                    },
                                  ),
                                );
                              },
                            );
                          },
                        ),
                      );
                    },
                  ),
          ),
        ],
      );
        },
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _openAddTransactionSheet,
        icon: const Icon(Icons.add),
        label: const Text('Adicionar'),
      ),
    );
  }
}

class _FilterChip extends StatelessWidget {
  final String label;
  final bool selected;
  final VoidCallback onSelected;
  final Color? color;

  const _FilterChip({
    required this.label,
    required this.selected,
    required this.onSelected,
    this.color,
  });

  @override
  Widget build(BuildContext context) {
    final themeColor = color ?? Theme.of(context).colorScheme.primary;

    return ChoiceChip(
      label: Text(label),
      selected: selected,
      onSelected: (_) => onSelected(),
      selectedColor: themeColor.withValues(alpha: 0.15),
      labelStyle: TextStyle(
        color: selected
            ? themeColor
            : Theme.of(context).colorScheme.onSurface,
        fontWeight: selected ? FontWeight.w600 : FontWeight.normal,
      ),
      side: BorderSide(
        color: selected
            ? themeColor
            : Theme.of(context).colorScheme.outline,
      ),
    );
  }
}

class _AddTransactionForm extends StatefulWidget {
  final VoidCallback onSubmit;
  final TransactionEntity? initial;
  final List<CategoryEntity> categories;

  const _AddTransactionForm({
    required this.onSubmit,
    this.initial,
    required this.categories,
  });

  @override
  State<_AddTransactionForm> createState() => _AddTransactionFormState();
}

class _AddTransactionFormState extends State<_AddTransactionForm> {
  final _formKey = GlobalKey<FormState>();

  String _description = '';
  CategoryEntity? _selectedCategory;
  double _amount = 0;
  DateTime _selectedDate = DateTime.now();
  TransactionType _type = TransactionType.expense;
  PaymentMethod _paymentMethod = PaymentMethod.dinheiro;
  String? _observacoes;
  bool _isSubmitting = false;

  @override
  void initState() {
    super.initState();

    if (widget.initial != null) {
      final t = widget.initial!;
      _description = t.description;
      _amount = t.amount;
      _selectedDate = t.date;
      _type = t.type;
      _paymentMethod = t.paymentMethod;
      _observacoes = t.observacoes;

      // acha categoria correspondente pelo ID ou nome
      try {
        if (t.categoriaId != null) {
          _selectedCategory = widget.categories.firstWhere(
            (c) => c.id == t.categoriaId.toString() && c.isIncome == (t.type == TransactionType.income),
            orElse: () => throw Exception('Category not found'),
          );
        } else {
          _selectedCategory = widget.categories.firstWhere(
            (c) =>
                c.name == t.category &&
                c.isIncome == (t.type == TransactionType.income),
            orElse: () => throw Exception('Category not found'),
          );
        }
      } catch (e) {
        // Se não encontrar categoria compatível, deixa null
        _selectedCategory = null;
      }
    }
  }


  List<CategoryEntity> get _availableCategories {
    // categorias compatíveis com o tipo selecionado
    return widget.categories
        .where((c) => c.isIncome == (_type == TransactionType.income))
        .toList();
  }

  Future<void> _pickDate() async {
    final now = DateTime.now();
    final picked = await showDatePicker(
      context: context,
      initialDate: _selectedDate,
      firstDate: DateTime(now.year - 1),
      lastDate: DateTime(now.year + 1),
    );

    if (picked != null) {
      setState(() {
        _selectedDate = picked;
      });
    }
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    _formKey.currentState!.save();

    setState(() {
      _isSubmitting = true;
    });

    final transactionProvider = context.read<TransactionProvider>();
    final isEditing = widget.initial != null;
    final categoriaId = int.parse(_selectedCategory!.id);
    final tipo = _type == TransactionType.income ? 'RECEITA' : 'DESPESA';

    bool success;
    if (isEditing) {
      success = await transactionProvider.updateTransaction(
        id: int.parse(widget.initial!.id),
        valor: _amount,
        tipo: tipo,
        metodoPagamento: _paymentMethod,
        categoriaId: categoriaId,
        descricao: _description,
        data: _selectedDate,
        observacoes: _observacoes,
      );
    } else {
      success = await transactionProvider.createTransaction(
        valor: _amount,
        tipo: tipo,
        metodoPagamento: _paymentMethod,
        categoriaId: categoriaId,
        descricao: _description,
        data: _selectedDate,
        observacoes: _observacoes,
      );
    }

    setState(() {
      _isSubmitting = false;
    });

    if (success) {
      widget.onSubmit();
    } else if (mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Erro ao salvar transação: ${transactionProvider.errorMessage ?? "Desconhecido"}'),
          backgroundColor: Colors.red,
        ),
      );
    }
  }

  String _formatDate(DateTime date) {
    return '${date.day.toString().padLeft(2, '0')}/'
        '${date.month.toString().padLeft(2, '0')}/'
        '${date.year}';
  }

  @override
  Widget build(BuildContext context) {
    final categories = _availableCategories;

    return Form(
      key: _formKey,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Text(
            widget.initial == null
                ? 'Nova transação'
                : 'Editar transação',
            style: Theme.of(context).textTheme.titleMedium,
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 16),
          DropdownButtonFormField<TransactionType>(
            value: _type,
            decoration: const InputDecoration(
              labelText: 'Tipo',
            ),
            items: const [
              DropdownMenuItem(
                value: TransactionType.income,
                child: Text('Receita'),
              ),
              DropdownMenuItem(
                value: TransactionType.expense,
                child: Text('Despesa'),
              ),
            ],
            onChanged: (value) {
              if (value != null) {
                setState(() {
                  _type = value;
                  _selectedCategory = null; // limpa categoria se trocar tipo
                });
              }
            },
          ),
          const SizedBox(height: 12),
          TextFormField(
            initialValue: _description,
            decoration: const InputDecoration(
              labelText: 'Descrição',
            ),
            validator: (value) {
              if (value == null || value.isEmpty) {
                return 'Informe a descrição';
              }
              return null;
            },
            onSaved: (value) => _description = value!.trim(),
          ),
          const SizedBox(height: 12),
          DropdownButtonFormField<CategoryEntity>(
            value: _selectedCategory,
            decoration: const InputDecoration(
              labelText: 'Categoria',
            ),
            items: categories
                .map(
                  (c) => DropdownMenuItem<CategoryEntity>(
                    value: c,
                    child: Text(c.name),
                  ),
                )
                .toList(),
            validator: (value) {
              if (value == null) {
                return 'Selecione uma categoria';
              }
              return null;
            },
            onChanged: (value) {
              setState(() {
                _selectedCategory = value;
              });
            },
            onSaved: (value) {
              _selectedCategory = value;
            },
          ),
          const SizedBox(height: 12),
          DropdownButtonFormField<PaymentMethod>(
            value: _paymentMethod,
            decoration: const InputDecoration(
              labelText: 'Método de pagamento',
            ),
            items: const [
              DropdownMenuItem(
                value: PaymentMethod.dinheiro,
                child: Text('Dinheiro'),
              ),
              DropdownMenuItem(
                value: PaymentMethod.pix,
                child: Text('PIX'),
              ),
              DropdownMenuItem(
                value: PaymentMethod.cartaoDebito,
                child: Text('Cartão de Débito'),
              ),
              DropdownMenuItem(
                value: PaymentMethod.cartaoCredito,
                child: Text('Cartão de Crédito'),
              ),
            ],
            onChanged: (value) {
              if (value != null) {
                setState(() {
                  _paymentMethod = value;
                });
              }
            },
          ),
          const SizedBox(height: 12),
          TextFormField(
            initialValue:
                _amount > 0 ? _amount.toStringAsFixed(2) : '',
            decoration: const InputDecoration(
              labelText: 'Valor (R\$)',
              hintText: 'Ex: 1.500,00 ou 1500',
            ),
            keyboardType:
                const TextInputType.numberWithOptions(decimal: true),
            inputFormatters: [MoneyInputFormatter()],
            validator: (value) {
              if (value == null || value.isEmpty) {
                return 'Informe o valor';
              }
              final parsed = FormatUtils.parseMoneyInput(value);
              if (parsed == null || parsed <= 0) {
                return 'Valor inválido';
              }
              return null;
            },
            onSaved: (value) {
              final parsed = FormatUtils.parseMoneyInput(value ?? '');
              _amount = parsed ?? 0;
            },
          ),
          const SizedBox(height: 12),
          Row(
            children: [
              Expanded(
                child: Text('Data: ${_formatDate(_selectedDate)}'),
              ),
              TextButton(
                onPressed: _pickDate,
                child: const Text('Selecionar data'),
              ),
            ],
          ),
          const SizedBox(height: 12),
          TextFormField(
            initialValue: _observacoes,
            decoration: const InputDecoration(
              labelText: 'Observações (opcional)',
            ),
            maxLines: 3,
            onSaved: (value) => _observacoes = value?.trim(),
          ),
          const SizedBox(height: 16),
          ElevatedButton(
            onPressed: _isSubmitting ? null : _submit,
            child: _isSubmitting
                ? const SizedBox(
                    height: 20,
                    width: 20,
                    child: CircularProgressIndicator(strokeWidth: 2),
                  )
                : const Text('Salvar'),
          ),
        ],
      ),
    );
  }
}
