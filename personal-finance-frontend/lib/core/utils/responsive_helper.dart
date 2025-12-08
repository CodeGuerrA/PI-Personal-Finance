import 'package:flutter/material.dart';

/// Helper para design responsivo
/// Fornece breakpoints e helpers para adaptar UI a diferentes tamanhos de tela
class ResponsiveHelper {
  // Breakpoints
  static const double mobileMaxWidth = 600;
  static const double tabletMaxWidth = 900;
  static const double desktopMaxWidth = 1200;

  /// Retorna largura da tela
  static double screenWidth(BuildContext context) {
    return MediaQuery.of(context).size.width;
  }

  /// Retorna altura da tela
  static double screenHeight(BuildContext context) {
    return MediaQuery.of(context).size.height;
  }

  /// Verifica se é mobile (< 600px)
  static bool isMobile(BuildContext context) {
    return screenWidth(context) < mobileMaxWidth;
  }

  /// Verifica se é tablet (600px - 900px)
  static bool isTablet(BuildContext context) {
    final width = screenWidth(context);
    return width >= mobileMaxWidth && width < tabletMaxWidth;
  }

  /// Verifica se é desktop (> 900px)
  static bool isDesktop(BuildContext context) {
    return screenWidth(context) >= tabletMaxWidth;
  }

  /// Retorna padding responsivo
  static double responsivePadding(BuildContext context) {
    if (isMobile(context)) return 12.0;
    if (isTablet(context)) return 16.0;
    return 24.0;
  }

  /// Retorna tamanho de fonte responsivo
  static double responsiveFontSize(BuildContext context, double baseFontSize) {
    if (isMobile(context)) return baseFontSize;
    if (isTablet(context)) return baseFontSize * 1.1;
    return baseFontSize * 1.2;
  }

  /// Retorna número de colunas para grid responsivo
  static int gridCrossAxisCount(BuildContext context) {
    if (isMobile(context)) return 2;
    if (isTablet(context)) return 3;
    return 4;
  }

  /// Retorna largura máxima para conteúdo centralizado
  static double maxContentWidth(BuildContext context) {
    if (isMobile(context)) return screenWidth(context);
    if (isTablet(context)) return tabletMaxWidth;
    return desktopMaxWidth;
  }

  /// Widget responsivo que adapta layout baseado no tamanho da tela
  static Widget responsive({
    required BuildContext context,
    required Widget mobile,
    Widget? tablet,
    Widget? desktop,
  }) {
    if (isDesktop(context) && desktop != null) {
      return desktop;
    } else if (isTablet(context) && tablet != null) {
      return tablet;
    } else {
      return mobile;
    }
  }

  /// Valor responsivo baseado no tamanho da tela
  static T responsiveValue<T>({
    required BuildContext context,
    required T mobile,
    T? tablet,
    T? desktop,
  }) {
    if (isDesktop(context) && desktop != null) {
      return desktop;
    } else if (isTablet(context) && tablet != null) {
      return tablet;
    } else {
      return mobile;
    }
  }

  /// EdgeInsets responsivo
  static EdgeInsets responsiveEdgeInsets(BuildContext context) {
    final padding = responsivePadding(context);
    return EdgeInsets.all(padding);
  }

  /// EdgeInsets horizontal responsivo
  static EdgeInsets responsiveHorizontalPadding(BuildContext context) {
    final padding = responsivePadding(context);
    return EdgeInsets.symmetric(horizontal: padding);
  }

  /// EdgeInsets vertical responsivo
  static EdgeInsets responsiveVerticalPadding(BuildContext context) {
    final padding = responsivePadding(context);
    return EdgeInsets.symmetric(vertical: padding);
  }

  /// Espaçamento vertical responsivo
  static SizedBox responsiveVerticalSpacing(BuildContext context, {double factor = 1.0}) {
    return SizedBox(height: responsivePadding(context) * factor);
  }

  /// Espaçamento horizontal responsivo
  static SizedBox responsiveHorizontalSpacing(BuildContext context, {double factor = 1.0}) {
    return SizedBox(width: responsivePadding(context) * factor);
  }

  /// Largura de card/container responsivo
  static double responsiveCardWidth(BuildContext context) {
    final width = screenWidth(context);
    if (isMobile(context)) return width - (responsivePadding(context) * 2);
    if (isTablet(context)) return width * 0.8;
    return width * 0.6;
  }

  /// Retorna orientação da tela
  static Orientation orientation(BuildContext context) {
    return MediaQuery.of(context).orientation;
  }

  /// Verifica se está em modo paisagem
  static bool isLandscape(BuildContext context) {
    return orientation(context) == Orientation.landscape;
  }

  /// Verifica se está em modo retrato
  static bool isPortrait(BuildContext context) {
    return orientation(context) == Orientation.portrait;
  }
}
