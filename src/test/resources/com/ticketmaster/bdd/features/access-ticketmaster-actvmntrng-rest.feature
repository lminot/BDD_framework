@active-monitoring-rest-full
Feature: access ticketmaster for active monitoring via REST services

  @am-rest-login
  Scenario: login and out of access ticketmaster with REST
    Given I execute a POST to login to tm360
    And the response code is success 200 OK
    When I attempt to logout
    Then the response code is success 200 OK