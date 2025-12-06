import '../../domain/entities/auth_tokens_entity.dart';
import '../../domain/entities/user_entity.dart';
import '../../domain/repositories/auth_repository.dart';
import '../datasources/auth_remote_datasource.dart';

class AuthRepositoryImpl implements AuthRepository {
  final AuthRemoteDataSource remoteDataSource;

  AuthRepositoryImpl(this.remoteDataSource);

  @override
  Future<AuthTokensEntity> login({
    required String username,
    required String password,
  }) async {
    final tokens = await remoteDataSource.login(
      username: username,
      password: password,
    );
    return tokens;
  }

  @override
  Future<UserEntity> registerUser({
    required String email,
    required String firstName,
    required String lastName,
    required String cpf,
  }) async {
    final userModel = await remoteDataSource.registerUser(
      email: email,
      firstName: firstName,
      lastName: lastName,
      cpf: cpf,
    );
    return userModel;
  }

  @override
  Future<UserEntity> getCurrentUser() async {
    final userModel = await remoteDataSource.getCurrentUser();
    return userModel;
  }

  @override
  Future<UserEntity> updateCurrentUser({
    required String email,
  }) async {
    final userModel = await remoteDataSource.updateCurrentUser(email: email);
    return userModel;
  }
}
