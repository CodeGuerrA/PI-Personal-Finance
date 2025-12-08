import '../entities/auth_tokens_entity.dart';
import '../entities/user_entity.dart';

abstract class AuthRepository {
  Future<AuthTokensEntity> login({
    required String username,
    required String password,
  });

  Future<UserEntity> registerUser({
    required String email,
    required String firstName,
    required String lastName,
    required String cpf,
  });

  Future<UserEntity> getCurrentUser();

  Future<UserEntity> updateCurrentUser({
    required String email,
  });

  Future<void> changePassword({
    required String currentPassword,
    required String newPassword,
  });

  Future<void> setPassword({
    required int userId,
    required String newPassword,
  });

  Future<void> forgotPassword(String email);

  Future<void> resetPassword({
    required String email,
    required String code,
    required String newPassword,
  });

  Future<void> deleteAccount();

  Future<AuthTokensEntity> refreshToken(String refreshToken);
}

