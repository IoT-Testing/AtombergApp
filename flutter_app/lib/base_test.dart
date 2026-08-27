import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';

abstract class BaseFlutterTest {
  late WidgetTester tester;
  late ExtentReportFlutter reporter;
  late String deviceSlot;

  /// Setup equivalent of @BeforeSuite
  void setUpAllTests() {
    IntegrationTestWidgetsFlutterBinding.ensureInitialized();
    reporter = ExtentReportFlutter('test-output/flutter_report.html');
  }

  /// Setup equivalent of @BeforeClass
  Future<void> setUp(WidgetTester tester, String deviceSlot) async {
    this.tester = tester;
    this.deviceSlot = deviceSlot;
    reporter.startTest(deviceSlot);

    // Load and pump app
    await tester.pumpAndSettle(const Duration(seconds: 2));
  }

  /// Teardown equivalent of @AfterClass
  Future<void> tearDown() async {
    reporter.endTest();
    await tester.pumpWidget(const SizedBox.shrink());
  }

  /// Recovery helper (equivalent of afterTestFailure())
  Future<void> afterTestFailure() async {
    await tester.tap(find.byType(BackButton));
    await tester.pumpAndSettle();
  }
}