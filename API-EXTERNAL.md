# IoT Server API Gateway - External API

The following endpoints are part of the the core API served by this gateway and accessible with the USER role.
Actual API endpoints are inherited from the project's [deployment repository](https://github.com/tlvlp/iot-server-deployment) via environment variables.

## Get All Units:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_GET_ALL_UNITS} |
| Method | GET |
| Content-Type | application/json |
| URL Parameters | None |
| Request Body | None |
| Success Response | Code: 200 OK Contents: List of Units (see below) |
| | Contents: None |
| Error Response | Code: No specific error |
| Authorization | USER |

Sample Request:
```bash
curl -X GET \
  -u username:password \
  http://0.0.0.0:8500/units \
  -H 'Content-Type: application/json'
```

Sample output:
```json
[
    {
        "unitID": "tlvlp.iot.BazsalikON-soil",
        "name": "soil",
        "project": "tlvlp.iot.BazsalikON",
        "active": true,
        "controlTopic": "/units/tlvlp.iot.BazsalikON-soil/control",
        "lastSeen": "2019-10-01T12:28:12.113",
        "modules": [
            {
                "unitID": "tlvlp.iot.BazsalikON-soil",
                "moduleID": "gl5528|lightPercent",
                "value": 85.0
            }
        ],
        "scheduledEvents": []
    }
]
```


## Get Unit By ID:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_GET_UNIT_BY_ID} |
| Method | GET |
| Content-Type | application/json |
| URL Parameters | unitID: String - the Unit ID |
| Request Body | None |
| Success Response | Code: 200 OK  |
| | Contents: The requested unit (see below) |
| Error Response | Code: 404 Not found |
| | Contents: Error details |
| Authorization | USER |

Sample Request:
```bash
curl -X GET \
  -u username:password \
  'http://0.0.0.0:8500/units/id?unitID=tlvlp.iot.BazsalikON-soil' \
  -H 'Content-Type: application/json'

```
Sample output:
```json
{
    "unitID": "tlvlp.iot.BazsalikON-soil",
    "name": "soil",
    "project": "tlvlp.iot.BazsalikON",
    "active": true,
    "controlTopic": "/units/tlvlp.iot.BazsalikON-soil/control",
    "lastSeen": "2019-10-01T12:28:12.113",
    "modules": [
        {
            "unitID": "tlvlp.iot.BazsalikON-soil",
            "moduleID": "gl5528|lightPercent",
            "value": 85.0
        }
    ],
    "scheduledEvents": []
}
```

## Request Global Unit Status:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_REQUEST_GLOBAL_UNIT_STATUS} |
| Method | POST |
| Content-Type | application/json |
| URL Parameters | None |
| Request Body | None |
| Success Response | Code: 202 ACCEPTED |
| | Contents: None |
| Error Response | No specific error |
| Authorization | USER |

Notes:
1. Gets a global status request message from the Unit service
2. Sends the message to the the units via the MQTT Client service
3. All active units should respond with their status update (processed separately)

Sample Request:
```bash
curl -X POST \
  -u username:password \
  http://0.0.0.0:8500/units/request \
  -H 'Content-Type: application/json' 
```

## Control Unit Module:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_UNIT_MODULE_CONTROL} |
| Method | POST |
| Content-Type | application/json |
| URL Parameters | None |
| Request Body | Updated Module object (see below) |
| Success Response | Code: 202 ACCEPTED |
| | Contents: None |
| Error Response | No specific error |
| Authorization | USER |

Notes:
1. Sends a module object to the Unit Service
2. Gets a Unit and Module specific control message from the Unit service
3. Sends the message to the the unit via the MQTT Client service
4. The unit should respond by performing the requested change and sending a status update (processed separately)

Sample Request:
```bash
curl -X POST \
  -u username:password \
  http://0.0.0.0:8500/units/modules/control \
  -H 'Content-Type: application/json' \
  -d '{
    "moduleID": "relay|growlight",
    "value": 1,
    "unitID": "tlvlp.iot.BazsalikON-soil"
}'
```

