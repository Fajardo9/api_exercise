@active
Feature: Update existing resources

  @smoke @resourcesUpdate
  Scenario Outline: Create a new resource
    Given I have to create a resource
    When I send a POST request to create the resource with the following format:
      | name   | trademark   | stock   | price   | description   | tags   | active   | id   |
      | <name> | <trademark> | <stock> | <price> | <description> | <tags> | <active> | <id> |
    Then the response should have a status code of 201
    And verify that the products has been created:
      | name   | trademark   | stock   | price   | description   | tags   | active   | id   |
      | <name> | <trademark> | <stock> | <price> | <description> | <tags> | <active> | <id> |
    Examples:
      | name        | trademark   | stock | price | description   | tags  | active | id  |
      | Resource 99 | Trademark 1 | 10    | 12.0  | Description 1 | Tags1 | true   | "1" |

  @smoke @resourcesUpdate
  Scenario: Update an existing resource
    Given the lastest resource created with the following details:
      | name        | trademark   | stock | price | description   | tags  | active | id  |
      | Resource 99 | Trademark 1 | 10    | 12.0  | Description 1 | Tags1 | true   | "1" |
    When I send a PUT request to update it with the following details:
      | name        | trademark    | stock | price | description             | tags     | active | id  |
      | Resource 69 | Trademark 13 | 6     | 119   | Esto es una descripcion | El barto | false  | "1" |
    Then the response should have a status code of 200
    And verify that the product has been modified

  @smoke @resourcesUpdate
  Scenario: Delete an existing resource
    Given the lastest resource created wich the following details:
      | name        | trademark    | stock | price | description             | tags     | active | id  |
      | Resource 69 | Trademark 13 | 6     | 119   | Esto es una descripcion | El barto | false  | "1" |
    When I send a Delete request to delete the latest resource
    Then the response should have a status code of 200
    And validates the response with resource list JSON schema
