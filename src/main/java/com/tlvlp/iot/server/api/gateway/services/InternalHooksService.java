package com.tlvlp.iot.server.api.gateway.services;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.tlvlp.iot.server.api.gateway.config.Properties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@Service
public class InternalHooksService {

    private Properties properties;
    private RestTemplate restTemplate;

    public InternalHooksService(Properties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    public void handleIncomingMQTTMessage(Object message) {
        DocumentContext processedMessage = JsonPath.parse(forwardMessageToUnitService(message));
        var messageType = processedMessage.read("$.type", String.class);
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

    private void notifySubscribers(Object unitLog) {
        //TODO Implement subscriber notification
    }

}
