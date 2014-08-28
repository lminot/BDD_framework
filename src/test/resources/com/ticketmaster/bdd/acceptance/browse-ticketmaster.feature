Feature: Open ticketmaster.com on a web browser and surf around 

@smoke 
Scenario: Search for Bruno Mars Tickets in firefox
	Given that I have loaded "www.ticketmaster.com" in a "firefox" 
	When I load a page
	Then search for the term "Garth Brooks"
	
@smoke 
Scenario: Search for Bruno Mars Tickets in chrome
	Given that I have loaded "www.ticketmaster.com" in a "chrome" 
	When I load a page
	Then search for the term "Garth Brooks"
	
@smoke 
Scenario: Search for Bruno Mars Tickets in internet explorer
	Given that I have loaded "www.ticketmaster.com" in a "internet explorer" 
	When I load a page
	Then search for the term "Garth Brooks"