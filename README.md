# IoT Server API Gateway

## Service
Part of the tlvlp IoT project's server side microservices.

This Dockerized SpringBoot-based service is responsible providing an API gateway to the other microservices:
- Expose API endpoints for higher level interaction with the services
- Expose API for user management
- Provide user profiles based authentication and authorization for the endpoints

## Building and publishing JAR + Docker image
This project is using the using the [Palantir Docker Gradle plugin](https://github.com/palantir/gradle-docker).
All configuration can be found in the [Gradle build file](build.gradle) file 
and is recommended to be run with the docker/dockerTagsPush task.

## Deployment
- This service is currently designed as **stateless** and can have an arbitrary number of instances running per Docker Swarm Stack.
- For settings and deployment details see the project's [deployment repository](https://gitlab.com/tlvlp/iot.server.deployment)

## Server-side API
Actual API endpoints are inherited from the project's [deployment repository](https://gitlab.com/tlvlp/iot.server.deployment) via environment variables.


### INTERNAL - POST Incoming messages:

1. Receives a message object from the MQTT Client service
2. Sends the message to Unit service for processing
3. Takes action based on the result type:
    - **error**: forwards the UnitLog to the subscribers
    - **inactive**: forwards the updated Unit to the subscribers
    - **status**: sends the updated modules to the reporting service and forwards the updated Unit to the subscribers 


#### Related environment variables:
- ${API_GATEWAY_API_INCOMING_MQTT_MESSAGE}

#### Input:
RequestBody: Message object - the service is agnostic of the contents

#### Output:
Acknowledgement Http response (202)


### GET All Units:

Returns All the Units from the database

#### Related environment variables:
- ${API_GATEWAY_API_GET_ALL_UNITS}

#### Input:
Takes no input

#### Output:
A list of Unit objects

### GET Unit By ID:

Returns the requested Unit

#### Related environment variables:
- ${API_GATEWAY_API_GET_UNIT_BY_ID}

#### Input:
RequestParam
- **unitID**: String - the unique ID of the unit

#### Output:
A Unit object or 404 Not Found


### POST Request Global Unit Status:

1. Gets a global status request message from the Unit service
2. Sends the message to the the units via the MQTT Client service
3. All active units should respond with their status update (processed separately)

#### Related environment variables:
- ${API_GATEWAY_API_REQUEST_GLOBAL_UNIT_STATUS}

#### Input:
Takes no input

#### Output:
Acknowledgement: Http response (202)


### POST Control Unit Module:

1. Sends a module object to the Unit Service
2. Gets a Unit and Module specific control message from the Unit service
3. Sends the message to the the unit via the MQTT Client service
4. The unit should respond by performing the requested change and sending a status update (processed separately)

#### Related environment variables:
- ${API_GATEWAY_API_UNIT_MODULE_CONTROL}

#### Input:
RequestBody:
- **moduleID**: String - module ID
- **value**: Double - requested value/state of the Module
- **unitID**: String - ID of the containing Unit

```
{
    "moduleID": "relay|growlight",
    "value": 1,
    "unitID": "tlvlp.iot.BazsalikON-soil"
}

```

#### Output:
Acknowledgement: Http response (202)



### POST Add Scheduled Event to Unit:

1. Posts the received event details to the Scheduler Service
2. Receives an eventID for the created/updated event
3. Calls the Unit service to add the eventID to the list of events for the given Unit

#### Related environment variables:
- ${API_GATEWAY_API_ADD_SCHEDULED_EVENT_TO_UNIT}

#### Input:
RequestBody:
- **unitID**: String - ID of the containing Unit
- **event**: ScheduledEvent details

```
{
    "unitID": "tlvlp.iot.BazsalikON-soil"
    "event": {...}
}

```

#### Output:
The updated Unit

