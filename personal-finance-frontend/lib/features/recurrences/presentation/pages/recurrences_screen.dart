import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sgfi/features/recurrences/domain/entities/recurrence_entity.dart';
import 'package:sgfi/features/recurrences/presentation/providers/recurrence_provider.dart';
import 'package:sgfi/features/transactions/domain/entities/transaction_entity.dart';
import 'package:sgfi/features/categories/domain/entities/category_entity.dart';
import 'package:sgfi/features/categories/presentation/providers/category_provider.dart';
import 'package:sgfi/core/utils/format_utils.dart';

class RecurrencesScreen extends StatefulWidget {
  const RecurrencesScreen({super.key});

  @override
  State<RecurrencesScreen> createState() => _RecurrencesScreenState();
}

class _RecurrencesScreenState extends State<RecurrencesScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<RecurrenceProvider>().loadRecurrences();
      context.read<CategoryProvider>().loadCategories();
    });
  }

  String _formatDate(DateTime date) {
    return '${date.day.toString().padLeft(2, '0')}/'
        '${date.month.toString().padLeft(2, '0')}/'
        '${date.year}';
  }

  String _frequencyLabel(RecurrenceFrequency f) {
    switch (f) {
      case RecurrenceFrequency.daily:
        return 'Diária';
      case RecurrenceFrequency.weekly:
        return 'Semanal';
      case RecurrenceFrequency.monthly:
        return 'Mensal';
      case RecurrenceFrequency.yearly:
        return 'Anual';
    }
  }

  IconData _typeIcon(TransactionType type) {
    return type == TransactionType.income
        ? Icons.arrow_upward
        : Icons.arrow_downward;
  }

  Color _typeColor(TransactionType type) {
    return type == TransactionType.income ? Colors.green : Colors.red;
  }

  void _openForm({RecurrenceEntity? editing}) {
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
          child: _RecurrenceForm(
            categories: categoryProvider.categories,
            initial: editing,
            onSubmit: () {
              Navigator.of(ctx).pop();
            },
          ),
        );
      },
    );
  }

  void _deleteRecurrence(RecurrenceEntity rec) async {
    final recurrenceProvider = context.read<RecurrenceProvider>();
    final success = await recurrenceProvider.deleteRecurrence(int.parse(rec.id));

    if (mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(success
              ? 'Recorrência "${rec.name}" removida.'
              : 'Erro ao remover recorrência: ${recurrenceProvider.errorMessage ?? "Desconhecido"}'),
          backgroundColor: success ? null : Colors.red,
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Transações recorrentes'),
      ),
      body: Consumer<RecurrenceProvider>(
        builder: (context, recurrenceProvider, child) {
          if (recurrenceProvider.isLoading) {
            return const Center(child: CircularProgressIndicator());
          }

          if (recurrenceProvider.errorMessage != null) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Icon(Icons.error_outline, size: 48, color: Colors.red),
                  const SizedBox(height: 16),
                  Text(recurrenceProvider.errorMessage!,
                      textAlign: TextAlign.center),
                  const SizedBox(height: 16),
                  ElevatedButton(
                    onPressed: () =>
                        recurrenceProvider.loadRecurrences(refresh: true),
                    child: const Text('Tentar novamente'),
                  ),
                ],
              ),
            );
          }

          final recurrences = recurrenceProvider.recurrences;
          return recurrences.isEmpty
              ? const Center(
                  child: Text('Nenhuma recorrência cadastrada.'),
                )
              : ListView.separated(
                  padding: const EdgeInsets.all(16),
                  itemCount: recurrences.length,
                  separatorBuilder: (_, __) => const SizedBox(height: 12),
                  itemBuilder: (context, index) {
                    final r = recurrences[index];
                    final color = _typeColor(r.type);
                    final icon = _typeIcon(r.type);

                    final status = r.isActive ? 'Ativa' : 'Inativa';
                    final dateInfo = r.endDate == null
                        ? 'Desde ${_formatDate(r.startDate)}'
                        : 'De ${_formatDate(r.startDate)} até ${_formatDate(r.endDate!)}';

                    return Dismissible(
                      key: ValueKey(r.id),
                      direction: DismissDirection.endToStart,
                      background: Container(
                        alignment: Alignment.centerRight,
                        padding: const EdgeInsets.symmetric(horizontal: 16),
                        color: Colors.red,
                        child: const Icon(
                          Icons.delete,
                          color: Colors.white,
                        ),
                      ),
                      confirmDismiss: (_) async {
                        final confirm = await showDialog<bool>(
                              context: context,
                              builder: (ctx) {
                                return AlertDialog(
                                  title: const Text('Remover recorrência'),
                                  content: Text(
                                    'Tem certeza que deseja remover a recorrência "${r.name}"?',
                                  ),
                                  actions: [
                                    TextButton(
                                      onPressed: () =>
                                          Navigator.of(ctx).pop(false),
                                      child: const Text('Cancelar'),
                                    ),
                                    TextButton(
                                      onPressed: () =>
                                          Navigator.of(ctx).pop(true),
                                      child: const Text(
                                        'Remover',
                                        style: TextStyle(color: Colors.red),
                                      ),
                                    ),
                                  ],
                                );
                              },
                            ) ??
                            false;
                        if (confirm) {
                          _deleteRecurrence(r);
                        }
                        return confirm;
                      },
                      child: Card(
                        child: ListTile(
                          leading: CircleAvatar(
                            backgroundColor: color.withValues(alpha: 0.1),
                            child: Icon(icon, color: color),
                          ),
                          title: Text(r.name),
                          subtitle: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                '${_frequencyLabel(r.frequency)} • ${r.categoryName}',
                              ),
                              Text(
                                '$status • $dateInfo',
                                style: const TextStyle(fontSize: 12),
                              ),
                            ],
                          ),
                          trailing: Text(
                            'R\$ ${r.amount.toStringAsFixed(2)}',
                            style: TextStyle(
                              color: color,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          onTap: () => _openForm(editing: r),
                        ),
                      ),
                    );
                  },
                );
        },
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => _openForm(),
        icon: const Icon(Icons.add),
        label: const Text('Nova recorrência'),
      ),
    );
  }
}

