package com.example.strainu.androidsensors

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import kotlinx.android.synthetic.main.sensor_item_layout.*
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import android.widget.Toast
import java.sql.Array
import java.util.*


/**
 * Created by strongheart on 11/10/17.
 */
class SensorsAdapter(context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val sensorsList: List<Sensor>
    private val sensorManager: SensorManager


    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorsList = sensorManager.getSensorList(Sensor.TYPE_ALL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sensor_item_layout, parent,false)
        return SensorHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val sensorHolder= holder as SensorHolder
        sensorHolder.sensorName.text = sensorsList[position].name
        attachListener(sensorsList[position], sensorHolder.sensorValue)
    }

    override fun getItemCount(): Int {
        return sensorsList.size
    }

    private fun attachListener(sensor: Sensor, sensorValue: TextView) {
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent) {
                sensorValue.text = Arrays.toString(sensorEvent.values)
//                Log.d("Sensor", Arrays.toString(sensorEvent.values))
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
//                Log.d("SensorAccChange", sensor.name + " - " + accuracy)
            }
        }
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI)
        Log.i("Sensor", "Registerered listener for ${sensor.name}")
    }


    class SensorHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val sensorName: TextView
        val sensorCheckBox: CheckBox
        var sensorValue: TextView

        init {
            sensorName = itemView.findViewById(R.id.sensor_name)
            sensorCheckBox = itemView.findViewById((R.id.sensor_check_box))
            sensorValue = itemView.findViewById(R.id.sensor_value)
        }
    }
}