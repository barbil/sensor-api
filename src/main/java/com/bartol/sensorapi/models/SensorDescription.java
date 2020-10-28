package com.bartol.sensorapi.models;

import lombok.Value;

@Value
public class SensorDescription {
    String username;
    double latitude;
    double longitude;
    String ipAddress;
    int port;
}
