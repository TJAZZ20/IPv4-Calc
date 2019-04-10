package com.example.shaaji.ipv4

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem


class HelpPop : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.help_screen)

       val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        window.setLayout((width*.85).toInt(), (height*.80).toInt() )
    }
    //getting back to Main Activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)

    }
}