package com.globant.api_tae.stepDefinitions;

import com.globant.api_tae.models.Resource;
import com.globant.api_tae.requests.ResourceRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Optional;

public class ResourceSteps {
    private static final Logger log = LogManager.getLogger(ResourceSteps.class);
    private final ResourceRequest resourceRequest = new ResourceRequest();
    private Response response;

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int responseCode) {
        Assert.assertEquals(responseCode, response.statusCode());
    }

    @And("the response body should match the resource creation schema")
    public void theResponseBodyShouldMatchTheResourceCreationSchema() {
        String path = "schemas/resourceSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        log.info("Successfully Validated schema from Client object");
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
        log.info("The number ofo active resources are: {}", activeResources);
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
            if (resource.isActive()) {
                resource.setActive(false);
                Response updateResponse = resourceRequest.updateResource(resource, resource.getId());
                Assert.assertEquals(200, updateResponse.statusCode());
                log.info("Resource updated to inactive: {}", resource.getName());
            }
        }
    }


    @Given("there are all the products marked as inactive")
    public void thereAreAllTheProductsMarkedAsInactive() {
        response = resourceRequest.getResources();
        resourceRequest.getResourcesEntity(response);
        List<Resource> resourceList;
        response = resourceRequest.getResources();
        resourceList = resourceRequest.getResourcesEntity(response);
        boolean allInactive = resourceList.stream().allMatch(resource -> !resource.isActive());

        Assert.assertTrue("Not all resources are inactive.", allInactive);
        log.info("All resources are confirmed to be inactive.");

    }

    @When("I send a GET request to view all the resources")
    public void iSendAGETRequestToViewAllTheResources() {
        response = resourceRequest.getResources();
        log.info("Response received: {}", response.asString());
    }

    @Given("a new client to create")
    public void aNewClientToCreate() {
        response = resourceRequest.getResources();
    }


    @And("verify that all the resources are marked as inactive")
    public void verifyThatAllTheResourcesAreMarkedAsInactive() {
        response = resourceRequest.getResources();
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        boolean allInactive = resourceList.stream().noneMatch(Resource::isActive);
        Assert.assertTrue("Not all resources are inactive.", allInactive);
        log.info("All resources are confirmed to be inactive.");
    }
}
