import 'package:flutter/material.dart';
import 'package:sgfi/core/routes/app_routes.dart';
import 'package:sgfi/core/validators/form_validators.dart';

// Imports para o modo backend (no futuro)
import 'package:sgfi/features/auth/data/datasources/auth_remote_datasource_impl.dart';
import 'package:sgfi/features/auth/data/repositories/auth_repository_impl.dart';
import 'package:sgfi/features/auth/domain/usecases/login_usecase.dart';

/// ATENÇÃO BACKEND:
/// - Em desenvolvimento: pode mudar para true para usar a API real.
/// - Em demo local: deixe false para usar admin / 123456.
const bool kUseBackendAuth = true;

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();

  String _username = '';
  String _password = '';
  bool _isLoading = false;
  String? _errorMessage;

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    _formKey.currentState!.save();
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      if (!kUseBackendAuth) {
        // ============================
        // MODO DEMO (SEM BACKEND)
        // ============================
        if (_username == 'admin' && _password == '123456') {
          if (!mounted) return;
          Navigator.of(context).pushReplacementNamed(AppRoutes.home);
        } else {
          setState(() {
            _errorMessage =
                'Usuário ou senha inválidos.\nUse admin / 123456 para acessar o modo demonstração.';
          });
        }
      } else {
        // ============================
        // MODO BACKEND (COM API)
        // ============================
        final dataSource = AuthRemoteDataSourceImpl();
        final repository = AuthRepositoryImpl(dataSource);
        final loginUseCase = LoginUseCase(repository);

        final tokens = await loginUseCase(
          LoginParams(
            username: _username,
            password: _password,
          ),
        );

        if (!mounted) return;

        if (tokens.requiresPasswordChange) {
          Navigator.of(context).pushReplacementNamed(
            AppRoutes.firstPassword,
            arguments: tokens.username,
          );
        } else {
          Navigator.of(context).pushReplacementNamed(AppRoutes.home);
        }
      }
    } catch (e) {
      setState(() {
        _errorMessage =
            'Erro ao fazer login. Tente novamente mais tarde.\nDetalhe: $e';
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
      body: SafeArea(
        child: Center(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(24.0),
            child: Form(
              key: _formKey,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  const Text(
                    'Personal Finance',
                    style: TextStyle(
                      fontSize: 28,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 4),
                  const Text(
                    'Modo demonstração: use admin / 123456',
                    style: TextStyle(
                      fontSize: 12,
                      color: Colors.grey,
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 24),

                  if (_errorMessage != null) ...[
                    Text(
                      _errorMessage!,
                      style: const TextStyle(color: Colors.red),
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 16),
                  ],

                  TextFormField(
                    decoration: const InputDecoration(
                      labelText: 'Usuário',
                      prefixIcon: Icon(Icons.person_outline),
                    ),
                    validator: (value) =>
                        FormValidators.username(value, fieldLabel: 'o usuário'),
                    onSaved: (value) => _username = value!.trim(),
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    decoration: const InputDecoration(
                      labelText: 'Senha',
                      prefixIcon: Icon(Icons.lock_outline),
                    ),
                    obscureText: true,
                    validator: (value) =>
                        FormValidators.password(value, minLength: 6),
                    onSaved: (value) => _password = value!,
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
                          : const Text('Entrar'),
                    ),
                  ),
                  const SizedBox(height: 8),
                  Align(
                    alignment: Alignment.centerRight,
                    child: TextButton(
                      onPressed: _isLoading
                          ? null
                          : () {
                              Navigator.of(context)
                                  .pushNamed(AppRoutes.forgotPassword);
                            },
                      child: const Text('Esqueci minha senha'),
                    ),
                  ),
                  const SizedBox(height: 12),
                  TextButton(
                    onPressed: () {
                      Navigator.of(context).pushNamed(AppRoutes.register);
                    },
                    child: const Text('Criar conta'),
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
