import '../entities/auth_tokens_entity.dart';
import '../repositories/auth_repository.dart';

class LoginParams {
  final String username;
  final String password;

  const LoginParams({
    required this.username,
    required this.password,
  });
}

class LoginUseCase {
  final AuthRepository repository;

  LoginUseCase(this.repository);

  Future<AuthTokensEntity> call(LoginParams params) {
    return repository.login(
      username: params.username,
      password: params.password,
    );
  }
}
