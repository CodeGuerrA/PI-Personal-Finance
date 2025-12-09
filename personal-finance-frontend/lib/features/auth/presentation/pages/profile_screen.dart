import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sgfi/core/routes/app_routes.dart';
import 'package:sgfi/features/auth/domain/entities/user_entity.dart';
import 'package:sgfi/features/auth/domain/repositories/auth_repository.dart';
import 'package:sgfi/features/auth/data/repositories/auth_repository_impl.dart';
import 'package:sgfi/features/auth/data/datasources/auth_remote_datasource_impl.dart';
import 'package:sgfi/core/providers/theme_provider.dart';
import 'package:sgfi/core/theme/module_icons.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  final AuthRepository _authRepository = AuthRepositoryImpl(
    AuthRemoteDataSourceImpl(),
  );

  UserEntity? _user;
  bool _isLoading = true;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _loadUserData();
  }

  Future<void> _loadUserData() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      final user = await _authRepository.getCurrentUser();
      setState(() {
        _user = user;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _errorMessage = 'Erro ao carregar dados do usuário: $e';
        _isLoading = false;
      });
    }
  }

  void _logout() async {
    final confirm = await showDialog<bool>(
      context: context,
      builder: (ctx) {
        return AlertDialog(
          title: const Text('Sair da conta'),
          content: const Text(
            'Tem certeza que deseja sair?',
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(ctx).pop(false),
              child: const Text('Cancelar'),
            ),
            TextButton(
              onPressed: () => Navigator.of(ctx).pop(true),
              style: TextButton.styleFrom(
                foregroundColor: Colors.red,
              ),
              child: const Text('Sair'),
            ),
          ],
        );
      },
    );

    if (confirm == true && mounted) {
      // Aqui você faria o logout real (limpar token, etc)
      // Por enquanto só volta para login
      Navigator.of(context).pushNamedAndRemoveUntil(
        AppRoutes.login,
        (route) => false,
      );
    }
  }

  void _openEditEmailDialog() async {
    if (_user == null) return;

    final controller = TextEditingController(text: _user!.email);

    final newEmail = await showDialog<String>(
      context: context,
      builder: (ctx) {
        return AlertDialog(
          title: const Text('Editar email'),
          content: TextField(
            controller: controller,
            decoration: const InputDecoration(
              labelText: 'Email',
            ),
            keyboardType: TextInputType.emailAddress,
            autofocus: true,
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(ctx).pop(null),
              child: const Text('Cancelar'),
            ),
            TextButton(
              onPressed: () {
                final email = controller.text.trim();
                if (email.isNotEmpty && email.contains('@')) {
                  Navigator.of(ctx).pop(email);
                }
              },
              child: const Text('Salvar'),
            ),
          ],
        );
      },
    );

    if (newEmail != null && newEmail != _user!.email) {
      try {
        final updatedUser = await _authRepository.updateCurrentUser(
          email: newEmail,
        );
        setState(() => _user = updatedUser);

        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Email atualizado com sucesso'),
              backgroundColor: Colors.green,
            ),
          );
        }
      } catch (e) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Erro ao atualizar email: $e'),
              backgroundColor: Colors.red,
            ),
          );
        }
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Perfil'),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            tooltip: 'Sair',
            onPressed: _logout,
          ),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _errorMessage != null
              ? Center(
                  child: Padding(
                    padding: const EdgeInsets.all(24),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        const Icon(
                          Icons.error_outline,
                          size: 64,
                          color: Colors.red,
                        ),
                        const SizedBox(height: 16),
                        Text(
                          _errorMessage!,
                          textAlign: TextAlign.center,
                          style: const TextStyle(color: Colors.red),
                        ),
                        const SizedBox(height: 16),
                        ElevatedButton.icon(
                          onPressed: _loadUserData,
                          icon: const Icon(Icons.refresh),
                          label: const Text('Tentar novamente'),
                        ),
                      ],
                    ),
                  ),
                )
              : SingleChildScrollView(
                  child: Column(
                    children: [
                      // HEADER COM AVATAR E INFORMAÇÕES
                      Container(
                        width: double.infinity,
                        padding: const EdgeInsets.all(24),
                        decoration: BoxDecoration(
                          color: theme.colorScheme.primary.withValues(alpha: 0.1),
                        ),
                        child: Column(
                          children: [
                            CircleAvatar(
                              radius: 50,
                              backgroundColor: theme.colorScheme.primary,
                              child: Text(
                                _user?.firstName.isNotEmpty == true
                                    ? _user!.firstName[0].toUpperCase()
                                    : '?',
                                style: TextStyle(
                                  fontSize: 40,
                                  color: theme.colorScheme.onPrimary,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                            const SizedBox(height: 16),
                            Text(
                              _user?.fullName ?? 'Usuário',
                              style: theme.textTheme.headlineSmall?.copyWith(
                                fontWeight: FontWeight.bold,
                              ),
                              textAlign: TextAlign.center,
                            ),
                            const SizedBox(height: 4),
                            Text(
                              _user?.email ?? '',
                              style: theme.textTheme.bodyMedium?.copyWith(
                                color: theme.colorScheme.onSurfaceVariant,
                              ),
                              textAlign: TextAlign.center,
                            ),
                            const SizedBox(height: 8),
                            Text(
                              '@${_user?.userName ?? ''}',
                              style: theme.textTheme.bodySmall?.copyWith(
                                color: theme.colorScheme.onSurfaceVariant,
                                fontStyle: FontStyle.italic,
                              ),
                              textAlign: TextAlign.center,
                            ),
                          ],
                        ),
                      ),

                      const SizedBox(height: 8),

                      // OPÇÕES DO PERFIL
                      _ProfileOption(
                        icon: Icons.person_outline,
                        title: 'Nome completo',
                        subtitle: _user?.fullName ?? '',
                        trailing: Builder(
                          builder: (context) => Text(
                            'Não editável',
                            style: TextStyle(
                              fontSize: 12,
                              color: Theme.of(context).colorScheme.onSurfaceVariant,
                            ),
                          ),
                        ),
                        onTap: null,
                      ),

                      _ProfileOption(
                        icon: Icons.alternate_email,
                        title: 'Nome de usuário',
                        subtitle: '@${_user?.userName ?? ''}',
                        trailing: Builder(
                          builder: (context) => Text(
                            'Não editável',
                            style: TextStyle(
                              fontSize: 12,
                              color: Theme.of(context).colorScheme.onSurfaceVariant,
                            ),
                          ),
                        ),
                        onTap: null,
                      ),

                      _ProfileOption(
                        icon: Icons.email_outlined,
                        title: 'Email',
                        subtitle: _user?.email ?? '',
                        onTap: _openEditEmailDialog,
                      ),

                      const Divider(),

                      // THEME SWITCHER
                      Consumer<ThemeProvider>(
                        builder: (context, themeProvider, _) {
                          return _ThemeOption(
                            currentMode: themeProvider.themeMode,
                            onModeChanged: (mode) {
                              themeProvider.setThemeMode(mode);
                            },
                          );
                        },
                      ),

                      const Divider(),

                      _ProfileOption(
                        icon: Icons.lock_outline,
                        title: 'Alterar senha',
                        subtitle: 'Clique para alterar sua senha',
                        onTap: () {
                          Navigator.of(context).pushNamed(AppRoutes.changePassword);
                        },
                      ),

                      const Divider(),

                      _ProfileOption(
                        icon: Icons.info_outline,
                        title: 'Sobre o app',
                        subtitle: 'Versão 1.0.0',
                        onTap: () {
                          showAboutDialog(
                            context: context,
                            applicationName: 'SGFI',
                            applicationVersion: '1.0.0',
                            applicationLegalese: '© 2025 Sistema de Gestão Financeira',
                          );
                        },
                      ),

                      const SizedBox(height: 16),

                      // BOTÃO DE SAIR (DESTAQUE)
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 16),
                        child: SizedBox(
                          width: double.infinity,
                          height: 50,
                          child: OutlinedButton.icon(
                            onPressed: _logout,
                            icon: const Icon(Icons.logout, color: Colors.red),
                            label: const Text(
                              'Sair da conta',
                              style: TextStyle(color: Colors.red),
                            ),
                            style: OutlinedButton.styleFrom(
                              side: const BorderSide(color: Colors.red),
                            ),
                          ),
                        ),
                      ),

                      const SizedBox(height: 24),
                    ],
                  ),
                ),
    );
  }
}

