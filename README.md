# kafka_poc
**Demo Kafka connector project**

1. Clone the repository to local.
2. clean install maven.
3. build the project.
4. Start Zookeeper
5. Start kafka
6. Run the spring boot application.

**Run the KafkaIntegrationTest.Java using <mvn test> command.**
In this test i'm validating if the consumed message contain the substring.

Output:

Produced message to Kafka: Post{id=1, title='sunt aut facere repellat provident occaecati excepturi optio 
reprehenderit', body='quia et suscipit
suscipit recusandae consequuntur expedita et cum
reprehenderit molestiae ut ut quas totam
nostrum rerum est autem sunt rem eveniet architecto'}.

The test case will pass.

I'm fetching a single record and testing for validation just to see the data is produced

Expected Behavior
•	ScheduledTaskService should send a valid message to Kafka.
•	The consumer should retrieve the message successfully.
•	The message should contain required fields.

**ScheduledTaskService (Service Class)**
•	Purpose: Fetches data from an external source (API, database, or static data) and sends it to a Kafka topic.
•	Key Features:
•	Uses @Scheduled to trigger data retrieval at fixed intervals.
•	Calls a data source (could be an API call, database query, or mock data).
•	Sends the fetched data to a Kafka topic using KafkaTemplate<String, String>.
•	Logs the messages for monitoring.

**JsonPlaceholderClient**

This interface defines a Feign Client to communicate with an external REST API (jsonplaceholder). It fetches a list of 
posts from a remote service.

**Steps for Integration test**

This test case verifies the end-to-end functionality of a Kafka producer and consumer within a Spring Boot application.
It ensures that messages produced by ScheduledTaskService are successfully sent to and consumed from an embedded Kafka 
topic.

**FeignConfig (Feign Configuration Class)**

The FeignConfig class is a configuration class for customizing Feign client behavior. It specifically customizes the 
error handling mechanism for Feign requests.

**ApiException (Custom Exception Class)**

The ApiException class is a custom exception that extends RuntimeException. It is designed to represent errors or 
issues occurring during API calls, typically thrown when the API response is not as expected (e.g., non-200 status code).

**KafkaProducerService (Service Class)**

The KafkaProducerService class is responsible for sending messages to a Kafka topic. It takes a Post object, converts 
it to a string (in this case, by calling toString()), and sends it to a specified Kafka topic.

KafkaIntegrationTest (Integration Test for Kafka Producer)

This is an integration test designed to verify the end-to-end functionality of Kafka message production
in the application. It ensures that data fetched by the ScheduledTaskService is successfully sent to Kafka and can be 
consumed by a Kafka consumer.

**Run the KafkaIntegrationTest.Java using <mvn test> command.**

Expected Behavior
•	ScheduledTaskService should send a valid message to Kafka.
•	The consumer should retrieve the message successfully.
•	The message should contain required fields.

