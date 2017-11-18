package com.example.strainu.androidsensors

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_list.layoutManager = LinearLayoutManager(this)
        recycler_list.setHasFixedSize(true)

        recycler_list.adapter = SensorsAdapter(this)
    }
}