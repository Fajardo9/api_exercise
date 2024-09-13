package com.globant.api_tae.requests;

import com.globant.api_tae.models.Resource;
import com.globant.api_tae.utils.Constants;
import com.globant.api_tae.utils.JsonFileReader;
import com.google.gson.Gson;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResourceRequest extends BaseRequest {
    private String endpoint;

    /**
     * Get Client list
     * @return rest-assured response
     */
    public Response getResources() {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Get client by id
     * @param clientId string
     * @return rest-assured response
     */
    public Response getResourceById(String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Create resource
     * @param resource model
     * @return rest-assured response
     */
    public Response createResource(Resource resource) {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestPost(endpoint, createBaseHeaders(), resource);
    }

    /**
     * Update resource by id
     * @param resource model
     * @param resourceId string
     * @return rest-assured response
     */
    public Response updateResource(Resource resource, String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, resourceId);
        return requestPut(endpoint, createBaseHeaders(), resource);
    }

    /**
     * Delete client by id
     * @param resource string
     * @return rest-assured response
     */
    public Response deleteResource(String resource) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, resource);
        return requestDelete(endpoint, createBaseHeaders());
    }

    public Resource getResourceEntity(@NotNull Response response) {
        return response
                .as(Resource.class);
    }

    public List<Resource> getResourceEntities(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Resource.class);
    }

    public Response createDefaultResource() {
        JsonFileReader jsonFile = new JsonFileReader();
        return this.createResource(jsonFile.getResourceByJson(Constants.DEFAULT_RESOURCE_FILE_PAT));
    }

    public Resource getResourceEntity(String resourceJson) {
        Gson gson = new Gson();
        return gson.fromJson(resourceJson, Resource.class);
    }

}
