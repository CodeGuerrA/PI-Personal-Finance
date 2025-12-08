import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sgfi/features/categories/domain/entities/category_entity.dart';
import 'package:sgfi/features/categories/presentation/providers/category_provider.dart';

class CategoriesScreen extends StatefulWidget {
  const CategoriesScreen({super.key});

  @override
  State<CategoriesScreen> createState() => _CategoriesScreenState();
}

class _CategoriesScreenState extends State<CategoriesScreen> {
  // Estado local apenas para filtro (UI state)
  bool? _filterIncome;

  @override
  void initState() {
    super.initState();
    // Carregar categorias do provider
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<CategoryProvider>().loadCategories();
    });
  }

  List<CategoryEntity> _getFilteredCategories(List<CategoryEntity> allCategories) {
    if (_filterIncome == null) return allCategories;
    return allCategories.where((c) => c.isIncome == _filterIncome).toList();
  }

  void _setFilter(bool? value) {
    setState(() {
      _filterIncome = value;
    });
  }

  void _openCategoryDialog({CategoryEntity? editing}) async {
    final isEditing = editing != null;
    final TextEditingController nameController = TextEditingController(
      text: editing?.name ?? '',
    );
    bool isIncome = editing?.isIncome ?? true;

    await showDialog(
      context: context,
      builder: (ctx) {
        return AlertDialog(
          title: Text(isEditing ? 'Editar categoria' : 'Nova categoria'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: nameController,
                decoration: const InputDecoration(
                  labelText: 'Nome da categoria',
                ),
              ),
              const SizedBox(height: 12),
              Row(
                children: [
                  Expanded(
                    child: RadioListTile<bool>(
                      value: true,
                      groupValue: isIncome,
                      dense: true,
                      title: const Text('Receita'),
                      onChanged: (value) {
                        if (value != null) {
                          setState(() {
                            isIncome = value;
                          });
                          // forçar rebuild do dialog
                          (ctx as Element).markNeedsBuild();
                        }
                      },
                    ),
                  ),
                  Expanded(
                    child: RadioListTile<bool>(
                      value: false,
                      groupValue: isIncome,
                      dense: true,
                      title: const Text('Despesa'),
                      onChanged: (value) {
                        if (value != null) {
                          setState(() {
                            isIncome = value;
                          });
                          (ctx as Element).markNeedsBuild();
                        }
                      },
                    ),
                  ),
                ],
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(ctx).pop(),
              child: const Text('Cancelar'),
            ),
            TextButton(
              onPressed: () async {
                final name = nameController.text.trim();
                if (name.isEmpty) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                      content: Text('Informe o nome da categoria.'),
                    ),
                  );
                  return;
                }

                Navigator.of(ctx).pop();

                final categoryProvider = context.read<CategoryProvider>();
                bool success;

                if (isEditing) {
                  success = await categoryProvider.updateCategory(
                    id: int.parse(editing.id),
                    nome: name,
                  );
                } else {
                  success = await categoryProvider.createCategory(
                    nome: name,
                    tipo: isIncome ? 'receita' : 'despesa',
                  );
                }

                if (!success && mounted) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Text('Erro ao salvar categoria: ${categoryProvider.errorMessage ?? "Desconhecido"}'),
                      backgroundColor: Colors.red,
                    ),
                  );
                }
              },
              child: Text(isEditing ? 'Salvar' : 'Adicionar'),
            ),
          ],
        );
      },
    );
  }

  void _deleteCategory(CategoryEntity category) async {
    if (category.isDefault) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text(
            'Categorias padrão do sistema não podem ser excluídas.',
          ),
        ),
      );
      return;
    }

    final confirm = await showDialog<bool>(
          context: context,
          builder: (ctx) {
            return AlertDialog(
              title: const Text('Remover categoria'),
              content: Text(
                'Tem certeza que deseja remover a categoria "${category.name}"?',
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

    final categoryProvider = context.read<CategoryProvider>();
    final success = await categoryProvider.deleteCategory(int.parse(category.id));

    if (mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(success
              ? 'Categoria "${category.name}" removida.'
              : 'Erro ao remover categoria: ${categoryProvider.errorMessage ?? "Desconhecido"}'),
          backgroundColor: success ? null : Colors.red,
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Categorias'),
      ),
      body: Consumer<CategoryProvider>(
        builder: (context, categoryProvider, child) {
          if (categoryProvider.isLoading) {
            return const Center(child: CircularProgressIndicator());
          }

          if (categoryProvider.errorMessage != null) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Icon(Icons.error_outline, size: 48, color: Colors.red),
                  const SizedBox(height: 16),
                  Text(categoryProvider.errorMessage!, textAlign: TextAlign.center),
                  const SizedBox(height: 16),
                  ElevatedButton(
                    onPressed: () => categoryProvider.loadCategories(refresh: true),
                    child: const Text('Tentar novamente'),
                  ),
                ],
              ),
            );
          }

          final categories = _getFilteredCategories(categoryProvider.categories);
          return Column(
        children: [
          Padding(
            padding:
                const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            child: Row(
              children: [
                ChoiceChip(
                  label: const Text('Todas'),
                  selected: _filterIncome == null,
                  onSelected: (_) => _setFilter(null),
                ),
                const SizedBox(width: 8),
                ChoiceChip(
                  label: const Text('Receitas'),
                  selected: _filterIncome == true,
                  onSelected: (_) => _setFilter(true),
                ),
                const SizedBox(width: 8),
                ChoiceChip(
                  label: const Text('Despesas'),
                  selected: _filterIncome == false,
                  onSelected: (_) => _setFilter(false),
                ),
              ],
            ),
          ),
          const Divider(height: 0),
          Expanded(
            child: categories.isEmpty
                ? const Center(
                    child: Text('Nenhuma categoria encontrada.'),
                  )
                : ListView.separated(
                    itemCount: categories.length,
                    separatorBuilder: (_, __) =>
                        const Divider(height: 0),
                    itemBuilder: (context, index) {
                      final c = categories[index];
                      final icon = c.isIncome
                          ? Icons.arrow_upward
                          : Icons.arrow_downward;
                      final color =
                          c.isIncome ? Colors.green : Colors.red;

                      return ListTile(
                        leading: CircleAvatar(
                          backgroundColor: color.withValues(alpha: 0.1),
                          child: Icon(
                            icon,
                            color: color,
                          ),
                        ),
                        title: Text(c.name),
                        subtitle: Text(
                          c.isIncome ? 'Receita' : 'Despesa',
                        ),
                        trailing: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            IconButton(
                              icon: const Icon(Icons.edit_outlined),
                              onPressed: () {
                                if (c.isDefault) {
                                  ScaffoldMessenger.of(context)
                                      .showSnackBar(
                                    const SnackBar(
                                      content: Text(
                                        'Categorias padrão não podem ser editadas.',
                                      ),
                                    ),
                                  );
                                  return;
                                }
                                _openCategoryDialog(editing: c);
                              },
                            ),
                            IconButton(
                              icon: const Icon(Icons.delete_outline),
                              onPressed: () => _deleteCategory(c),
                            ),
                          ],
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
        onPressed: () => _openCategoryDialog(),
        icon: const Icon(Icons.add),
        label: const Text('Adicionar'),
      ),
    );
  }
}
