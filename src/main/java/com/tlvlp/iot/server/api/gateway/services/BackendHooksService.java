package com.tlvlp.iot.server.api.gateway.services;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.tlvlp.iot.server.api.gateway.config.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

@Service
public class BackendHooksService {

    private static final Logger log = LoggerFactory.getLogger(BackendHooksService.class);
    private Properties properties;
    private RestTemplate restTemplate;

    public BackendHooksService(Properties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    public void handleIncomingMQTTMessage(Object message) {
        try {
            DocumentContext processedMessage = JsonPath.parse(forwardMessageToUnitService(message));
            var messageType = processedMessage.read("$.type", String.class);
            log.debug(String.format("Handling incoming MQTT message of type: %s", messageType));
            switch (messageType) {
                case "error":
                case "inactive":
                    Object unit = processedMessage.read("$.object");
                    notifySubscribers(unit);
                    break;
                case "status":
                    List unitModules = processedMessage.read("$.object.modules");
                    forwardUnitModuleUpdatesToReporting(unitModules);
                    notifySubscribers(processedMessage.read("$.object"));
                    break;
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Cannot process incoming MQTT message: %s", e.getMessage()));
        }
    }

    private String forwardMessageToUnitService(Object message) {
        return restTemplate.postForObject(
                String.format("http://%s:%s%s",
                        properties.getUNIT_SERVICE_NAME(),
                        properties.getUNIT_SERVICE_PORT(),
                        properties.getUNIT_SERVICE_API_INCOMING_MESSAGE()),
                message,
                String.class
        );
    }

    private HashMap forwardUnitModuleUpdatesToReporting(List unitModules) {
        return restTemplate.postForObject(
                String.format("http://%s:%s%s",
                        properties.getREPORTING_SERVICE_NAME(),
                        properties.getREPORTING_SERVICE_PORT(),
                        properties.getREPORTING_SERVICE_API_POST_VALUES()),
                unitModules,
                HashMap.class
        );
    }

    private void notifySubscribers(Object updatedUnit) {
        //TODO Implement subscriber notification
    }

    public void handleOutgoingMQTTMessage(Object message) {
        try {
            log.debug("Forwarding outgoing MQTT message");
            ResponseEntity response = restTemplate.postForEntity(
                    String.format("http://%s:%s%s",
                            properties.getMQTT_CLIENT_SERVICE_NAME(),
                            properties.getMQTT_CLIENT_SERVICE_PORT(),
                            properties.getMQTT_CLIENT_API_OUTGOING_MESSAGE()),
                    message,
                    String.class);
        } catch (Exception e) {
            log.error("Cannot forward outgoing MQTT message: " + e.getMessage());
        }
    }
}
