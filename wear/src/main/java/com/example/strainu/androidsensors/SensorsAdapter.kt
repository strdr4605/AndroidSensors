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
import kotlin.collections.ArrayList

/**
 * Created by strongheart on 11/10/17.
 */
class SensorsAdapter(context: Context,
                     private val sensorsList: MutableList<Sensor>): RecyclerView.Adapter<SensorHolder>() {

    private var context: Context = context
    private lateinit var sensorsDataArray : ArrayList<SensorData>
    private val TAG = "SensorAdapter"

    init {
        sensorsDataArray = ArrayList()
        sensorsList.forEach { sensor ->
            sensorsDataArray.add(SensorData(sensor.name, false, "Nothing"))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorHolder {
        val view = LayoutInflater.from(parent.context)
                                 .inflate(R.layout.sensor_item_layout, parent,false)

        return SensorHolder(view)
    }

    override fun onBindViewHolder(holder: SensorHolder, position: Int) {
        Log.e(TAG, "OnBindViewHolder " + position)
        holder.sensorName.text = sensorsDataArray[position].sensorName
        triggerFor(holder, sensorsList.get(position))
//        sensorHolder.sensorCheckBox.isChecked = sensorsDataArray[position].isChecked

//        Log.i(TAG, "***********************************************************************")
//        Log.i(TAG, "position = $position, sensors_name =  ${sensorsDataArray[position].sensorName}")
//
//
//        sensorHolder.sensorCheckBox.setOnCheckedChangeListener(null)
//        sensorHolder.sensorCheckBox.isChecked = sensorsDataArray[position].isChecked

//        holder.sensorCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
//            Log.e(TAG, "" + position)
//                //sensorsDataArray[position].isChecked = isChecked
//        }
    }

    override fun getItemCount(): Int {
        return sensorsDataArray.size
    }

    fun triggerFor(holder: SensorHolder, sensor: Sensor) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(holder.sensorHandler)

        sensorManager.registerListener(holder.sensorHandler, sensor, SensorManager.SENSOR_DELAY_UI)
    }

}