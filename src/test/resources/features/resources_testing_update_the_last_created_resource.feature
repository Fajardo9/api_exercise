@active
Feature: Update existing resources

  @smoke @resourceUpdate
  Scenario:Get the list of active resources
    Given there are at least 15 active resources in the system
    When I find all the active resources
    Then the response should have a status code 200
    And validates the response with resource list JSON schema


  @smoke @resourceUpdate
  Scenario: Update an existing resource
    Given the lastest resource created with the following details:
      | name        | trademark   | stock | price | description   | tags  | active | id  |
      | Resource 99 | Trademark 1 | 10    | 12.0  | Description 1 | Tags1 | true   | "1" |
    When I send a PUT request to update it with the following details:
      | name        | trademark    | stock | price | description             | tags     | active | id  |
      | Resource 69 | Trademark 13 | 6     | 119   | Esto es una descripcion | El barto | false  | "1" |
    Then the response should have a status code of 200
    And verify that the product has been modified

  @smoke @resourceUpdate
  Scenario: Delete an existing resource
    Given the lastest resource created wich the following details:
      | name        | trademark    | stock | price | description             | tags     | active | id  |
      | Resource 69 | Trademark 13 | 6     | 119   | Esto es una descripcion | El barto | false  | "1" |
    When I send a Delete request to delete the latest resource
    Then the response should have a status code of 200
    And validates the response with resource JSON schema
