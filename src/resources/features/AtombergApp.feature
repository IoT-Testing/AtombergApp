# features/AtombergApp.feature

Feature: Atomberg Smart Home App End-to-End Flow
  As a user of the Atomberg app,
  I want to perform key actions like login, control devices, manage profile, and log out,
  So that I can verify the app works correctly from start to finish.

  Background:
    Given The Atomberg app is launched on Android device

  @login
  Scenario: Successful Login to Atomberg App
    When User logs in with valid credentials
    Then User should be redirected to the home screen

  @profile
  Scenario: Edit User Profile
    Given User is on the home screen
    When User navigates to Profile section and edits profile
    Then Profile update should be confirmed

  @family
  Scenario: Manage Family Members
    Given User is on the home screen
    When User opens the Manage section and views family members
    Then Family list or header should be visible

  @fans
  Scenario: Control Connected Fans
    Given User is on the home screen
    When User checks connected fans
    Then At least one fan should be controllable

  @locks
  Scenario: Control Connected Locks
    Given User is on the home screen
    When User checks connected locks
    Then Unlock handle should be accessible

  @analytics
  Scenario: View Analytics Data
    Given User is on the home screen
    When User opens the Analytics section
    Then Analytics chart should be displayed

  @help
  Scenario: Navigate Help & Support Section
    Given User is on the home screen
    When User navigates through all help options
    Then User should return to the home screen after completion

  @logout
  Scenario: Logout from Account
    Given User is logged in
    When User performs logout
    Then Login screen should appear again

  @teardown
  Scenario: Close Application
    Given The app session is complete
    When Driver and test server are closed
    Then No errors should occur during cleanup