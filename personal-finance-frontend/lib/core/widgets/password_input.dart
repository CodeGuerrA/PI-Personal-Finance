import 'package:flutter/material.dart';

/// A reusable password input field with visibility toggle
///
/// This component provides a consistent password input experience across
/// the entire app with built-in show/hide password functionality.
///
/// Example usage:
/// ```dart
/// PasswordInput(
///   labelText: 'Senha',
///   validator: FormValidators.password,
///   onSaved: (value) => _password = value!,
/// )
/// ```
class PasswordInput extends StatefulWidget {
  /// Label text displayed above the input
  final String? labelText;

  /// Hint text displayed inside the input when empty
  final String? hintText;

  /// Helper text displayed below the input
  final String? helperText;

  /// Validator function for form validation
  final FormFieldValidator<String>? validator;

  /// Callback when the form is saved
  final FormFieldSetter<String>? onSaved;

  /// Callback when the input value changes
  final ValueChanged<String>? onChanged;

  /// Text editing controller for external control
  final TextEditingController? controller;

  /// Whether this field should autofocus
  final bool autofocus;

  /// Icon displayed at the start of the field
  final IconData? prefixIcon;

  /// Whether the field is enabled
  final bool enabled;

  /// Initial value for the field
  final String? initialValue;

  /// Text input action (done, next, etc.)
  final TextInputAction? textInputAction;

  /// Callback when the field is submitted
  final ValueChanged<String>? onFieldSubmitted;

  /// Focus node for managing focus
  final FocusNode? focusNode;

  const PasswordInput({
    super.key,
    this.labelText,
    this.hintText,
    this.helperText,
    this.validator,
    this.onSaved,
    this.onChanged,
    this.controller,
    this.autofocus = false,
    this.prefixIcon,
    this.enabled = true,
    this.initialValue,
    this.textInputAction,
    this.onFieldSubmitted,
    this.focusNode,
  });

  @override
  State<PasswordInput> createState() => _PasswordInputState();
}

class _PasswordInputState extends State<PasswordInput> {
  /// Controls whether the password is visible or obscured
  bool _obscureText = true;

  /// Toggle password visibility
  void _toggleVisibility() {
    setState(() {
      _obscureText = !_obscureText;
    });
  }

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: widget.controller,
      obscureText: _obscureText,
      autofocus: widget.autofocus,
      enabled: widget.enabled,
      initialValue: widget.initialValue,
      textInputAction: widget.textInputAction ?? TextInputAction.done,
      focusNode: widget.focusNode,
      decoration: InputDecoration(
        labelText: widget.labelText,
        hintText: widget.hintText,
        helperText: widget.helperText,
        prefixIcon: widget.prefixIcon != null
            ? Icon(widget.prefixIcon)
            : const Icon(Icons.lock_outline),
        suffixIcon: IconButton(
          icon: Icon(
            _obscureText ? Icons.visibility_off : Icons.visibility,
          ),
          onPressed: widget.enabled ? _toggleVisibility : null,
          tooltip: _obscureText ? 'Mostrar senha' : 'Ocultar senha',
        ),
      ),
      validator: widget.validator,
      onSaved: widget.onSaved,
      onChanged: widget.onChanged,
      onFieldSubmitted: widget.onFieldSubmitted,
    );
  }
}
