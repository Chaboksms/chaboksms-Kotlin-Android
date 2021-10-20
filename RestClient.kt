package ir.mp.java.mpjava

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Enumeration
import java.util.Hashtable

class RestClient(private val UserName: String, private val Password: String) {

    private val endpoint = "https://rest.payamak-panel.com/api/SendSMS/"

    private val sendOp = "SendSMS"
    private val sendByBaseNumber = "BaseServiceNumber"
    private val getDeliveryOp = "GetDeliveries2"
    private val getMessagesOp = "GetMessages"
    private val getCreditOp = "GetCredit"
    private val getBasePriceOp = "GetBasePrice"
    private val getUserNumbersOp = "GetUserNumbers"

    @Throws(IOException::class)
    private fun makeRequest(url: URL, values: Hashtable<String, String>): String {
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"

        val result = StringBuilder()
        var line: String

        try {
            conn.doOutput = true
            conn.setChunkedStreamingMode(0)

            //consider encoding
            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(getPostParamString(values))
            writer.flush()
            writer.close()

            //you can deserialize response as it is json
            val r = BufferedReader(InputStreamReader(conn.inputStream))
            line = r.readLine()
            while (line != null) {
                result.append(line).append('\n')
            }

        } finally {
            conn.disconnect()
        }

        return result.toString()
    }

    private fun getPostParamString(params: Hashtable<String, String>): String {
        if (params.size == 0)
            return ""

        val buf = StringBuffer()
        val keys = params.keys()
        while (keys.hasMoreElements()) {
            buf.append(if (buf.length == 0) "" else "&")
            val key = keys.nextElement()
            buf.append(key).append("=").append(params[key])
        }
        return buf.toString()
    }


    @Throws(IOException::class)
    fun Send(to: String, from: String, message: String, isflash: Boolean): String {

        val values = Hashtable<String, String>()
        values["username"] = UserName
        values["password"] = Password
        values["to"] = to
        values["from"] = from
        values["text"] = message
        values["isFlash"] = isflash.toString()

        val url = URL(endpoint + sendOp)
        return makeRequest(url, values)
    }

    @Throws(IOException::class)
    fun SendByBaseNumber(text: String, to: String, bodyId: Long): String {

        val values = Hashtable<String, String>()
        values["username"] = UserName
        values["password"] = Password
        values["to"] = to
        values["text"] = text
        values["bodyId"] = bodyId.toString()

        val url = URL(endpoint + sendByBaseNumber)
        return makeRequest(url, values)
    }

    @Throws(IOException::class)
    fun GetDelivery(recid: Long): String {

        val values = Hashtable<String, String>()
        values["username"] = UserName
        values["password"] = Password
        values["recID"] = recid.toString()

        val url = URL(endpoint + getDeliveryOp)
        return makeRequest(url, values)
    }


    @Throws(IOException::class)
    fun GetMessages(location: Int, from: String, index: String, count: Int): String {

        val values = Hashtable<String, String>()
        values["username"] = UserName
        values["password"] = Password
        values["Location"] = location.toString()
        values["From"] = from
        values["Index"] = index
        values["Count"] = count.toString()

        val url = URL(endpoint + getMessagesOp)
        return makeRequest(url, values)
    }

    @Throws(IOException::class)
    fun GetCredit(): String {

        val values = Hashtable<String, String>()
        values["username"] = UserName
        values["password"] = Password

        val url = URL(endpoint + getCreditOp)
        return makeRequest(url, values)
    }

    @Throws(IOException::class)
    fun GetBasePrice(): String {

        val values = Hashtable<String, String>()
        values["username"] = UserName
        values["password"] = Password

        val url = URL(endpoint + getBasePriceOp)
        return makeRequest(url, values)
    }

    @Throws(IOException::class)
    fun GetUserNumbers(): String {

        val values = Hashtable<String, String>()
        values["username"] = UserName
        values["password"] = Password

        val url = URL(endpoint + getUserNumbersOp)
        return makeRequest(url, values)
    }

}

