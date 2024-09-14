@active
Feature: Resource Management

  @smoke @dx @xd
  Scenario Outline: Create active and inactive resources
    Given there are not enough resources for the test
    When I create the following inactive resources:
      | Name           | Trademark           | Stock           | Price           | Description           | Tags           | Active |
      | <inactiveName> | <inactiveTrademark> | <inactiveStock> | <inactivePrice> | <inactiveDescription> | <inactiveTags> | false  |
    And I create the following active resources:
      | Name         | Trademark         | Stock         | Price         | Description         | Tags         | Active |
      | <activeName> | <activeTrademark> | <activeStock> | <activePrice> | <activeDescription> | <activeTags> | true   |
    Then the response should have a status code of 201
    And the response body should match the resource creation schema
    And verify that the products has been created:
      | Name           | Trademark           | Stock           | Price           | Description           | Tags           | Active |
      | <inactiveName> | <inactiveTrademark> | <inactiveStock> | <inactivePrice> | <inactiveDescription> | <inactiveTags> | false  |
      | <activeName>   | <activeTrademark>   | <activeStock>   | <activePrice>   | <activeDescription>   | <activeTags>   | true   |
    Examples:
      | inactiveName | inactiveTrademark    | inactiveStock | inactivePrice | inactiveDescription                    | inactiveTags | activeName | activeTrademark  | activeStock | activePrice | activeDescription                                                                                                          | activeTags |
      | Table        | Inactive Trademark 1 | 0             | 0.00          | Inactive resource for testing.         | createdAt    | Chair      | Hansen - Leannon | 76508       | 40845.37    | Ergonomic executive chair upholstered in bonded black leather and PVC padded seat and back for all-day comfort and support | createdAt  |
      | Desk         | Inactive Trademark 2 | 0             | 0.00          | Inactive desk resource for testing.    | createdAt    | Keyboard   | Stehr Inc        | 88500       | 14840.40    | Ergonomic keyboard designed for comfort and efficiency.                                                                    | createdAt  |
      | Lamp         | Inactive Trademark 3 | 0             | 0.00          | Inactive lamp resource for testing.    | createdAt    | Lamp       | Bright Lights    | 20000       | 1500.00     | LED desk lamp with adjustable brightness and color temperature.                                                            | createdAt  |
      | Monitor      | Inactive Trademark 4 | 0             | 0.00          | Inactive monitor resource for testing. | createdAt    | Monitor    | Vision Display   | 5000        | 30000.00    | 24-inch full HD monitor with ultra-slim design and built-in speakers.                                                      | createdAt  |
      | Printer      | Inactive Trademark 5 | 0             | 0.00          | Inactive printer resource for testing. | createdAt    | Printer    | PrintMaster      | 15000       | 25000.00    | All-in-one inkjet printer with wireless connectivity and mobile printing capabilities.                                     | createdAt  |

  @smoke @dx
  Scenario:Get the list of active resources
    Given there are at least 5 active resources in the sistem
    When I find all the active resources
    Then the response should have a status code 200
    And validates the response with resource list JSON schema

  @smoke @dx
  Scenario: Update the list of active resources as inactive
    Given there are active resources in the system
    When I update the status of the resourcrs to inactive
    Then the response should have a status code 200
    And verify that all the resources are inactive
    And validates the response with client list JSON schema

  @smoke @dx
  Scenario: Get the list of all the resources
    Given there are all the products marked as inactive
    When I send a GET request to view all the resources
    Then the response should have a status 200 code
    And verify that all the resources are marked as inactive

