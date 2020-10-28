package com.bartol.sensorapi.models;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UserAddress {
    String ipAddress;
    int port;
}
