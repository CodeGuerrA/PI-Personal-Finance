import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class TokenStorage {
  static const String _accessTokenKey = 'access_token';
  static const String _refreshTokenKey = 'refresh_token';
  static const String _expiresInKey = 'expires_in';
  static const String _refreshExpiresInKey = 'refresh_expires_in';
  static const String _usernameKey = 'username';
  static const String _requiresPasswordChangeKey = 'requires_password_change';

  final FlutterSecureStorage _storage;

  TokenStorage({FlutterSecureStorage? storage})
      : _storage = storage ?? const FlutterSecureStorage();

  Future<void> saveTokens({
    required String accessToken,
    required String refreshToken,
    required int expiresIn,
    required int refreshExpiresIn,
    String? username,
    bool? requiresPasswordChange,
  }) async {
    await _storage.write(key: _accessTokenKey, value: accessToken);
    await _storage.write(key: _refreshTokenKey, value: refreshToken);
    await _storage.write(key: _expiresInKey, value: expiresIn.toString());
    await _storage.write(key: _refreshExpiresInKey, value: refreshExpiresIn.toString());

    if (username != null) {
      await _storage.write(key: _usernameKey, value: username);
    }

    if (requiresPasswordChange != null) {
      await _storage.write(key: _requiresPasswordChangeKey, value: requiresPasswordChange.toString());
    }
  }

  Future<String?> getAccessToken() async {
    return await _storage.read(key: _accessTokenKey);
  }

  Future<String?> getRefreshToken() async {
    return await _storage.read(key: _refreshTokenKey);
  }

  Future<int?> getExpiresIn() async {
    final value = await _storage.read(key: _expiresInKey);
    return value != null ? int.tryParse(value) : null;
  }

  Future<int?> getRefreshExpiresIn() async {
    final value = await _storage.read(key: _refreshExpiresInKey);
    return value != null ? int.tryParse(value) : null;
  }

  Future<String?> getUsername() async {
    return await _storage.read(key: _usernameKey);
  }

  Future<bool?> getRequiresPasswordChange() async {
    final value = await _storage.read(key: _requiresPasswordChangeKey);
    return value != null ? value.toLowerCase() == 'true' : null;
  }

  Future<void> clearTokens() async {
    await _storage.delete(key: _accessTokenKey);
    await _storage.delete(key: _refreshTokenKey);
    await _storage.delete(key: _expiresInKey);
    await _storage.delete(key: _refreshExpiresInKey);
    await _storage.delete(key: _usernameKey);
    await _storage.delete(key: _requiresPasswordChangeKey);
  }

  Future<bool> hasValidToken() async {
    final token = await getAccessToken();
    return token != null && token.isNotEmpty;
  }
}
