@active-monitoring-full
Feature: access ticketmaster for active monitoring

  @tm360-login
  Scenario: login and out of access ticketmaster
    Given that I have navigated to "access.ticketmaster.com" in "chrome"
    And I load the page
    When I have logged in with "LucienMinot@livenation.com" and "Test123"
    And I attempt to log out
    Then I am on the login page