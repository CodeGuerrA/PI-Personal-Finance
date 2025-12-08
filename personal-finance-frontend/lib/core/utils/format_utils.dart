import 'package:intl/intl.dart';

/// Extensão para formatação de valores monetários
extension CurrencyFormat on double {
  /// Formata o valor como moeda brasileira (R$)
  /// Exemplo: 1234.56 -> "R$ 1.234,56"
  ///          -1234.56 -> "-R$ 1.234,56"
  String toCurrency() {
    final formatter = NumberFormat.currency(
      locale: 'pt_BR',
      symbol: 'R\$',
      decimalDigits: 2,
    );
    return formatter.format(this);
  }

  /// Formata o valor como moeda compacta
  /// Exemplo: 1234.56 -> "R$ 1,2K"
  ///          1234567.89 -> "R$ 1,2M"
  String toCompactCurrency() {
    final formatter = NumberFormat.compactCurrency(
      locale: 'pt_BR',
      symbol: 'R\$',
      decimalDigits: 1,
    );
    return formatter.format(this);
  }
}

/// Extensão para formatação de datas
extension DateTimeFormat on DateTime {
  /// Formata a data no formato brasileiro (dd/MM/yyyy)
  /// Exemplo: 2024-01-15 -> "15/01/2024"
  String toBrazilianDate() {
    return '${day.toString().padLeft(2, '0')}/'
        '${month.toString().padLeft(2, '0')}/'
        '$year';
  }

  /// Formata a data no formato curto (dd/MM)
  /// Exemplo: 2024-01-15 -> "15/01"
  String toShortDate() {
    return '${day.toString().padLeft(2, '0')}/'
        '${month.toString().padLeft(2, '0')}';
  }

  /// Formata a data com mês por extenso
  /// Exemplo: 2024-01-15 -> "15 de janeiro de 2024"
  String toFullDate() {
    final formatter = DateFormat('dd \'de\' MMMM \'de\' yyyy', 'pt_BR');
    return formatter.format(this);
  }

  /// Formata a data e hora
  /// Exemplo: 2024-01-15 14:30 -> "15/01/2024 às 14:30"
  String toFullDateTime() {
    return '${toBrazilianDate()} às ${hour.toString().padLeft(2, '0')}:'
        '${minute.toString().padLeft(2, '0')}';
  }

  /// Retorna o nome do mês em português
  /// Exemplo: 2024-01-15 -> "Janeiro"
  String getMonthName() {
    const months = [
      'Janeiro',
      'Fevereiro',
      'Março',
      'Abril',
      'Maio',
      'Junho',
      'Julho',
      'Agosto',
      'Setembro',
      'Outubro',
      'Novembro',
      'Dezembro',
    ];
    return months[month - 1];
  }

  /// Retorna o nome do mês abreviado em português
  /// Exemplo: 2024-01-15 -> "Jan"
  String getShortMonthName() {
    const months = [
      'Jan',
      'Fev',
      'Mar',
      'Abr',
      'Mai',
      'Jun',
      'Jul',
      'Ago',
      'Set',
      'Out',
      'Nov',
      'Dez',
    ];
    return months[month - 1];
  }
}

/// Funções utilitárias de formatação
class FormatUtils {
  /// Formata um número de CPF
  /// Exemplo: "12345678900" -> "123.456.789-00"
  static String formatCPF(String cpf) {
    if (cpf.length != 11) return cpf;
    return '${cpf.substring(0, 3)}.${cpf.substring(3, 6)}.'
        '${cpf.substring(6, 9)}-${cpf.substring(9, 11)}';
  }

  /// Remove formatação de CPF
  /// Exemplo: "123.456.789-00" -> "12345678900"
  static String cleanCPF(String cpf) {
    return cpf.replaceAll(RegExp(r'[^0-9]'), '');
  }

  /// Formata porcentagem
  /// Exemplo: 0.1234 -> "12,34%"
  static String formatPercentage(double value, {int decimals = 2}) {
    final formatter = NumberFormat.percentPattern('pt_BR');
    formatter.minimumFractionDigits = decimals;
    formatter.maximumFractionDigits = decimals;
    return formatter.format(value);
  }
}
