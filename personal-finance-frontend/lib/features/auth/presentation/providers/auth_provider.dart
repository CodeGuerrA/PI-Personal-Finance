import 'package:flutter/material.dart';
import 'package:sgfi/features/auth/domain/entities/user_entity.dart';
import 'package:sgfi/features/auth/domain/repositories/auth_repository.dart';
import 'package:sgfi/core/storage/token_storage.dart';

/// Provider para gerenciar estado de autenticação
class AuthProvider extends ChangeNotifier {
  final AuthRepository _repository;
  final TokenStorage _tokenStorage;

  AuthProvider(this._repository, this._tokenStorage);

  // Estado
  UserEntity? _currentUser;
  bool _isAuthenticated = false;
  bool _isLoading = false;
  String? _errorMessage;
  bool _requiresPasswordChange = false;

  // Getters
  UserEntity? get currentUser => _currentUser;
  bool get isAuthenticated => _isAuthenticated;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;
  bool get requiresPasswordChange => _requiresPasswordChange;

  /// Inicializa estado de autenticação (verificar token salvo)
  Future<void> initialize() async {
    _isLoading = true;
    notifyListeners();

    try {
      // Verificar se tem token válido salvo
      final hasToken = await _tokenStorage.hasValidToken();

      if (hasToken) {
        // Carregar dados do usuário
        await loadCurrentUser();
      }

      _isAuthenticated = hasToken;
      _isLoading = false;
      notifyListeners();
    } catch (e) {
      _isAuthenticated = false;
      _isLoading = false;
      notifyListeners();
    }
  }

  /// Faz login
  Future<bool> login(String username, String password) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final authTokens = await _repository.login(
        username: username,
        password: password,
      );

      // Salvar tokens
      await _tokenStorage.saveTokens(
        accessToken: authTokens.accessToken,
        refreshToken: authTokens.refreshToken,
        expiresIn: authTokens.expiresIn,
        refreshExpiresIn: authTokens.refreshExpiresIn,
        username: username,
        requiresPasswordChange: authTokens.requiresPasswordChange,
      );

      // Carregar dados do usuário após login
      await loadCurrentUser();
      _isAuthenticated = true;
      _requiresPasswordChange = authTokens.requiresPasswordChange;

      // Debug logs
      print('AuthProvider - Login bem-sucedido!');
      print('AuthProvider - Username: ${authTokens.username}');
      print('AuthProvider - requiresPasswordChange: ${authTokens.requiresPasswordChange}');
      print('AuthProvider - _requiresPasswordChange setado para: $_requiresPasswordChange');

      _isLoading = false;
      notifyListeners();

      return true;
    } catch (e) {
      _errorMessage = _parseErrorMessage(e.toString());
      _isAuthenticated = false;
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  /// Registra novo usuário
  Future<bool> register({
    required String userName,
    required String firstName,
    required String lastName,
    required String email,
    required String cpf,
  }) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      await _repository.registerUser(
        email: email,
        firstName: firstName,
        lastName: lastName,
        cpf: cpf,
      );

      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _errorMessage = _parseErrorMessage(e.toString());
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  /// Carrega dados do usuário atual
  Future<bool> loadCurrentUser() async {
    try {
      _currentUser = await _repository.getCurrentUser();
      notifyListeners();
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao carregar dados do usuário';
      notifyListeners();
      return false;
    }
  }

  /// Atualiza perfil do usuário
  Future<bool> updateProfile({
    String? firstName,
    String? lastName,
    String? email,
  }) async {
    if (email == null) {
      _errorMessage = 'Email é obrigatório';
      notifyListeners();
      return false;
    }

    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final updatedUser = await _repository.updateCurrentUser(
        email: email,
      );

      _currentUser = updatedUser;
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _errorMessage = _parseErrorMessage(e.toString());
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  /// Altera senha
  Future<bool> changePassword({
    required String currentPassword,
    required String newPassword,
  }) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      await _repository.changePassword(
        currentPassword: currentPassword,
        newPassword: newPassword,
      );

      _requiresPasswordChange = false;
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _errorMessage = _parseErrorMessage(e.toString());
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  /// Define senha permanente (primeiro acesso)
  Future<bool> setPassword({
    required int userId,
    required String newPassword,
  }) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      await _repository.setPassword(
        userId: userId,
        newPassword: newPassword,
      );

      _requiresPasswordChange = false;
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _errorMessage = _parseErrorMessage(e.toString());
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  /// Solicita recuperação de senha
  Future<bool> forgotPassword(String email) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      await _repository.forgotPassword(email);

      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _errorMessage = _parseErrorMessage(e.toString());
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  /// Redefine senha com código
  Future<bool> resetPassword({
    required String email,
    required String code,
    required String newPassword,
  }) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      await _repository.resetPassword(
        email: email,
        code: code,
        newPassword: newPassword,
      );

      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _errorMessage = _parseErrorMessage(e.toString());
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  /// Faz logout
  Future<void> logout() async {
    // Limpar tokens
    await _tokenStorage.clearTokens();

    // Limpar cache de dados
    // await CacheService.clearAllCache(); // Descomentar quando integrar

    // Resetar estado
    _currentUser = null;
    _isAuthenticated = false;
    _requiresPasswordChange = false;
    _errorMessage = null;

    notifyListeners();
  }

  /// Deleta conta do usuário
  Future<bool> deleteAccount() async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      await _repository.deleteAccount();

      // Fazer logout após deletar
      await logout();

      _isLoading = false;
      notifyListeners();
      return true;
    } catch (e) {
      _errorMessage = _parseErrorMessage(e.toString());
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  /// Atualiza token usando refresh token
  Future<bool> refreshToken() async {
    try {
      final refreshToken = await _tokenStorage.getRefreshToken();
      if (refreshToken == null) return false;

      final newTokens = await _repository.refreshToken(refreshToken);

      // Salvar novos tokens
      await _tokenStorage.saveTokens(
        accessToken: newTokens.accessToken,
        refreshToken: newTokens.refreshToken,
        expiresIn: newTokens.expiresIn,
        refreshExpiresIn: newTokens.refreshExpiresIn,
      );

      return true;
    } catch (e) {
      // Se falhar refresh, fazer logout
      await logout();
      return false;
    }
  }

  /// Limpa erro
  void clearError() {
    _errorMessage = null;
    notifyListeners();
  }

  /// Parse de mensagens de erro mais amigáveis
  String _parseErrorMessage(String error) {
    if (error.contains('401')) {
      return 'Usuário ou senha incorretos';
    } else if (error.contains('400')) {
      return 'Dados inválidos. Verifique os campos';
    } else if (error.contains('404')) {
      return 'Usuário não encontrado';
    } else if (error.contains('409')) {
      return 'Usuário já cadastrado';
    } else if (error.contains('network')) {
      return 'Erro de conexão. Verifique sua internet';
    } else {
      return 'Erro inesperado. Tente novamente';
    }
  }
}
