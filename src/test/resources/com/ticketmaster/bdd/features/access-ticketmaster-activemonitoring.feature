@active-monitoring-full
Feature: access ticketmaster for active monitoring

  @tm360-login
  Scenario: login and out of access "tm360.corp" in "chrome"
    And I load the page
    When I have logged in with "AM_tester" and "Test7890"
    And I attempt to log out
    Then I am on the login page