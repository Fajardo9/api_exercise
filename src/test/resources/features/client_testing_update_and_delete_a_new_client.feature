Feature: Client Management

  @smoke @clientManagement
  Scenario: Create a new Client
    Given a new client to create
    When i send a post request to create a new client wich this parameters:
      | Name      | LastName | Country  | City | Email                      | Phone      | Id |
      | Alejandro | Martinez | Colombia | Cali | alejandro.fajardo@mail.com | 3122585565 | 1  |
    Then the client response should have a status code of 201
    And validates the response with client JSON schema
    And verify that the new client wich the following characteristics has been created:
      | Name      | LastName | Country  | City | Email                      | Phone      | Id |
      | Alejandro | Martinez | Colombia | Cali | alejandro.fajardo@mail.com | 3122585565 | 1  |

  @smoke @clientManagement
  Scenario: Update a new client
    Given a new client created
    When I send a GET request to find the new client:
      | Name      | LastName | Country  | City | Email                      | Phone      | Id |
      | Alejandro | Martinez | Colombia | Cali | alejandro.fajardo@mail.com | 3122585565 | 1  |
    Then the client response should have a status code of 200
    When I update the new client with new parameters:
      | Name      | LastName | Country   | City | Email                      | Phone      | Id |
      | Alejandro | Martinez | Argentina | Cali | alejandro.fajardo@mail.com | 3122585565 | 1  |
    Then the client response should have a status code of 200
    And verify that the client has been updated:
      | Name      | LastName | Country   | City | Email                      | Phone      | Id |
      | Alejandro | Martinez | Argentina | Cali | alejandro.fajardo@mail.com | 3122585565 | 1  |

  @smoke @clientManagement
  Scenario: Delete the client
    When I delete the new client:
      | Name      | LastName | Country   | City | Email                      | Phone      | Id |
      | Alejandro | Martinez | Argentina | Cali | alejandro.fajardo@mail.com | 3122585565 | 1  |
    Then the client response should have a status code of 200
    And verify that the client "Alejandro" "Martinez" email "alejandro.fajardo@mail.com" no longer exists