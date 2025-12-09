import 'package:flutter/material.dart';

/// Ultra-modern, eye-friendly color palette
/// Optimized for long viewing sessions and professional finance apps
class AppColors {
  // Primary Brand Colors - Elegant Indigo/Blue (calming and professional)
  static const Color primary = Color(0xFF5B7CE3); // Softer indigo
  static const Color primaryDark = Color(0xFF4461D7);
  static const Color primaryLight = Color(0xFF7B9BF0);
  static const Color primaryContainer = Color(0xFFEBEFFD);

  // Secondary Colors - Fresh Teal (energy without strain)
  static const Color secondary = Color(0xFF00C4B4); // Turquoise
  static const Color secondaryDark = Color(0xFF009688);
  static const Color secondaryLight = Color(0xFF26D9CB);
  static const Color secondaryContainer = Color(0xFFE0F9F6);

  // Success - Vibrant yet Soft Green
  static const Color success = Color(0xFF22C893);
  static const Color successDark = Color(0xFF0F9E6E);
  static const Color successLight = Color(0xFF48D8AA);
  static const Color successContainer = Color(0xFFE6F9F2);

  // Warning - Warm Amber (attention without alarm)
  static const Color warning = Color(0xFFFFAB40);
  static const Color warningDark = Color(0xFFFF9100);
  static const Color warningLight = Color(0xFFFFBC5D);
  static const Color warningContainer = Color(0xFFFFF3E0);

  // Error - Modern Coral Red (clear but not harsh)
  static const Color error = Color(0xFFFF5C79);
  static const Color errorDark = Color(0xFFE63950);
  static const Color errorLight = Color(0xFFFF8096);
  static const Color errorContainer = Color(0xFFFFEBEE);

  // Info - Calm Sky Blue
  static const Color info = Color(0xFF42A5F5);
  static const Color infoDark = Color(0xFF1976D2);
  static const Color infoLight = Color(0xFF64B5F6);
  static const Color infoContainer = Color(0xFFE3F2FD);

  // Income - Fresh Spring Green
  static const Color income = Color(0xFF26E07F);
  static const Color incomeLight = Color(0xFF7CFFB3);
  static const Color incomeContainer = Color(0xFFE8F8EF);

  // Expense - Modern Rose
  static const Color expense = Color(0xFFFF6B9D);
  static const Color expenseLight = Color(0xFFFFB1C8);
  static const Color expenseContainer = Color(0xFFFFF0F5);

  // Neutral Colors - Ultra-soft and easy on the eyes
  static const Color background = Color(0xFFFAFBFD); // Very soft blue-tinted white
  static const Color surface = Color(0xFFFFFFFF);
  static const Color surfaceVariant = Color(0xFFF6F8FB); // Softer gray-blue

  static const Color onBackground = Color(0xFF1A2138); // Deep blue-gray instead of pure black
  static const Color onSurface = Color(0xFF2D3748);
  static const Color onSurfaceVariant = Color(0xFF6B7A90);

  // Text Colors - Optimized for readability without strain
  static const Color textPrimary = Color(0xFF1E2739); // Soft black
  static const Color textSecondary = Color(0xFF4A5568); // Warm gray
  static const Color textTertiary = Color(0xFFA0AEC0); // Light gray
  static const Color textDisabled = Color(0xFFCBD5E0);

  // Border Colors - Subtle and elegant
  static const Color border = Color(0xFFE6EAEF);
  static const Color borderLight = Color(0xFFF1F4F8);
  static const Color divider = Color(0xFFEBEFF5);

  // Shadow Colors - Soft and diffused
  static Color shadow = const Color(0xFF1E2739).withValues(alpha: 0.05);
  static Color shadowMedium = const Color(0xFF1E2739).withValues(alpha: 0.08);
  static Color shadowStrong = const Color(0xFF1E2739).withValues(alpha: 0.12);

  // Accent Colors - For special highlights
  static const Color accent1 = Color(0xFFB794F6); // Soft purple
  static const Color accent2 = Color(0xFFFC8181); // Coral
  static const Color accent3 = Color(0xFFFBD38D); // Golden
  static const Color accent4 = Color(0xFF81E6D9); // Mint

