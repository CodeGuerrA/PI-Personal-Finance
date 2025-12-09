/// Design tokens for consistent spacing, sizing, and timing across the app
///
/// These constants ensure visual consistency and make it easy to adjust
/// the design system globally. All values follow an 8px base grid system.

/// Spacing system based on 8px grid
class Spacing {
  Spacing._(); // Private constructor to prevent instantiation

  /// Extra small spacing - 4px
  static const double xs = 4.0;

  /// Small spacing - 8px
  static const double sm = 8.0;

  /// Medium spacing - 16px (base unit)
  static const double md = 16.0;

  /// Large spacing - 24px
  static const double lg = 24.0;

  /// Extra large spacing - 32px
  static const double xl = 32.0;

  /// Extra extra large spacing - 48px
  static const double xxl = 48.0;

  /// Extra extra extra large spacing - 64px
  static const double xxxl = 64.0;
}

/// Border radius system for rounded corners
class AppRadius {
  AppRadius._(); // Private constructor to prevent instantiation

  /// Extra small radius - 4px (subtle rounding)
  static const double xs = 4.0;

  /// Small radius - 8px (chips, small buttons)
  static const double sm = 8.0;

  /// Medium radius - 12px (cards, inputs, buttons)
  static const double md = 12.0;

  /// Large radius - 16px (large cards, FABs)
  static const double lg = 16.0;

  /// Extra large radius - 24px (bottom sheets, dialogs)
  static const double xl = 24.0;

  /// Full radius - 9999px (circular/pill shape)
  static const double full = 9999.0;
}

/// Elevation system for shadows and depth
class AppElevation {
  AppElevation._(); // Private constructor to prevent instantiation

  /// No elevation - flat design
  static const double none = 0.0;

  /// Small elevation - subtle depth (2dp)
  static const double sm = 2.0;

  /// Medium elevation - moderate depth (4dp)
  static const double md = 4.0;

  /// Large elevation - prominent depth (8dp)
  static const double lg = 8.0;

  /// Extra large elevation - maximum depth (16dp)
  static const double xl = 16.0;
}

/// Icon size system for consistent icon dimensions
class IconSize {
  IconSize._(); // Private constructor to prevent instantiation

  /// Extra small icon - 16px
  static const double xs = 16.0;

  /// Small icon - 20px
  static const double sm = 20.0;

  /// Medium icon - 24px (default Material icon size)
  static const double md = 24.0;

  /// Large icon - 32px
  static const double lg = 32.0;

  /// Extra large icon - 48px
  static const double xl = 48.0;

  /// Extra extra large icon - 64px
  static const double xxl = 64.0;
}

/// Animation duration constants for consistent timing
class AnimDuration {
  AnimDuration._(); // Private constructor to prevent instantiation

  /// Fast animation - 150ms (quick transitions)
  static const Duration fast = Duration(milliseconds: 150);

  /// Normal animation - 300ms (standard transitions)
  static const Duration normal = Duration(milliseconds: 300);

  /// Slow animation - 500ms (emphasis transitions)
  static const Duration slow = Duration(milliseconds: 500);

  /// Very slow animation - 800ms (special effects)
  static const Duration verySlow = Duration(milliseconds: 800);
}

/// Border width constants
class BorderWidth {
  BorderWidth._(); // Private constructor to prevent instantiation

  /// Thin border - 1px
  static const double thin = 1.0;

  /// Medium border - 1.5px
  static const double medium = 1.5;

  /// Thick border - 2px
  static const double thick = 2.0;

  /// Extra thick border - 3px
  static const double extraThick = 3.0;
}

/// Opacity constants for consistent transparency
class AppOpacity {
  AppOpacity._(); // Private constructor to prevent instantiation

  /// Disabled state - 38% opacity
  static const double disabled = 0.38;

  /// Medium emphasis - 60% opacity
  static const double medium = 0.60;

  /// High emphasis - 87% opacity
  static const double high = 0.87;

  /// Hover/Focus overlay - 8% opacity
  static const double hover = 0.08;

  /// Selected overlay - 12% opacity
  static const double selected = 0.12;
}
