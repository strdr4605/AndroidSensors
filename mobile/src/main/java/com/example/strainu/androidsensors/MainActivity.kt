package com.example.strainu.androidsensors

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.wearable.Wearable
import java.nio.charset.Charset


class MainActivity :
        AppCompatActivity(),
        MessageClient.OnMessageReceivedListener {

    private val SENSOR_DATA_MESSAGE_PATH = "/sensor_data"
    private val TAG = "MainActivityMobile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabClicker()

        recycler_list.layoutManager = LinearLayoutManager(this)
        recycler_list.setHasFixedSize(true)

        recycler_list.adapter = SensorsAdapter(this)
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.getPath().equals(SENSOR_DATA_MESSAGE_PATH)) {
            val message = "Received message: " + messageEvent.data.toString(Charset.defaultCharset())
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.i(TAG, message)
        }
    }

    override fun onResume() {
        super.onResume()
        Wearable.getMessageClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()
        Wearable.getMessageClient(this).removeListener(this)
    }


    fun fabClicker(): Unit {
        var toggleFab = true

        fab.setOnClickListener { view ->
            var message = ""
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