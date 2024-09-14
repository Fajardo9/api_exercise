package com.globant.api_tae.stepDefinitions;

import com.globant.api_tae.models.Client;
import com.globant.api_tae.models.Resource;
import com.globant.api_tae.requests.ClientRequest;
import com.globant.api_tae.requests.ResourceRequest;
import com.globant.api_tae.utils.Constants;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Hooks {
    private static final Logger log = LogManager.getLogger(Hooks.class);
    private static final ResourceRequest resourceRequest = new ResourceRequest();
    private static final ClientRequest clientRequest = new ClientRequest();

    @Before
    public void setupActiveResources(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("resour")) {
            Response resourceResponse = resourceRequest.getResources();
            if (resourceResponse.getStatusCode() != 200) {
                log.error("Failed to get resources. Status code: {}. Response: {}", resourceResponse.getStatusCode(), resourceResponse.getBody().asString());
                throw new RuntimeException("Failed to get resources: " + resourceResponse.getStatusLine());
            }

            List<Resource> resourcesList = resourceRequest.getResourcesEntity(resourceResponse);
            long activeResources = resourcesList.stream().filter(Resource::isActive).count();
            int desiredResources = 15;
            log.info("The actual active resources are: {}", activeResources);

            if (activeResources <= desiredResources) {
                for (int i = Math.toIntExact(activeResources); i <= desiredResources + 1; i++) {
                    Resource resource = Resource.builder()
                            .name("Resource " + i)
                            .trademark("Trademark " + i)
                            .stock(i)
                            .price(i + 12.0)
                            .description("Descripcion" + i)
                            .tags("Tags" + i)
                            .active(true)
                            .id("2")
                            .build();
                    Response createResponse = resourceRequest.createResource(resource);
                    log.info("The status code is: {}", createResponse.statusCode());
                }
            }
        }
        if (scenario.getSourceTagNames().contains("clien")){
            int desiredClients = 5;
            Response clientResponse = clientRequest.getClients();
            List<Client> clientList = clientRequest.getClientsEntity(clientResponse);
            if (clientList.isEmpty() || clientList.size() <= desiredClients - 1) {
                for (int i = clientList.size(); i < desiredClients; i++) {
                    Client client = Client.builder()
                            .name("Client " + i)
                            .lastName("Client " + i)
                            .country("Country " + i)
                            .city("City " + i)
                            .email("Email " + i)
                            .phone("Phone " + i)
                            .build();
                    Response createclientResponse = clientRequest.createClient(client);
                    log.info(createclientResponse.jsonPath().prettify());
                    log.info("The status code is: {}", createclientResponse.statusCode());
                }
            } else {
                log.info("Ther are the following quantity of clients: {}", clientList.size());
            }

        }
    }

    @Before
    public void testStart(Scenario scenario) {
        log.info("*****************************************************************************************");
        log.info("	Scenario: {} ", scenario.getName());
        log.info("*****************************************************************************************");
        RestAssured.baseURI = Constants.BASE_URL;
    }


    @After
    public void cleanUp(Scenario scenario) {
        log.info("*****************************************************************************************");
        log.info("	Scenario finished: {}" , scenario.getName());
        log.info("*****************************************************************************************");
    }

    // This will run after all tests
    @AfterAll
    public static void cleanUpDataResources() {
        log.info("Cleaning up resources after all scenarios...");
        Response response = resourceRequest.getResources();
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        resourceList.stream()
                .filter(r -> r.getName().contains("Resource"))
                .forEach(r -> {
                    log.info("Deleting resource with ID: {}", r.getId());
                    Response deletingResponse = resourceRequest.deleteResource(r.getId());
                    log.info("Status response: {}", deletingResponse.statusCode());
                });
    }
}