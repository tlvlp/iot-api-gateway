package com.tlvlp.iot.server.api.gateway.rpc;

import com.tlvlp.iot.server.api.gateway.services.InternalHooksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController("/hooks")
public class InternalHooksAPI {

    private InternalHooksService internalHooksService;

    public InternalHooksAPI(InternalHooksService internalHooksService) {
        this.internalHooksService = internalHooksService;
    }

    @RequestMapping("${API_GATEWAY_API_INCOMING_MQTT_MESSAGE}")
    public ResponseEntity handleIncomingMQTTMessage(@RequestBody Map message) {
        try {
            internalHooksService.handleIncomingMQTTMessage(message);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
