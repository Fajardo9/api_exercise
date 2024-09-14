package com.globant.api_tae.stepDefinitions;

import com.globant.api_tae.models.Client;
import com.globant.api_tae.requests.ClientRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * The {@code ClientModificationSteps} class provides the step definitions for
 * modifying client data in a test suite. It uses REST API calls to interact with
 * client data and perform various operations like creating, updating, verifying
 * and deleting client information.
 */
public class ClientModificationSteps {
    private static final Logger log = LogManager.getLogger(ClientModificationSteps.class);
    private final ClientRequest clientRequest = new ClientRequest();
    private Response response;
    private Client auxClient;
    private String auxNumber;
    protected Boolean clientWasCreated = false;
    private Boolean thereWasAtLeastTenClients = false;


    /**
     * Sends a GET request to retrieve a list of all clients.
     */
    @Given("I send a GET request to view all the clients")
    public void iSendAGETRequestToViewAllTheClient() {
        response = clientRequest.getClients();
    }

    /**
     * Receive the response of the request and validates that is Ok(200)
     */
    @When("receive the response")
    public void receiveTheResponse() {
        log.info(response.jsonPath().prettify());
        log.info("The status code is: {}", response.statusCode());
        assertEquals(200, response.statusCode());
    }

    /**
     * Creates missing clients if the current number of clients is less than the desired number.
     * {@code @Annotation} The Clients generated will have the name and lastName as "Client" by defect
     *
     * @param desiredClients The desired number of clients.
     */
    @Then("create the missing clients if there are less than {int} clients")
    public void createTheMissingClientsIfThereAreLessThanClients(int desiredClients) {
        List<Client> clientList = clientRequest.getClientsEntity(response);
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
                Response createResponse = clientRequest.createClient(client);
                log.info(createResponse.jsonPath().prettify());
                log.info("The status code is: {}", createResponse.statusCode());
            }
        } else {
            log.info("Ther are the following quantity of clients: {}", clientList.size());
            thereWasAtLeastTenClients = true;
        }
    }

    /**
     * Verifies that the number of clients in the system is at least the expected count.
     *
     * @param expectedCount the minimum number of clients expected
     */
    @Then("verify that the number of clients is {int}")
    public void verifyThatTheNumberOfClientsIs(int expectedCount) {
        response = clientRequest.getClients();
        List<Client> clientList = clientRequest.getClientsEntity(response);
        int actualCount = clientList.size();
        assertTrue("The number of clients does not match the expected count. " + expectedCount + " " + actualCount, expectedCount <= actualCount);
    }

    /**
     * Verifies that there is a client with the specified name.
     *
     * @param nameClient the name of the client to be verified
     */
    @Then("Verify that there is a client with the name {string}")
    public void verifyThatThereIsAClientWithTheName(String nameClient) {
        response = clientRequest.getClients();
        List<Client> clientList = clientRequest.getClientsEntity(response);

        Optional<Client> optionalClient = clientList.stream()
                .filter(client -> client.getName().equalsIgnoreCase(nameClient))
                .findFirst();

        if (optionalClient.isPresent()) {
            log.info("The client with the name '{}' exists.", nameClient);
            auxClient = optionalClient.get();
        } else {
            log.info("The client with the name '{}' does not exist.", nameClient);
        }
    }

    /**
     * Checks if there is a client with the name Laura.
     * If the client does not exist, creates a new client with the provided data.
     *
     * @param clientData DataTable containing the following client information:
     *                   Name, LastName, Country, City, Email, Phone, and ID.
     */

    @Then("if there is not a client with the name Laura, create a new client with the following data:")
    public void ifThereIsNotAClientWithTheNameLauraCreateANewClientWithTheFollowingData(@NotNull DataTable clientData) {
        Map<String, String> clientDataMap = clientData.asMaps().get(0);
        if (auxClient == null) {
            response = clientRequest.createClient(Client.builder()
                    .name(clientDataMap.get("Name"))
                    .lastName(clientDataMap.get("LastName"))
                    .country(clientDataMap.get("Country"))
                    .city(clientDataMap.get("City"))
                    .email(clientDataMap.get("Email"))
                    .phone(clientDataMap.get("Phone")).id(clientDataMap.get("Id")).build());
            log.info(response.statusCode());
            log.info(response.jsonPath().prettify());
            Assert.assertEquals(201, response.statusCode());
            clientWasCreated = true;
        } else {
            log.info("Client already exists with data: Name - {}, LastName - {}, Country - {}, City - {}, Email - {}, Phone - {}",
                    auxClient.getName(), auxClient.getLastName(), auxClient.getCountry(), auxClient.getCity(), auxClient.getEmail(), auxClient.getPhone());
        }

    }

    /**
     * Changes the phone number of a client to the provided by the user
     * and save the old number in auxiliary variable.
     *
     * @param phoneNumber the new phone number to be set for the client
     */
    @Then("save the existent phone number and change it to {string}")
    public void saveTheExistentPhoneNumberAndChangeItTo(String phoneNumber) {
        auxNumber = auxClient.getPhone();
        auxClient.setPhone(phoneNumber);
        response = clientRequest.updateClient(auxClient, auxClient.getId());
        Assert.assertEquals(200, response.statusCode());
        log.info(response.statusCode());
    }

    /**
     * Verifies that the phone number of a client has been changed correctly to the specified new phone number.
     *
     * @param newPhoneNumber The new phone number to verify against the client's updated phone number.
     */
    @Then("verify that the phone number has been changed correctly to {string}")
    public void verifyThatThePhoneNumberHasBeenChangedCorrectlyTo(String newPhoneNumber) {
        response = clientRequest.getClient(auxClient.getId());
        //og.info("Are different the phone numbers?{}, {}", auxNumber, response.jsonPath().getString("phone"));
        assertNotEquals(auxNumber, response.jsonPath().getString("phone"));
        assertEquals(newPhoneNumber, response.jsonPath().getString("phone"));
        log.info(response.jsonPath().prettify());

    }

    @Given("Verify if any data was created")
    public void verifyIfAnyDataWasCreated() {
        if (clientWasCreated) {
            log.info("The client was created: {}", auxClient.getName());
        } else if (!thereWasAtLeastTenClients) {
            response = clientRequest.getClients();
            List<Client> clientList = clientRequest.getClientsEntity(response);
            log.info("The number of clients created is: {} ",
                    clientList.stream().filter(c -> c.getName().contains("Client")).count());
        }
    }

    /**
     * Checks if a client with the specified name and phone number already exists.
     * If the client exists, rolls back her phone number to a previous state.
     *
     * @param nameClient the name of the client whose existence is being checked
     * @param lastName the phone number of the client being checked
     */
    @When("I verify if the client {string} with the {string} previously exists, rollback her phone number")
    public void ifTheClientAlreadyExistsRollbackHerPhoneNumber(String nameClient, String lastName) {
        if (!clientWasCreated && auxClient != null) {
            List<Client> clientList = clientRequest.getClientsEntity(response);
            Optional<Client> optionalClient = clientList.stream()
                    .filter(client -> client.getName().equalsIgnoreCase(nameClient) && client.getLastName().equalsIgnoreCase(lastName))
                    .findFirst();
            optionalClient.ifPresent(client -> auxClient = client);
            auxClient.setPhone(auxNumber);
            response = clientRequest.updateClient(auxClient, auxClient.getId());
            Assert.assertEquals(200, response.statusCode());
            log.info(response.statusCode());
            Assert.assertEquals(auxNumber, response.jsonPath().getString("phone"));
            log.info(response.jsonPath().prettify());
        }
    }

    @Then("delete all created data.")
    public void deleteAllCreatedData() {
        List<Client> clientList = clientRequest.getClientsEntity(response);
        Optional<Client> optionalClient = clientList.stream()
                .filter(client -> client.getName().equalsIgnoreCase("Laura"))
                .findFirst();
        if(optionalClient.isPresent() && clientWasCreated) {
            auxClient = optionalClient.get();
            clientRequest.deleteClient(auxClient.getId());
            log.info("The client was deleted: {}", auxClient.getName());
            log.info(response.jsonPath().prettify());
            Assert.assertEquals(200, response.statusCode());
            Assert.assertEquals(auxClient.getId(), response.jsonPath().getString("id"));
        }
        if (!thereWasAtLeastTenClients) {
            clientList.stream()
                    .filter(c -> c.getName().contains("Client"))
                    .forEach(c -> {
                        log.info("Deleting client with ID: {}", c.getId());
                        clientRequest.deleteClient(c.getId());
                    });
        }
    }

    @Then("validates the response with client JSON schema")
    public void userValidatesResponseWithClientJSONSchema() {
        String path = "schemas/clientSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        log.info("Successfully Validated schema from Client object");
    }

    @Then("validates the response with client list JSON schema")
    public void userValidatesResponseWithClientListJSONSchema() {
        String path = "schemas/clientListSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        log.info("Successfully Validated schema from Client List object");
    }
}