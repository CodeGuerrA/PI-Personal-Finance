import 'package:flutter/material.dart';
import 'package:sgfi/core/routes/app_routes.dart';
import 'package:sgfi/core/validators/form_validators.dart';

class DefinePasswordScreen extends StatefulWidget {
  const DefinePasswordScreen({super.key});

  @override
  State<DefinePasswordScreen> createState() => _DefinePasswordScreenState();
}

class _DefinePasswordScreenState extends State<DefinePasswordScreen> {
  final _formKey = GlobalKey<FormState>();

  String _newPassword = '';
  String _confirmPassword = '';
  bool _isLoading = false;
  String? _errorMessage;

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    _formKey.currentState!.save();

    if (_newPassword != _confirmPassword) {
      setState(() {
        _errorMessage = 'As senhas não conferem.';
      });
      return;
    }

    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      // 🧪 MODO DEMO (sem backend)
      // Aqui no futuro você vai chamar:
      // PATCH /users/{id}/password com http + token etc.

      await Future.delayed(const Duration(milliseconds: 800));

      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Senha definida com sucesso! Faça login novamente.'),
        ),
      );

      // Volta para a tela de login
      Navigator.of(context).pushNamedAndRemoveUntil(
        AppRoutes.login,
        (route) => false,
      );
    } catch (e) {
      setState(() {
        _errorMessage = 'Erro ao definir senha. Tente novamente.\nDetalhe: $e';
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
        title: const Text('Definir senha permanente'),
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
                  const Text(
                    'Por segurança, defina uma nova senha permanente para sua conta.',
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
                      labelText: 'Nova senha',
                      prefixIcon: Icon(Icons.lock_reset),
                    ),
                    obscureText: true,
                    validator: (value) =>
                        FormValidators.password(value, minLength: 8),
                    onSaved: (value) => _newPassword = value ?? '',
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    decoration: const InputDecoration(
                      labelText: 'Confirmar nova senha',
                      prefixIcon: Icon(Icons.lock_outline),
                    ),
                    obscureText: true,
                    validator: (value) =>
                        FormValidators.password(value, minLength: 8),
                    onSaved: (value) => _confirmPassword = value ?? '',
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
                          : const Text('Salvar nova senha'),
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