## Add Scheduled Event to Unit:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_ADD_SCHEDULED_EVENT_TO_UNIT} |
| Method | POST |
| Content-Type | application/json |
| URL Parameters | None |
| Request Body | UnitId and the Scheduled Event (see below) |
| Success Response | Code: 200 OK |
| | Contents: The Unit with the ID of the scheduled event added to the event list (see below) |
| Error Response | No specific error |
| Authorization | USER |

Notes:
1. Posts the received event details to the Scheduler Service for creation/update
2. Receives an eventID for the created/updated event
3. Calls the Unit service to add the eventID to the given unit's list of events

Sample Request:
```bash
curl -X POST \
  -u username:password \
  http://0.0.0.0:8500/units/events/add \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache' \
  -d '{
	"unitID": "tlvlp.iot.BazsalikON-soil",
	"event": {
        "eventID": "2019-10-01-EVENT-4E6F2401-8E56-4CB0-ACCE-284D60D73CA8",
	    "cronSchedule": "* * * * *",
	    "info": "Posts an mqtt message every minute to the /global/test topic",
	    "payload": {
	        "topic": "/global/test",
	        "payload": {
	            "key1": "value1",
	            "key2": "value2"
	        }
	    }
	}
}'
```

Sample Response:
```json
{
    "unitID": "tlvlp.iot.BazsalikON-soil",
    "name": "soil",
    "project": "tlvlp.iot.BazsalikON",
    "active": true,
    "controlTopic": "/units/tlvlp.iot.BazsalikON-soil/control",
    "lastSeen": "2019-10-01T12:28:12.113",
    "modules": [],
    "scheduledEvents": [
        "2019-10-07-EVENT-E7C216AF-19D4-46D4-BFCB-CB9CC5FCC3E0"
    ]
}
```


## Delete Scheduled Event from Unit:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_DELETE_SCHEDULED_EVENT_FROM_UNIT} |
| Method | POST |
| Content-Type | application/json |
| URL Parameters | None |
| Request Body | Scheduled event details. Note that only the eventID is used (see below) |
| Success Response | Code: 200 OK |
| | Contents: The updated Unit with the event ID removed from the event list |
| Error Response | No specific error |
| Authorization | USER |

Notes:
1. Posts the received event details to the Scheduler Service for deletion
2. Receives an eventID for the deleted event
3. Calls the Unit service to remove the eventID from the given unit's list of events

Sample Request:
```bash
curl -X POST \
  -u username:password \
  http://0.0.0.0:8500/units/events/delete \
  -H 'Content-Type: application/json' \
  -d '{
	"unitID": "tlvlp.iot.BazsalikON-soil",
	"event": {
		"eventID": "2019-10-07-EVENT-E7C216AF-19D4-46D4-BFCB-CB9CC5FCC3E0",
	}
}'
```

Sample Response:
```json
{
    "unitID": "tlvlp.iot.BazsalikON-soil",
    "name": "soil",
    "project": "tlvlp.iot.BazsalikON",
    "active": true,
    "controlTopic": "/units/tlvlp.iot.BazsalikON-soil/control",
    "lastSeen": "2019-10-01T12:28:12.113",
    "modules": [],
    "scheduledEvents": []
}
```

## Get Unit by ID with Scheduled Events and Logs:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_GET_UNIT_BY_ID_WITH_SCHEDULES_AND_LOGS} |
| Method | GET |
| Content-Type | application/json |
| URL Parameters | unitID: String with the unitID |
| | timeFrom: LocalDateTime - Retrieve logs from this time (inclusive) |
| | timeTo: LocalDateTime - Retrieve logs up to this time (exclusive) |
| Request Body | None |
| Success Response | Code: 200 OK  |
| | Contents: A map of lists containing the Unit and the related Scheduled Events and Unit Logs (see below) |
| Error Response | 404 Not Found  |
| | Contents: Error details |
| Authorization | USER |

Notes:
1. Retrieves a Unit by ID from the Unit service and adds it to a Map under "unit"
2. Retrieves the UnitLogs from the Unit service for that unit to a given time frame and adds them to a Map under "logs"
3. Retrieves the units Scheduled Events from the Scheduler service and adds them to a Map under "events"
4. Return the map

