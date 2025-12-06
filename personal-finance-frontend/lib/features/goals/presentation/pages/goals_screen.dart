import 'package:flutter/material.dart';
import 'package:sgfi/features/goals/domain/entities/goal_entity.dart';
import 'package:sgfi/features/goals/domain/repositories/goal_repository.dart';
import 'package:sgfi/features/goals/data/repositories/goal_repository_impl.dart';
import 'package:sgfi/features/goals/data/datasources/goal_remote_datasource_impl.dart';

class GoalsScreen extends StatefulWidget {
  const GoalsScreen({super.key});

  @override
  State<GoalsScreen> createState() => _GoalsScreenState();
}

class _GoalsScreenState extends State<GoalsScreen> {
  late final GoalRepository _goalRepository;
  List<GoalEntity> _goals = [];
  bool _isLoading = true;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _goalRepository = GoalRepositoryImpl(
      remoteDataSource: GoalRemoteDataSourceImpl(),
    );
    _loadGoals();
  }

  Future<void> _loadGoals() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      final goals = await _goalRepository.getAllGoals();
      setState(() {
        _goals = goals;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _errorMessage = 'Erro ao carregar metas: ${e.toString()}';
        _isLoading = false;
      });
    }
  }

  void _openGoalForm({GoalEntity? editing}) {
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
          child: _GoalForm(
            repository: _goalRepository,
            initial: editing,
            onSubmit: () {
              Navigator.of(ctx).pop();
              _loadGoals();
            },
          ),
        );
      },
    );
  }

  void _addValueToGoal(GoalEntity goal) async {
    final controller = TextEditingController();

    final valor = await showDialog<double>(
      context: context,
      builder: (ctx) {
        return AlertDialog(
          title: const Text('Adicionar valor à meta'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(
                'Meta: ${goal.name}\nAtual: R\$ ${goal.currentAmount.toStringAsFixed(2)}',
                style: const TextStyle(fontSize: 14),
              ),
              const SizedBox(height: 16),
              TextField(
                controller: controller,
                decoration: const InputDecoration(
                  labelText: 'Valor a adicionar (R\$)',
                  hintText: 'Ex: 50.00',
                ),
                keyboardType: const TextInputType.numberWithOptions(decimal: true),
                autofocus: true,
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(ctx).pop(null),
              child: const Text('Cancelar'),
            ),
            TextButton(
              onPressed: () {
                final text = controller.text.replaceAll(',', '.');
                final parsed = double.tryParse(text);
                if (parsed != null && parsed > 0) {
                  Navigator.of(ctx).pop(parsed);
                }
              },
              child: const Text('Adicionar'),
            ),
          ],
        );
      },
    );

    if (valor == null) return;

    try {
      final novoValor = goal.currentAmount + valor;
      await _goalRepository.updateGoalValue(
        id: int.parse(goal.id),
        novoValor: novoValor,
      );
      _loadGoals();
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(
              'R\$ ${valor.toStringAsFixed(2)} adicionado à meta "${goal.name}"',
            ),
            backgroundColor: Colors.green,
          ),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Erro ao atualizar meta: ${e.toString()}'),
            backgroundColor: Colors.red,
          ),
        );
      }
    }
  }

  void _deleteGoal(GoalEntity goal) async {
    final confirm = await showDialog<bool>(
          context: context,
          builder: (ctx) {
            return AlertDialog(
              title: const Text('Remover meta'),
              content: Text(
                'Tem certeza que deseja remover a meta "${goal.name}"?',
              ),
              actions: [
                TextButton(
                  onPressed: () => Navigator.of(ctx).pop(false),
                  child: const Text('Cancelar'),
                ),
                TextButton(
                  onPressed: () => Navigator.of(ctx).pop(true),
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

    if (!confirm) return;

    try {
      await _goalRepository.deactivateGoal(int.parse(goal.id));
      _loadGoals();
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Meta "${goal.name}" removida.'),
          ),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Erro ao remover meta: ${e.toString()}'),
            backgroundColor: Colors.red,
          ),
        );
      }
    }
  }

  Color _statusColor(GoalEntity g) {
    if (g.progress >= 1.0 && g.currentAmount > g.targetAmount) {
      return Colors.purple;
    } else if (g.progress >= 1.0) {
      return Colors.green;
    } else if (g.progress >= 0.8) {
      return Colors.orange;
    } else {
      return Colors.blueGrey;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Metas financeiras'),
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _errorMessage != null
              ? Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Icon(Icons.error_outline,
                          size: 48, color: Colors.red),
                      const SizedBox(height: 16),
                      Text(_errorMessage!, textAlign: TextAlign.center),
                      const SizedBox(height: 16),
                      ElevatedButton(
                        onPressed: _loadGoals,
                        child: const Text('Tentar novamente'),
                      ),
                    ],
                  ),
                )
              : _goals.isEmpty
                  ? const Center(
                      child: Text('Nenhuma meta cadastrada.'),
                    )
                  : ListView.separated(
                      padding: const EdgeInsets.all(16),
                      itemCount: _goals.length,
                      separatorBuilder: (_, __) => const SizedBox(height: 12),
                      itemBuilder: (context, index) {
                        final g = _goals[index];
                        final progress = g.progress;
                        final color = _statusColor(g);
                        final percent =
                            (progress * 100).clamp(0, 200).toStringAsFixed(0);

                        return Dismissible(
                          key: ValueKey(g.id),
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
                            return await showDialog<bool>(
                                  context: context,
                                  builder: (ctx) {
                                    return AlertDialog(
                                      title: const Text('Remover meta'),
                                      content: Text(
                                        'Tem certeza que deseja remover a meta "${g.name}"?',
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
                          },
                          onDismissed: (_) => _deleteGoal(g),
                          child: Card(
                            child: InkWell(
                              borderRadius: BorderRadius.circular(12),
                              onTap: () => _openGoalForm(editing: g),
                              child: Padding(
                                padding: const EdgeInsets.all(12.0),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Row(
                                      mainAxisAlignment:
                                          MainAxisAlignment.spaceBetween,
                                      children: [
                                        Expanded(
                                          child: Text(
                                            g.name,
                                            style: const TextStyle(
                                              fontSize: 16,
                                              fontWeight: FontWeight.bold,
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 8),
                                        Row(
                                          children: [
                                            IconButton(
                                              icon: const Icon(Icons.add_circle,
                                                  color: Colors.green, size: 28),
                                              onPressed: () => _addValueToGoal(g),
                                              tooltip: 'Adicionar valor',
                                              padding: EdgeInsets.zero,
                                              constraints: const BoxConstraints(),
                                            ),
                                            const SizedBox(width: 8),
                                            Text(
                                              g.statusLabel,
                                              style: TextStyle(
                                                fontSize: 12,
                                                color: color,
                                                fontWeight: FontWeight.w600,
                                              ),
                                            ),
                                          ],
                                        ),
                                      ],
                                    ),
                                    const SizedBox(height: 8),
                                    LinearProgressIndicator(
                                      value: progress > 2 ? 2 : progress,
                                      minHeight: 8,
                                      backgroundColor: Colors.grey.shade200,
                                      valueColor:
                                          AlwaysStoppedAnimation<Color>(
                                        color,
                                      ),
                                    ),
                                    const SizedBox(height: 8),
                                    Text(
                                      '$percent% • R\$ ${g.currentAmount.toStringAsFixed(2)} '
                                      'de R\$ ${g.targetAmount.toStringAsFixed(2)}',
                                      style: const TextStyle(fontSize: 12),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          ),
                        );
                      },
                    ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => _openGoalForm(),
        icon: const Icon(Icons.add),
        label: const Text('Nova meta'),
      ),
    );
  }
}

class _GoalForm extends StatefulWidget {
  final GoalRepository repository;
  final GoalEntity? initial;
  final VoidCallback onSubmit;

  const _GoalForm({
    required this.repository,
    required this.onSubmit,
    this.initial,
  });

  @override
  State<_GoalForm> createState() => _GoalFormState();
}

class _GoalFormState extends State<_GoalForm> {
  final _formKey = GlobalKey<FormState>();

  String _name = '';
  double _targetAmount = 0;
  double _currentAmount = 0;
  bool _isActive = true;
  bool _isSubmitting = false;

  String _onlyDigitsAndComma(String value) {
    return value.replaceAll(RegExp(r'[^0-9,\.]'), '');
  }

  @override
  void initState() {
    super.initState();
    if (widget.initial != null) {
      final g = widget.initial!;
      _name = g.name;
      _targetAmount = g.targetAmount;
      _currentAmount = g.currentAmount;
      _isActive = g.isActive;
    }
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    _formKey.currentState!.save();

    setState(() {
      _isSubmitting = true;
    });

    try {
      final isEditing = widget.initial != null;

      if (isEditing) {
        await widget.repository.updateGoal(
          id: int.parse(widget.initial!.id),
          descricao: _name,
          valorObjetivo: _targetAmount,
          valorAtual: _currentAmount,
        );
      } else {
        await widget.repository.createGoal(
          descricao: _name,
          valorObjetivo: _targetAmount,
          categoriaId: 1,
          tipo: ObjectiveType.metaEconomiaMes,
        );
      }

      widget.onSubmit();
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Erro ao salvar meta: ${e.toString()}'),
            backgroundColor: Colors.red,
          ),
        );
      }
      setState(() {
        _isSubmitting = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final isEditing = widget.initial != null;

    return Form(
      key: _formKey,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Text(
            isEditing ? 'Editar meta' : 'Nova meta',
            style: Theme.of(context).textTheme.titleMedium,
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 16),
          TextFormField(
            initialValue: _name,
            decoration: const InputDecoration(
              labelText: 'Nome da meta',
            ),
            validator: (value) {
              if (value == null || value.trim().isEmpty) {
                return 'Informe o nome da meta';
              }
              return null;
            },
            onSaved: (value) => _name = value!.trim(),
          ),
          const SizedBox(height: 12),
          TextFormField(
            initialValue:
                _targetAmount > 0 ? _targetAmount.toStringAsFixed(2) : '',
            decoration: const InputDecoration(
              labelText: 'Valor alvo (R\$)',
            ),
            keyboardType: const TextInputType.numberWithOptions(decimal: true),
            validator: (value) {
              final clean = _onlyDigitsAndComma(value ?? '');
              if (clean.isEmpty) {
                return 'Informe o valor alvo';
              }
              final parsed = double.tryParse(clean.replaceAll(',', '.'));
              if (parsed == null || parsed <= 0) {
                return 'Valor inválido';
              }
              return null;
            },
            onSaved: (value) {
              final clean = _onlyDigitsAndComma(value ?? '');
              _targetAmount = double.parse(clean.replaceAll(',', '.'));
            },
          ),
          const SizedBox(height: 12),
          TextFormField(
            initialValue:
                _currentAmount > 0 ? _currentAmount.toStringAsFixed(2) : '',
            decoration: const InputDecoration(
              labelText: 'Valor atual (R\$)',
            ),
            keyboardType: const TextInputType.numberWithOptions(decimal: true),
            validator: (value) {
              final clean = _onlyDigitsAndComma(value ?? '');
              if (clean.isEmpty) {
                return null;
              }
              final parsed = double.tryParse(clean.replaceAll(',', '.'));
              if (parsed == null || parsed < 0) {
                return 'Valor inválido';
              }
              return null;
            },
            onSaved: (value) {
              final clean = _onlyDigitsAndComma(value ?? '');
              if (clean.isEmpty) {
                _currentAmount = 0;
              } else {
                _currentAmount = double.parse(clean.replaceAll(',', '.'));
              }
            },
          ),
          const SizedBox(height: 12),
          SwitchListTile(
            title: const Text('Meta ativa'),
            value: _isActive,
            onChanged: (value) {
              setState(() {
                _isActive = value;
              });
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
