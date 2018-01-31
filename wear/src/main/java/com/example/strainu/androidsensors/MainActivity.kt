package com.example.strainu.androidsensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.wearable.activity.WearableActivity
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : WearableActivity() {
    private val SENSOR_DATA_CAPABILITY_NAME = "receive_sensor_data"
    private val SENSOR_DATA_MESSAGE_PATH = "/sensor_data"
    private val TAG = "MainActivityWear"
    private var sensorDataReceiverNodeId: String? = null
    private lateinit var handler: Handler
    private lateinit var runnable : Runnable
    private lateinit var sensorsDataArray : ArrayList<SensorData>
    private lateinit var sensorsAdapter : SensorsAdapter
    private var delay: Long = 200 //miliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Enables Always-on
        setAmbientEnabled()

        Log.i(TAG, "Wear activity created")

        recycler_list.layoutManager = LinearLayoutManager(this)
        recycler_list.setHasFixedSize(true)

        val sensorManager : SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorsList: MutableList<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        initialiseSensorDataArray(sensorsList)
        registerListenersToSensors(sensorManager, sensorsList)

        sensorsAdapter = SensorsAdapter(this, sensorsDataArray)

        recycler_list.adapter = sensorsAdapter

        // Trigger an AsyncTask that will get the handheld device node
        StartSetupSensorDataTask().execute(this)

        setCronometer()
    }

    override fun onResume() {
        super.onResume()
        startSendingMessage()
        Log.i(TAG, "Wear activity resumed")
    }

    override fun onPause() {
        super.onPause()
        stopSendingMessage()
        Log.i(TAG, "Wear activity paused")
    }

    private fun initialiseSensorDataArray(sensorsList: MutableList<Sensor>) {
        sensorsDataArray = ArrayList<SensorData>()
        sensorsList.forEach { sensor ->
            sensorsDataArray.add(SensorData(sensor.name, false, "Nothing", Date().time))
        }
    }

    private fun registerListenersToSensors(sensorManager : SensorManager, sensorsList: MutableList<Sensor>) {
        sensorsDataArray.forEachIndexed { index, sensorData ->
            val sensorEventListener = object : SensorEventListener {
                override fun onSensorChanged(sensorEvent: SensorEvent) {
                    sensorData.sensorValue = Arrays.toString(sensorEvent.values)
                    sensorData.time = Date().time
                    // Log.d("Sensor", Arrays.toString(sensorEvent.values))
                }

                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                    // Log.d("SensorAccChange", sensorsList[index].name + " - " + accuracy)
                }
            }
            sensorManager.registerListener(sensorEventListener, sensorsList[index], SensorManager.SENSOR_DELAY_UI)
        }
    }

    private fun retriveCapableNodes() {
        val capabilityInfo = Tasks.await(
                Wearable.getCapabilityClient(this).getCapability(
                        SENSOR_DATA_CAPABILITY_NAME, CapabilityClient.FILTER_REACHABLE))
        // capabilityInfo has the reachable nodes with the transcription capability
        updateNodesCapability(capabilityInfo)

        // to detect capable nodes as they connect to the wearable device,
        // can also use lambda function here
        val capabilityListener = CapabilityClient.OnCapabilityChangedListener {
                    updateNodesCapability(it) }
        Wearable.getCapabilityClient(this).addListener(
                capabilityListener,
                SENSOR_DATA_CAPABILITY_NAME)
    }

    private fun updateNodesCapability(capabilityInfo: CapabilityInfo) {
        val connectedNodes = capabilityInfo.nodes
        sensorDataReceiverNodeId = pickBestNodeId(connectedNodes)
    }

    private fun pickBestNodeId(nodes: Set<Node>): String? {
        var bestNodeId : String? = null
        // Find a nearby node or pick one arbitrarily
        for (node in nodes) {
            if (node.isNearby) {
                return node.id
            }
            bestNodeId = node.id
        }
        return bestNodeId
    }

    private fun sendMessage(messageData: ByteArray) {
        if (sensorDataReceiverNodeId != null) {
            val sendTask : Task<Int> =
            Wearable.getMessageClient(this).sendMessage(
                    sensorDataReceiverNodeId!!, SENSOR_DATA_MESSAGE_PATH, messageData)
            // You can add success and/or failure listeners,
            // Or you can call Tasks.await() and catch ExecutionException
            // A successful result code does not guarantee delivery of the message.
            sendTask.addOnSuccessListener {Log.i(TAG, "Message send succesufully")}
            sendTask.addOnFailureListener {Log.i(TAG, "Message failed to send")}
        } else {
            // Unable to retrieve node with receive sensor data capabilities
            Log.i(TAG, "Unable to retrieve node with receive sensors data capability")
        }
    }

    private fun setCronometer() {
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                val sensorsDataArrayCopy = ArrayList<SensorData>()
                sensorsDataArray.forEach{sensorsDataArrayCopy.add(it.copy())}
                updateWearUi(sensorsDataArrayCopy)
                val messageJson = getSensorsDataJson(sensorsDataArrayCopy)
                Log.i(TAG, "Send Json: + $messageJson")
                sendMessage(messageJson.toByteArray())
                handler.postDelayed(this, delay)
            }
        }
    }

    private fun startSendingMessage() {
        handler.post(runnable)
    }

    private fun stopSendingMessage() {
        handler.removeCallbacks(runnable)
    }

    private fun getSensorsDataJson(sensorsDataArrayCopy: ArrayList<SensorData>): String  {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(sensorsDataArrayCopy.filter { it.isChecked })
    }

    private fun updateWearUi(sensorsDataArrayCopy: ArrayList<SensorData>) {
        sensorsDataArrayCopy.forEachIndexed { index, sensorData ->
            sensorsAdapter.notifyItemChanged(index, sensorData)
        }
    }

    private class StartSetupSensorDataTask : AsyncTask<MainActivity, Void, Void>() {
        override fun doInBackground(vararg params: MainActivity?): Void? {
            Log.i(params[0]?.TAG, "Starting setup sensor data Task")
            params[0]?.retriveCapableNodes()
            return null
        }
    }

}
