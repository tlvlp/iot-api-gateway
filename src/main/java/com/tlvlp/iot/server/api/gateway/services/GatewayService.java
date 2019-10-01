package com.tlvlp.iot.server.api.gateway.services;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.tlvlp.iot.server.api.gateway.config.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GatewayService {

    private static final Logger log = LoggerFactory.getLogger(GatewayService.class);
    private RestTemplate restTemplate;
    private Properties properties;

    public GatewayService(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public ResponseEntity<List> getAllUnits() {
        return restTemplate.getForEntity(
                String.format("http://%s:%s%s",
                        properties.getUNIT_SERVICE_NAME(),
                        properties.getUNIT_SERVICE_PORT(),
                        properties.getUNIT_SERVICE_API_LIST_ALL_UNIT()),
                List.class);
    }

    public Map getUnitById(String unitID) {
        return restTemplate.getForObject(
                String.format("http://%s:%s%s?unitID=%s",
                        properties.getUNIT_SERVICE_NAME(),
                        properties.getUNIT_SERVICE_PORT(),
                        properties.getUNIT_SERVICE_API_GET_UNIT_BY_ID(),
                        unitID),
                Map.class);
    }

    public Map<String, Object> getUnitByIdWithSchedulesAndReports(
            String unitID, LocalDateTime timeFrom, LocalDateTime timeTo) {
        Map unit = getUnitById(unitID);
        List eventIDList;
        try {
            eventIDList = JsonPath.parse(unit).read("$.scheduledEvents", List.class);
        } catch (PathNotFoundException e) {
            eventIDList = List.of();
        }
        return Map.of(
                "unit", unit,
                "events", getScheduledEventsFromIDs(eventIDList),
                "logs", getUnitLogs(unitID, timeFrom, timeTo));
    }

    private List getScheduledEventsFromIDs(List scheduledEventIDList) {
        return restTemplate.postForObject(
                String.format("http://%s:%s%s",
                        properties.getSCHEDULER_SERVICE_NAME(),
                        properties.getSCHEDULER_SERVICE_PORT(),
                        properties.getSCHEDULER_SERVICE_API_GET_EVENTS_FROM_LIST()),
                scheduledEventIDList,
                List.class);
    }

    private List getUnitLogs(String unitID, LocalDateTime timeFrom, LocalDateTime timeTo) {
        return restTemplate.getForObject(
                String.format("http://%s:%s%s?unitID=%s&timeFrom=%s&timeTo=%s",
                        properties.getUNIT_SERVICE_NAME(),
                        properties.getUNIT_SERVICE_PORT(),
                        properties.getUNIT_SERVICE_API_GET_UNIT_LOGS(),
                        unitID,
                        timeFrom,
                        timeTo),
                List.class);
    }

    public ResponseEntity requestGlobalStatus() {
        try {
            Object statusRequestMessage = restTemplate.getForEntity(
                    String.format("http://%s:%s%s",
                            properties.getUNIT_SERVICE_NAME(),
                            properties.getUNIT_SERVICE_PORT(),
                            properties.getUNIT_SERVICE_API_REQUEST_GLOBAL_STATUS()),
                    String.class);
            if (statusRequestMessage == null) {
                throw new NoValueRetrievedFromServiceException("Unable to get status request message from Unit service.");
            }
            return restTemplate.postForEntity(
                    String.format("http://%s:%s%s",
                            properties.getMQTT_CLIENT_SERVICE_NAME(),
                            properties.getMQTT_CLIENT_SERVICE_PORT(),
                            properties.getMQTT_CLIENT_API_OUTGOING_MESSAGE()),
                    statusRequestMessage,
                    String.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Cannot send global status request message: %s", e.getMessage()));
        }
    }

    public ResponseEntity controlUnitModule(Object updatedModule) {
        try {
            Object moduleControlMessage = restTemplate.postForEntity(
                    String.format("http://%s:%s%s",
                            properties.getUNIT_SERVICE_NAME(),
                            properties.getUNIT_SERVICE_PORT(),
                            properties.getUNIT_SERVICE_API_MODULE_CONTROL()),
                    updatedModule,
                    String.class);
            if (moduleControlMessage == null) {
                throw new NoValueRetrievedFromServiceException("Unable to get module control message from Unit service.");
            }
            return restTemplate.postForEntity(
                    String.format("http://%s:%s%s",
                            properties.getMQTT_CLIENT_SERVICE_NAME(),
                            properties.getMQTT_CLIENT_SERVICE_PORT(),
                            properties.getMQTT_CLIENT_API_OUTGOING_MESSAGE()),
                    moduleControlMessage,
                    String.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Cannot send Unit module control message: %s", e.getMessage()));
        }
    }

    public ResponseEntity addScheduledEventToUnit(Map<String, Object> scheduledEventDetails) {
        try {
            var unitID = scheduledEventDetails.get("unitID");
            var scheduledEvent = scheduledEventDetails.get("event");
            String eventID = restTemplate.postForObject(
                    String.format("http://%s:%s%s",
                            properties.getSCHEDULER_SERVICE_NAME(),
                            properties.getSCHEDULER_SERVICE_PORT(),
                            properties.getSCHEDULER_SERVICE_API_POST_MQTT_MESSAGE_SEND_EVENT()),
                    scheduledEvent,
                    String.class);
            if (eventID == null) {
                throw new NoValueRetrievedFromServiceException("Unable to create event at Scheduler service.");
            }
            return restTemplate.postForEntity(
                    String.format("http://%s:%s%s",
                            properties.getUNIT_SERVICE_NAME(),
                            properties.getUNIT_SERVICE_PORT(),
                            properties.getUNIT_SERVICE_API_ADD_SCHEDULED_EVENT()),
                    Map.of("unitID", unitID,
                            "eventID", eventID),
                    String.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Cannot create new Scheduled Event for Unit: %s", e.getMessage()));
        }
    }

    public ResponseEntity deleteScheduledEventFromUnit(Map<String, Object> scheduledEventDetails) {
        try {
            var unitID = scheduledEventDetails.get("unitID");
            var scheduledEvent = scheduledEventDetails.get("event");
            String eventID = restTemplate.postForObject(
                    String.format("http://%s:%s%s",
                            properties.getSCHEDULER_SERVICE_NAME(),
                            properties.getSCHEDULER_SERVICE_PORT(),
                            properties.getSCHEDULER_SERVICE_API_DELETE_EVENT()),
                    scheduledEvent,
                    String.class);
            if (eventID == null) {
                throw new NoValueRetrievedFromServiceException("Unable to delete event at Scheduler service.");
            }
            return restTemplate.postForEntity(
                    String.format("http://%s:%s%s",
                            properties.getUNIT_SERVICE_NAME(),
                            properties.getUNIT_SERVICE_PORT(),
                            properties.getUNIT_SERVICE_API_DELETE_SCHEDULED_EVENT()),
                    Map.of("unitID", unitID,
                            "eventID", eventID),
                    String.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Cannot delete Scheduled Event from Unit: %s", e.getMessage()));
        }
    }

    public ResponseEntity getReportsForUnit(String unitID, String moduleID, LocalDateTime timeFrom,
                                            LocalDateTime timeTo, Set<ChronoUnit> requestedScopes){
        try {
            return restTemplate.getForEntity(
                    String.format("http://%s:%s%s?unitID=%s&moduleID=%s&timeFrom=%s&timeTo=%s&requestedScopes=%s",
                            properties.getREPORTING_SERVICE_NAME(),
                            properties.getREPORTING_SERVICE_PORT(),
                            properties.getREPORTING_SERVICE_API_GET_AVERAGES(),
                            unitID,
                            moduleID,
                            timeFrom,
                            timeTo,
                            requestedScopes),
                    String.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Cannot generate Reports for Unit: %s", e.getMessage()));
        }
    }

}
