class FormValidators {
  /// Campo obrigatório genérico
  static String? requiredField(String? value, String fieldName) {
    if (value == null || value.trim().isEmpty) {
      return 'Informe $fieldName';
    }
    return null;
  }

  /// Validação de e-mail
  static String? email(String? value) {
    if (value == null || value.trim().isEmpty) {
      return 'Informe o e-mail';
    }

    final trimmed = value.trim();

    // Regex simples e prática para e-mail
    final emailRegex = RegExp(
      r'^[\w\.\-]+@[\w\.\-]+\.[a-zA-Z]{2,}$',
    );

    if (!emailRegex.hasMatch(trimmed)) {
      return 'E-mail inválido';
    }

    return null;
  }

  /// Validação de usuário (login)
  static String? username(String? value, {String fieldLabel = 'usuário'}) {
    if (value == null || value.trim().isEmpty) {
      return 'Informe o $fieldLabel';
    }

    final trimmed = value.trim();

    if (trimmed.length < 3) {
      return 'O $fieldLabel deve ter pelo menos 3 caracteres';
    }

    return null;
  }

  /// Validação de senha
  static String? password(String? value, {int minLength = 6}) {
    if (value == null || value.isEmpty) {
      return 'Informe a senha';
    }

    if (value.length < minLength) {
      return 'A senha deve ter pelo menos $minLength caracteres';
    }

    // Se quiser, depois dá pra adicionar regras de número/letra/etc.

    return null;
  }

  /// Mantém apenas dígitos
  static String onlyDigits(String value) {
    return value.replaceAll(RegExp(r'[^0-9]'), '');
  }

  /// Validação de CPF (formato e dígitos verificadores)
  static String? cpf(String? value) {
    final clean = onlyDigits(value ?? '');

    if (clean.isEmpty) {
      return 'Informe o CPF';
    }

    if (clean.length != 11) {
      return 'CPF deve ter 11 dígitos';
    }

    if (!_isValidCpfDigits(clean)) {
      return 'CPF inválido';
    }

    return null;
  }

  /// Implementação do algoritmo de CPF
  static bool _isValidCpfDigits(String digits) {
    // descarta CPFs com todos os dígitos iguais (000..., 111..., etc.)
    if (RegExp(r'^(\d)\1{10}$').hasMatch(digits)) {
      return false;
    }

    // primeiro dígito verificador
    int sum = 0;
    for (int i = 0; i < 9; i++) {
      sum += int.parse(digits[i]) * (10 - i);
    }
    int firstCheck = (sum * 10) % 11;
    if (firstCheck == 10) firstCheck = 0;
    if (firstCheck != int.parse(digits[9])) {
      return false;
    }

    // segundo dígito verificador
    sum = 0;
    for (int i = 0; i < 10; i++) {
      sum += int.parse(digits[i]) * (11 - i);
    }
    int secondCheck = (sum * 10) % 11;
    if (secondCheck == 10) secondCheck = 0;
    if (secondCheck != int.parse(digits[10])) {
      return false;
    }

    return true;
  }
}
