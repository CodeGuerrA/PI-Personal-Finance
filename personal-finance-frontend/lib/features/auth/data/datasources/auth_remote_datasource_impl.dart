import '../../../../core/network/api_client.dart';
import '../../../../core/storage/token_storage.dart';
import '../models/auth_tokens_model.dart';
import '../models/user_model.dart';
import 'auth_remote_datasource.dart';

class AuthRemoteDataSourceImpl implements AuthRemoteDataSource {
  final ApiClient _apiClient;
  final TokenStorage _tokenStorage;

  AuthRemoteDataSourceImpl({
    ApiClient? apiClient,
    TokenStorage? tokenStorage,
  })  : _apiClient = apiClient ?? ApiClient(),
        _tokenStorage = tokenStorage ?? TokenStorage();

  @override
  Future<AuthTokensModel> login({
    required String username,
    required String password,
  }) async {
    final response = await _apiClient.post(
      '/auth/login',
      body: {
        'username': username,
        'password': password,
      },
      requiresAuth: false,
    );

    if (response.statusCode == 200) {
      final authTokens = _apiClient.parseResponse<AuthTokensModel>(
        response,
        (json) => AuthTokensModel.fromJson(json),
      );

      if (authTokens != null) {
        await _tokenStorage.saveTokens(
          accessToken: authTokens.accessToken,
          refreshToken: authTokens.refreshToken,
          expiresIn: authTokens.expiresIn,
          refreshExpiresIn: authTokens.refreshExpiresIn,
          username: authTokens.username,
          requiresPasswordChange: authTokens.requiresPasswordChange,
        );
        return authTokens;
      }
    }

    throw Exception(
      'Erro ao fazer login (código ${response.statusCode})',
    );
  }

  @override
  Future<UserModel> registerUser({
    required String email,
    required String firstName,
    required String lastName,
    required String cpf,
  }) async {
    final response = await _apiClient.post(
      '/users',
      body: {
        'email': email,
        'firstName': firstName,
        'lastName': lastName,
        'cpf': cpf,
      },
      requiresAuth: false,
    );

    if (response.statusCode == 201 || response.statusCode == 200) {
      final user = _apiClient.parseResponse<UserModel>(
        response,
        (json) => UserModel.fromJson(json),
      );

      if (user != null) {
        return user;
      }
    }

    throw Exception(
      'Erro ao cadastrar usuário (código ${response.statusCode})',
    );
  }

  @override
  Future<AuthTokensModel> refreshToken({
    required String refreshToken,
  }) async {
    final response = await _apiClient.post(
      '/auth/refresh?refreshToken=$refreshToken',
      body: {},
      requiresAuth: false,
    );

    if (response.statusCode == 200) {
      final authTokens = _apiClient.parseResponse<AuthTokensModel>(
        response,
        (json) => AuthTokensModel.fromJson(json),
      );

      if (authTokens != null) {
        await _tokenStorage.saveTokens(
          accessToken: authTokens.accessToken,
          refreshToken: authTokens.refreshToken,
          expiresIn: authTokens.expiresIn,
          refreshExpiresIn: authTokens.refreshExpiresIn,
          username: authTokens.username,
          requiresPasswordChange: authTokens.requiresPasswordChange,
        );
        return authTokens;
      }
    }

    throw Exception('Erro ao renovar token');
  }

  @override
  Future<UserModel> getCurrentUser() async {
    final response = await _apiClient.get('/users/me');

    final user = _apiClient.parseResponse<UserModel>(
      response,
      (json) => UserModel.fromJson(json),
    );

    if (user != null) {
      return user;
    }

    throw Exception('Erro ao buscar usuário');
  }

  @override
  Future<UserModel> updateCurrentUser({
    required String email,
  }) async {
    final response = await _apiClient.put(
      '/users/me',
      body: {
        'email': email,
      },
    );

    final user = _apiClient.parseResponse<UserModel>(
      response,
      (json) => UserModel.fromJson(json),
    );

    if (user != null) {
      return user;
    }

    throw Exception('Erro ao atualizar usuário');
  }

  @override
  Future<void> changePassword({
    required String currentPassword,
    required String newPassword,
  }) async {
    final response = await _apiClient.patch(
      '/users/me/password',
      body: {
        'currentPassword': currentPassword,
        'newPassword': newPassword,
      },
    );

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception('Erro ao alterar senha');
    }
  }

  @override
  Future<void> deleteCurrentUser() async {
    await _apiClient.delete('/users/me');
    await _tokenStorage.clearTokens();
  }

  @override
  Future<void> forgotPassword({
    required String email,
  }) async {
    final response = await _apiClient.post(
      '/auth/forgot-password',
      body: {
        'email': email,
      },
      requiresAuth: false,
    );

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception('Erro ao solicitar recuperação de senha');
    }
  }

  @override
  Future<void> resetPassword({
    required String email,
    required String code,
    required String newPassword,
  }) async {
    final response = await _apiClient.post(
      '/auth/reset-password',
      body: {
        'email': email,
        'code': code,
        'newPassword': newPassword,
      },
      requiresAuth: false,
    );

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception('Erro ao redefinir senha');
    }
  }
}
