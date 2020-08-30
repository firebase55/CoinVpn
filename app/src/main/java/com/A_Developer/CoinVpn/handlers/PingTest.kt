package com.A_Developer.CoinVpn.handlers

import java.io.BufferedReader
import java.io.InputStreamReader

class PingTest : Thread {
    private var serverAddress : String
    var isPingTestFinished = false
    var pingRate = 0.0

    constructor(serverAddress: String) {
        this.serverAddress = serverAddress
    }

    override fun run() {

        try {
            val ps = ProcessBuilder("ping", "-c 10", serverAddress)
            ps.redirectErrorStream(true)
            val pr = ps.start()

            val br = BufferedReader(InputStreamReader(pr.inputStream))
            var line: String
            val output = ""

            while (br.readLine() != null) {
                line = br.readLine()
                if (line.contains("icmp_seq")) {
                    pingRate = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 2].replace("time=", "").toDouble()
                }
            }
            pr.waitFor()
            br.close()

        }catch (e : Exception){
            println(e.message)
        }
        isPingTestFinished = true
        interrupt()

    }
}