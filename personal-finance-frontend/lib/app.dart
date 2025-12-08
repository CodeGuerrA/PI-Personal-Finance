import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'core/routes/app_routes.dart';
import 'core/theme/app_theme.dart';
import 'core/storage/token_storage.dart';

// Transaction
import 'features/transactions/presentation/providers/transaction_provider.dart';
import 'features/transactions/data/repositories/transaction_repository_impl.dart';
import 'features/transactions/data/datasources/transaction_remote_datasource_impl.dart';

// Category
import 'features/categories/presentation/providers/category_provider.dart';
import 'features/categories/data/repositories/category_repository_impl.dart';
import 'features/categories/data/datasources/category_remote_datasource_impl.dart';

// Goal
import 'features/goals/presentation/providers/goal_provider.dart';
import 'features/goals/data/repositories/goal_repository_impl.dart';
import 'features/goals/data/datasources/goal_remote_datasource_impl.dart';

// Investment
import 'features/investments/presentation/providers/investment_provider.dart';
import 'features/investments/data/repositories/investment_repository_impl.dart';
import 'features/investments/data/datasources/investment_remote_datasource_impl.dart';

// Recurrence
import 'features/recurrences/presentation/providers/recurrence_provider.dart';
import 'features/recurrences/data/repositories/recurrence_repository_impl.dart';
import 'features/recurrences/data/datasources/recurrence_remote_datasource_impl.dart';

// Investment Movement
import 'features/investment_movements/presentation/providers/investment_movement_provider.dart';
import 'features/investment_movements/data/repositories/investment_movement_repository_impl.dart';
import 'features/investment_movements/data/datasources/investment_movement_remote_datasource_impl.dart';

// Auth
import 'features/auth/presentation/providers/auth_provider.dart';
import 'features/auth/data/repositories/auth_repository_impl.dart';
import 'features/auth/data/datasources/auth_remote_datasource_impl.dart';

class SgfiApp extends StatelessWidget {
  const SgfiApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        // 🔐 Auth Provider (primeiro para verificar autenticação)
        ChangeNotifierProvider(
          create: (_) => AuthProvider(
            AuthRepositoryImpl(
              AuthRemoteDataSourceImpl(),
            ),
            TokenStorage(),
          )..initialize(), // Inicializa automaticamente
        ),

        // 💰 Transaction Provider
        ChangeNotifierProvider(
          create: (_) => TransactionProvider(
            TransactionRepositoryImpl(
              remoteDataSource: TransactionRemoteDataSourceImpl(),
            ),
          ),
        ),

        // 📁 Category Provider
        ChangeNotifierProvider(
          create: (_) => CategoryProvider(
            CategoryRepositoryImpl(
              remoteDataSource: CategoryRemoteDataSourceImpl(),
            ),
          ),
        ),

        // 🎯 Goal Provider
        ChangeNotifierProvider(
          create: (_) => GoalProvider(
            GoalRepositoryImpl(
              remoteDataSource: GoalRemoteDataSourceImpl(),
            ),
          ),
        ),

        // 📈 Investment Provider
        ChangeNotifierProvider(
          create: (_) => InvestmentProvider(
            InvestmentRepositoryImpl(
              remoteDataSource: InvestmentRemoteDataSourceImpl(),
            ),
          ),
        ),

        // 🔄 Recurrence Provider
        ChangeNotifierProvider(
          create: (_) => RecurrenceProvider(
            RecurrenceRepositoryImpl(
              remoteDataSource: RecurrenceRemoteDataSourceImpl(),
            ),
          ),
        ),

        // 📊 Investment Movement Provider
        ChangeNotifierProvider(
          create: (_) => InvestmentMovementProvider(
            InvestmentMovementRepositoryImpl(
              remoteDataSource: InvestmentMovementRemoteDataSourceImpl(),
            ),
          ),
        ),
      ],
      child: MaterialApp(
        title: 'SGFI',
        debugShowCheckedModeBanner: false,
        theme: AppTheme.lightTheme,
        initialRoute: AppRoutes.splash,
        onGenerateRoute: AppRoutes.generateRoute,
      ),
    );
  }
}
