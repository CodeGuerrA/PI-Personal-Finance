import '../entities/user_entity.dart';
import '../repositories/auth_repository.dart';

class RegisterUserParams {
  final String email;
  final String firstName;
  final String lastName;
  final String cpf;

  const RegisterUserParams({
    required this.email,
    required this.firstName,
    required this.lastName,
    required this.cpf,
  });
}

class RegisterUserUseCase {
  final AuthRepository repository;

  RegisterUserUseCase(this.repository);

  Future<UserEntity> call(RegisterUserParams params) {
    return repository.registerUser(
      email: params.email,
      firstName: params.firstName,
      lastName: params.lastName,
      cpf: params.cpf,
    );
  }
}
