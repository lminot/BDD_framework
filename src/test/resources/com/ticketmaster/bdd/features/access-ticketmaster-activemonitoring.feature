@active-monitoring-full
Feature: access ticketmaster for active monitoring

  @am-login
  Scenario: login and out of access ticketmaster
    Given that I have loaded "prodPage" in "chrome"
    And I load a page
    When I have logged in with "LucienMinot@livenation.com" and "Test123"
    Then I am on the login page
  
  @am-nav  
  Scenario: navigate acess ticketmaster
    Given that I have loaded "prodPage" in "chrome"
    And I load a page
    When I have logged in with "LucienMinot@livenation.com" and "Test123"