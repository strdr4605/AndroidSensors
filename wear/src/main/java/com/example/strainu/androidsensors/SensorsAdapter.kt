package com.example.strainu.androidsensors

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

/**
 * Created by strongheart on 11/10/17.
 */
class SensorsAdapter(context:Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var sensorsList: List<Sensor>

    init {
        val sensorsManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorsList = sensorsManager.getSensorList(Sensor.TYPE_ALL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sensor_item_layout, parent,false)
        return SensorHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val sensorHolder= holder as SensorHolder
        sensorHolder.sensorName.setText(sensorsList[position].name)
    }

    override fun getItemCount(): Int {
        return sensorsList.size
    }

    class SensorHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val sensorName: TextView
        val sensorCheckBox: CheckBox

        init {
            sensorName = itemView.findViewById(R.id.sensor_name)
            sensorCheckBox = itemView.findViewById((R.id.sensor_check_box))
        }
    }
}