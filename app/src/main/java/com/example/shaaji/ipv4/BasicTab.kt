package com.example.shaaji.ipv4

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Activity
import android.widget.TextView
import kotlinx.android.synthetic.main.basic_tab.*


class BasicTab : Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                return inflater.inflate(R.layout.basic_tab, container, false)
    }

}