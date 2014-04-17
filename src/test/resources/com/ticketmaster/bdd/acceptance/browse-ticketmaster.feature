Feature: Open ticketmaster.com on a web browser and surf around

  @local
  Scenario: Search for Bruno Mars Tickets in PhantomJs
  Given that I have loaded "google.com" in a "phantomjs"
  When search for the term "Bruno Mars"
  Then I should get a page that looks like this
    
  @remote @local
  Scenario: Search for Bruno Mars Tickets in Chrome
  Given that I have loaded "google.com" in a "chrome"
  When search for the term "Bruno+Mars"
  Then I should get a page that looks like this
  
  @remote @local
  Scenario: Search for Bruno Mars Tickets in FireFox
  Given that I have loaded "google.com" in a "firefox"
  When search for the term "Bruno Mars"
  Then I should get a page that looks like this