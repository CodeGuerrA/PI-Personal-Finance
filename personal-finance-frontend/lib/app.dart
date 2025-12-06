import 'package:flutter/material.dart';
import 'core/routes/app_routes.dart';
import 'core/theme/app_theme.dart';

class SgfiApp extends StatelessWidget {
  const SgfiApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SGFI',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme,
      initialRoute: AppRoutes.splash,
      onGenerateRoute: AppRoutes.generateRoute,
    );
  }
}