class _RecurrenceForm extends StatefulWidget {
  final List<CategoryEntity> categories;
  final VoidCallback onSubmit;
  final RecurrenceEntity? initial;

  const _RecurrenceForm({
    required this.categories,
    required this.onSubmit,
    this.initial,
  });

  @override
  State<_RecurrenceForm> createState() => _RecurrenceFormState();
}

class _RecurrenceFormState extends State<_RecurrenceForm> {
  final _formKey = GlobalKey<FormState>();

  String _name = '';
  double _amount = 0;
  TransactionType _type = TransactionType.expense;
  CategoryEntity? _selectedCategory;
  RecurrenceFrequency _frequency = RecurrenceFrequency.monthly;
  DateTime _startDate = DateTime.now();
  DateTime? _endDate;
  int? _diaVencimento;
  String? _observacoes;
  bool _isActive = true;
  bool _isSubmitting = false;


  @override
  void initState() {
    super.initState();
    if (widget.initial != null) {
      final r = widget.initial!;
      _name = r.name;
      _amount = r.amount;
      _type = r.type;
      _frequency = r.frequency;
      _startDate = r.startDate;
      _endDate = r.endDate;
      _isActive = r.isActive;
      _diaVencimento = r.diaVencimento;
      _observacoes = r.observacoes;

      // Encontrar categoria correspondente pelo ID ou nome
      try {
        if (r.categoriaId != null) {
          _selectedCategory = widget.categories.firstWhere(
            (c) => c.id == r.categoriaId.toString() && c.isIncome == (r.type == TransactionType.income),
            orElse: () => throw Exception('Category not found'),
          );
        } else if (r.categoryName.isNotEmpty) {
          _selectedCategory = widget.categories.firstWhere(
            (c) =>
                c.name == r.categoryName &&
                c.isIncome == (r.type == TransactionType.income),
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
    return widget.categories
        .where((c) => c.isIncome == (_type == TransactionType.income))
        .toList();
  }

  Future<void> _pickStartDate() async {
    final picked = await showDatePicker(
      context: context,
      initialDate: _startDate,
      firstDate: DateTime(DateTime.now().year - 2),
      lastDate: DateTime(DateTime.now().year + 2),
    );

    if (picked != null) {
      setState(() {
        _startDate = picked;
      });
    }
  }

  Future<void> _pickEndDate() async {
    final picked = await showDatePicker(
      context: context,
      initialDate: _endDate ?? _startDate,
      firstDate: _startDate,
      lastDate: DateTime(DateTime.now().year + 5),
    );

    if (picked != null) {
      setState(() {
        _endDate = picked;
      });
    }
  }

  String _formatDate(DateTime date) {
    return '${date.day.toString().padLeft(2, '0')}/'
        '${date.month.toString().padLeft(2, '0')}/'
        '${date.year}';
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    _formKey.currentState!.save();

    setState(() {
      _isSubmitting = true;
    });

    final recurrenceProvider = context.read<RecurrenceProvider>();
    final isEditing = widget.initial != null;
    final categoriaId = int.parse(_selectedCategory!.id);
    final tipo = _type == TransactionType.income ? 'RECEITA' : 'DESPESA';
    final frequencia = _serializeFrequency(_frequency);

    bool success;
    if (isEditing) {
      success = await recurrenceProvider.updateRecurrence(
        id: int.parse(widget.initial!.id),
        valor: _amount,
        tipo: tipo,
        categoriaId: categoriaId,
        descricao: _name,
        dataInicio: _startDate,
        frequencia: frequencia,
        dataFim: _endDate,
        diaVencimento: _diaVencimento,
        observacoes: _observacoes,
        ativa: _isActive,
      );
    } else {
      success = await recurrenceProvider.createRecurrence(
        valor: _amount,
        tipo: tipo,
        categoriaId: categoriaId,
        descricao: _name,
        dataInicio: _startDate,
        frequencia: frequencia,
        dataFim: _endDate,
        diaVencimento: _diaVencimento,
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
          content: Text(
              'Erro ao salvar recorrência: ${recurrenceProvider.errorMessage ?? "Desconhecido"}'),
          backgroundColor: Colors.red,
        ),
      );
    }
  }

  String _serializeFrequency(RecurrenceFrequency frequency) {
    switch (frequency) {
      case RecurrenceFrequency.daily:
        return 'DIARIA';
      case RecurrenceFrequency.weekly:
        return 'SEMANAL';
      case RecurrenceFrequency.monthly:
        return 'MENSAL';
      case RecurrenceFrequency.yearly:
        return 'ANUAL';
    }
  }

  @override
  Widget build(BuildContext context) {
    final isEditing = widget.initial != null;
    final categories = _availableCategories;

    return Form(
      key: _formKey,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Text(
            isEditing ? 'Editar recorrência' : 'Nova recorrência',
            style: Theme.of(context).textTheme.titleMedium,
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 16),
          TextFormField(
            initialValue: _name,
            decoration: const InputDecoration(
              labelText: 'Descrição',
            ),
            validator: (value) {
              if (value == null || value.trim().isEmpty) {
                return 'Informe a descrição';
              }
              return null;
            },
            onSaved: (value) => _name = value!.trim(),
          ),
          const SizedBox(height: 12),
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
                  _selectedCategory = null; // Limpar categoria ao trocar tipo
                });
              }
            },
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
          TextFormField(
            initialValue: _diaVencimento?.toString() ?? '',
            decoration: const InputDecoration(
              labelText: 'Dia de vencimento (1-31, opcional)',
              hintText: 'Ex: 15',
            ),
            keyboardType: TextInputType.number,
            validator: (value) {
              if (value != null && value.isNotEmpty) {
                final dia = int.tryParse(value);
                if (dia == null || dia < 1 || dia > 31) {
                  return 'Informe um dia válido entre 1 e 31';
                }
              }
              return null;
            },
            onSaved: (value) {
              if (value != null && value.isNotEmpty) {
                _diaVencimento = int.parse(value);
              } else {
                _diaVencimento = null;
              }
            },
          ),
          const SizedBox(height: 12),
          DropdownButtonFormField<RecurrenceFrequency>(
            value: _frequency,
            decoration: const InputDecoration(
              labelText: 'Frequência',
            ),
            items: const [
              DropdownMenuItem(
                value: RecurrenceFrequency.daily,
                child: Text('Diária'),
              ),
              DropdownMenuItem(
                value: RecurrenceFrequency.weekly,
                child: Text('Semanal'),
              ),
              DropdownMenuItem(
                value: RecurrenceFrequency.monthly,
                child: Text('Mensal'),
              ),
              DropdownMenuItem(
                value: RecurrenceFrequency.yearly,
                child: Text('Anual'),
              ),
            ],
            onChanged: (value) {
              if (value != null) {
                setState(() => _frequency = value);
              }
            },
          ),
          const SizedBox(height: 12),
          Row(
            children: [
              Expanded(
                child: Text('Início: ${_formatDate(_startDate)}'),
              ),
              TextButton(
                onPressed: _pickStartDate,
                child: const Text('Selecionar início'),
              ),
            ],
          ),
          Row(
            children: [
              Expanded(
                child: Text(
                  _endDate == null
                      ? 'Sem data final'
                      : 'Fim: ${_formatDate(_endDate!)}',
                ),
              ),
              TextButton(
                onPressed: _pickEndDate,
                child: const Text('Selecionar fim'),
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
          const SizedBox(height: 12),
          SwitchListTile(
            title: const Text('Recorrência ativa'),
            value: _isActive,
            onChanged: (value) {
              setState(() => _isActive = value);
            },
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
                : Text(isEditing ? 'Salvar alterações' : 'Salvar'),
          ),
        ],
      ),
    );
  }
}
