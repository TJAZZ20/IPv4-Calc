package com.example.shaaji.ipv4

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_subnet.*
import java.lang.IllegalArgumentException

class SubnetActivity : AppCompatActivity() {
    private var ipaddress: String = ""
    private var broadcast: String = ""
    private var cidr: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subnet)
        val intent = intent

        ipaddress = intent.getStringExtra("network")
        broadcast = intent.getStringExtra("broadcast")
        cidr = intent.getIntExtra("cidr_new", 0)


        // Separate each octet into a single parts
        ipaddress = "$ipaddress."
        val octet: Array<Int> = arrayOf(0, 0, 0, 0)
        var st = 0; var en: Int; var index: Int; var temp: Int
        for (i in 0..2) {
            index = ipaddress.indexOf(".", st, true)
            en = index - 1
            temp = ipaddress.slice(st..en).toInt()
            octet[i] = temp
            st = index + 1
        }
        try{

            val ipv4 = IPv4(octet[0], octet[1], octet[2], octet[3], cidr)
            newCidrtextView.text = cidr.toString()
            broadcastTextView.text= broadcast
            netAddressTextView.text = ipv4.discoverNet()
            ipClassTextView.text = ipv4.getClass().toString()
            netTypeTextView.text = ipv4.getNetworkType()
            noHostTextView.text = ipv4.getHost().toString()
            wildCardTextView.text = ipv4.getWildCard()
            subnetMaskTextView.text = ipv4.getMask()
            binaryTextView.text = ipv4.getBinary()

        }catch (e: IllegalArgumentException){
            Toast.makeText(this@SubnetActivity, e.localizedMessage, Toast.LENGTH_LONG).show()

        }







        //enable back Button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    //getting back to listview
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)

    }


}
