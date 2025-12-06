class UserEntity {
  final String id;
  final String email;
  final String firstName;
  final String lastName;
  final String? cpf;
  final String userName;

  const UserEntity({
    required this.id,
    required this.email,
    required this.firstName,
    required this.lastName,
    this.cpf,
    required this.userName,
  });

  String get fullName => '$firstName $lastName';
}
