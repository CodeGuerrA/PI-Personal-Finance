import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sgfi/features/auth/presentation/providers/auth_provider.dart';
import 'package:sgfi/core/widgets/password_input.dart';
import 'package:sgfi/core/widgets/custom_snackbar.dart';

class ChangePasswordScreen extends StatefulWidget {
  const ChangePasswordScreen({super.key});

  @override
  State<ChangePasswordScreen> createState() => _ChangePasswordScreenState();
}

class _ChangePasswordScreenState extends State<ChangePasswordScreen> {
  final _formKey = GlobalKey<FormState>();

  String _currentPassword = '';
  String _newPassword = '';
  String _confirmPassword = '';

  bool _isSubmitting = false;

  String? _validatePassword(String? value) {
    if (value == null || value.isEmpty) {
      return 'Informe a senha';
    }
    if (value.length < 6) {
      return 'A senha deve ter pelo menos 6 caracteres';
    }
    return null;
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    if (_newPassword != _confirmPassword) {
      CustomSnackbar.showError(
        context,
        message: 'A confirmação da nova senha não confere.',
      );
      return;
    }

    _formKey.currentState!.save();

    setState(() => _isSubmitting = true);

    try {
      final authProvider = context.read<AuthProvider>();

      final success = await authProvider.changePassword(
        currentPassword: _currentPassword,
        newPassword: _newPassword,
      );

      if (!mounted) return;

      if (success) {
        CustomSnackbar.showSuccess(
          context,
          message: 'Senha alterada com sucesso!',
        );

        await Future.delayed(const Duration(seconds: 1));
        if (!mounted) return;
        Navigator.of(context).pop();
      } else {
        CustomSnackbar.showError(
          context,
          message: authProvider.errorMessage ??
              'Erro ao alterar senha. Verifique a senha atual.',
        );
      }
    } catch (e) {
      if (!mounted) return;
      CustomSnackbar.showError(
        context,
        message: 'Erro ao alterar senha. Tente novamente.',
      );
    } finally {
      if (mounted) {
        setState(() => _isSubmitting = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Alterar senha'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Center(
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 420),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Icon(
                  Icons.lock_reset,
                  size: 64,
                  color: theme.colorScheme.primary,
                ),
                const SizedBox(height: 16),
                Text(
                  'Atualize sua senha',
                  style: theme.textTheme.titleLarge,
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 8),
                Text(
                  'Por segurança, informe sua senha atual e defina uma nova senha.',
                  style: theme.textTheme.bodyMedium,
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 24),
                Form(
                  key: _formKey,
                  child: Column(
                    children: [
                      PasswordInput(
                        labelText: 'Senha atual',
                        prefixIcon: Icons.lock_outline,
                        validator: _validatePassword,
                        onSaved: (value) =>
                            _currentPassword = (value ?? '').trim(),
                        helperText: 'Digite sua senha atual',
                      ),
                      const SizedBox(height: 12),
                      PasswordInput(
                        labelText: 'Nova senha',
                        prefixIcon: Icons.lock_reset,
                        validator: _validatePassword,
                        onChanged: (value) => _newPassword = value,
                        onSaved: (value) =>
                            _newPassword = (value ?? '').trim(),
                        helperText: 'Mínimo de 6 caracteres',
                      ),
                      const SizedBox(height: 12),
                      PasswordInput(
                        labelText: 'Confirmar nova senha',
                        prefixIcon: Icons.lock_outline,
                        validator: _validatePassword,
                        onChanged: (value) => _confirmPassword = value,
                        onSaved: (value) =>
                            _confirmPassword = (value ?? '').trim(),
                        textInputAction: TextInputAction.done,
                        onFieldSubmitted: (_) => _submit(),
                      ),
                      const SizedBox(height: 24),
                      SizedBox(
                        width: double.infinity,
                        height: 48,
                        child: ElevatedButton(
                          onPressed: _isSubmitting ? null : _submit,
                          child: _isSubmitting
                              ? const CircularProgressIndicator()
                              : const Text('Salvar nova senha'),
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