Sample Request:
```bash
curl -X GET \
  -u username:password \
  'http://0.0.0.0:8500/units/collected\
  ?unitID=tlvlp.iot.BazsalikON-soil\
  &timeFrom=2019-09-21T00:24:27.471\
  &timeTo=2019-10-26T00:24:27.471' \
  -H 'Content-Type: application/json'
```

Sample output:
```json
{
    "events": [
        {
            "eventID": "2019-10-07-EVENT-157E4D44-7C5C-4888-9F6B-85E4CF115175",
            "schedulerID": "6250e7db54e22bdd1efa5a7c0000016da341343509da380c",
            "cronSchedule": "* * * * *",
            "targetURL": "http://api-gateway:8500/backend/mqtt/outgoing",
            "info": "Posts an mqtt message every minute",
            "lastUpdated": "2019-10-07T00:47:02.453",
            "payload": {
                "topic": "/global/test",
                "payload": {
                    "pay": "load"
                }
            }
        }
    ],
    "unit": {
        "unitID": "tlvlp.iot.BazsalikON-soil",
        "name": "soil",
        "project": "tlvlp.iot.BazsalikON",
        "active": true,
        "controlTopic": "/units/tlvlp.iot.BazsalikON-soil/control",
        "lastSeen": "2019-10-01T12:28:12.113",
        "modules": [
            {
                "unitID": "tlvlp.iot.BazsalikON-soil",
                "moduleID": "gl5528|lightPercent",
                "value": 85.0
            }
        ],
        "scheduledEvents": [
            "2019-10-07-EVENT-157E4D44-7C5C-4888-9F6B-85E4CF115175"
        ]
    },
    "logs": [
        {
            "logID": "2019-10-01-LOG-E1C0EFB5-A799-4890-BFFD-F5C36F4783F9",
            "unitID": null,
            "name": null,
            "project": null,
            "arrived": "2019-10-01T11:42:42.847",
            "logEntry": "Unit seen for the first time"
        }
    ]
}
```

## Get Average Value Reports for Unit:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_GET_REPORTS_FOR_UNIT_MODULE} |
| Method | GET |
| Content-Type | application/json |
| URL Parameters | unitID: String with the unitID |
| | moduleID: String - module ID |
| | timeFrom: LocalDateTime - Retrieve reports from this time (inclusive) |
| | timeTo: LocalDateTime - Retrieve reports up to this time (exclusive) |
| | requestedScopes: Set<String> requested scopes |
| Request Body | None |
| Success Response | Code: 200 OK  |
| | Contents: A list of averages each containing the scope they belong to (see below) |
| Error Response | Code: No specific error |
| Authorization | USER |

Note:
**requestedScopes**: Set of ChronoUnits as Strings - A list of requested scopes to be included in the report:
- **MINUTES**: All the raw values from the module within the given interval 
- **HOURS**: Hourly averages from the module within the given interval 
- **DAYS**: Daily averages from the module within the given interval 
- **MONTHS**: Monthly averages from the module within the given interval
- **YEARS**: Yearly averages from the module within the given interval 

 Sample Request:
 ```bash
curl -X GET \
  -u username:password \
  'http://0.0.0.0:8500/units/modules/report\
  ?unitID=tlvlp.iot.BazsalikON-soil\
  &moduleID=gl5528|lightPercent\
  &timeFrom=2019-09-21T00:24:27.471\
  &timeTo=2019-10-25T00:24:27.471\
  &requestedScopes=MINUTES,HOURS,DAYS,MONTHS,YEARS' \
  -H 'Content-Type: application/json'
 ```
 
 Sample output:
  ```json
[
    {
        "scope": "HOURS",
        "date": "2019-10-01T12:00",
        "value": 85.0
    },
    {
        "scope": "HOURS",
        "date": "2019-10-01T11:00",
        "value": 82.5
    },
    {
        "scope": "DAYS",
        "date": "2019-10-01",
        "value": 83.125
    },
    {
        "scope": "MONTHS",
        "date": "2019-10",
        "value": 83.125
    },
    {
        "scope": "YEARS",
        "date": "2019",
        "value": 83.125
    }
]
  ```
 