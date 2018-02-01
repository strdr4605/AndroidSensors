package com.example.strainu.androidsensors

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*


/**
 * Created by strongheart on 11/10/17.
 */
class WearSensorsAdapter(private val sensorsDataArray: ArrayList<SensorData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "SensorAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wear_sensor_item_layout, parent,false)
        return WearSensorHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val wearSensorHolder: WearSensorHolder = holder as WearSensorHolder
        wearSensorHolder.sensorName.text = sensorsDataArray[position].sensorName
        wearSensorHolder.sensorValue.text = sensorsDataArray[position].sensorValue
    }

    override fun getItemCount(): Int {
        return sensorsDataArray.size
    }
}