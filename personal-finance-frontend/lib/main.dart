import 'package:flutter/material.dart';
import 'package:sgfi/core/cache/cache_service.dart';
import 'app.dart';

void main() async {
  // Garante que os bindings do Flutter estão inicializados
  WidgetsFlutterBinding.ensureInitialized();

  // Inicializa o Hive para cache local
  await CacheService.init();

  runApp(const SgfiApp());
}
