# Central system

## How to run

- Run docker desktop
- cd Into project directory and run from terminal `./gradlew bootRun`
- Initialize data for database
  - Use any sql client and connect via `psql postgresql://myuser:secret@localhost:5432/mydatabase`
  - execute sql from file `src\main\resources\init-data.sql`

## How to use the app

- Make api call for start charging
```
   curl --location 'http://localhost:8088/charge-transactions' \
    --header 'Content-Type: application/json' \
    --data '{
    "stationId": "2b17a6ab-cbca-40c0-a2dd-e4123dc30d03",
    "driverId": "2b17a6ab-cbca-40c0-a2dd-e4123dc30d02",
    "callBackUrl": "https://jsonplaceholder.typicode.com/posts"
    }'
```
- This returns 202 ACCEPTED with following json 
``` 
{
    "message": "Request is being processed asynchronously. The result will be sent to the provided callback URL.",
    "status": "ACCEPTED"
}
```
- Given invalid input API returns error like following
``` 
{
    "errors": {
        "driverId": "[ driverId ] must be between 20 and 80 char can only have letters numbers and only following special symbol [- . _ ~]"
    },
    "code": "INVALID_INPUT",
    "timestamp": "2025-05-22T11:09:14.314372Z"
}
```

## Application design

- A controller (ChargeTransactionController) receive the initial request for start changing
- Given input is valid it returns 202 ACCEPTED immediately
- Another thread send the request body to a RabbitMQ queue using producer (ChargeTransactionMessageProducer)
- A consumer (ChargeTransactionMessageConsumer) consumes this message
- Checks the database to see if driver is allowed to charge on the requested charge point
- Sends the decision via the callback URL received in initial API call (CallbackService)

![design.png](design.png)

## Technical design aspect

- App is created with Kotlin, Spring Boot, RabbitMQ an Postgres Database with JDK 21
- Cloud event spec (https://cloudevents.io/) is used for MQ message for future integration and compatibility 
- App is using virtual thead with helps better system performance
- The entire flow of the app is Async. No thread is blocked at any point of execution
- Proper input validation is implemented
- Flyway used to migrate DDL properly
- DLQ used in queue to handle unknown driver or station or their association
- DLQ can be processed later depending on business need
- App is written with clean and testable code in mind
- All business logic is properly tested

## Future improvements

- Given the expected time to finish to task in 5 hours, following things can be improved with more time
- Better database design e.g. include User, Driver, ChargePoint table
- Retry item from DLQ
- More granular exception handling
- More column in DB table can be added e.g. DateCreated, DateModified
- Validation can be added e.g. When one changing session is ongoing another session will be denied











