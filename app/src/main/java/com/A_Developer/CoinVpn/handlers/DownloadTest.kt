package com.A_Developer.CoinVpn.handlers

import java.net.HttpURLConnection
import java.net.URL

class DownloadTest : Thread  {

    private var fileUrl : String
    var isDownloadTestFinish = false
    var downloadSpeed = 0.0

    constructor(fileUrl: String) {
        this.fileUrl = fileUrl
    }

    override fun run() {
        try{
                sleep(2500)
                var url: URL
                var downloadedByte = 0
                var downloadElapsedTime = 0.0
                var responseCode = 0
                val startTime = System.currentTimeMillis()

                val listOfFileUrl = mutableListOf<String>()
                listOfFileUrl.add(fileUrl + "random4000x4000.jpg")
                listOfFileUrl.add(fileUrl + "random3000x3000.jpg")

                outer@ for (fileUrl in listOfFileUrl){
                    try {
                        url = URL(fileUrl)
                        val httpConn = url.openConnection() as HttpURLConnection
                        if(httpConn.responseCode == HttpURLConnection.HTTP_OK){
                            val buffer = ByteArray(10240)

                            val inputStream = httpConn.inputStream
                            var len = 0

                            while (inputStream.read(buffer) != -1){
                                len = inputStream.read(buffer)
                                downloadedByte += len
                                val endTime = System.currentTimeMillis()
                                downloadElapsedTime = (endTime - startTime) / 1000.0
                                showDownloadSpeed(downloadedByte,downloadElapsedTime)
                                if(downloadElapsedTime >= 15){
                                    break@outer
                                }
                            }
                            inputStream.close()
                            httpConn.disconnect()
                        } else {
                            println("Link Not found")
                        }
                    }catch (e : Exception){
                        println("message   ${e.message}")
                    }
                }

        }catch (e : Exception){
            println(e.message)
        }
        isDownloadTestFinish = true
        interrupt()
    }

    private fun showDownloadSpeed(downloadedByte: Int, downloadElapsedTime: Double) {
        val speed  = if(downloadedByte > 0){
            (((downloadedByte * 8) / (1000*1000))/downloadElapsedTime)
        } else {
            0.0
        }
        downloadSpeed = String.format("%.2f",speed).toDouble()
    }
}