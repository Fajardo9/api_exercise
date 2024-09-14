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

import java.util.List;
import java.util.Map;

public class ResourceSteps {
    private static final Logger log = LogManager.getLogger(ResourceRequest.class);
    private final ResourceRequest resourceRequest = new ResourceRequest();
    private Response response;

    @Given("there are not enough resources for the test")
    public void thereAreNotEnoughResourcesForTheTest() {
        response = resourceRequest.getResources();
        List<Resource> resources = resourceRequest.getResourcesEntity(response); // Obtener la lista de recursos
        log.info("Current resources: {}", resources.size());

        Assert.assertTrue("There should be less than 20 resources, but found: " + resources.size(), resources.size() < 20);
    }

    @When("I create the following inactive resources:")
    public void iCreateTheFollowingInactiveResources(DataTable inactiveResourceData) {
        // Obtener recursos existentes
        response = resourceRequest.getResources();
        List<Resource> resources = resourceRequest.getResourcesEntity(response);

        //log.info("Current resources count: {}", resources.size());

        // Verificar si hay menos de 5 recursos
        if (resources.size() < 10) {
            // Crear recursos inactivos a partir de la DataTable
            List<Map<String, String>> resourceData = inactiveResourceData.asMaps(String.class, String.class);
            for (Map<String, String> resource : resourceData) {
                Resource inactiveResource = Resource.builder()
                        .name(resource.get("Name"))
                        .trademark(resource.get("Trademark"))
                        .stock(Integer.parseInt(resource.get("Stock")))
                        .price(Double.parseDouble(resource.get("Price")))
                        .description(resource.get("Description"))
                        .tags(resource.get("Tags"))
                        .active(false)
                        .build();

                // Crear el recurso
                response = resourceRequest.createResource(inactiveResource);
                //log.info("Inactive resource created: {}", response.jsonPath().prettify());
                //Assert.assertEquals(201, response.statusCode()); // Verificación de creación exitosa
                //log.info("Response from getResources: {}", response.getBody().asString());
            }

            // Volver a obtener la lista de recursos después de la creación
            response = resourceRequest.getResources();
            log.info("Response from getResources: {}", response.getBody().asString());
            //resources = resourceRequest.getResourcesEntity(response);
            //log.info("Response from getResources: {}", response.getBody().asString());
           // log.info("New resources count after creation: {}", resources.size());
        } else {
            log.info("There are already enough resources ({}), no need to create more.", resources.size());
            log.info("Response from getResources: {}", response.getBody().asString());
        }

    }

    @And("I create the following active resources:")
    public void iCreateTheFollowingActiveResources(DataTable activeResourceData) {
        List<Map<String, String>> resources = activeResourceData.asMaps(String.class, String.class);
        for (Map<String, String> resource : resources) {
            Resource inactiveResource = Resource.builder()
                    .name(resource.get("Name"))
                    .trademark(resource.get("Trademark"))
                    .stock(Integer.parseInt(resource.get("Stock")))
                    .price(Double.parseDouble(resource.get("Price")))
                    .description(resource.get("Description"))
                    .tags(resource.get("Tags"))
                    .active(true)
                    .build();
            response = resourceRequest.createResource(inactiveResource);
            log.info("Inactive resource created: {}", response.jsonPath().prettify());
        }
        log.info("Ther quantity of Active resources generated is: {}", resourceRequest.getResourcesEntity(response).size());
    }

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
    public void verifyThatTheProductsHasBeenCreathed() {
        response = resourceRequest.getResources();
        log.info("Available Resources: {}", response.jsonPath().prettify());
    }


}
