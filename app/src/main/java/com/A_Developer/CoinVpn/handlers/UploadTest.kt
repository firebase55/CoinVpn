package com.A_Developer.CoinVpn.handlers

import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import kotlin.Exception
class UploadTest : Thread{
    private var fileUrl : String
    var isUploadComplete = false
    private var elapsedTime = 0.0
    private var uploadedKByte = 0
    private var startTime: Long = 0
    private var uploadElapsedTime = 0.0
    private var finalUploadRate = 0.0

    constructor(fileUrl: String) {
        this.fileUrl = fileUrl
    }

   val uploadSpeed : Double
    get(){
        if(uploadedKByte >= 0){
            val now = System.currentTimeMillis()
            elapsedTime = (now - startTime) / 1000.0
            return String.format("%.2f",((uploadedKByte /10000.0) * 8)/elapsedTime).toDouble()
        }
        return 0.0
    }

    override fun run() {
        sleep(2500)
        try {
            val url = URL(fileUrl)
            uploadedKByte = 0
            startTime = System.currentTimeMillis()

            val executor = Executors.newFixedThreadPool(4)
            for (i in 0 until 4) {
                executor.execute {
                    val buffer = ByteArray(150 * 1024)
                    val startTime = System.currentTimeMillis()
                    val timeout = 10

                    while (true) {
                        try {
                            val conn = url.openConnection() as HttpURLConnection
                            conn.doOutput = true
                            conn.requestMethod = "POST"
                            conn.setRequestProperty("Connection", "Keep-Alive")

                            val dos = DataOutputStream(conn.outputStream)

                            dos.write(buffer, 0, buffer.size)
                            dos.flush()

                            uploadedKByte += buffer.size / 1024.0.toInt()
                            val endTime = System.currentTimeMillis()
                            val uploadElapsedTime = (endTime - startTime) / 1000.0
                            if (uploadElapsedTime >= timeout) {
                                break
                            }

                            dos.close()
                            conn.disconnect()

                        } catch (e: Exception) {
                            println(e.message)
                        }
                    }
                }
            }
            executor.shutdown()
            while (!executor.isTerminated) {
                try {
                    sleep(100)
                } catch (e: Exception) {
                    println(e.message)
                }
            }
            val now = System.currentTimeMillis()
            uploadElapsedTime = (now - startTime) / 1000.0
            finalUploadRate = (uploadedKByte / 1000.0 * 8 / uploadElapsedTime)

        } catch (e: Exception) {
            println(e.message)
        }
        isUploadComplete = true
        interrupt()
    }
}

