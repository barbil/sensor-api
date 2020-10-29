package com.bartol.sensorapi.controllers;

import com.bartol.sensorapi.models.Measurement;
import com.bartol.sensorapi.models.SensorDescription;
import com.bartol.sensorapi.models.UserAddress;
import com.bartol.sensorapi.services.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SensorDescription description) {
        if (description.getUsername() != null &&
                !sensorService.findByUsername(description.getUsername()).isPresent()) {

            sensorService.register(description);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/search")
    public ResponseEntity<UserAddress> searchNeighbour(@RequestBody Map<String, String> sensor) {
        log.info("Accepted username is " + sensor.get("username"));
        Optional<UserAddress> optionalUserAddress = sensorService.searchNeighbour(sensor.get("username"));

        if (optionalUserAddress.isPresent()) {
            log.info("User address is present");
            UserAddress userAddress = optionalUserAddress.get();

            log.info("Closest neighbour has IP address " + userAddress.getIpAddress() + ":"
                    + userAddress.getPort());

            return ResponseEntity.ok(userAddress);
        }

        log.info("User address is not present");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/store")
    public ResponseEntity<?> storeMeasurement(@RequestBody Measurement measurement) {
        if (!sensorService.findByUsername(measurement.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Sensor with username " + measurement.getUsername() + " has average value "
                + measurement.getAverageValue() + " of parameter " + measurement.getParameter());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/blacklist")
    public ResponseEntity<?> blacklist(@RequestBody UserAddress userAddress) {
        sensorService.blacklist(userAddress);
        return ResponseEntity.ok().build();
    }
}
