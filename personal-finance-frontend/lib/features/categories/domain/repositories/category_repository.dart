import '../entities/category_entity.dart';

abstract class CategoryRepository {
  Future<CategoryEntity> createCategory({
    required String nome,
    required String tipo,
    String? cor,
    String? icone,
  });

  Future<CategoryEntity> getCategoryById(int id);

  Future<List<CategoryEntity>> getAllCategories();

  Future<List<CategoryEntity>> getCategoriesByTipo(String tipo);

  Future<CategoryEntity> updateCategory({
    required int id,
    required String nome,
    String? cor,
    String? icone,
  });

  Future<void> deleteCategory(int id);

  Future<List<CategoryEntity>> searchCategoriesByName(String nome);

  Future<int> countCategories();
}
