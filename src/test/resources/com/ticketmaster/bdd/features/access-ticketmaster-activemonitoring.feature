@active-monitoring-full
Feature: access ticketmaster for active monitoring

  @am-login
  Scenario: login and out of access ticketmaster
    Given that I have loaded "access.ticketmaster.com" in a "chrome"
    When I load a page
    And that I have logged in with "LucienMinot@livenation.com" and "Test123"
    Then I logout