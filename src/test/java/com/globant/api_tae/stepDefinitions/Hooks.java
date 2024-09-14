package com.globant.api_tae.stepDefinitions;

import com.globant.api_tae.models.Resource;
import com.globant.api_tae.requests.ResourceRequest;
import com.globant.api_tae.utils.Constants;
import io.cucumber.java.After;
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
    private Response response;

    @Before
    public void testStart(Scenario scenario) {
        log.info("*****************************************************************************************");
        log.info("	Scenario: " + scenario.getName());
        log.info("*****************************************************************************************");
        RestAssured.baseURI = Constants.BASE_URL;
    }
    @Before
    public void setupActiveResources(){
        response = resourceRequest.getResources();
        List<Resource> resourcesList = resourceRequest.getResourcesEntity(response);
        long activeResources = resourcesList.stream().map(Resource::isActive).count();
        int desiredResources = 5;
        if ( activeResources <= desiredResources) {
            for (int i = Math.toIntExact(activeResources); i < desiredResources; i++){
                Resource resource = Resource.builder()
                        .name("Resource " + i)
                        .trademark("Trademark " + i)
                        .stock(i)
                        .price(i+12.0)
                        .description("Descripcion" + i)
                        .tags("Tags" + i)
                        .active(true)
                        .id("1")
                        .build();
                Response createResponse = resourceRequest.createResource(resource);
                log.info(createResponse.jsonPath().prettify());
                log.info("The status code is: {}", createResponse.statusCode());
            }
        } else {
            log.info("There are the following quantity of resources: {}", resourcesList.size());
        }
    }

    @After
    public void cleanUp(Scenario scenario){
        log.info("*****************************************************************************************");
        log.info("	Scenario finished: " + scenario.getName());
        log.info("*****************************************************************************************");

    }

    public void cleanUpData(String phone){

    }
}
