@active
Feature:  Change the phone number of the first Client named Laura

  @Smoke @xd
  Scenario: Validate that there are at least 10 clients when a GET request is made before the client modification
    Given I send a GET request to view all the clients
    When receive the response
    Then create the missing clients if there are less than 10 clients
    And verify that the number of clients is 10

  @Smoke @xd
  Scenario: Verify that there are a client with the name Laura and change their phone number
    Given I send a GET request to view all the clients
    When receive the response
    Then Verify that there is a client with the name "Laura"
    And if there is not a client with the name Laura, create a new client with the following data:
      | Name  | LastName | Country  | City | Email              | Phone      | Id |
      | Laura | Martinez | Colombia | Cali | lauracali@mail.com | 3122585565 | 1  |
    And Verify that there is a client with the name "Laura"
    And save the existent phone number and change it to "3135515223"
    And verify that the phone number has been changed correctly to "3135515223"

  @Smoke @xd
  Scenario: Cleanup all data created during the test execution
    Given Verify if any data was created
    When if the client "Laura" already exists, rollback her phone number
    Then delete all created data.
    And Verify that the changes has been done
