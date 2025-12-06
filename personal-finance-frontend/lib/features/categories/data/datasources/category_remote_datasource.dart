import '../models/category_model.dart';

abstract class CategoryRemoteDataSource {
  Future<CategoryModel> createCategory({
    required String nome,
    required String tipo,
    String? cor,
    String? icone,
  });

  Future<CategoryModel> getCategoryById(int id);

  Future<List<CategoryModel>> getAllCategories();

  Future<List<CategoryModel>> getCategoriesByTipo(String tipo);

  Future<CategoryModel> updateCategory({
    required int id,
    required String nome,
    String? cor,
    String? icone,
  });

  Future<void> deleteCategory(int id);

  Future<List<CategoryModel>> searchCategoriesByName(String nome);

  Future<int> countCategories();

  Future<List<Map<String, dynamic>>> getMostUsedCategories();
}
