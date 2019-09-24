package com.tlvlp.iot.server.api.gateway.rpc;

import com.tlvlp.iot.server.api.gateway.services.GatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GatewayAPI {

    //TODO
    // request global status (UnitS)
    // unit module control (UnitS)
    // get scheduled tasks for a unit (SchedulerS)
    // add scheduled task to unit (SchedulerS) (UnitS)
    // delete task from unit (SchedulerS) (UnitS)
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

}
