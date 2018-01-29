package com.example.strainu.androidsensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by strongheart on 11/10/17.
 */
class SensorsAdapter(context: Context,
                     private val sensorsDataArray: ArrayList<SensorData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "SensorAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sensor_item_layout, parent,false)
        return SensorHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val sensorHolder: SensorHolder = holder as SensorHolder
        sensorHolder.sensorName.text = sensorsDataArray[position].sensorName
//        sensorHolder.sensorCheckBox.isChecked = sensorsDataArray[position].isChecked
        sensorHolder.sensorValue.text = sensorsDataArray[position].sensorValue
//        Log.i(TAG, "***********************************************************************")
//        Log.i(TAG, "position = $position, sensors_name =  ${sensorsDataArray[position].sensorName}")
//
//
//        sensorHolder.sensorCheckBox.setOnCheckedChangeListener(null)
//        sensorHolder.sensorCheckBox.isChecked = sensorsDataArray[position].isChecked
        sensorHolder.sensorCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                sensorsDataArray[position].isChecked = isChecked
        }
    }

    override fun getItemCount(): Int {
        return sensorsDataArray.size
    }

}