import '../models/auth_tokens_model.dart';
import '../models/user_model.dart';

abstract class AuthRemoteDataSource {
  Future<AuthTokensModel> login({
    required String username,
    required String password,
  });

  Future<AuthTokensModel> refreshToken({
    required String refreshToken,
  });

  Future<UserModel> registerUser({
    required String email,
    required String firstName,
    required String lastName,
    required String cpf,
  });

  Future<UserModel> getCurrentUser();

  Future<UserModel> updateCurrentUser({
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

  Future<void> deleteCurrentUser();

  Future<void> deleteAccount();

  Future<void> forgotPassword(String email);

  Future<void> resetPassword({
    required String email,
    required String code,
    required String newPassword,
  });
}

