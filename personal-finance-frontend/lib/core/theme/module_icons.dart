import 'package:flutter/material.dart';

/// Module-specific iconography and color system
///
/// Each module in FinTrack has a distinct color identity and icon.
/// This ensures visual differentiation and helps users quickly identify
/// different sections of the app. Colors are chosen for high visibility
/// and sufficient contrast in both light and dark modes.
///
/// Key Principle: NO white-only icons that disappear into backgrounds.
class ModuleIcons {
  ModuleIcons._(); // Private constructor to prevent instantiation

  // ============================================================================
  // HOME / DASHBOARD MODULE
  // ============================================================================

  /// Home/Dashboard module color - Indigo Blue
  static const Color homeColor = Color(0xFF5B7CE3);

  /// Home/Dashboard icon
  static const IconData homeIcon = Icons.home_outlined;

  /// Home gradient (for premium cards)
  static const LinearGradient homeGradient = LinearGradient(
    colors: [Color(0xFF5B7CE3), Color(0xFF7B9BF0)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  // ============================================================================
  // TRANSACTIONS MODULE
  // ============================================================================

  /// Transactions module color - Vibrant Green
  static const Color transactionsColor = Color(0xFF22C893);

  /// Transactions icon - swap/exchange symbol
  static const IconData transactionsIcon = Icons.swap_vert_rounded;

  /// Alternative transactions icon
  static const IconData transactionsIconAlt = Icons.receipt_long_outlined;

  /// Transactions gradient
  static const LinearGradient transactionsGradient = LinearGradient(
    colors: [Color(0xFF22C893), Color(0xFF26E07F)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  // ============================================================================
  // INVESTMENTS MODULE
  // ============================================================================

  /// Investments module color - Royal Purple
  static const Color investmentsColor = Color(0xFFB794F6);

  /// Investments icon - trending up
  static const IconData investmentsIcon = Icons.trending_up_rounded;

  /// Alternative investments icon
  static const IconData investmentsIconAlt = Icons.show_chart_rounded;

  /// Investments gradient
  static const LinearGradient investmentsGradient = LinearGradient(
    colors: [Color(0xFFB794F6), Color(0xFFD4B4FE)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  // ============================================================================
  // GOALS MODULE
  // ============================================================================

  /// Goals module color - Golden Yellow
  static const Color goalsColor = Color(0xFFFFAB40);

  /// Goals icon - flag/target
  static const IconData goalsIcon = Icons.flag_outlined;

  /// Alternative goals icon
  static const IconData goalsIconAlt = Icons.military_tech_outlined;

  /// Goals gradient
  static const LinearGradient goalsGradient = LinearGradient(
    colors: [Color(0xFFFFAB40), Color(0xFFFFCC80)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  // ============================================================================
  // RECURRENCES MODULE
  // ============================================================================

  /// Recurrences module color - Teal
  static const Color recurrencesColor = Color(0xFF00C4B4);

  /// Recurrences icon - repeat
  static const IconData recurrencesIcon = Icons.repeat_rounded;

  /// Alternative recurrences icon
  static const IconData recurrencesIconAlt = Icons.schedule_rounded;

  /// Recurrences gradient
  static const LinearGradient recurrencesGradient = LinearGradient(
    colors: [Color(0xFF00C4B4), Color(0xFF26E0D0)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  // ============================================================================
  // CATEGORIES MODULE
  // ============================================================================

  /// Categories module color - Coral
  static const Color categoriesColor = Color(0xFFFC8181);

  /// Categories icon
  static const IconData categoriesIcon = Icons.category_outlined;

  /// Alternative categories icon
  static const IconData categoriesIconAlt = Icons.label_outlined;

  /// Categories gradient
  static const LinearGradient categoriesGradient = LinearGradient(
    colors: [Color(0xFFFC8181), Color(0xFFFEB2B2)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  // ============================================================================
  // REPORTS MODULE
  // ============================================================================

  /// Reports module color - Sky Blue
  static const Color reportsColor = Color(0xFF42A5F5);

  /// Reports icon - charts/analytics
  static const IconData reportsIcon = Icons.bar_chart_rounded;

  /// Alternative reports icon
  static const IconData reportsIconAlt = Icons.analytics_outlined;

  /// Reports gradient
  static const LinearGradient reportsGradient = LinearGradient(
    colors: [Color(0xFF42A5F5), Color(0xFF64B5F6)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  // ============================================================================
  // PROFILE / SETTINGS MODULE
  // ============================================================================

  /// Profile module color - Mint
  static const Color profileColor = Color(0xFF81E6D9);

  /// Profile icon
  static const IconData profileIcon = Icons.person_outline_rounded;

  /// Settings icon
  static const IconData settingsIcon = Icons.settings_outlined;

  /// Profile gradient
  static const LinearGradient profileGradient = LinearGradient(
    colors: [Color(0xFF81E6D9), Color(0xFFA7F3E9)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  // ============================================================================
  // HELPER METHODS
  // ============================================================================

  /// Get module color by name
  static Color getModuleColor(String moduleName) {
    switch (moduleName.toLowerCase()) {
      case 'home':
      case 'dashboard':
        return homeColor;
      case 'transactions':
      case 'transações':
        return transactionsColor;
      case 'investments':
      case 'investimentos':
        return investmentsColor;
      case 'goals':
      case 'objetivos':
      case 'metas':
        return goalsColor;
      case 'recurrences':
      case 'recorrências':
        return recurrencesColor;
      case 'categories':
      case 'categorias':
        return categoriesColor;
      case 'reports':
      case 'relatórios':
        return reportsColor;
      case 'profile':
      case 'perfil':
      case 'settings':
      case 'configurações':
        return profileColor;
      default:
        return homeColor; // Default fallback
    }
  }

  /// Get module icon by name
  static IconData getModuleIcon(String moduleName) {
    switch (moduleName.toLowerCase()) {
      case 'home':
      case 'dashboard':
        return homeIcon;
      case 'transactions':
      case 'transações':
        return transactionsIcon;
      case 'investments':
      case 'investimentos':
        return investmentsIcon;
      case 'goals':
      case 'objetivos':
      case 'metas':
        return goalsIcon;
      case 'recurrences':
      case 'recorrências':
        return recurrencesIcon;
      case 'categories':
      case 'categorias':
        return categoriesIcon;
      case 'reports':
      case 'relatórios':
        return reportsIcon;
      case 'profile':
      case 'perfil':
      case 'settings':
      case 'configurações':
        return profileIcon;
      default:
        return homeIcon; // Default fallback
    }
  }

  /// Get module gradient by name
  static LinearGradient getModuleGradient(String moduleName) {
    switch (moduleName.toLowerCase()) {
      case 'home':
      case 'dashboard':
        return homeGradient;
      case 'transactions':
      case 'transações':
        return transactionsGradient;
      case 'investments':
      case 'investimentos':
        return investmentsGradient;
      case 'goals':
      case 'objetivos':
      case 'metas':
        return goalsGradient;
      case 'recurrences':
      case 'recorrências':
        return recurrencesGradient;
      case 'categories':
      case 'categorias':
        return categoriesGradient;
      case 'reports':
      case 'relatórios':
        return reportsGradient;
      case 'profile':
      case 'perfil':
      case 'settings':
      case 'configurações':
        return profileGradient;
      default:
        return homeGradient; // Default fallback
    }
  }
}
