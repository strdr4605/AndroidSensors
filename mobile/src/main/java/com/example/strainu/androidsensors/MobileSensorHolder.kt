package com.example.strainu.androidsensors

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.TextView

/**
 * Created by strongheart on 1/29/18.
 */
class MobileSensorHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val sensorName: TextView = itemView.findViewById(R.id.sensor_name)
    val sensorCheckBox: CheckBox = itemView.findViewById((R.id.sensor_check_box))
    var sensorValue: TextView = itemView.findViewById(R.id.sensor_value)
}