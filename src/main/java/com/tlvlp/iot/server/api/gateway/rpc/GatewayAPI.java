package com.tlvlp.iot.server.api.gateway.rpc;

import com.tlvlp.iot.server.api.gateway.services.GatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class GatewayAPI {

    //TODO
    // delete task from unit (SchedulerS) (UnitS)
    // get all scheduled tasks for a unit (list of tasks)
    // get reports for unit (ReportingS)

    private GatewayService gatewayService;

    public GatewayAPI(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("${API_GATEWAY_API_GET_ALL_UNITS}")
    public ResponseEntity<List> getAllUnits() {
        return gatewayService.getAllUnits();
    }

    @GetMapping("${API_GATEWAY_API_GET_UNIT_BY_ID}")
    public ResponseEntity getUnitById(@RequestParam String unitID) {
        return gatewayService.getUnitById(unitID);
    }

    @PostMapping("${API_GATEWAY_API_REQUEST_GLOBAL_UNIT_STATUS}")
    public ResponseEntity requestGlobalStatus() {
        return gatewayService.requestGlobalStatus();
    }

    @PostMapping("${API_GATEWAY_API_UNIT_MODULE_CONTROL}")
    public ResponseEntity controlUnitModule(@RequestBody Object updatedModule) {
        return gatewayService.controlUnitModule(updatedModule);
    }

    @PostMapping("${API_GATEWAY_API_ADD_SCHEDULED_EVENT_TO_UNIT}")
    public ResponseEntity addScheduledEventToUnit(@RequestBody Map<String, Object> scheduledEventDetails) {
        return gatewayService.addScheduledEventToUnit(scheduledEventDetails);
    }

    @PostMapping("${API_GATEWAY_API_DELETE_SCHEDULED_EVENT_FROM_UNIT}")
    public ResponseEntity deleteScheduledEventFromUnit(@RequestBody Map<String, Object> scheduledEventDetails) {
        return gatewayService.deleteScheduledEventFromUnit(scheduledEventDetails);
    }

}
