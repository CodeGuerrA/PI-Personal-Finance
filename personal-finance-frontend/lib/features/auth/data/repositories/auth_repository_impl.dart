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

  @override
  Future<void> changePassword({
    required String currentPassword,
    required String newPassword,
  }) async {
    await remoteDataSource.changePassword(
      currentPassword: currentPassword,
      newPassword: newPassword,
    );
  }

  @override
  Future<void> setPassword({
    required int userId,
    required String newPassword,
  }) async {
    await remoteDataSource.setPassword(
      userId: userId,
      newPassword: newPassword,
    );
  }

  @override
  Future<void> forgotPassword(String email) async {
    await remoteDataSource.forgotPassword(email);
  }

  @override
  Future<void> resetPassword({
    required String email,
    required String code,
    required String newPassword,
  }) async {
    await remoteDataSource.resetPassword(
      email: email,
      code: code,
      newPassword: newPassword,
    );
  }

  @override
  Future<void> deleteAccount() async {
    await remoteDataSource.deleteAccount();
  }

  @override
  Future<AuthTokensEntity> refreshToken(String refreshToken) async {
    final tokens = await remoteDataSource.refreshToken(
      refreshToken: refreshToken,
    );
    return tokens;
  }
}
