package com.example.strainu.androidsensors

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.wearable.activity.WearableActivity
import android.support.wear.widget.WearableRecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_list.layoutManager = LinearLayoutManager(this)
        recycler_list.setHasFixedSize(true)

        val myDataset= Array(20, {x -> "Sensor$x"})
        recycler_list.adapter = SensorsAdapter(myDataset)

        // Enables Always-on
        setAmbientEnabled()
    }
}
