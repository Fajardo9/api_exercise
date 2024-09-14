@active
Feature: Resource Management

  @smoke @dx
  Scenario:Get the list of active resources
    Given there are at least 5 active resources in the system
    When I find all the active resources
    Then the response should have a status code 200
    And validates the response with resource list JSON schema

  @smoke @dx
  Scenario: Update the list of active resources as inactive
    Given there are active resources in the system
    When I update the status of the resources to inactive
    Then the response should have a status code 200
    And validates the response with client list JSON schema
    And verify that all the resources are marked as inactive

  @smoke @dx
  Scenario: Get the list of all the resources
    Given there are all the products marked as inactive
    When I send a GET request to view all the resources
    Then the response should have a status code 200
    And verify that all the resources are marked as inactive

