package com.bartol.sensorapi.models;

import lombok.Value;

@Value
public class Measurement {
    String username;
    String parameter;
    float averageValue;
}