  // Gradient Colors - Modern and elegant
  static const LinearGradient primaryGradient = LinearGradient(
    colors: [Color(0xFF5B7CE3), Color(0xFF7B9BF0)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  static const LinearGradient successGradient = LinearGradient(
    colors: [Color(0xFF22C893), Color(0xFF48D8AA)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  static const LinearGradient warningGradient = LinearGradient(
    colors: [Color(0xFFFFAB40), Color(0xFFFFBC5D)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  static const LinearGradient errorGradient = LinearGradient(
    colors: [Color(0xFFFF5C79), Color(0xFFFF8096)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  // Income/Expense Gradients
  static const LinearGradient incomeGradient = LinearGradient(
    colors: [Color(0xFF26E07F), Color(0xFF7CFFB3)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  static const LinearGradient expenseGradient = LinearGradient(
    colors: [Color(0xFFFF6B9D), Color(0xFFFFB1C8)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  // Chart Colors - Vibrant yet harmonious
  static const List<Color> chartColors = [
    Color(0xFF5B7CE3), // Primary blue
    Color(0xFF00C4B4), // Teal
    Color(0xFF22C893), // Green
    Color(0xFFFFAB40), // Amber
    Color(0xFFFF5C79), // Coral
    Color(0xFFB794F6), // Purple
    Color(0xFFFC8181), // Rose
    Color(0xFF81E6D9), // Mint
    Color(0xFFFBD38D), // Golden
    Color(0xFF42A5F5), // Sky blue
  ];

  // Special Effects
  static Color overlay = const Color(0xFF1E2739).withValues(alpha: 0.4);
  static Color shimmer = Colors.white.withValues(alpha: 0.3);

  // Investment Status Colors
  static const Color profit = Color(0xFF22C893);
  static const Color loss = Color(0xFFFF5C79);
  static const Color neutral = Color(0xFF6B7A90);
}

/// Dark mode color palette - Premium and eye-friendly
/// Optimized for OLED displays with WCAG AA contrast compliance
/// Lighter, more vibrant colors to maintain visual hierarchy on dark backgrounds
class AppColorsDark {
  // Primary Brand Colors - Lighter for dark backgrounds
  static const Color primary = Color(0xFF7B9BF0); // Brighter indigo for contrast
  static const Color primaryDark = Color(0xFF5B7CE3);
  static const Color primaryLight = Color(0xFF9BB5FF);
  static const Color primaryContainer = Color(0xFF1E2739); // Dark container

  // Secondary Colors - Brighter teal
  static const Color secondary = Color(0xFF26D9CB);
  static const Color secondaryDark = Color(0xFF00C4B4);
  static const Color secondaryLight = Color(0xFF4DE9DF);
  static const Color secondaryContainer = Color(0xFF1A2E2C);

  // Success - Vibrant green for dark mode
  static const Color success = Color(0xFF48D8AA);
  static const Color successDark = Color(0xFF22C893);
  static const Color successLight = Color(0xFF6EEDC0);
  static const Color successContainer = Color(0xFF0D2B22);

  // Warning - Bright amber
  static const Color warning = Color(0xFFFFBC5D);
  static const Color warningDark = Color(0xFFFFAB40);
  static const Color warningLight = Color(0xFFFFD180);
  static const Color warningContainer = Color(0xFF2B2416);

  // Error - Bright coral for visibility
  static const Color error = Color(0xFFFF8096);
  static const Color errorDark = Color(0xFFFF5C79);
  static const Color errorLight = Color(0xFFFFA6B5);
  static const Color errorContainer = Color(0xFF2D1519);

  // Info - Bright sky blue
  static const Color info = Color(0xFF64B5F6);
  static const Color infoDark = Color(0xFF42A5F5);
  static const Color infoLight = Color(0xFF90CAF9);
  static const Color infoContainer = Color(0xFF1A2535);

  // Income - Bright spring green
  static const Color income = Color(0xFF7CFFB3);
  static const Color incomeLight = Color(0xFF9DFFC7);
  static const Color incomeContainer = Color(0xFF0D2B1C);

  // Expense - Bright rose
  static const Color expense = Color(0xFFFFB1C8);
  static const Color expenseLight = Color(0xFFFFCFDD);
  static const Color expenseContainer = Color(0xFF2D1A20);

  // Background Colors - True dark mode with OLED optimization
  static const Color background = Color(0xFF0F1419); // Near black for OLED
  static const Color surface = Color(0xFF1A1F26); // Elevated surface
  static const Color surfaceVariant = Color(0xFF252B35); // Higher elevation

  static const Color onBackground = Color(0xFFE8EAED);
  static const Color onSurface = Color(0xFFDCDFE3);
  static const Color onSurfaceVariant = Color(0xFF9CA3AF);

  // Text Colors - High contrast for readability
  static const Color textPrimary = Color(0xFFE8EAED); // Off-white for comfort
  static const Color textSecondary = Color(0xFFB3B6BA); // Medium gray
  static const Color textTertiary = Color(0xFF7A7E82); // Dim gray
  static const Color textDisabled = Color(0xFF4A4D51);

  // Border Colors - Subtle separation
  static const Color border = Color(0xFF2D3339);
  static const Color borderLight = Color(0xFF23272D);
  static const Color divider = Color(0xFF252A30);

  // Shadow Colors - Darker shadows for depth
  static Color shadow = const Color(0xFF000000).withValues(alpha: 0.3);
  static Color shadowMedium = const Color(0xFF000000).withValues(alpha: 0.5);
  static Color shadowStrong = const Color(0xFF000000).withValues(alpha: 0.7);

  // Accent Colors - Vibrant for dark mode
  static const Color accent1 = Color(0xFFD4B4FE); // Brighter purple
  static const Color accent2 = Color(0xFFFFA8A8); // Brighter coral
  static const Color accent3 = Color(0xFFFFE4A0); // Brighter golden
  static const Color accent4 = Color(0xFFA7F3E9); // Brighter mint

  // Gradient Colors - Optimized for dark backgrounds
  static const LinearGradient primaryGradient = LinearGradient(
    colors: [Color(0xFF7B9BF0), Color(0xFF9BB5FF)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  static const LinearGradient successGradient = LinearGradient(
    colors: [Color(0xFF48D8AA), Color(0xFF6EEDC0)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  static const LinearGradient warningGradient = LinearGradient(
    colors: [Color(0xFFFFBC5D), Color(0xFFFFD180)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  static const LinearGradient errorGradient = LinearGradient(
    colors: [Color(0xFFFF8096), Color(0xFFFFA6B5)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  static const LinearGradient incomeGradient = LinearGradient(
    colors: [Color(0xFF7CFFB3), Color(0xFF9DFFC7)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  static const LinearGradient expenseGradient = LinearGradient(
    colors: [Color(0xFFFFB1C8), Color(0xFFFFCFDD)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  // Chart Colors - Bright and distinct for dark mode
  static const List<Color> chartColors = [
    Color(0xFF7B9BF0), // Bright blue
    Color(0xFF26D9CB), // Bright teal
    Color(0xFF48D8AA), // Bright green
    Color(0xFFFFBC5D), // Bright amber
    Color(0xFFFF8096), // Bright coral
    Color(0xFFD4B4FE), // Bright purple
    Color(0xFFFFA8A8), // Bright rose
    Color(0xFFA7F3E9), // Bright mint
    Color(0xFFFFE4A0), // Bright golden
    Color(0xFF64B5F6), // Bright sky blue
  ];

  // Special Effects
  static Color overlay = const Color(0xFF000000).withValues(alpha: 0.6);
  static Color shimmer = Colors.white.withValues(alpha: 0.1);

  // Investment Status Colors - Brighter for dark mode
  static const Color profit = Color(0xFF48D8AA);
  static const Color loss = Color(0xFFFF8096);
  static const Color neutral = Color(0xFF9CA3AF);
}
