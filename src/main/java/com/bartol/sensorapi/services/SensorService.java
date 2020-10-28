package com.bartol.sensorapi.services;

import com.bartol.sensorapi.models.SensorDescription;
import com.bartol.sensorapi.models.UserAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SensorService {
    private final List<SensorDescription> sensors;

    public SensorService() {
        this.sensors = new ArrayList<>();
    }

    public void register(SensorDescription sensor) {
        sensors.add(sensor);
    }

    public Optional<SensorDescription> findByUsername(String username) {
        return sensors.stream()
                .filter(t -> t.getUsername().equals(username))
                .findFirst();
    }

    public Optional<UserAddress> searchNeighbour(String username) {
        double minDistance = Double.MAX_VALUE;
        SensorDescription minSensor = null;

        Optional<SensorDescription> optionalSensor = findByUsername(username);

        if (optionalSensor.isPresent()) {
            log.info("Sensor with username " + username);
            SensorDescription sensor1 = optionalSensor.get();

            for (SensorDescription sensor : sensors) {
                double distance = findDistance(sensor1, sensor);

                if (distance < minDistance && !sensor.getUsername().equals(username)) {
                    minDistance = distance;
                    minSensor = sensor;
                }
            }

            if (minSensor != null) {
                log.info("Distance from closest neighbour is " + minDistance);
                return Optional.of(new UserAddress(minSensor.getIpAddress(), minSensor.getPort()));
            }
        }

        return Optional.empty();
    }

    private double findDistance(SensorDescription sensor1, SensorDescription sensor2) {
        int R = 6371;
        double dlon = sensor2.getLongitude() - sensor1.getLongitude();
        double dlat = sensor2.getLatitude() - sensor1.getLatitude();

        double a = Math.sin(dlat / 2.0) * Math.sin(dlat / 2.0) +
                Math.cos(sensor1.getLatitude()) * Math.cos(sensor2.getLatitude()) *
                        Math.sin(dlon / 2.0) * Math.sin(dlon / 2.0);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
