class CategoryEntity {
  final String id;
  final String name;
  final bool isDefault; // true = categoria padrão do sistema
  final bool isIncome;  // true = receita, false = despesa
  final String? cor;
  final String? icone;
  final bool? ativa;

  CategoryEntity({
    required this.id,
    required this.name,
    required this.isDefault,
    required this.isIncome,
    this.cor,
    this.icone,
    this.ativa,
  });
}
