package com.example.strainu.androidsensors

import java.util.*

/**
 * Created by strongheart on 1/28/18.
 */
data class SensorData(
        val sensorName: String,
        var isChecked: Boolean,
        var sensorValue: String,
        var time: Date?
)