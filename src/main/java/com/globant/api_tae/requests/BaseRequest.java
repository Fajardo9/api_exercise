package com.globant.api_tae.requests;

import com.globant.api_tae.utils.Constants;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * The BaseRequest class provides general methods to perform HTTP requests
 * using the RestAssured library. It includes methods for GET, POST, PUT, PATCH, and DELETE
 * requests, as well as creating base headers and validating JSON schema.
 */
public class BaseRequest {
    /**
     * This is a function to read elements using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     * @return Response
     */
    protected Response requestGet(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .when()
                .get(endpoint);
    }

    /**
     * This is a function to create a new element using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     * @param body     model object
     * @return Response
     */
    protected Response requestPost(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .body(body)
                .when()
                .post(endpoint);
    }

    /**
     * This is a function to create an element using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     * @param body     model object
     * @return Response
     */
    protected Response requestPut(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .body(body)
                .when()
                .put(endpoint);
    }

    /**
     * This is a function to update an element using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     * @param body     model object
     * @return Response
     */
    protected Response requestPatch(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .body(body)
                .when()
                .patch(endpoint);
    }


    /**
     * This is a function to delete an element using rest-assured
     *
     * @param endpoint api url
     * @param headers  a map of headers
     * @return Response
     */
    protected Response requestDelete(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .when()
                .delete(endpoint);
    }

    /**
     * This is a function to create a default header map object
     *
     * @return default headers
     */
    protected Map<String, String> createBaseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);
        return headers;
    }

    public boolean validateSchema(Response response, String schemaPath) {
        try {
            response.then()
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            return true;
        } catch (AssertionError e) {
            throw new AssertionError("Schema invalid"+ e.getMessage());
        }
    }
}
