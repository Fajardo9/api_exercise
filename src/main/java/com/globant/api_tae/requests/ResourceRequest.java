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
     * Get Resources list
     *
     * @return rest-assured response
     */
    public Response getResources() {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Get Resource by id
     *
     * @param resourceId string
     * @return rest-assured response
     */
    public Response getResource(String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Create Resource
     *
     * @param resource model
     * @return rest-assured response
     */
    public Response createResource(Resource resource) {
        endpoint = String.format(Constants.URL,Constants.RESOURCES_PATH);
        return requestPost(endpoint, createBaseHeaders(), resource);
    }

    /**
     * Update resource by id
     *
     * @param resource   model
     * @param resourceId string
     * @return rest-assured response
     */
    public Response updateResource(Resource resource, String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestPatch(endpoint, createBaseHeaders(), resource);
    }

    /**
     * Delete resource by id
     *
     * @param resourceId string
     * @return rest-assured response
     */
    public Response deleteResource(String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestDelete(endpoint, createBaseHeaders());
    }

    public Resource getResourceEntity(@NotNull Response response) {
        return response.as(Resource.class);
    }

    public List<Resource> getResourcesEntity(@NotNull Response response) {
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
