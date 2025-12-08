import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sgfi/core/routes/app_routes.dart';
import 'package:sgfi/core/validators/form_validators.dart';
import 'package:sgfi/features/auth/presentation/providers/auth_provider.dart';

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
      final authProvider = context.read<AuthProvider>();

      final success = await authProvider.login(_username, _password);

      if (!mounted) return;

      if (success) {
        // Debug logs
        print('LoginScreen - Login bem-sucedido!');
        print('LoginScreen - Verificando requiresPasswordChange...');
        print('LoginScreen - authProvider.requiresPasswordChange = ${authProvider.requiresPasswordChange}');

        // Verificar se precisa trocar senha
        if (authProvider.requiresPasswordChange) {
          print('LoginScreen - Redirecionando para firstPassword');
          Navigator.of(context).pushReplacementNamed(
            AppRoutes.firstPassword,
            arguments: _username,
          );
        } else {
          print('LoginScreen - Redirecionando para home');
          Navigator.of(context).pushReplacementNamed(AppRoutes.home);
        }
      } else {
        setState(() {
          _errorMessage = authProvider.errorMessage ??
              'Erro ao fazer login. Verifique suas credenciais.';
        });
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
                      prefixIcon: const Icon(Icons.person_outline),
                    ),
                    validator: (value) =>
                        FormValidators.username(value, fieldLabel: 'o usuário'),
                    onSaved: (value) => _username = value!.trim(),
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    decoration: const InputDecoration(
                      labelText: 'Senha',
                      prefixIcon: const Icon(Icons.lock_outline),
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
                      Navigator.of(context).pushNamed(AppRoutes.firstPassword);
                    },
                    child: const Text('Primeiro acesso (senha temporária)'),
                  ),
                  const SizedBox(height: 4),
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
