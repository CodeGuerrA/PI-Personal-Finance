import 'package:flutter/material.dart';

import 'package:sgfi/features/auth/presentation/pages/splash_screen.dart';
import 'package:sgfi/features/auth/presentation/pages/login_screen.dart';
import 'package:sgfi/features/home/presentation/pages/home_dashboard_screen.dart';
import 'package:sgfi/features/transactions/presentation/pages/transactions_list_screen.dart';
import 'package:sgfi/features/auth/presentation/pages/register_screen.dart';
import 'package:sgfi/features/investments/presentation/pages/investments_screen.dart';
import 'package:sgfi/features/goals/presentation/pages/goals_screen.dart';
import 'package:sgfi/features/auth/presentation/pages/define_password_screen.dart';
import 'package:sgfi/features/categories/presentation/pages/categories_screen.dart';
import 'package:sgfi/features/recurrences/presentation/pages/recurrences_screen.dart';
import 'package:sgfi/features/auth/presentation/pages/forgot_password_screen.dart';
import 'package:sgfi/features/auth/presentation/pages/profile_screen.dart';
import 'package:sgfi/features/auth/presentation/pages/change_password_screen.dart';
import 'package:sgfi/features/reports/presentation/pages/reports_overview_screen.dart';
import 'package:sgfi/features/auth/presentation/pages/first_password_screen.dart';






class AppRoutes {
  static const String splash = '/';
  static const String login = '/login';
  static const String home = '/home';
  static const String forgotPassword = '/forgot-password';
  static const String profile = '/profile';
  static const String changePassword = '/change-password';
  static const String transactions = '/transactions';
  static const String register = '/register';
  static const String investments = '/investments';
  static const String goals = '/goals';
  static const String definePassword = '/define-password';
  static const String categories = '/categories';
  static const String recurrences = '/recurrences';
  static const String reportsOverview = '/reports-overview';
  static const String firstPassword = '/first-password';



    static Route<dynamic> generateRoute(RouteSettings settings) {
    switch (settings.name) {
      case splash:
        return MaterialPageRoute(
          builder: (_) => const SplashScreen(),
          settings: settings,
        );
      case login:
        return MaterialPageRoute(
          builder: (_) => const LoginScreen(),
          settings: settings,
        );
      case home:
        return MaterialPageRoute(
          builder: (_) => const HomeDashboardScreen(),
          settings: settings,
        );
      case transactions:
        return MaterialPageRoute(
          builder: (_) => const TransactionsListScreen(),
          settings: settings,
        );
      case register:
        return MaterialPageRoute(
          builder: (_) => const RegisterScreen(),
          settings: settings,
        );
      case AppRoutes.forgotPassword:
        return MaterialPageRoute(
          builder: (_) => const ForgotPasswordScreen(),
          settings: settings,
        );
      case investments:
        return MaterialPageRoute(
          builder: (_) => const InvestmentsScreen(),
          settings: settings,
        );
      case goals:
        return MaterialPageRoute(
          builder: (_) => const GoalsScreen(),
          settings: settings,
        );
      case definePassword:
        return MaterialPageRoute(
          builder: (_) => const DefinePasswordScreen(),
          settings: settings,
        );
      case categories:
        return MaterialPageRoute(
          builder: (_) => const CategoriesScreen(),
          settings: settings,
        );
      case recurrences:
        return MaterialPageRoute(
          builder: (_) => const RecurrencesScreen(),
          settings: settings,
        );
      case AppRoutes.profile:
        return MaterialPageRoute(
          builder: (_) => const ProfileScreen(),
          settings: settings,
        );
      case AppRoutes.changePassword:
        return MaterialPageRoute(
          builder: (_) => const ChangePasswordScreen(),
          settings: settings,
        );
      case AppRoutes.reportsOverview:
        return MaterialPageRoute(
          builder: (_) => const ReportsOverviewScreen(),
          settings: settings,
        );
      case AppRoutes.firstPassword:                        // ⬅️ NOVO CASE
        final username = settings.arguments as String?;
        return MaterialPageRoute(
          builder: (_) => FirstPasswordScreen(
            username: username ?? '',
          ),
          settings: settings,
        );

      default:
        return MaterialPageRoute(
          builder: (_) => const Scaffold(
            body: Center(
              child: Text('Rota não encontrada'),
            ),
          ),
        );
    }
  }
}

