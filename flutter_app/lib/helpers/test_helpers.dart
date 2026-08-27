import 'package:flutter_test/flutter_test.dart';

class TestHelper {
  /// Equivalent of TestHelper.waitForElement()
  static Future<void> waitForElement(
    WidgetTester tester,
    Finder finder,
    int timeoutSeconds,
  ) async {
    final timeout = Duration(seconds: timeoutSeconds);
    await tester.pumpWidget(SizedBox());

    final stopwatch = Stopwatch()..start();
    while (stopwatch.elapsed < timeout) {
      if (finder.evaluate().isNotEmpty) {
        return;
      }
      await tester.pump(const Duration(milliseconds: 100));
    }
    throw TimeoutException('Element not found within $timeoutSeconds seconds');
  }

  /// Equivalent of TestHelper.isElementDisplayed()
  static bool isElementDisplayed(WidgetTester tester, Finder finder) {
    try {
      expect(finder, findsOneWidget);
      return true;
    } catch (e) {
      return false;
    }
  }

  /// Equivalent of TestHelper.retryOperation()
  static Future<T> retryOperation<T>(
    Future<T> Function() operation,
    int attempts, {
    int initialDelayMs = 100,
  }) async {
    for (int i = 0; i < attempts; i++) {
      try {
        return await operation();
      } catch (e) {
        if (i == attempts - 1) rethrow;
        await Future.delayed(
          Duration(milliseconds: initialDelayMs * (i + 1)),
        );
      }
    }
    throw Exception('Retry operation failed');
  }
}