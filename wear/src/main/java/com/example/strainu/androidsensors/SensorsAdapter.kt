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
    private val sensorManager : SensorManager
    private val sensorsList : List<Sensor>
    private var sensorsListenerNameMap: MutableMap<String, Pair<Sensor, SensorEventListener?>>
    private val TAG = "WearSensorAdapter"

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorsList = sensorManager.getSensorList(Sensor.TYPE_ALL)
        sensorsListenerNameMap = mutableMapOf()

        sensorsList.forEach { sensorsListenerNameMap[it.name] = Pair(it, null) }

        for((t,u) in sensorsListenerNameMap) {
            Log.i(TAG, "sensorKeyName = $t         sensorFirstName = ${u.first.name}" )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sensor_item_layout, parent,false)
        return SensorHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val sensorHolder: SensorHolder = holder as SensorHolder
        sensorHolder.sensorName.text = sensorsDataArray[position].sensorName
        Log.i(TAG, "***********************************************************************")
        Log.i(TAG, "position = $position, sensors_name =  ${sensorsDataArray[position].sensorName}")


        sensorHolder.sensorCheckBox.setOnCheckedChangeListener(null)
        sensorHolder.sensorCheckBox.isChecked = sensorsDataArray[position].isChecked
        sensorHolder.sensorCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                sensorsDataArray[position].isChecked = isChecked
        }

        // dissaatage listerner here
//        sensorHolder.sensorValue.text = sensorsDataArray[position].sensorValue
        attachListener(position, sensorHolder)
    }

    override fun getItemCount(): Int {
        return sensorsList.size
    }

    private fun attachListener(position: Int, sensorHolder: SensorHolder) {
        var sensorListenerPair: Pair<Sensor, SensorEventListener?> = sensorsListenerNameMap[sensorsDataArray[position].sensorName]!!
        if(sensorListenerPair.second != null) {
            sensorManager.unregisterListener(sensorListenerPair.second, sensorListenerPair.first)
        }

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent) {
//                sensorHolder.sensorName.text = sensorsDataArray[position].sensorName
                sensorHolder.sensorValue.text = Arrays.toString(sensorEvent.values)
                sensorsDataArray[position].sensorValue = Arrays.toString(sensorEvent.values)
//                Log.d("Sensor", Arrays.toString(sensorEvent.values))
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
//                Log.d("SensorAccChange", sensor.name + " - " + accuracy)
            }
        }
        sensorsListenerNameMap[sensorsDataArray[position].sensorName] = sensorListenerPair.copy(second = sensorEventListener)

        sensorListenerPair = sensorsListenerNameMap[sensorsDataArray[position].sensorName]!!
        sensorManager.registerListener(sensorListenerPair.second, sensorListenerPair.first, SensorManager.SENSOR_DELAY_UI)

        Log.i(TAG, "Registerered listener for ${sensorListenerPair.first.name} |||||  ${sensorsDataArray[position].sensorName}")

    }



    class SensorHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val sensorName: TextView = itemView.findViewById(R.id.sensor_name)
        val sensorCheckBox: CheckBox = itemView.findViewById((R.id.sensor_check_box))
        var sensorValue: TextView = itemView.findViewById(R.id.sensor_value)
    }
}