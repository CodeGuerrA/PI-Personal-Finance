import 'package:flutter/material.dart';

/// Widget que exibe um indicador de carregamento centralizado
class LoadingWidget extends StatelessWidget {
  final String? message;

  const LoadingWidget({super.key, this.message});

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const CircularProgressIndicator(),
          if (message != null) ...[
            const SizedBox(height: 16),
            Text(
              message!,
              style: Theme.of(context).textTheme.bodyMedium,
              textAlign: TextAlign.center,
            ),
          ],
        ],
      ),
    );
  }
}

/// Widget que exibe uma mensagem de erro com opção de retry
class ErrorWidget extends StatelessWidget {
  final String message;
  final VoidCallback? onRetry;
  final String? retryLabel;

  const ErrorWidget({
    super.key,
    required this.message,
    this.onRetry,
    this.retryLabel,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final errorColor = theme.colorScheme.error;

    return Center(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              Icons.error_outline,
              size: 48,
              color: errorColor,
            ),
            const SizedBox(height: 16),
            Text(
              message,
              style: theme.textTheme.bodyMedium,
              textAlign: TextAlign.center,
            ),
            if (onRetry != null) ...[
              const SizedBox(height: 16),
              ElevatedButton(
                onPressed: onRetry,
                child: Text(retryLabel ?? 'Tentar novamente'),
              ),
            ],
          ],
        ),
      ),
    );
  }
}

/// Widget que exibe uma mensagem quando não há dados
class EmptyStateWidget extends StatelessWidget {
  final String message;
  final IconData? icon;
  final Widget? action;

  const EmptyStateWidget({
    super.key,
    required this.message,
    this.icon,
    this.action,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final iconColor = theme.brightness == Brightness.dark
        ? theme.colorScheme.onSurface.withValues(alpha: 0.3)
        : theme.colorScheme.onSurface.withValues(alpha: 0.4);

    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              icon ?? Icons.inbox_outlined,
              size: 64,
              color: iconColor,
            ),
            const SizedBox(height: 16),
            Text(
              message,
              style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                    color: Colors.grey[600],
                  ),
              textAlign: TextAlign.center,
            ),
            if (action != null) ...[
              const SizedBox(height: 24),
              action!,
            ],
          ],
        ),
      ),
    );
  }
}

/// Builder widget que gerencia os estados de loading, erro e sucesso
class StateBuilder<T> extends StatelessWidget {
  final bool isLoading;
  final String? errorMessage;
  final T? data;
  final Widget Function(BuildContext context, T data) builder;
  final VoidCallback? onRetry;
  final Widget? emptyState;
  final String? loadingMessage;

  const StateBuilder({
    super.key,
    required this.isLoading,
    required this.errorMessage,
    required this.data,
    required this.builder,
    this.onRetry,
    this.emptyState,
    this.loadingMessage,
  });

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return LoadingWidget(message: loadingMessage);
    }

    if (errorMessage != null) {
      return ErrorWidget(
        message: errorMessage!,
        onRetry: onRetry,
      );
    }

    if (data == null) {
      return emptyState ??
          const EmptyStateWidget(
            message: 'Nenhum dado disponível',
          );
    }

    return builder(context, data!);
  }
}

/// Builder widget otimizado para listas
class ListStateBuilder<T> extends StatelessWidget {
  final bool isLoading;
  final String? errorMessage;
  final List<T> items;
  final Widget Function(BuildContext context, List<T> items) builder;
  final VoidCallback? onRetry;
  final String? emptyMessage;
  final Widget? emptyAction;
  final String? loadingMessage;

  const ListStateBuilder({
    super.key,
    required this.isLoading,
    required this.errorMessage,
    required this.items,
    required this.builder,
    this.onRetry,
    this.emptyMessage,
    this.emptyAction,
    this.loadingMessage,
  });

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return LoadingWidget(message: loadingMessage);
    }

    if (errorMessage != null) {
      return ErrorWidget(
        message: errorMessage!,
        onRetry: onRetry,
      );
    }

    if (items.isEmpty) {
      return EmptyStateWidget(
        message: emptyMessage ?? 'Nenhum item encontrado',
        action: emptyAction,
      );
    }

    return builder(context, items);
  }
}
