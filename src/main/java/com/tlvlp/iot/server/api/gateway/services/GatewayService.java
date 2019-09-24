package com.tlvlp.iot.server.api.gateway.services;

import com.tlvlp.iot.server.api.gateway.config.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

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

    public ResponseEntity getUnitById(String unitID) {
        return restTemplate.getForEntity(
                String.format("http://%s:%s%s?unitID=%s",
                        properties.getUNIT_SERVICE_NAME(),
                        properties.getUNIT_SERVICE_PORT(),
                        properties.getUNIT_SERVICE_API_GET_UNIT_BY_ID(),
                        unitID),
                String.class);
    }

    public ResponseEntity requestGlobalStatus() {
        Object statusRequestMessage = restTemplate.getForEntity(
                String.format("http://%s:%s%s",
                        properties.getUNIT_SERVICE_NAME(),
                        properties.getUNIT_SERVICE_PORT(),
                        properties.getUNIT_SERVICE_API_REQUEST_GLOBAL_STATUS()),
                String.class);
        return restTemplate.postForEntity(
                String.format("http://%s:%s%s",
                        properties.getMQTT_CLIENT_SERVICE_NAME(),
                        properties.getMQTT_CLIENT_SERVICE_PORT(),
                        properties.getMQTT_CLIENT_API_OUTGOING_MESSAGE()),
                statusRequestMessage,
                String.class);
    }

    public ResponseEntity controlUnitModule(Object updatedModule) {
        Object moduleControlMessage = restTemplate.postForEntity(
                String.format("http://%s:%s%s",
                        properties.getUNIT_SERVICE_NAME(),
                        properties.getUNIT_SERVICE_PORT(),
                        properties.getUNIT_SERVICE_API_MODULE_CONTROL()),
                updatedModule,
                String.class);
        return restTemplate.postForEntity(
                String.format("http://%s:%s%s",
                        properties.getMQTT_CLIENT_SERVICE_NAME(),
                        properties.getMQTT_CLIENT_SERVICE_PORT(),
                        properties.getMQTT_CLIENT_API_OUTGOING_MESSAGE()),
                moduleControlMessage,
                String.class);
    }

    public ResponseEntity addScheduledEventToUnit(Map<String, Object> scheduledEventDetails) {
        var unitID = scheduledEventDetails.get("unitID");
        var scheduledEvent = scheduledEventDetails.get("event");
        String eventID = restTemplate.postForObject(
                String.format("http://%s:%s%s",
                        properties.getSCHEDULER_SERVICE_NAME(),
                        properties.getSCHEDULER_SERVICE_PORT(),
                        properties.getSCHEDULER_SERVICE_API_POST_EVENT()),
                scheduledEvent,
                String.class);
        return restTemplate.postForEntity(
                String.format("http://%s:%s%s",
                        properties.getUNIT_SERVICE_NAME(),
                        properties.getUNIT_SERVICE_PORT(),
                        properties.getUNIT_SERVICE_API_ADD_SCHEDULED_EVENT()),
                Map.of("unitID", unitID,
                        "eventID", eventID),
                String.class);
    }
}
