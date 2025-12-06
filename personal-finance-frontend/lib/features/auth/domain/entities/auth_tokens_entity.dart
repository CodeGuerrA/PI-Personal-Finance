class AuthTokensEntity {
  final String accessToken;
  final String refreshToken;
  final int expiresIn;
  final int refreshExpiresIn;
  final bool requiresPasswordChange;
  final String username;

  const AuthTokensEntity({
    required this.accessToken,
    required this.refreshToken,
    required this.expiresIn,
    required this.refreshExpiresIn,
    required this.requiresPasswordChange,
    required this.username,
  });
}
