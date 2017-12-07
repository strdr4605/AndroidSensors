package com.example.strainu.androidsensors

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabClicker()

        recycler_list.layoutManager = LinearLayoutManager(this)
        recycler_list.setHasFixedSize(true)

        recycler_list.adapter = SensorsAdapter(this)
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