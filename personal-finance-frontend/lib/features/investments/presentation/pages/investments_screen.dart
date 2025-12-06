import 'package:flutter/material.dart';
import 'package:sgfi/features/investments/domain/entities/investment_entity.dart';
import 'package:sgfi/features/investments/domain/repositories/investment_repository.dart';
import 'package:sgfi/features/investments/data/repositories/investment_repository_impl.dart';
import 'package:sgfi/features/investments/data/datasources/investment_remote_datasource_impl.dart';

class InvestmentsScreen extends StatefulWidget {
  const InvestmentsScreen({super.key});

  @override
  State<InvestmentsScreen> createState() => _InvestmentsScreenState();
}

class _InvestmentsScreenState extends State<InvestmentsScreen> {
  late final InvestmentRepository _investmentRepository;
  List<InvestmentEntity> _allInvestments = [];
  bool _isLoading = true;
  String? _errorMessage;
  InvestmentType? _filterType;

  @override
  void initState() {
    super.initState();
    _investmentRepository = InvestmentRepositoryImpl(
      remoteDataSource: InvestmentRemoteDataSourceImpl(),
    );
    _loadInvestments();
  }

  Future<void> _loadInvestments() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      final investments = await _investmentRepository.getAllInvestments();
      setState(() {
        _allInvestments = investments;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _errorMessage = 'Erro ao carregar investimentos: ${e.toString()}';
        _isLoading = false;
      });
    }
  }

  List<InvestmentEntity> get _filteredInvestments {
    if (_filterType == null) return _allInvestments;
    return _allInvestments.where((i) => i.type == _filterType).toList();
  }

  double get _totalInvested =>
      _allInvestments.fold(0.0, (sum, i) => sum + (i.totalInvested ?? 0));

  double get _currentValue =>
      _allInvestments.fold(0.0, (sum, i) => sum + (i.currentValue ?? 0));

  double get _totalProfit => _currentValue - _totalInvested;

  String _formatCurrency(double value) {
    final sign = value < 0 ? '-' : '';
    final abs = value.abs().toStringAsFixed(2);
    return '${sign}R\$ $abs';
  }

  Color _profitColor(double value) {
    if (value > 0) return Colors.green;
    if (value < 0) return Colors.red;
    return Colors.grey;
  }

  String _typeLabel(InvestmentType type) {
    switch (type) {
      case InvestmentType.rendaFixa:
        return 'Renda fixa';
      case InvestmentType.acao:
        return 'Ação';
      case InvestmentType.fundo:
        return 'Fundo';
      case InvestmentType.cripto:
        return 'Cripto';
      case InvestmentType.tesouroDireto:
        return 'Tesouro direto';
      case InvestmentType.cdb:
        return 'CDB';
    }
  }

  void _setFilter(InvestmentType? type) {
    setState(() {
      _filterType = type;
    });
  }

  void _openAddInvestmentSheet() {
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
          child: _InvestmentForm(
            repository: _investmentRepository,
            onSubmit: () {
              Navigator.of(ctx).pop();
              _loadInvestments();
            },
          ),
        );
      },
    );
  }

  void _openEditInvestmentSheet(InvestmentEntity investment) {
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
            bottom: MediaQuery.of(ctx).viewInsets.bottom + 16),
          child: _InvestmentForm(
            repository: _investmentRepository,
            initial: investment,
            onSubmit: () {
              Navigator.of(ctx).pop();
              _loadInvestments();
            },
          ),
        );
      },
    );
  }

  void _deleteInvestment(InvestmentEntity inv) async {
    try {
      await _investmentRepository.deleteInvestment(int.parse(inv.id));
      _loadInvestments();
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Investimento "${inv.name}" removido.'),
          ),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Erro ao remover investimento: ${e.toString()}'),
            backgroundColor: Colors.red,
          ),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final investments = _filteredInvestments;
    final profitColor = _profitColor(_totalProfit);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Investimentos'),
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _openAddInvestmentSheet,
        icon: const Icon(Icons.add),
        label: const Text('Adicionar'),
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
                        onPressed: _loadInvestments,
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
              'Resumo da carteira',
              style: Theme.of(context).textTheme.titleMedium,
            ),
            const SizedBox(height: 8),
            Row(
              children: [
                Expanded(
                  child: _SummaryCard(
                    title: 'Total investido',
                    value: _formatCurrency(_totalInvested),
                    highlightColor: Colors.blue,
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: _SummaryCard(
                    title: 'Valor atual',
                    value: _formatCurrency(_currentValue),
                    highlightColor: Colors.deepPurple,
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: _SummaryCard(
                    title: 'Resultado',
                    value: _formatCurrency(_totalProfit),
                    highlightColor: profitColor,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 16),

            Text(
              'Filtrar por tipo',
              style: Theme.of(context).textTheme.titleMedium,
            ),
            const SizedBox(height: 8),
            SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: Row(
                children: [
                  _FilterChip(
                    label: 'Todos',
                    selected: _filterType == null,
                    onSelected: () => _setFilter(null),
                  ),
                  const SizedBox(width: 8),
                  _FilterChip(
                    label: 'Renda fixa',
                    selected: _filterType == InvestmentType.rendaFixa,
                    onSelected: () => _setFilter(InvestmentType.rendaFixa),
                  ),
                  const SizedBox(width: 8),
                  _FilterChip(
                    label: 'Ações',
                    selected: _filterType == InvestmentType.acao,
                    onSelected: () => _setFilter(InvestmentType.acao),
                  ),
                  const SizedBox(width: 8),
                  _FilterChip(
                    label: 'Fundos',
                    selected: _filterType == InvestmentType.fundo,
                    onSelected: () => _setFilter(InvestmentType.fundo),
                  ),
                  const SizedBox(width: 8),
                  _FilterChip(
                    label: 'Cripto',
                    selected: _filterType == InvestmentType.cripto,
                    onSelected: () => _setFilter(InvestmentType.cripto),
                  ),
                  const SizedBox(width: 8),
                  _FilterChip(
                    label: 'Tesouro',
                    selected: _filterType == InvestmentType.tesouroDireto,
                    onSelected: () => _setFilter(InvestmentType.tesouroDireto),
                  ),
                  const SizedBox(width: 8),
                  _FilterChip(
                    label: 'CDB',
                    selected: _filterType == InvestmentType.cdb,
                    onSelected: () => _setFilter(InvestmentType.cdb),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),

            Text(
              'Meus investimentos',
              style: Theme.of(context).textTheme.titleMedium,
            ),
            const SizedBox(height: 8),
            Expanded(
              child: investments.isEmpty
                  ? const Center(
                      child: Text('Nenhum investimento cadastrado.'),
                    )
                  : ListView.separated(
                      itemCount: investments.length,
                      separatorBuilder: (_, __) => const Divider(height: 0),
                      itemBuilder: (context, index) {
                        final inv = investments[index];
                        final p = inv.profit ?? 0;
                        final pColor = _profitColor(p);

                        return Dismissible(
                          key: ValueKey(inv.id),
                          direction: DismissDirection.endToStart,
                          background: Container(
                            color: Colors.red,
                            alignment: Alignment.centerRight,
                            padding: const EdgeInsets.symmetric(horizontal: 16),
                            child: const Icon(Icons.delete, color: Colors.white),
                          ),
                          confirmDismiss: (direction) async {
                            final result = await showDialog<bool>(
                                  context: context,
                                  builder: (ctx) {
                                    return AlertDialog(
                                      title: const Text('Remover investimento'),
                                      content: Text(
                                        'Tem certeza que deseja remover o investimento "${inv.name}"?',
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
                            return result;
                          },
                          onDismissed: (_) {
                            _deleteInvestment(inv);
                          },
                          child: ListTile(
                            leading: CircleAvatar(
                              child: Text(
                                inv.name.isNotEmpty
                                    ? inv.name[0].toUpperCase()
                                    : '?',
                              ),
                            ),
                            title: Text(inv.name),
                            subtitle: Text(
                              '${_typeLabel(inv.type)} • ${inv.broker}',
                            ),
                            trailing: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              crossAxisAlignment: CrossAxisAlignment.end,
                              children: [
                                Text(
                                  _formatCurrency(inv.currentValue ?? 0),
                                  style: const TextStyle(
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                                const SizedBox(height: 2),
                                Text(
                                  '${_formatCurrency(p)} '
                                  '(${(inv.profitPercent ?? 0).toStringAsFixed(1)}%)',
                                  style: TextStyle(
                                    fontSize: 12,
                                    color: pColor,
                                  ),
                                ),
                              ],
                            ),
                            onTap: () {
                              _openEditInvestmentSheet(inv);
                            },
                          ),
                        );
                      },
                    ),
            ),
          ],
        ),
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
                fontSize: 16,
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

class _FilterChip extends StatelessWidget {
  final String label;
  final bool selected;
  final VoidCallback onSelected;

  const _FilterChip({
    required this.label,
    required this.selected,
    required this.onSelected,
  });

  @override
  Widget build(BuildContext context) {
    final themeColor = Theme.of(context).colorScheme.primary;

    return ChoiceChip(
      label: Text(label),
      selected: selected,
      onSelected: (_) => onSelected(),
      selectedColor: themeColor.withValues(alpha: 0.15),
      labelStyle: TextStyle(
        color: selected ? themeColor : Colors.black87,
      ),
      side: BorderSide(
        color: selected ? themeColor : Colors.grey.shade300,
      ),
    );
  }
}

class _InvestmentForm extends StatefulWidget {
  final InvestmentRepository repository;
  final VoidCallback onSubmit;
  final InvestmentEntity? initial;

  const _InvestmentForm({
    required this.repository,
    required this.onSubmit,
    this.initial,
  });

  @override
  State<_InvestmentForm> createState() => _InvestmentFormState();
}

class _InvestmentFormState extends State<_InvestmentForm> {
  final _formKey = GlobalKey<FormState>();

  InvestmentType _type = InvestmentType.rendaFixa;
  String _name = '';
  String _simbolo = '';
  String _broker = '';
  double _quantity = 0;
  double _buyPrice = 0;
  double? _currentPrice;
  bool _isSubmitting = false;

  @override
  void initState() {
    super.initState();

    if (widget.initial != null) {
      final inv = widget.initial!;
      _type = inv.type;
      _name = inv.name;
      _simbolo = inv.symbol;
      _broker = inv.broker;
      _quantity = inv.quantity;
      _buyPrice = inv.buyPrice;
      _currentPrice = inv.currentPrice;
    }
  }

  String _onlyDigitsAndComma(String value) {
    return value.replaceAll(RegExp(r'[^0-9,\.]'), '');
  }

  double _parseCurrency(String? value) {
    final clean = _onlyDigitsAndComma(value ?? '');
    if (clean.isEmpty) return 0;
    return double.parse(clean.replaceAll(',', '.'));
  }

  String _serializeInvestmentType(InvestmentType type) {
    switch (type) {
      case InvestmentType.cripto:
        return 'CRIPTO';
      case InvestmentType.acao:
        return 'ACAO';
      case InvestmentType.fundo:
        return 'FUNDO';
      case InvestmentType.rendaFixa:
        return 'RENDA_FIXA';
      case InvestmentType.tesouroDireto:
        return 'TESOURO_DIRETO';
      case InvestmentType.cdb:
        return 'CDB';
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

      final tipoString = _serializeInvestmentType(_type);

      if (isEditing) {
        await widget.repository.updateInvestment(
          id: int.parse(widget.initial!.id),
          nomeAtivo: _name,
          simbolo: _simbolo,
          quantidade: _quantity,
          valorCompra: _buyPrice,
          dataCompra: DateTime.now(),
          corretora: _broker,
        );
      } else {
        await widget.repository.createInvestment(
          tipoInvestimento: tipoString,
          nomeAtivo: _name,
          simbolo: _simbolo,
          quantidade: _quantity,
          valorCompra: _buyPrice,
          dataCompra: DateTime.now(),
          corretora: _broker,
        );
      }

      widget.onSubmit();
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Erro ao salvar investimento: ${e.toString()}'),
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
      child: SingleChildScrollView(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Text(
              isEditing ? 'Editar investimento' : 'Novo investimento',
              style: Theme.of(context).textTheme.titleMedium,
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 16),
            DropdownButtonFormField<InvestmentType>(
              value: _type,
              decoration: const InputDecoration(
                labelText: 'Tipo',
              ),
              items: const [
                DropdownMenuItem(
                  value: InvestmentType.rendaFixa,
                  child: Text('Renda fixa'),
                ),
                DropdownMenuItem(
                  value: InvestmentType.acao,
                  child: Text('Ação'),
                ),
                DropdownMenuItem(
                  value: InvestmentType.fundo,
                  child: Text('Fundo'),
                ),
                DropdownMenuItem(
                  value: InvestmentType.cripto,
                  child: Text('Cripto'),
                ),
                DropdownMenuItem(
                  value: InvestmentType.tesouroDireto,
                  child: Text('Tesouro direto'),
                ),
                DropdownMenuItem(
                  value: InvestmentType.cdb,
                  child: Text('CDB'),
                ),
              ],
              onChanged: (value) {
                if (value != null) {
                  setState(() {
                    _type = value;
                  });
                }
              },
            ),
            const SizedBox(height: 12),
            TextFormField(
              initialValue: _name,
              decoration: const InputDecoration(
                labelText: 'Nome do ativo',
              ),
              validator: (value) {
                if (value == null || value.trim().isEmpty) {
                  return 'Informe o nome';
                }
                return null;
              },
              onSaved: (value) => _name = value!.trim(),
            ),
            const SizedBox(height: 12),
            TextFormField(
              initialValue: _simbolo,
              decoration: const InputDecoration(
                labelText: 'Símbolo / Ticker',
                hintText: 'Ex: PETR4, BTCUSD, TESOURO SELIC',
              ),
              validator: (value) {
                if (value == null || value.trim().isEmpty) {
                  return 'Informe o símbolo';
                }
                if (value.trim().length > 20) {
                  return 'Símbolo deve ter no máximo 20 caracteres';
                }
                return null;
              },
              onSaved: (value) => _simbolo = value!.trim(),
            ),
            const SizedBox(height: 12),
            TextFormField(
              initialValue: _broker,
              decoration: const InputDecoration(
                labelText: 'Corretora / Instituição',
              ),
              validator: (value) {
                if (value == null || value.trim().isEmpty) {
                  return 'Informe a corretora';
                }
                return null;
              },
              onSaved: (value) => _broker = value!.trim(),
            ),
            const SizedBox(height: 12),
            TextFormField(
              initialValue: _quantity > 0 ? _quantity.toStringAsFixed(2) : '',
              decoration: const InputDecoration(
                labelText: 'Quantidade',
              ),
              keyboardType:
                  const TextInputType.numberWithOptions(decimal: true),
              validator: (value) {
                final clean = _onlyDigitsAndComma(value ?? '');
                if (clean.isEmpty) {
                  return 'Informe a quantidade';
                }
                final parsed = double.tryParse(clean.replaceAll(',', '.'));
                if (parsed == null || parsed <= 0) {
                  return 'Quantidade inválida';
                }
                return null;
              },
              onSaved: (value) {
                _quantity = _parseCurrency(value);
              },
            ),
            const SizedBox(height: 12),
            TextFormField(
              initialValue: _buyPrice > 0 ? _buyPrice.toStringAsFixed(2) : '',
              decoration: const InputDecoration(
                labelText: 'Preço de compra (R\$)',
              ),
              keyboardType:
                  const TextInputType.numberWithOptions(decimal: true),
              validator: (value) {
                final clean = _onlyDigitsAndComma(value ?? '');
                if (clean.isEmpty) {
                  return 'Informe o preço de compra';
                }
                final parsed = double.tryParse(clean.replaceAll(',', '.'));
                if (parsed == null || parsed <= 0) {
                  return 'Preço inválido';
                }
                return null;
              },
              onSaved: (value) {
                _buyPrice = _parseCurrency(value);
              },
            ),
            const SizedBox(height: 12),
            TextFormField(
              initialValue:
                  _currentPrice != null ? _currentPrice!.toStringAsFixed(2) : '',
              decoration: const InputDecoration(
                labelText: 'Preço atual (R\$) - opcional',
              ),
              keyboardType:
                  const TextInputType.numberWithOptions(decimal: true),
              onSaved: (value) {
                final clean = _onlyDigitsAndComma(value ?? '');
                if (clean.isEmpty) {
                  _currentPrice = null;
                } else {
                  _currentPrice = _parseCurrency(value);
                }
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
      ),
    );
  }
}
