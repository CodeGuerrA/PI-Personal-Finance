import 'package:intl/intl.dart';
import 'package:flutter/services.dart';

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

  /// Normaliza entrada de valor monetário
  /// Aceita tanto vírgula quanto ponto como separador decimal
  /// Remove separadores de milhar (. ou ,)
  /// Retorna string no formato com ponto decimal: "1234.56"
  ///
  /// Exemplos:
  /// "20.000" -> "20000"
  /// "20,000" -> "20000"
  /// "20.000,50" -> "20000.50"
  /// "20,000.50" -> "20000.50"
  /// "1.234,56" -> "1234.56"
  /// "1,234.56" -> "1234.56"
  static String normalizeMoneyInput(String input) {
    if (input.isEmpty) return '';

    // Remove espaços
    String normalized = input.trim();

    // Detectar qual formato está sendo usado
    final lastComma = normalized.lastIndexOf(',');
    final lastDot = normalized.lastIndexOf('.');

    // Se tem ambos vírgula e ponto
    if (lastComma != -1 && lastDot != -1) {
      // O que vem por último é o separador decimal
      if (lastComma > lastDot) {
        // Formato brasileiro: 1.234,56
        normalized = normalized.replaceAll('.', ''); // Remove separadores de milhar
        normalized = normalized.replaceAll(',', '.'); // Vírgula vira ponto decimal
      } else {
        // Formato internacional: 1,234.56
        normalized = normalized.replaceAll(',', ''); // Remove separadores de milhar
        // Ponto já é o separador decimal
      }
    }
    // Se tem apenas vírgula
    else if (lastComma != -1) {
      // Verificar se é separador decimal (se tem 2 dígitos após) ou de milhar
      final afterComma = normalized.substring(lastComma + 1);
      if (afterComma.length <= 2 && !afterComma.contains('.')) {
        // É separador decimal: 1234,56
        normalized = normalized.replaceAll(',', '.');
      } else {
        // É separador de milhar: 1,234
        normalized = normalized.replaceAll(',', '');
      }
    }
    // Se tem apenas ponto
    else if (lastDot != -1) {
      // Verificar se é separador decimal (se tem 2 dígitos após) ou de milhar
      final afterDot = normalized.substring(lastDot + 1);
      if (afterDot.length <= 2 && !afterDot.contains(',')) {
        // É separador decimal: 1234.56 (mantém como está)
      } else {
        // É separador de milhar: 1.234
        normalized = normalized.replaceAll('.', '');
      }
    }

    // Remove qualquer caractere que não seja dígito ou ponto
    normalized = normalized.replaceAll(RegExp(r'[^\d.]'), '');

    return normalized;
  }

  /// Converte string de entrada para double
  /// Aceita tanto vírgula quanto ponto como separador decimal
  /// Aceita formato brasileiro: 1.234,56 ou formato internacional: 1,234.56
  /// Retorna null se a conversão falhar
  ///
  /// Exemplos:
  /// "20.000" -> 20000.0
  /// "20,000" -> 20000.0
  /// "20.000,50" -> 20000.50
  /// "20,000.50" -> 20000.50
  /// "1.234,56" -> 1234.56
  /// "1,234.56" -> 1234.56
  static double? parseMoneyInput(String input) {
    if (input.isEmpty) return null;

    final normalized = normalizeMoneyInput(input);
    if (normalized.isEmpty) return null;

    return double.tryParse(normalized);
  }

  /// Limpa entrada mantendo apenas dígitos, vírgula e ponto
  /// Útil para usar em inputFormatters
  static String cleanMoneyInput(String value) {
    return value.replaceAll(RegExp(r'[^\d,\.]'), '');
  }
}

/// TextInputFormatter para campos de valor monetário
/// Formata automaticamente com separador de milhar (ponto) e decimal (vírgula opcional)
/// Aceita tanto vírgula quanto ponto durante digitação
/// Exibe sempre no formato: 20.000 ou 20.000,50
class MoneyInputFormatter extends TextInputFormatter {
  final int decimalDigits;

  MoneyInputFormatter({this.decimalDigits = 2});

  @override
  TextEditingValue formatEditUpdate(
    TextEditingValue oldValue,
    TextEditingValue newValue,
  ) {
    // Se está vazio, retorna vazio
    if (newValue.text.isEmpty) {
      return newValue;
    }

    // Remove tudo que não é dígito, vírgula ou ponto
    String cleaned = FormatUtils.cleanMoneyInput(newValue.text);

    // Separar parte inteira e decimal
    String integerPart = '';
    String decimalPart = '';
    bool hasDecimalSeparator = false;

    // Procurar separador decimal (última vírgula ou último ponto com até 2 dígitos depois)
    final lastComma = cleaned.lastIndexOf(',');
    final lastDot = cleaned.lastIndexOf('.');

    int decimalSeparatorIndex = -1;

    if (lastComma != -1 || lastDot != -1) {
      // Determinar qual é o separador decimal
      if (lastComma > lastDot) {
        decimalSeparatorIndex = lastComma;
      } else if (lastDot != -1) {
        final afterDot = cleaned.substring(lastDot + 1);
        // Se tem até 2 dígitos depois do ponto, é separador decimal
        if (afterDot.length <= decimalDigits && !afterDot.contains(',')) {
          decimalSeparatorIndex = lastDot;
        }
      }
    }

    if (decimalSeparatorIndex != -1) {
      hasDecimalSeparator = true;
      integerPart = cleaned.substring(0, decimalSeparatorIndex);
      decimalPart = cleaned.substring(decimalSeparatorIndex + 1);

      // Limpar parte inteira de pontos e vírgulas
      integerPart = integerPart.replaceAll(RegExp(r'[^\d]'), '');
      // Limitar parte decimal
      decimalPart = decimalPart.replaceAll(RegExp(r'[^\d]'), '');
      if (decimalPart.length > decimalDigits) {
        decimalPart = decimalPart.substring(0, decimalDigits);
      }
    } else {
      // Sem separador decimal, tudo é parte inteira
      integerPart = cleaned.replaceAll(RegExp(r'[^\d]'), '');
    }

    // Se não tem dígitos, retorna vazio
    if (integerPart.isEmpty && decimalPart.isEmpty) {
      return const TextEditingValue(text: '');
    }

    // Formatar parte inteira com separador de milhar (ponto)
    String formattedInteger = '';
    if (integerPart.isNotEmpty) {
      final reversed = integerPart.split('').reversed.toList();
      for (int i = 0; i < reversed.length; i++) {
        if (i > 0 && i % 3 == 0) {
          formattedInteger = '.$formattedInteger';
        }
        formattedInteger = reversed[i] + formattedInteger;
      }
    } else {
      formattedInteger = '0';
    }

    // Montar texto final
    String formatted = formattedInteger;
    if (hasDecimalSeparator || decimalPart.isNotEmpty) {
      formatted += ',$decimalPart';
    }

    // Ajustar posição do cursor
    int newOffset = formatted.length;

    // Se estava no meio do texto, tentar manter posição relativa
    if (newValue.selection.baseOffset < newValue.text.length) {
      newOffset = newValue.selection.baseOffset;
      // Ajustar para não ficar no meio de um separador
      if (newOffset > formatted.length) {
        newOffset = formatted.length;
      }
    }

    return TextEditingValue(
      text: formatted,
      selection: TextSelection.collapsed(offset: newOffset),
    );
  }
}
