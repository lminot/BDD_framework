Feature: Open ticketmaster.com on a web browser and surf around 

@smoke 
Scenario Outline: Search for Bruno Mars Tickets in three browsers 
	Given that I have loaded "www.ticketmaster.com" in a "<browser>" 
	When I load a page
	Examples:
	| browser |
	| firefox |
	| chrome |
	| internet explorer |