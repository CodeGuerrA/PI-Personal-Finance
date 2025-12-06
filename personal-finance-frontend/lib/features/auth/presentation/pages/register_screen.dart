import 'package:flutter/material.dart';
import 'package:sgfi/core/validators/form_validators.dart';
import 'package:sgfi/features/auth/data/datasources/auth_remote_datasource_impl.dart';
import 'package:sgfi/features/auth/data/repositories/auth_repository_impl.dart';
import 'package:sgfi/features/auth/domain/usecases/register_user_usecase.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({super.key});

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final _formKey = GlobalKey<FormState>();

  String _firstName = '';
  String _lastName = '';
  String _email = '';
  String _cpf = '';

  bool _isLoading = false;
  String? _errorMessage;
  String? _successMessage;

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    _formKey.currentState!.save();

    setState(() {
      _isLoading = true;
      _errorMessage = null;
      _successMessage = null;
    });

    try {
      final dataSource = AuthRemoteDataSourceImpl();
      final repository = AuthRepositoryImpl(dataSource);
      final usecase = RegisterUserUseCase(repository);

      final user = await usecase(
        RegisterUserParams(
          email: _email,
          firstName: _firstName,
          lastName: _lastName,
          cpf: _cpf,
        ),
      );

      if (!mounted) return;

      setState(() {
        _successMessage =
            'Usuário criado com sucesso!\nUsername: ${user.userName}\nA senha temporária é gerada no backend.';
      });
    } catch (e) {
      setState(() {
        _errorMessage =
            'Erro ao cadastrar usuário. Verifique os dados ou tente novamente.\nDetalhe: $e';
      });
    } finally {
      if (mounted) {
        setState(() => _isLoading = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Criar conta'),
      ),
      body: SafeArea(
        child: Center(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(24.0),
            child: Form(
              key: _formKey,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  if (_errorMessage != null) ...[
                    Text(
                      _errorMessage!,
                      style: const TextStyle(color: Colors.red),
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 16),
                  ],
                  if (_successMessage != null) ...[
                    Text(
                      _successMessage!,
                      style: const TextStyle(color: Colors.green),
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 16),
                  ],
                  TextFormField(
                    decoration: const InputDecoration(
                      labelText: 'Primeiro nome',
                      prefixIcon: Icon(Icons.person_outline),
                    ),
                    validator: (value) =>
                        FormValidators.requiredField(value, 'o primeiro nome'),
                    onSaved: (value) => _firstName = value!.trim(),
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    decoration: const InputDecoration(
                      labelText: 'Sobrenome',
                      prefixIcon: Icon(Icons.person_outline),
                    ),
                    validator: (value) =>
                        FormValidators.requiredField(value, 'o sobrenome'),
                    onSaved: (value) => _lastName = value!.trim(),
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    decoration: const InputDecoration(
                      labelText: 'E-mail',
                      prefixIcon: Icon(Icons.email_outlined),
                    ),
                    keyboardType: TextInputType.emailAddress,
                    validator: FormValidators.email,
                    onSaved: (value) => _email = value!.trim(),
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    decoration: const InputDecoration(
                      labelText: 'CPF',
                      prefixIcon: Icon(Icons.badge_outlined),
                    ),
                    keyboardType: TextInputType.number,
                    validator: FormValidators.cpf,
                    onSaved: (value) =>
                        _cpf = FormValidators.onlyDigits(value ?? ''),
                  ),
                  const SizedBox(height: 24),
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton(
                      onPressed: _isLoading ? null : _submit,
                      child: _isLoading
                          ? const SizedBox(
                              width: 20,
                              height: 20,
                              child: CircularProgressIndicator(strokeWidth: 2),
                            )
                          : const Text('Cadastrar'),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
