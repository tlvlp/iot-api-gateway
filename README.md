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

