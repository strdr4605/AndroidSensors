package com.example.strainu.androidsensors

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*


/**
 * Created by strongheart on 11/10/17.
 */
class MobileSensorsAdapter(private val sensorsDataArray: ArrayList<SensorData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "SensorAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mobile_sensor_item_layout, parent,false)
        return MobileSensorHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val mobileSensorHolder: MobileSensorHolder = holder as MobileSensorHolder
        mobileSensorHolder.sensorName.text = sensorsDataArray[position].sensorName
        mobileSensorHolder.sensorValue.text = sensorsDataArray[position].sensorValue

        mobileSensorHolder.sensorCheckBox.setOnCheckedChangeListener(null)
        mobileSensorHolder.sensorCheckBox.isChecked = sensorsDataArray[position].isChecked
        mobileSensorHolder.sensorCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            sensorsDataArray[position].isChecked = isChecked
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: MutableList<Any>?) {
        if(!payloads!!.isEmpty()) {
            if (payloads[0] is SensorData) {
                val mobileSensorHolder: MobileSensorHolder = holder as MobileSensorHolder
                val payloadsData = payloads[0] as SensorData
                mobileSensorHolder.sensorName.text = payloadsData.sensorName
                mobileSensorHolder.sensorValue.text = payloadsData.sensorValue
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