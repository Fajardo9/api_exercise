package com.globant.api_tae.utils;

public final class Constants {
    public static final  String VALUE_CONTENT_TYPE       = "application/json";
    public static final  String CONTENT_TYPE             = "Content-Type";
    public static final  String CLIENTS_PATH             = "Clients";
    public static final  String RESOURCES_PATH           = "resources";
    public static final  String DEFAULT_CLIENT_FILE_PATH = "src/main/resources/data/defaultClient.json";
    public static final  String DEFAULT_RESOURCE_FILE_PAT= "src/main/resources/data/defaulResource.json";
    //public static final  String BASE_URL                 = "https://63dc0e8ca3ac95cec5b0bbcc.mockapi.io";
    public static final  String BASE_URL                 = "https://66e355c2494df9a478e4f6d0.mockapi.io";
    public static final  String URL                      = "/api/v1/%s";
    public static final  String URL_WITH_PARAM           = "/api/v1/%s/%s";
    public static final String FEATURES_PATH = "src/test/resources/features";
    public static final String PRETTY_PLUGIN = "pretty:target/cucumber/cucumber.txt";
    public static final String HTML_PLUGIN = "html:target/cucumber/report";
    public static final String JSON_PLUGIN = "json:target/cucumber.json";

    private Constants() {
    }
}
