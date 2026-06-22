import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import '../helpers/test_helper.dart';

class LoginPage {
  final WidgetTester tester;

  LoginPage(this.tester);

  // Locators (equivalent of Locators.java)
  static final emailInput = find.byKey(const ValueKey('email_input'));
  static final passwordInput = find.byKey(const ValueKey('password_input'));
  static final loginButton = find.byType(ElevatedButton);
  static final homeScreen = find.byType(HomePage);

  // Actions
  Future<void> enterEmail(String email) async {
    await TestHelper.waitForElement(tester, emailInput, 10);
    await tester.enterText(emailInput, email);
  }

  Future<void> enterPassword(String password) async {
    await TestHelper.waitForElement(tester, passwordInput, 10);
    await tester.enterText(passwordInput, password);
  }

  Future<void> tapLoginButton() async {
    await TestHelper.waitForElement(tester, loginButton, 10);
    await tester.tap(loginButton);
    await tester.pumpAndSettle();
  }

  Future<void> goHome() async {
    await tester.tap(find.byType(BackButton));
    await tester.pumpAndSettle();
  }
}