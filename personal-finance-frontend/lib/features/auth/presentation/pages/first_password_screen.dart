import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:sgfi/core/routes/app_routes.dart';
import 'package:sgfi/core/validators/form_validators.dart';
import 'package:sgfi/core/widgets/password_input.dart';
import 'package:sgfi/core/widgets/custom_snackbar.dart';

class FirstPasswordScreen extends StatefulWidget {
  const FirstPasswordScreen({super.key});

  @override
  State<FirstPasswordScreen> createState() => _FirstPasswordScreenState();
}

class _FirstPasswordScreenState extends State<FirstPasswordScreen> {
  final _formKey = GlobalKey<FormState>();

  String _username = '';
  String _temporaryPassword = '';
  String _newPassword = '';
  String _confirmPassword = '';

  bool _isSubmitting = false;

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    if (_newPassword != _confirmPassword) {
      if (!mounted) return;
      CustomSnackbar.showError(context, message: 'As senhas não conferem.');
      return;
    }

    _formKey.currentState!.save();

    setState(() {
      _isSubmitting = true;
    });

    try {
      // Chamar endpoint /auth/first-access
      final response = await http.post(
        Uri.parse('http://localhost:8082/auth/first-access'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'username': _username,
          'temporaryPassword': _temporaryPassword,
          'newPassword': _newPassword,
        }),
      );

      if (!mounted) return;

      if (response.statusCode == 200) {
        CustomSnackbar.showSuccess(
          context,
          message: 'Senha definida com sucesso! Faça login com sua nova senha.',
        );

        // Redirecionar para tela de login após pequeno delay
        await Future.delayed(const Duration(seconds: 2));
        if (!mounted) return;
        Navigator.of(context).pushReplacementNamed(AppRoutes.login);
      } else {
        if (!mounted) return;
        CustomSnackbar.showError(
          context,
          message: 'Erro ao definir senha. Verifique os dados e tente novamente.',
        );
      }
    } catch (e) {
      if (!mounted) return;
      CustomSnackbar.showError(
        context,
        message: 'Erro ao definir senha. Tente novamente mais tarde.',
      );
    } finally {
      if (mounted) {
        setState(() => _isSubmitting = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Primeiro Acesso'),
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
                  const Icon(
                    Icons.lock_reset,
                    size: 72,
                    color: Colors.blue,
                  ),
                  const SizedBox(height: 16),
                  const Text(
                    'Defina sua senha permanente',
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 8),
                  const Text(
                    'Use sua senha temporária para definir uma senha permanente',
                    style: TextStyle(
                      fontSize: 14,
                      color: Colors.grey,
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 24),
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
                  PasswordInput(
                    labelText: 'Senha Temporária',
                    prefixIcon: Icons.lock_clock,
                    validator: (value) =>
                        FormValidators.password(value, minLength: 6),
                    onSaved: (value) => _temporaryPassword = value!,
                    helperText: 'Use a senha temporária recebida',
                  ),
                  const SizedBox(height: 16),
                  PasswordInput(
                    labelText: 'Nova Senha',
                    prefixIcon: Icons.lock_reset,
                    validator: (value) =>
                        FormValidators.password(value, minLength: 8),
                    onChanged: (value) => _newPassword = value,
                    onSaved: (value) => _newPassword = value!,
                    helperText: 'Mínimo de 8 caracteres',
                  ),
                  const SizedBox(height: 16),
                  PasswordInput(
                    labelText: 'Confirmar Nova Senha',
                    prefixIcon: Icons.lock_outline,
                    validator: (value) =>
                        FormValidators.password(value, minLength: 8),
                    onChanged: (value) => _confirmPassword = value,
                    onSaved: (value) => _confirmPassword = value!,
                    textInputAction: TextInputAction.done,
                    onFieldSubmitted: (_) => _submit(),
                  ),
                  const SizedBox(height: 24),
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton(
                      onPressed: _isSubmitting ? null : _submit,
                      child: _isSubmitting
                          ? const SizedBox(
                              width: 20,
                              height: 20,
                              child: CircularProgressIndicator(strokeWidth: 2),
                            )
                          : const Text('Definir Senha Permanente'),
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
