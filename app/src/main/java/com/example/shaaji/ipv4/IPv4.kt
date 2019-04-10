package com.example.shaaji.ipv4

/* This class performs basic operation of ipv4
    1. Converting from digit to binary
    2. Resolving ip to its subnet
    3. Wildcard for Access Control List
    4. Number of host on the network
    5. Subnet Mask from it slash (/) notation
    6. Network Type either Private or Public
 */

open class IPv4(private val oct1: Int,
                private val oct2: Int,
                private val oct3: Int,
                private val oct4: Int,
                private val mask: Int){
    init {
        require(oct1 in 0..255){"Invalid Value range 0 to 255 "}
        require(oct2 in 0..255){"Invalid Value range 0 to 255 "}
        require(oct3 in 0..255){"Invalid Value range 0 to 255 "}
        require(oct4 in 0..255){"Invalid Value range 0 to 255 "}
        require(mask in 1..32){"Invalid Value range 1 to 32 "}
    }
    private val maskValue: ArrayList<Int> = arrayListOf(128, 192, 224, 240, 248, 252, 254, 255)
    private val weight  = arrayOf(128, 64, 32, 16, 8, 4, 2, 1)
    private val default  = 255

    fun getClass(): Char {
        var clas = 'D'
        when (oct1) {
            in 0..127 -> clas = 'A'
            in 128..191 -> clas = 'B'
            in 192..223 -> clas = 'C'
        }
        return clas
    }
    fun getNetworkType(): String{

        return when {
            oct1 == 10 -> "Private"
            (oct1 == 192) && (oct2 == 168) -> "Private"
            (oct1==172) && (oct2 in 16..31) -> "Private"
            else -> "Public"
        }
    }
    fun getBinary(): String {
        val ip = arrayOf(oct1, oct2, oct3, oct4)
        var value = ""
        for (j in ip){
            value += convert(j)
            value += " "
        }
        return value
    }
    private fun convert(oct: Int):String{

        var bin = ""
        var temp = oct
        for (i in 0..7){
            if (temp >= weight[i]){
                temp -= weight[i]
                bin +=1
            }else{
                bin += 0
            }

        }
        return bin


    }
    fun getMask(): String{
        val subnetMask: String
        fun getWeight(): Int {
            var k = 0
            var temp = mask
            return if(temp % 8 == 0) 0
            else {
                while (temp > 8 ){
                    temp -= 8
                    k = temp
                }; maskValue[k-1]
            }

        }
        subnetMask = when {
            mask < 0 -> "0.0.0.0"
            mask <= 8 -> "${maskValue[mask-1]}.0.0.0"

            else -> {
                val m = mask / 8.0


                when(m.toInt()){
                    1 -> "255.${getWeight()}.0.0"
                    2 -> "255.255.${getWeight()}.0"
                    3 -> "255.255.255.${getWeight()}"
                    else -> {
                        "255.255.255.255"
                    }
                }
            }
        }



        return subnetMask
    }
    fun getWildCard(): String{
        var card = "" ; var st = 0;  var temp: Int ; var en: Int; var index: Int
        val mask = "${getMask()}."
        for (i in 1..4){
            index = mask.indexOf(".", st, true)
            en = index - 1
            temp = mask.slice(st..en).toInt()
            card += "${default-temp}."
            st = index + 1
        }
        return    card.removeRange(card.lastIndex..card.lastIndex)

    }
    fun discoverNet(): String{
        var tempMask = mask
        val oct = tempMask / 8
        var networkID = ""

        var bits = 1
        fun getBlockSize(e: Int = 8, i: Int): Int{
            val blockSize = weight[e-1]
            return ((i / blockSize) * blockSize)
        }
        if (tempMask % 8 == 0){
            when (oct){
                1 -> networkID = "${getBlockSize(i = oct1)}.0.0.0"
                2 -> networkID = "$oct1.${getBlockSize(i = oct2)}.0.0"
                3 -> networkID = "$oct1.$oct2.${getBlockSize(i = oct3)}.0"
                4 -> networkID = "$oct1.$oct2.$oct3.${getBlockSize(i = oct4)}"
            }

        }
        else{
            while (tempMask > 8 ){
                tempMask -= 8
                bits = tempMask
            }

            when (oct){
                1 -> networkID = "$oct1.${getBlockSize(bits, i = oct2)}.0.0"
                2 -> networkID = "$oct1.$oct2.${getBlockSize(bits, i = oct3)}.0"
                3 -> networkID = "$oct1.$oct2.$oct3.${getBlockSize(bits, i = oct4)}"
            }
        }

        return networkID
    }
    open fun getHost(): Int {
        var totalHost: Double
        val default = 32
        totalHost = (default- mask).toDouble()
        totalHost = Math.pow(2.0, totalHost)
        return (totalHost.toInt())-2


    }
    fun getIP(): String{
        return "$oct1.$oct2.$oct3.$oct4  /$mask"
    }
}
