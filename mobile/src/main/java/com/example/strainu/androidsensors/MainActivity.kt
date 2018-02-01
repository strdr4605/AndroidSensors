package com.example.strainu.androidsensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.wearable.Wearable
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.nio.charset.Charset
import java.util.*


class MainActivity :
        AppCompatActivity(),
        MessageClient.OnMessageReceivedListener {

    private val SENSOR_DATA_MESSAGE_PATH = "/sensor_data"
    private val TAG = "MainActivityMobile"
    private lateinit var handler: Handler
    private lateinit var runnable : Runnable
    private lateinit var wearSensorsDataArray: ArrayList<SensorData>
    private lateinit var mobileSensorsDataArray: ArrayList<SensorData>
    private lateinit var wearSensorsAdapter : WearSensorsAdapter
    private lateinit var mobileSensorsAdapter : MobileSensorsAdapter
    private lateinit var menu: Menu
    private var delay: Long = 200 //miliseconds


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "Mobile activity created")

        recycler_list.layoutManager = LinearLayoutManager(this)
        recycler_list.setHasFixedSize(true)

        val sensorManager : SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorsList: MutableList<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        initialiseSensorsDataArrays(sensorsList)
        registerListenersToSensors(sensorManager, sensorsList)

        wearSensorsAdapter = WearSensorsAdapter(wearSensorsDataArray)
        mobileSensorsAdapter = MobileSensorsAdapter(mobileSensorsDataArray)

        recycler_list.adapter = mobileSensorsAdapter

        setFabClickListener()

        setRunnable()
    }

    override fun onResume() {
        super.onResume()
        startRunnable()
        Wearable.getMessageClient(this).addListener(this)
        Log.i(TAG, "Mobile activity resumed")
    }

    override fun onPause() {
        super.onPause()
        stopRunnable()
        Wearable.getMessageClient(this).removeListener(this)
        Log.i(TAG, "Mobile activity paused")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu!!
        menuInflater.inflate(R.menu.custom_menu, menu)
        data_origine.text = this.menu.findItem(R.id.mobile_sensors)?.title
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle item selection
        data_origine.text = item?.title
        when (item?.itemId) {
            R.id.mobile_sensors -> recycler_list.adapter = mobileSensorsAdapter
            R.id.wear_sensors -> recycler_list.adapter = wearSensorsAdapter
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.getPath().equals(SENSOR_DATA_MESSAGE_PATH)) {
            val receivedDataString: String = messageEvent.data.toString(Charset.defaultCharset())
            Log.i(TAG, "Received message: $receivedDataString")
            wearSensorsDataArray.clear()
            wearSensorsDataArray.addAll(getSensorsDataFromJson(receivedDataString))
            wearSensorsAdapter.notifyDataSetChanged()

            ////here send wearable data through wifi

        }
    }


    private fun initialiseSensorsDataArrays(sensorsList: MutableList<Sensor>) {
        wearSensorsDataArray = ArrayList<SensorData>()
        mobileSensorsDataArray = ArrayList<SensorData>()
        sensorsList.forEach { sensor ->
            mobileSensorsDataArray.add(SensorData(sensor.name, false, "Nothing", Date().time))
        }
    }

    private fun registerListenersToSensors(sensorManager : SensorManager, sensorsList: MutableList<Sensor>) {
        mobileSensorsDataArray.forEachIndexed { index, sensorData ->
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

    private fun setRunnable() {
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                val mobileSensorsDataArrayCopy = ArrayList<SensorData>()
                mobileSensorsDataArray.forEach{mobileSensorsDataArrayCopy.add(it.copy())}
                updateMobileSensorsUiData(mobileSensorsDataArrayCopy)

                //here send mobile data through wifi

                handler.postDelayed(this, delay)
            }
        }
    }

    private fun startRunnable() {
        handler.post(runnable)
    }

    private fun stopRunnable() {
        handler.removeCallbacks(runnable)
    }

    private fun updateMobileSensorsUiData(sensorsDataArrayCopy: ArrayList<SensorData>) {
        sensorsDataArrayCopy.forEachIndexed { index, sensorData ->
            mobileSensorsAdapter.notifyItemChanged(index, sensorData)
        }
    }

    private fun getSensorsDataFromJson(receivedData: String): ArrayList<SensorData>  {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.fromJson(receivedData, object : TypeToken<ArrayList<SensorData>>() {}.type)
    }

    private fun setFabClickListener() {
        var toggleFab = true

        fab.setOnClickListener { view ->
            val message: String
            if (toggleFab) {
                toggleFab = !toggleFab
                fab.setImageDrawable(getDrawable(android.R.drawable.ic_media_pause))
                message = "Streaming started"
            } else {
                toggleFab = !toggleFab
                fab.setImageDrawable(getDrawable(android.R.drawable.ic_media_play))
                message = "Streaming in paused"
            }
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
}