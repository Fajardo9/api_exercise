# API Rest-assure Final Exercise
This project is an automation exercise for API testing, designed to integrate the Gherkin language and implement Cucumber with Rest-Assured. Withh this exercise, you will practice how to write behavior-driven tests for RESTful services, making your code easier to understand.

- Page: [mockapi.io]([https://63b6dfe11907f863aa04ff81.mockapi.io/api/v1/])
.
## Requires
- JDK 11 or higher (JDK 17 is already configured inside the `POM.xml` file)
- Maven
### Environment used
- Windows 11
- IntellijIdea v. 2024.2.1
## Installation Instructions
1. Clone this repository into your local machine
2. Navigate to the root directory where was cloned the repository (e.g. *C:\users\documents\api_exercise*)
3. Run the following command:
   `mvn install`
### Execution Instructions
1. Execute one of the suites inside the root directory of the project `src/test/java/com/globant/api_tae/runner'
  - To execute the Change the phone number of the first Client named Laura test case: TestRunnerChangingNumber.java
  - To execute the Get the list of active resource test case: TestRunnerClientManagement.java
  - To execute the Update and delete a New Client test case: TestRunnerResourceManagement.java
  - To execute the Update the last created resource test case: TestRunnerResourceUpdated.java
  - To execute a smoke test: TestRunner.java


2. Verify the results of the test selected and its corresponding logs

# License 
-This project is distribuited under the e.g. "globant" license
