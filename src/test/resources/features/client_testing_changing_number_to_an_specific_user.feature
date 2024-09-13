@active
Feature:  Change the phone number of the first Client named Laura

  @Smoke @xd
  Scenario: Validate that there are at least 10 clients when a GET request is made before the client modification
    Given I send a GET request to view all the clients
    When receive the response
    Then create the missing clients if there are less than 10 clients
    And verify that the number of clients is 10

 
