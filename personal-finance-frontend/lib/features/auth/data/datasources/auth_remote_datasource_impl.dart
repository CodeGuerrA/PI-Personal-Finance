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
      try {
        final user = _apiClient.parseResponse<UserModel>(
          response,
          (json) => UserModel.fromJson(json),
        );

        if (user != null) {
          return user;
        }
      } catch (e) {
        // Se não conseguir fazer parse do JSON, tenta extrair informações da resposta
        print('Erro ao fazer parse da resposta: $e');
        print('Response body: ${response.body}');

        // Se o cadastro foi bem-sucedido (201/200) mas não retornou JSON válido,
        // retorna um UserModel básico com os dados fornecidos
        if (response.statusCode == 201 || response.statusCode == 200) {
          // Extrai username da mensagem se possível
          final body = response.body;
          String? userName;

          // Tenta extrair username da mensagem "Usuário criado com sucesso: username"
          final match = RegExp(r'sucesso[:\s]+(\w+)').firstMatch(body);
          if (match != null && match.groupCount >= 1) {
            userName = match.group(1);
          }

          return UserModel(
            id: '0', // ID temporário - será atualizado no próximo login
            userName: userName ?? email.split('@')[0],
            firstName: firstName,
            lastName: lastName,
            email: email,
            cpf: cpf,
          );
        }
      }
    }

    throw Exception(
      'Erro ao cadastrar usuário (código ${response.statusCode}): ${response.body}',
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
  Future<void> setPassword({
    required int userId,
    required String newPassword,
  }) async {
    final response = await _apiClient.post(
      '/users/$userId/set-password',
      body: {
        'newPassword': newPassword,
      },
    );

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception('Erro ao definir senha');
    }
  }

  @override
  Future<void> deleteCurrentUser() async {
    await _apiClient.delete('/users/me');
    await _tokenStorage.clearTokens();
  }

  @override
  Future<void> deleteAccount() async {
    await deleteCurrentUser();
  }

  @override
  Future<void> forgotPassword(String email) async {
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
