import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';
import 'package:atomberg_app_flutter/main.dart' as app;

void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  group('Login Tests', () {
    testWidgets('Valid login with email and password',
        (WidgetTester tester) async {
      app.main();
      await tester.pumpAndSettle();

      // Find and interact with email field
      await tester.enterText(
        find.byKey(const ValueKey('email_field')),
        'test@example.com',
      );

      // Find and interact with password field
      await tester.enterText(
        find.byKey(const ValueKey('password_field')),
        'password123',
      );

      // Tap login button
      await tester.tap(find.byKey(const ValueKey('login_btn')));
      await tester.pumpAndSettle();

      // Verify successful login
      expect(find.text('Home'), findsOneWidget);
    });

    testWidgets('Invalid credentials show error',
        (WidgetTester tester) async {
      app.main();
      await tester.pumpAndSettle();

      await tester.enterText(
        find.byKey(const ValueKey('email_field')),
        'invalid@test.com',
      );
      await tester.enterText(
        find.byKey(const ValueKey('password_field')),
        'wrongpass',
      );

      await tester.tap(find.byKey(const ValueKey('login_btn')));
      await tester.pumpAndSettle();

      // Verify error message
      expect(find.text('Invalid credentials'), findsOneWidget);
    });
  });
}