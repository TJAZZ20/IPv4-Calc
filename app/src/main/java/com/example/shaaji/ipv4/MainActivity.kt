package com.example.shaaji.ipv4

import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.basic_tab.*
import kotlinx.android.synthetic.main.row_data.view.*
import kotlinx.android.synthetic.main.subnetting_tab.*
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {

    //regex pattern to vaildate the ip address
    private val IpPattern =
        Pattern.compile("^(?:(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])(\\.(?!\$)|\$)){4}\$")


//    var listView: ListView? = null
    var broadcastArray = arrayListOf<String>()
    var networkArray = arrayListOf<String>()
    private var cidrS = 0


    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        fab.setOnClickListener { view ->
            val intent2 = Intent(applicationContext, HelpPop::class.java)
            startActivity(intent2)

        }

    }

    fun subnetCal(view: View) {

        if (TextUtils.isEmpty(ipeditTextSub.text)) {
            ipeditTextSub.error = "Enter IP"
            ipeditTextSub.requestFocus()
        } else if (TextUtils.isEmpty(cidrEditTextSub.text)) {
            cidrEditTextSub.error = "Enter CIDR"
            cidrEditTextSub.requestFocus()
        } else if (TextUtils.isEmpty(bitEditTextSub.text)) {
            bitEditTextSub.error = "Enter Bit's"
            bitEditTextSub.requestFocus()
        } else if (!IpPattern.matcher(ipeditTextSub.text).matches()) {
            ipeditTextSub.error = "Enter Valid IP"
            ipeditTextSub.requestFocus()
        } else {
            var ipAddressSub = ipeditTextSub.text.toString()
            val cidrTextSub = cidrEditTextSub.text.toString().toInt()
            val bitTextSub = bitEditTextSub.text.toString().toInt()

            // Separate each octet into a single parts
            ipAddressSub = "$ipAddressSub."
            val octet: Array<Int> = arrayOf(0, 0, 0, 0)
            var st = 0; var en: Int; var index: Int; var temp: Int
            for (i in 0..3) {
                index = ipAddressSub.indexOf(".", st, true)
                en = index - 1
                temp = ipAddressSub.slice(st..en).toInt()
                octet[i] = temp
                st = index + 1
            }

            try {
                // connect to the subnnecting class
                val ipv4Sub = IPv4Sub(octet[0], octet[1], octet[2], octet[3], cidrTextSub, bitTextSub)

                val (NetworkArray, BroadcastArray) = ipv4Sub.getSubnet()
                networkArray = NetworkArray
                broadcastArray = BroadcastArray
                cidrS = ipv4Sub.getCIDR()
                     if(networkArray[0]=="Invalid credentials") {

                        throw IllegalArgumentException(networkArray[0])
                    }
                    else{
                        adapterFun()
                    }
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this@MainActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }


        }// end of else statement


    }


    fun startTest(view: View) {


        when {
            TextUtils.isEmpty(IPeditText.text) -> {
                IPeditText.error = "Enter Value"
                IPeditText.requestFocus()

            }
            TextUtils.isEmpty(CDIReditText.text) -> {
                CDIReditText.error = "Enter Value"
                CDIReditText.requestFocus()

            }
            !IpPattern.matcher(IPeditText.text).matches() -> {
                IPeditText.error = "Enter Valid IP Address"
                IPeditText.requestFocus()
            }
            else -> {

                var ipAddress = IPeditText.text.toString()
                val cdirText = CDIReditText.text.toString().toInt()

                // Separate each octet into a single parts
                ipAddress = "$ipAddress."
                val octet: Array<Int> = arrayOf(0, 0, 0, 0)
                var st = 0; var en: Int; var index: Int; var temp: Int
                for (i in 0..3) {
                    index = ipAddress.indexOf(".", st, true)
                    en = index - 1
                    temp = ipAddress.slice(st..en).toInt()
                    octet[i] = temp
                    st = index + 1
                }

                // Handle errors that may occur in the IPV4 class
                try {

                    val ipv4 = IPv4(octet[0], octet[1], octet[2], octet[3], cdirText)
                    ipAddressTextView.text = ipv4.getIP()
                    netAddressTextView.text = ipv4.discoverNet()
                    ipClassTextView.text = ipv4.getClass().toString()
                    netTypeTextView.text = ipv4.getNetworkType()
                    noHostTextView.text = ipv4.getHost().toString()
                    wildCardTextView.text = ipv4.getWildCard()
                    subnetMaskTextView.text = ipv4.getMask()
                    binaryTextView.text = ipv4.getBinary()
                    Toast.makeText(this@MainActivity, "Success!!!", Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }


            }
        }

    }

 private   fun adapterFun(){
     val  listView = itemList

     val myAdapter = CustomAdapter()
     listView.adapter = myAdapter


     listView.onItemClickListener = OnItemClickListener {parent, view, position, id ->

         val intent1 = Intent(applicationContext, SubnetActivity::class.java)
                    intent1.putExtra("cidr_new", cidrS)
                    intent1.putExtra("network", networkArray[position])
                    intent1.putExtra("broadcast", broadcastArray[position])
                    intent1.putExtra("IDcount", position)
                    startActivity(intent1)

                }


    }
    inner class CustomAdapter: BaseAdapter(){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val view = layoutInflater.inflate(R.layout.row_data, null)
            val net1 = view.net
            val broad1 = view.broad
            val count1 = view.count
            broad1.text = broadcastArray[position]
            net1.text = networkArray[position]
            count1.text = (position + 1).toString()
            return view

        }

        override fun getItem(position: Int): Any {


            return networkArray[position]
        }

        override fun getItemId(position: Int): Long {

            return position.toLong()
        }

        override fun getCount(): Int {

            return broadcastArray.size
        }
    }
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return when (position) {
                0 -> BasicTab()
                1 -> SubnettingTab()
                else -> null

            }!!
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }


}
