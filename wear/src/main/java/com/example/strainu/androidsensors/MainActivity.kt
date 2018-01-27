package com.example.strainu.androidsensors

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.wearable.activity.WearableActivity
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : WearableActivity() {

    private val SENSOR_DATA_CAPABILITY_NAME = "sensor_data"
    private val SENSOR_DATA_MESSAGE_PATH = "/sensor_data"
    private var sensorDataNodeId : String? = null
    private val TAG = "MainActivityWear"
    private var handler: Handler? = null
    private var runnable : Runnable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "Wear activity created")

        recycler_list.layoutManager = LinearLayoutManager(this)
        recycler_list.setHasFixedSize(true)

        recycler_list.adapter = SensorsAdapter(this)

        // Enables Always-on
        setAmbientEnabled()

        // Trigger an AsyncTask that will get the handheld device node
        StartSetupSensorDataTask().execute()

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

    private fun setupSensorData() {
        val capabilityInfo = Tasks.await(
                Wearable.getCapabilityClient(this).getCapability(
                        SENSOR_DATA_CAPABILITY_NAME, CapabilityClient.FILTER_REACHABLE))
        // capabilityInfo has the reachable nodes with the transcription capability
        updateSensorDataCapability(capabilityInfo)

        // to detect capable nodes as they connect to the wearable device,
        // can also use lambda function here
        val capabilityListener = CapabilityClient.OnCapabilityChangedListener {
                    updateSensorDataCapability(it) }
        Wearable.getCapabilityClient(this).addListener(
                capabilityListener,
                SENSOR_DATA_CAPABILITY_NAME)
    }

    private fun updateSensorDataCapability(capabilityInfo: CapabilityInfo) {
        val connectedNodes = capabilityInfo.nodes
        sensorDataNodeId = pickBestNodeId(connectedNodes)
    }

    private fun pickBestNodeId(nodes: Set<Node>): String? {
        var bestNodeId : String? = null
        // Find a nearby node or pick one arbitrarily
        for (node in nodes) {
            Log.i(TAG, "nodes " + node.id)
            if (node.isNearby()) {
                return node.id
            }
            bestNodeId = node.id
        }
        return bestNodeId
    }

    private fun sendMessage(messageData: ByteArray) {
        if (sensorDataNodeId != null) {
            val sendTask : Task<Int> =
            Wearable.getMessageClient(this).sendMessage(
                    sensorDataNodeId!!, SENSOR_DATA_MESSAGE_PATH, messageData);
            // You can add success and/or failure listeners,
            // Or you can call Tasks.await() and catch ExecutionException
            // A successful result code does not guarantee delivery of the message.
            sendTask.addOnSuccessListener {Log.i(TAG, "Message send succesufully")}
            sendTask.addOnFailureListener {Log.i(TAG, "Message failed to send")}
        } else {
            // Unable to retrieve node with transcription capability
            Log.i(TAG, "Unable to retrieve node with transcription capability")
        }
    }

    private fun setCronometer() {
        var number : Int = 0
        val message = "Cool message "
        handler = Handler()
        val miliseconds : Long = 2000

        runnable = object : Runnable {
            override fun run() {
                val fullMessage = message + number.toString()
                sendMessage(fullMessage.toByteArray())
                ++number
                handler!!.postDelayed(this, miliseconds)
            }
        }
    }

    private fun startSendingMessage() {
        handler!!.post(runnable)
    }


    private fun stopSendingMessage() {
        handler!!.removeCallbacks(runnable)
    }







    private inner class StartSetupSensorDataTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg args: Void): Void? {
            Log.i(TAG, "Starting setup sensor data Task")
            setupSensorData()
            return null
        }
    }

}
