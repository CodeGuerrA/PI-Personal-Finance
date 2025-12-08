import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:sgfi/core/routes/app_routes.dart';
import 'package:sgfi/core/validators/form_validators.dart';

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
  String? _errorMessage;

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    if (_newPassword != _confirmPassword) {
      setState(() {
        _errorMessage = 'As senhas não conferem.';
      });
      return;
    }

    _formKey.currentState!.save();

    setState(() {
      _isSubmitting = true;
      _errorMessage = null;
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
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Senha definida com sucesso! Faça login com sua nova senha.'),
            backgroundColor: Colors.green,
          ),
        );

        // Redirecionar para tela de login
        Navigator.of(context).pushReplacementNamed(AppRoutes.login);
      } else {
        setState(() {
          _errorMessage = 'Erro: ${response.body}';
        });
      }
    } catch (e) {
      setState(() {
        _errorMessage = 'Erro ao definir senha: $e';
      });
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
                      labelText: 'Senha Temporária',
                      prefixIcon: Icon(Icons.lock_clock),
                    ),
                    obscureText: true,
                    validator: (value) =>
                        FormValidators.password(value, minLength: 6),
                    onSaved: (value) => _temporaryPassword = value!,
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    decoration: const InputDecoration(
                      labelText: 'Nova Senha',
                      prefixIcon: Icon(Icons.lock_reset),
                    ),
                    obscureText: true,
                    validator: (value) =>
                        FormValidators.password(value, minLength: 8),
                    onChanged: (value) => _newPassword = value,
                    onSaved: (value) => _newPassword = value!,
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    decoration: const InputDecoration(
                      labelText: 'Confirmar Nova Senha',
                      prefixIcon: Icon(Icons.lock_outline),
                    ),
                    obscureText: true,
                    validator: (value) =>
                        FormValidators.password(value, minLength: 8),
                    onChanged: (value) => _confirmPassword = value,
                    onSaved: (value) => _confirmPassword = value!,
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
