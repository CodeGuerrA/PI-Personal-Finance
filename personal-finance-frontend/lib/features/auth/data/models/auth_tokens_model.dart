import '../../domain/entities/auth_tokens_entity.dart';

class AuthTokensModel extends AuthTokensEntity {
  const AuthTokensModel({
    required super.accessToken,
    required super.refreshToken,
    required super.expiresIn,
    required super.refreshExpiresIn,
    required super.requiresPasswordChange,
    required super.username,
  });

  factory AuthTokensModel.fromJson(Map<String, dynamic> json) {
    // Debug: imprimir JSON recebido
    print('AuthTokensModel - JSON recebido: $json');

    // Parse robusto do requiresPasswordChange
    bool requiresPasswordChange = false;

    // Tenta vários nomes de campo que o backend pode usar
    if (json.containsKey('requiresPasswordChange')) {
      requiresPasswordChange = json['requiresPasswordChange'] == true ||
          json['requiresPasswordChange'] == 'true' ||
          json['requiresPasswordChange'] == 1;
    } else if (json.containsKey('requires_password_change')) {
      requiresPasswordChange = json['requires_password_change'] == true ||
          json['requires_password_change'] == 'true' ||
          json['requires_password_change'] == 1;
    } else if (json.containsKey('temporaryPassword')) {
      requiresPasswordChange = json['temporaryPassword'] == true ||
          json['temporaryPassword'] == 'true' ||
          json['temporaryPassword'] == 1;
    } else if (json.containsKey('temporary_password')) {
      requiresPasswordChange = json['temporary_password'] == true ||
          json['temporary_password'] == 'true' ||
          json['temporary_password'] == 1;
    }

    print('AuthTokensModel - requiresPasswordChange: $requiresPasswordChange');

    return AuthTokensModel(
      accessToken: json['accessToken'] as String,
      refreshToken: json['refreshToken'] as String,
      expiresIn: json['expiresIn'] as int,
      refreshExpiresIn: json['refreshExpiresIn'] as int,
      requiresPasswordChange: requiresPasswordChange,
      username: json['username'] as String,
    );
  }
}
