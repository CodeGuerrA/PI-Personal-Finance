import '../../domain/entities/category_entity.dart';

class CategoryModel extends CategoryEntity {
  CategoryModel({
    required super.id,
    required super.name,
    required super.isDefault,
    required super.isIncome,
    super.cor,
    super.icone,
    super.ativa,
    this.usuarioId,
  });

  final int? usuarioId;

  factory CategoryModel.fromJson(Map<String, dynamic> json) {
    return CategoryModel(
      id: json['id'].toString(),
      name: json['nome'] ?? '',
      isDefault: json['padrao'] ?? false,
      isIncome: _parseCategoryType(json['tipo']),
      cor: json['cor'],
      icone: json['icone'],
      ativa: json['ativa'],
      usuarioId: json['usuarioId'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nome': name,
      'tipo': isIncome ? 'RECEITA' : 'DESPESA',
      'padrao': isDefault,
      'cor': cor,
      'icone': icone,
      'ativa': ativa,
      'usuarioId': usuarioId,
    };
  }

  static bool _parseCategoryType(String? tipo) {
    if (tipo == null) return false;
    return tipo.toUpperCase() == 'RECEITA';
  }
}