class _ProfileOption extends StatelessWidget {
  final IconData icon;
  final String title;
  final String subtitle;
  final VoidCallback? onTap;
  final Widget? trailing;

  const _ProfileOption({
    required this.icon,
    required this.title,
    required this.subtitle,
    this.onTap,
    this.trailing,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(icon, size: 28),
      title: Text(
        title,
        style: const TextStyle(
          fontWeight: FontWeight.w500,
        ),
      ),
      subtitle: Text(
        subtitle,
        style: const TextStyle(fontSize: 13),
      ),
      trailing: trailing ??
          (onTap != null
              ? const Icon(Icons.chevron_right)
              : null),
      onTap: onTap,
      enabled: onTap != null,
    );
  }
}

/// Premium theme switcher widget
class _ThemeOption extends StatelessWidget {
  final AppThemeMode currentMode;
  final ValueChanged<AppThemeMode> onModeChanged;

  const _ThemeOption({
    required this.currentMode,
    required this.onModeChanged,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final isDark = theme.brightness == Brightness.dark;

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(
                Icons.palette_outlined,
                size: 28,
                color: ModuleIcons.profileColor,
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      'Tema do aplicativo',
                      style: TextStyle(
                        fontWeight: FontWeight.w500,
                        fontSize: 16,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      'Escolha entre claro, escuro ou automático',
                      style: TextStyle(
                        fontSize: 13,
                        color: theme.textTheme.bodySmall?.color,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
          const SizedBox(height: 12),
          // Theme mode selector with chips
          Row(
            children: [
              const SizedBox(width: 44), // Align with text
              Expanded(
                child: Wrap(
                  spacing: 8,
                  runSpacing: 8,
                  children: [
                    _ThemeChip(
                      label: 'Claro',
                      icon: Icons.light_mode,
                      isSelected: currentMode == AppThemeMode.light,
                      onSelected: () => onModeChanged(AppThemeMode.light),
                      isDarkMode: isDark,
                    ),
                    _ThemeChip(
                      label: 'Escuro',
                      icon: Icons.dark_mode,
                      isSelected: currentMode == AppThemeMode.dark,
                      onSelected: () => onModeChanged(AppThemeMode.dark),
                      isDarkMode: isDark,
                    ),
                    _ThemeChip(
                      label: 'Sistema',
                      icon: Icons.brightness_auto,
                      isSelected: currentMode == AppThemeMode.system,
                      onSelected: () => onModeChanged(AppThemeMode.system),
                      isDarkMode: isDark,
                    ),
                  ],
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

/// Individual theme selection chip
class _ThemeChip extends StatelessWidget {
  final String label;
  final IconData icon;
  final bool isSelected;
  final VoidCallback onSelected;
  final bool isDarkMode;

  const _ThemeChip({
    required this.label,
    required this.icon,
    required this.isSelected,
    required this.onSelected,
    required this.isDarkMode,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Material(
      color: isSelected
          ? ModuleIcons.profileColor.withValues(alpha: 0.2)
          : (isDarkMode
              ? Colors.white.withValues(alpha: 0.05)
              : Colors.black.withValues(alpha: 0.05)),
      borderRadius: BorderRadius.circular(12),
      child: InkWell(
        onTap: onSelected,
        borderRadius: BorderRadius.circular(12),
        child: Container(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(12),
            border: Border.all(
              color: isSelected
                  ? ModuleIcons.profileColor
                  : (isDarkMode
                      ? Colors.white.withValues(alpha: 0.1)
                      : Colors.black.withValues(alpha: 0.1)),
              width: isSelected ? 2 : 1,
            ),
          ),
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Icon(
                icon,
                size: 18,
                color: isSelected
                    ? ModuleIcons.profileColor
                    : theme.textTheme.bodyMedium?.color,
              ),
              const SizedBox(width: 6),
              Text(
                label,
                style: TextStyle(
                  fontSize: 13,
                  fontWeight: isSelected ? FontWeight.w600 : FontWeight.normal,
                  color: isSelected
                      ? ModuleIcons.profileColor
                      : theme.textTheme.bodyMedium?.color,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
