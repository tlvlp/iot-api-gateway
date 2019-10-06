package com.tlvlp.iot.server.api.gateway.rpc;

import com.tlvlp.iot.server.api.gateway.services.GatewayService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin
public class GatewayAPI {

    private GatewayService gatewayService;

    public GatewayAPI(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("${API_GATEWAY_API_GET_ALL_UNITS}")
    public ResponseEntity<List> getAllUnits() {
        return gatewayService.getAllUnits();
    }

    @GetMapping("${API_GATEWAY_API_GET_UNIT_BY_ID}")
    public ResponseEntity<Object> getUnitById(@RequestParam String unitID) {
        return new ResponseEntity<>(gatewayService.getUnitById(unitID), HttpStatus.OK);
    }

    @GetMapping("${API_GATEWAY_API_GET_UNIT_BY_ID_WITH_SCHEDULES_AND_LOGS}")
    public ResponseEntity<Map<String, Object>> getUnitByIdWithSchedulesAndLogs(
            @RequestParam String unitID,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeFrom,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeTo) {
        return new ResponseEntity<>(
                gatewayService.getUnitByIdWithSchedulesAndLogs(unitID, timeFrom, timeTo),
                HttpStatus.OK);
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

    @GetMapping("${API_GATEWAY_API_GET_REPORTS_FOR_UNIT_MODULE}")
    public ResponseEntity getReportsForUnit(
            @RequestParam String unitID,
            @RequestParam String moduleID,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeFrom,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeTo,
            @RequestParam Set<String> requestedScopes) {
        return gatewayService.getReportsForUnit(unitID, moduleID, timeFrom, timeTo, requestedScopes);
    }

}
