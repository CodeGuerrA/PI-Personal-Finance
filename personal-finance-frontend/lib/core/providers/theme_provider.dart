import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

/// Theme mode options for the application
enum AppThemeMode {
  /// Always use light theme
  light,

  /// Always use dark theme
  dark,

  /// Follow system theme setting
  system,
}

/// Provider for managing app theme state
///
/// Handles theme persistence using SharedPreferences and notifies
/// listeners when the theme changes.
class ThemeProvider extends ChangeNotifier {
  /// Current theme mode setting
  AppThemeMode _themeMode = AppThemeMode.system;

  /// SharedPreferences key for storing theme preference
  static const String _themePreferenceKey = 'app_theme_mode';

  /// Get the current theme mode
  AppThemeMode get themeMode => _themeMode;

  /// Convert AppThemeMode to Flutter's ThemeMode
  ThemeMode get effectiveThemeMode {
    switch (_themeMode) {
      case AppThemeMode.light:
        return ThemeMode.light;
      case AppThemeMode.dark:
        return ThemeMode.dark;
      case AppThemeMode.system:
        return ThemeMode.system;
    }
  }

  /// Initialize the theme provider by loading saved preference
  ///
  /// Call this method once during app initialization
  Future<void> initialize() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final savedMode = prefs.getString(_themePreferenceKey);

      if (savedMode != null) {
        // Try to find matching enum value
        _themeMode = AppThemeMode.values.firstWhere(
          (mode) => mode.name == savedMode,
          orElse: () => AppThemeMode.system,
        );
        notifyListeners();
      }
    } catch (e) {
      // If loading fails, just use the default (system)
      debugPrint('Error loading theme preference: $e');
    }
  }

  /// Set the theme mode and persist the choice
  ///
  /// [mode] The new theme mode to apply
  Future<void> setThemeMode(AppThemeMode mode) async {
    if (_themeMode == mode) return; // No change needed

    _themeMode = mode;
    notifyListeners();

    // Persist the choice
    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString(_themePreferenceKey, mode.name);
    } catch (e) {
      debugPrint('Error saving theme preference: $e');
    }
  }

  /// Toggle between light and dark mode
  ///
  /// If currently in system mode, switches to light.
  /// If in light mode, switches to dark.
  /// If in dark mode, switches to light.
  Future<void> toggleTheme() async {
    final newMode = switch (_themeMode) {
      AppThemeMode.system => AppThemeMode.light,
      AppThemeMode.light => AppThemeMode.dark,
      AppThemeMode.dark => AppThemeMode.light,
    };

    await setThemeMode(newMode);
  }

  /// Check if currently in dark mode
  ///
  /// Returns true if explicitly dark OR system mode with dark preference
  bool isDarkMode(BuildContext context) {
    switch (_themeMode) {
      case AppThemeMode.dark:
        return true;
      case AppThemeMode.light:
        return false;
      case AppThemeMode.system:
        return MediaQuery.of(context).platformBrightness == Brightness.dark;
    }
  }

  /// Check if currently in light mode
  ///
  /// Returns true if explicitly light OR system mode with light preference
  bool isLightMode(BuildContext context) {
    return !isDarkMode(context);
  }

  /// Get a user-friendly name for the current theme mode
  String get themeModeName {
    switch (_themeMode) {
      case AppThemeMode.light:
        return 'Claro';
      case AppThemeMode.dark:
        return 'Escuro';
      case AppThemeMode.system:
        return 'Sistema';
    }
  }

  /// Get an icon representing the current theme mode
  IconData get themeModeIcon {
    switch (_themeMode) {
      case AppThemeMode.light:
        return Icons.light_mode;
      case AppThemeMode.dark:
        return Icons.dark_mode;
      case AppThemeMode.system:
        return Icons.brightness_auto;
    }
  }
}
