package com.globant.api_tae.stepDefinitions;

import com.globant.api_tae.models.Resource;
import com.globant.api_tae.requests.ResourceRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.*;

public class ResourceSteps {
    private static final Logger log = LogManager.getLogger(ResourceSteps.class);
    private final ResourceRequest resourceRequest = new ResourceRequest();
    private static Resource auxResource;
    private Response response;

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int responseCode) {
        Assert.assertEquals(responseCode, response.statusCode());
    }

    @And("verify that the products has been created:")
    public void verifyThatTheProductsHasBeenCreated() {
        response = resourceRequest.getResources();
        log.info("Available Resources: {}", response.jsonPath().prettify());
    }


    @Given("there are at least {int} active resources in the system")
    public void thereAreAtLeastActiveResourcesInTheSistem(int desiredResources) {
        response = resourceRequest.getResources();
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        long activeResources = resourceList.stream().filter(Resource::isActive).count();
        log.info("The number of active resources are: {}", activeResources);
        Assert.assertTrue(activeResources >= desiredResources);
    }

    @When("I find all the active resources")
    public void iFindAllTheActiveResources() {
        response = resourceRequest.getResources();
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        long activeResources = resourceList.stream().filter(Resource::isActive).count();
        log.info("The number of the active resources are: {}", activeResources);
    }

    @Then("the response should have a status code {int}")
    public void theResponseShouldHaveAStatusCode(int expectedCode) {
        response = resourceRequest.getResources();
        Assert.assertEquals(expectedCode, response.getStatusCode());
        log.info(response.jsonPath().prettify());
    }

    @And("validates the response with resource list JSON schema")
    public void validatesTheResponseWithResourceListJSONSchema() {
        String path = "schemas/resourceListSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        log.info("Successfully Validated schema from Client List object");
    }

    @Then("validates the response with resource JSON schema")
    public void userValidatesResponseWithResourceJSONSchema() {
        String path = "schemas/resourceSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        log.info("Successfully Validated schema from Client object");
    }

    @Given("there are active resources in the system")
    public void thereAreActiveResourcesInTheSystem() {
        response = resourceRequest.getResources();
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        Optional<Resource> optionalResource = resourceList.stream().filter(Resource::isActive)
                .findAny();
        if (optionalResource.isPresent()) {
            log.info("Active resource found: {}", optionalResource.get().getName());
            log.info("There are {} active products", optionalResource.stream().count());
        } else {
            log.warn("No active resources found in the system.");
        }
    }

    @When("I update the status of the resources to inactive")
    public void iUpdateTheStatusOfTheResourcesToInactive() {
        response = resourceRequest.getResources();
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);

        for (Resource resource : resourceList) {
            Response updateResponse = resourceRequest.updateResource(resource, resource.getId());
            Assert.assertEquals(200, updateResponse.statusCode());
            Assert.assertTrue("The status was not changed", Objects.equals(resourceRequest.getResource(resource.getId()).jsonPath().prettify(), updateResponse.jsonPath().prettify()));
            log.info("Resource updated to inactive: {}", resource.getName());
        }
    }


    @Given("there are all the products marked as inactive")
    public void thereAreAllTheProductsMarkedAsInactive() {
        response = resourceRequest.getResources();
        resourceRequest.getResourcesEntity(response);
        List<Resource> resourceList;
        response = resourceRequest.getResources();
        resourceList = resourceRequest.getResourcesEntity(response);
        boolean allInactive = resourceList.stream().noneMatch(Resource::isActive);
        log.info(response.jsonPath().prettify());
        Assert.assertTrue("Not all resources are inactive.", allInactive);
        log.info("All resources are confirmed to be inactive.");

    }

    @When("I send a GET request to view all the resources")
    public void iSendAGETRequestToViewAllTheResources() {
        response = resourceRequest.getResources();
        log.info("Response received: {}", response.asString());
    }

    @And("verify that all the resources are marked as inactive")
    public void verifyThatAllTheResourcesAreMarkedAsInactive() {
        response = resourceRequest.getResources();
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        boolean allInactive = resourceList.stream().noneMatch(Resource::isActive);
        Assert.assertTrue("Not all resources are inactive.", allInactive);
        log.info("All resources are confirmed to be inactive.");
    }

    @Given("I have to create a resource:")
    public void iHaveToCreateAResource(DataTable dataTable) {
        Map<String, String> clientDataMap = dataTable.asMaps().get(0);
        auxResource = Resource.builder()
                .name(clientDataMap.get("Name"))
                .trademark(clientDataMap.get("Trademark"))
                .stock(Integer.parseInt(clientDataMap.get("Stock")))
                .price(Double.parseDouble(clientDataMap.get("Price")))
                .description(clientDataMap.get("Description"))
                .tags(clientDataMap.get("Tag"))
                .active(true)
                .id("2")
                .build();
        log.info("This is the element created {}", auxResource);

    }

    @When("I send a POST request to create the resource with the following format:")
    public void iSendAPOSTRequestToCreateTheResourceWithTheFollowingFormat() {
        response = resourceRequest.createResource(auxResource);
        Resource createdResource = response.as(Resource.class);
        Assert.assertEquals("The resource name should match", auxResource.getName(), createdResource.getName());
        Assert.assertEquals("The resource trademark should match", auxResource.getTrademark(), createdResource.getTrademark());
        Assert.assertEquals("The resource stock should match", auxResource.getStock(), createdResource.getStock());
        Assert.assertEquals("The resource price should match", auxResource.getPrice(), createdResource.getPrice(), 0.01);
        Assert.assertEquals("The resource description should match", auxResource.getDescription(), createdResource.getDescription());
        Assert.assertEquals("The resource tags should match", auxResource.getTags(), createdResource.getTags());
        Assert.assertTrue("The resource should be active", createdResource.isActive());


    }

    @Given("the lastest resource created with the following details:")
    public void theLastestResourceCreatedWithTheFollowingDetails(DataTable dataTable) {
        Map<String, String> expectedResourceDetails = dataTable.asMaps().get(0);
        response = resourceRequest.getResources();
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        auxResource = resourceList.stream()
                .max(Comparator.comparingInt(resource -> Integer.parseInt(resource.getId())))
                .orElse(null);
        Assert.assertNotNull("No resource was found", auxResource);

        log.info("The resource with the highest ID is: {}", auxResource.getName());
    }

    @When("I send a PUT request to update it with the following details:")
    public void iSendAPUTRequestToUpdateItWithTheFollowingDetails(DataTable dataTable) {
        Map<String, String> updateData = dataTable.asMaps().get(0);
        response = resourceRequest.getResources();
        response = resourceRequest.updateResource(Resource.builder()
                .name(updateData.get("name"))
                .trademark(updateData.get("trademark"))
                .stock(Integer.parseInt(updateData.get("stock")))
                .price(Double.parseDouble(updateData.get("price")))
                .description(updateData.get("description"))
                .tags(updateData.get("tags"))
                .active(Boolean.parseBoolean(updateData.get("active")))
                .id(updateData.get("id"))
                .build(), auxResource.getId());
        log.info("The modified product is:{}", auxResource.getName());
    }

    @And("verify that the product has been modified")
    public void verifyThatTheProductHasBeenModified() {
        Response getResponse = resourceRequest.getResource(auxResource.getId());
        Resource updatedResource = getResponse.as(Resource.class);
        Assert.assertEquals("The resource name should match", "Resource 69", updatedResource.getName());
        Assert.assertEquals("The resource trademark should match", "Trademark 13", updatedResource.getTrademark());

        Assert.assertEquals("The resource description should match", "Esto es una descripcion", updatedResource.getDescription());
        Assert.assertEquals("The resource tags should match", "El barto", updatedResource.getTags());
        Assert.assertEquals("The resource active status should match", false, updatedResource.isActive());
        Assert.assertTrue("The resource stock should match", (int) 6 == updatedResource.getStock());
        Assert.assertTrue("The resource price should match", (double) 119 == updatedResource.getPrice());


    }

    @Given("the lastest resource created wich the following details:")
    public void theLastestResourceCreatedWichTheFollowingDetails(DataTable dataTable) {
        Map<String, String> updateData = dataTable.asMaps().get(0);
        response = resourceRequest.getResources();
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        Optional<Resource> resourceOptional = resourceList.stream()
                .filter(r -> r.getName().equalsIgnoreCase(updateData.get("name"))
                        && r.getTags().equalsIgnoreCase(updateData.get("tags")))
                .findFirst();
        if (resourceOptional.isPresent()) {
            auxResource = resourceOptional.get();
        } else {
            throw new RuntimeException("Resource not found with the specified name and tags.");
        }
    }

    @When("I send a Delete request to delete the latest resource")
    public void iSendADeleteRequestToDeleteTheLatestResource() {
        if (auxResource == null) {
            log.info("No resource is available to delete. Ensure the resource was found in the previous step.");
        }
        response = resourceRequest.deleteResource(auxResource.getId());
        Assert.assertEquals(200, response.getStatusCode());
        log.info("Successfully delete the resource. Status code: {}", response.getStatusCode());
        log.info(response.jsonPath().prettify());

    }

    @And("verify that the resource was deleted")
    public void verifyThatTheResourceWasDeleted() {
        if (auxResource == null) {
            throw new RuntimeException("No resource was found to verify deletion. Ensure that a resource was created and deleted in previous steps.");
        }
        Response getResponse = resourceRequest.getResource(auxResource.getId()); // Asumiendo que tienes un método para obtener el recurso por ID
        if (getResponse.getStatusCode() != 404) {
            log.info("Resource was not deleted. Expected 404, but got: {}", getResponse.getStatusCode());
        }
    }
}

