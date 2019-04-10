package com.example.shaaji.ipv4
/* This class is intend to perform the task of
    Subnetting that is dividing a network into
    segments of different networks
    ----> It calculate the number of sub-nets to segment
    ----> It generate the Network ID and Broadcast ID of each network
    ----> It Returns the New CIDR notation
    ----> It also returns the number of Host on each network
 CIDR stands for Classless Inter-domain routing
    |------------------------------------------------------------|
    |           The class receive the following values           |
    |         1. The IP address                                  |
    |         2. The Sub-net Mask as in CIDR notation            |
    |         3. The Number of bits to borrow                    |
    |------------------------------------------------------------|
                The class has the two types of methods the getters and private methods that performs specified task
           Getters methods
              1. getHost:- Returns the number of host on the network
              2. getCIDR:- Returns the New CIDR notation calculated
              3. getNoSubnet:- Returns the number of segmented network's
              4.  getSubnet: The Method return pair array one for the newtorks-ID and the other for Broadcast-ID
          Private Methods
              1. ClassABC:- This method works with in a single octet and does not have the ability to modify
                            the next or previous octet
              2. ClassINCR:- This method is an incremental, it task is to move around octet and do modification where needed



*/
// Declaration of the class SubIPv4
class IPv4Sub(
    oct1: Int,
    oct2: Int,
    oct3: Int,
    oct4: Int, private var mask: Int, private var borrowBits: Int){
    private val allOct: Array<Int> = arrayOf(0, 0, 0, 0)
    private val ip = "$oct1.$oct2.$oct3.$oct4."
    private val maskValue: ArrayList<Int> = arrayListOf(128, 64, 32, 16, 8, 4, 2, 1)
    init {
        // Validating the user values is in range
        require(oct1 in 0..255){"Invalid  range 0 t0 255 "}
        require(oct2 in 0..255){"Invalid  range 0 t0 255 "}
        require(oct3 in 0..255){"Invalid  range 0 t0 255 "}
        require(oct4 in 0..255){"Invalid  range 0 t0 255 "}
        require(mask in 1..32){"Invalid   range 1 t0 32 "}

        var st=0; var en: Int; var index: Int; var temp: Int
        for (i in 0..2){
            index = ip.indexOf(".", st, true)
            en = index - 1
            temp = ip.slice(st..en).toInt()
            allOct[i]= temp
            st = index + 1
        }
    }

    //val Mask = 17
    // val borrowBits =9
    private var tempOCT = getCIDR()
    private var bitsCheck = mask
    private var workingOCT = 1
    private var blockSizeArray = 0
    //var blockSize: Int
    private var noSubnets = getNoSubnet()



    fun getSubnet(): Pair<ArrayList<String>, ArrayList<String>>{
        val returnNetwork: ArrayList<String> = arrayListOf()
        val returnBroadcast: ArrayList<String> = arrayListOf()
        while (tempOCT >= 8) {
            tempOCT -= 8
            workingOCT++
            blockSizeArray = tempOCT
        }
        while(bitsCheck>=8){
            bitsCheck -=8
        }
        if (blockSizeArray==0) blockSizeArray =1

        var flag = true
        var flag1 = 0
        if (mask>=24)flag1 =mask
        // Making sure the octet is actually valid
        val workingOCT2 = when{
            workingOCT > 3 -> 3
            workingOCT == 3 -> 2
            workingOCT == 2 -> 1
            else -> 0
        }
        // Validating certain values before computing
        when {
            allOct[workingOCT2] !=0 -> flag = false
            mask + borrowBits > 32 -> flag = false
            ((mask>24) && (borrowBits>6)) || ((flag1 + borrowBits)>30) -> flag = false
            workingOCT > 4 -> blockSizeArray = 8
        }
        if (flag){
            if ((bitsCheck + borrowBits) >= 8 || (bitsCheck + borrowBits) == 0 ) {
                if(bitsCheck+borrowBits==8){
                    blockSizeArray = 1
                    val (network, broadcast) = classINCR(blockSizeArray)
                    returnNetwork.addAll(network)
                    returnBroadcast.addAll(broadcast)
                }
                else {
                    blockSizeArray = when {
                        (bitsCheck+borrowBits) == 23 -> (bitsCheck+borrowBits) -15
                        (bitsCheck+borrowBits) == 22 -> (bitsCheck+borrowBits) -14
                        (bitsCheck+borrowBits) == 21 -> (bitsCheck+borrowBits) -13
                        (bitsCheck+borrowBits) == 20 -> (bitsCheck+borrowBits) -12
                        (bitsCheck+borrowBits) == 19 -> (bitsCheck+borrowBits) -11
                        (bitsCheck+borrowBits) == 18 -> (bitsCheck+borrowBits) -10
                        (bitsCheck+borrowBits) == 17 -> (bitsCheck+borrowBits) -9
                        else -> (bitsCheck+borrowBits) - 8
                }


                    val (network, broadcast) = classINCR(blockSizeArray)
                    returnNetwork.addAll(network)
                    returnBroadcast.addAll(broadcast)

                }

            }
            else {
                val (network, broadcast) = classABC()
                returnNetwork.addAll(network)
                returnBroadcast.addAll(broadcast)
            }
        }else{returnNetwork.add("Invalid credentials")
            returnBroadcast.add("Invalid credentials")

        }// end of flag check
        return Pair(returnNetwork, returnBroadcast)
    }

    private fun classABC(): Pair<ArrayList<String>, ArrayList<String>> {
        val  blockSize= maskValue[blockSizeArray - 1]
        if (workingOCT>4){
            workingOCT = 4
        }
        val network: ArrayList<String> = arrayListOf()
        val broadcast: ArrayList<String> = arrayListOf()
        var networkID = allOct[workingOCT - 1]
        var tempSubnets = noSubnets
        var broadcastID = networkID + blockSize

        while (tempSubnets > 0) {
            when (workingOCT) {
                3 -> {
                    network.add("${allOct[0]}.${allOct[1]}.$networkID.${allOct[3]}")
                    broadcast.add("${allOct[0]}.${allOct[1]}.${broadcastID - 1}.255")
                }
                2 -> {
                    network.add("${allOct[0]}.$networkID.${allOct[2]}.${allOct[3]}")
                    broadcast.add("${allOct[0]}.${broadcastID - 1}.255.255")
                }
                4 -> {
                    network.add("${allOct[0]}.${allOct[1]}.${allOct[2]}.$networkID")
                    broadcast.add("${allOct[0]}.${allOct[1]}.${allOct[2]}.${broadcastID - 1}")
                }
            }

            networkID = broadcastID
            broadcastID += blockSize
            tempSubnets--
        }



        return Pair(network, broadcast)

    }
    private fun classINCR(block: Int): Pair<ArrayList<String>, ArrayList<String>> {
        val   blockSize= maskValue[block - 1]
        if (workingOCT>4){
            workingOCT = 4
        }
        val network: ArrayList<String> = arrayListOf()
        val broadcast: ArrayList<String> = arrayListOf()
        var networkID = allOct[workingOCT - 1]
        var tempSubnets = noSubnets
        var broadcastID = networkID + blockSize

        while (tempSubnets > 0) {
            when (workingOCT) {
                3 -> {
                    network.add("${allOct[0]}.${allOct[1]}.$networkID.${allOct[3]}")
                    broadcast.add("${allOct[0]}.${allOct[1]}.${broadcastID - 1}.255")
                }
                4 -> {
                    network.add("${allOct[0]}.${allOct[1]}.${allOct[2]}.$networkID")
                    broadcast.add("${allOct[0]}.${allOct[1]}.${allOct[2]}.${broadcastID - 1}")
                }

            }

            networkID = broadcastID
            broadcastID += blockSize


            when(workingOCT){
                3 -> {
                    if (networkID>255 || networkID==254){
                        allOct[1] +=1
                        // networkID
                        broadcastID = blockSize
                    }

                }
                4 -> {
                    if (networkID>255 || networkID==254){
                        allOct[2] +=1
                        networkID -= networkID
                        broadcastID = blockSize
                    }
                }

            }
            tempSubnets--

        }

        return Pair(network, broadcast)

    }
    private fun getNoSubnet(): Int{
        return (Math.pow(2.0, borrowBits.toDouble())).toInt()
    }
    fun getCIDR():Int{
        return mask+borrowBits
    }


}