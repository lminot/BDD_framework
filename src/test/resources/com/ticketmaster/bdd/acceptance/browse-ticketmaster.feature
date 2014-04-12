Feature: Open ticketmaster.com on a web browser and surf arount
  
  @smoke
  Scenario: Search for Bruno Mars Tickets in Firefox
  Given that I have loaded ticketmaster.com in a "Firefox"
  When search for the term "Bruno Mars"
  Then I should get a page that looks like this:
  
  @smoke
  Scenario: Search for Bruno Mars Tickets in Chrome
  Given that I have loaded ticketmaster.com in a "Chrome"
  When search for the term "Bruno Mars"
  Then I should get a page that looks like this