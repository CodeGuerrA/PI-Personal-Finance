import '../../domain/entities/category_entity.dart';
import '../../domain/repositories/category_repository.dart';
import '../datasources/category_remote_datasource.dart';

class CategoryRepositoryImpl implements CategoryRepository {
  final CategoryRemoteDataSource _remoteDataSource;

  CategoryRepositoryImpl({required CategoryRemoteDataSource remoteDataSource})
      : _remoteDataSource = remoteDataSource;

  @override
  Future<CategoryEntity> createCategory({
    required String nome,
    required String tipo,
    String? cor,
    String? icone,
  }) async {
    return await _remoteDataSource.createCategory(
      nome: nome,
      tipo: tipo,
      cor: cor,
      icone: icone,
    );
  }

  @override
  Future<CategoryEntity> getCategoryById(int id) async {
    return await _remoteDataSource.getCategoryById(id);
  }

  @override
  Future<List<CategoryEntity>> getAllCategories() async {
    final models = await _remoteDataSource.getAllCategories();
    return models.cast<CategoryEntity>();
  }

  @override
  Future<List<CategoryEntity>> getCategoriesByTipo(String tipo) async {
    final models = await _remoteDataSource.getCategoriesByTipo(tipo);
    return models.cast<CategoryEntity>();
  }

  @override
  Future<CategoryEntity> updateCategory({
    required int id,
    required String nome,
    String? cor,
    String? icone,
  }) async {
    return await _remoteDataSource.updateCategory(
      id: id,
      nome: nome,
      cor: cor,
      icone: icone,
    );
  }

  @override
  Future<void> deleteCategory(int id) async {
    return await _remoteDataSource.deleteCategory(id);
  }

  @override
  Future<List<CategoryEntity>> searchCategoriesByName(String nome) async {
    final models = await _remoteDataSource.searchCategoriesByName(nome);
    return models.cast<CategoryEntity>();
  }

  @override
  Future<int> countCategories() async {
    return await _remoteDataSource.countCategories();
  }
}
