package com.example.strainu.androidsensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * Created by strongheart on 1/29/18.
 */
class SensorHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val sensorName: TextView = itemView.findViewById(R.id.sensor_name)
    val sensorCheckBox: CheckBox = itemView.findViewById((R.id.sensor_check_box))
    var sensorValue: TextView = itemView.findViewById(R.id.sensor_value)

    private val TAG = "SensorAdapter"

//    init {
//        sensorCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
//            Log.e(TAG, "" + position)
//            //sensorsDataArray[position].isChecked = isChecked
//        }
//
//    }


    val sensorHandler = object : SensorEventListener {
        override fun onSensorChanged(sensorEvent: SensorEvent) {
            sensorValue.text = Arrays.toString(sensorEvent.values)
//                    Log.i("Listener", "Listener for ${sensorData.sensorName} vs ${sensorsList[index].name} registered" )
            //                Log.d("Sensor", Arrays.toString(sensorEvent.values))
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            //                Log.d("SensorAccChange", sensor.name + " - " + accuracy)
        }
    }
}