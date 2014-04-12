Feature: Open ticketmaster.com on a web browser and surf arount
  
  @smoke
  Scenario: Search for Bruno Mars Tickets
  Given that I have loaded ticketmaster.com in a browser
  When search for the term "Bruno Mars"
  Then I should get a page that looks like this
  
  Scenario: Search for Book of Mormon