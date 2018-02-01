package com.example.strainu.androidsensors

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlin.collections.ArrayList

/**
 * Created by strongheart on 11/10/17.
 */
class SensorsAdapter(private val sensorsDataArray: ArrayList<SensorData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "SensorAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sensor_item_layout, parent,false)
        return SensorHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val sensorHolder: SensorHolder = holder as SensorHolder
        sensorHolder.sensorName.text = sensorsDataArray[position].sensorName
        sensorHolder.sensorValue.text = sensorsDataArray[position].sensorValue

        sensorHolder.sensorCheckBox.setOnCheckedChangeListener(null)
        sensorHolder.sensorCheckBox.isChecked = sensorsDataArray[position].isChecked
        sensorHolder.sensorCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            sensorsDataArray[position].isChecked = isChecked
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: MutableList<Any>?) {
        if(!payloads!!.isEmpty()) {
            if (payloads[0] is SensorData) {
                val sensorHolder: SensorHolder = holder as SensorHolder
                val payloadsData = payloads[0] as SensorData
                sensorHolder.sensorName.text = payloadsData.sensorName
                sensorHolder.sensorValue.text = payloadsData.sensorValue
            }
        }else {
//            Log.i(TAG, "******************************")
            super.onBindViewHolder(holder,position, payloads);
        }
    }

    override fun getItemCount(): Int {
        return sensorsDataArray.size
    }

}