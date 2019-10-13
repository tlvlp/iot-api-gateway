# IoT Server API Gateway - Internal API

The following endpoints are reserved for backend services with the BACKEND role.
Actual API endpoints are inherited from the project's [deployment repository](https://github.com/tlvlp/iot-server-deployment) via environment variables.

##  Incoming messages:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_INCOMING_MQTT_MESSAGE} |
| Method | POST |
| Content-Type | application/json |
| URL Parameters | None |
| Request Body | Message object (see sample below) |
| Success Response | Code: 202 ACCEPTED Contents: None |
| | Contents: None |
| Error Response | No specific error |
| Authorization | BACKEND |

Notes: 
1. Receives a message object from the MQTT Client service
2. Sends the message to Unit service for processing
3. Takes action based on the result type:
    - **error**: forwards the UnitLog to the subscribers
    - **inactive**: forwards the updated Unit to the subscribers
    - **status**: sends the updated modules to the reporting service and forwards the updated Unit to the subscribers 

Sample Request:
```bash
curl -X POST \
  -u username:password \
  http://0.0.0.0:8500/backend/mqtt/incoming \
  -H 'Content-Type: application/json' \
  -d '{
	"topic": "/global/status",
	"payload": {
		"name": "soil", 
		"relay|growlight": 0, 
		"project": "tlvlp.iot.BazsalikON", 
		"gl5528|lightPercent": "85", 
		"unitID": "tlvlp.iot.BazsalikON-soil", 
		"somo|soilMoisturePercent": "80"}
}'
```
##  Outgoing messages:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_OUTGOING_MQTT_MESSAGE} |
| Method | POST |
| Content-Type | application/json |
| URL Parameters | None |
| Request Body | Message object (see sample below) |
| Success Response | Code: 202 ACCEPTED Contents: None |
| | Contents: None |
| Error Response | No specific error |
| Authorization | BACKEND |

Notes: 
1. Receives a message object from a backend service
2. Sends the message to the MQTT Client service to be forwarded to the Units

Sample Request:
```bash
curl -X POST \
  -u username:password \
  http://0.0.0.0:8500/backend/mqtt/outgoing \
  -H 'Content-Type: application/json' \
  -d '{
	"topic": "/unit_control_topic_comes_here",
	"payload": { "relay|growlight": 0 }
}'
```


