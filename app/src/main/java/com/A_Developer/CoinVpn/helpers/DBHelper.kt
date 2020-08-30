package com.A_Developer.CoinVpn.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DBHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null,1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE $TAB_TOTAL_COIN($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_COIN TEXT, " +
                "$COL_COIN_DATE TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun addCoin(coinToAdd: Int) {
        val db = this.writableDatabase
        val date = SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(Date(System.currentTimeMillis()))
        if(haveToInsert(date)){
            val cv = ContentValues()
            cv.put(COL_COIN,coinToAdd)
            cv.put(COL_COIN_DATE,date)
            db.insert(TAB_TOTAL_COIN,null,cv)
        } else {
            val cv = ContentValues()
            cv.put(COL_COIN,getCoin()+coinToAdd)
            cv.put(COL_COIN_DATE,date)
            db.update(TAB_TOTAL_COIN,cv,"$COL_COIN_DATE = ?", arrayOf(date))
        }
    }

    private fun haveToInsert(date : String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TAB_TOTAL_COIN,null,"$COL_COIN_DATE = ?", arrayOf(date),null,null,null)
        cursor.moveToFirst()
        return cursor.count == 0
    }

    private fun getCoin(): Int {
        val date = SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(Date(System.currentTimeMillis()))
        val db = this.readableDatabase
        val cursor = db.query(TAB_TOTAL_COIN,null,"$COL_COIN_DATE = ?", arrayOf(date),null,null,null)
        var coin = 0
        if(cursor.moveToFirst()){
            do{
                coin = cursor.getInt(cursor.getColumnIndexOrThrow(COL_COIN))
            }while (cursor.moveToNext())
        }
        cursor.close()
        return coin
    }

    fun getTodayCoin(): String {
        val db = this.readableDatabase
        val date = SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(Date(System.currentTimeMillis()))
        val cursor = db.query(TAB_TOTAL_COIN,null,"$COL_COIN_DATE = ?", arrayOf(date),null,null,null)
        var coin = ""
        if(cursor.moveToFirst()){
            do{
                coin = cursor.getString(cursor.getColumnIndexOrThrow(COL_COIN))
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return coin
    }

    fun getTotalCoin(): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COL_COIN) as $TOTAL_COIN FROM $TAB_TOTAL_COIN",null)
        var totalCoin = ""
        if (cursor.moveToFirst()){
            totalCoin = cursor.getString(cursor.getColumnIndexOrThrow(TOTAL_COIN)) ?: "0"
        }
        cursor.close()
        db.close()
        return totalCoin
    }

    fun deductCoin(serverPrice: Int) {
        val db = this.writableDatabase
        val coinInDb = getCoin()
        val date = SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(Date(System.currentTimeMillis()))
        val cv = ContentValues()
        if(coinInDb > serverPrice) {
            cv.put(COL_COIN, coinInDb.minus(serverPrice))
        }
        cv.put(COL_COIN_DATE,date)
        db.update(TAB_TOTAL_COIN,cv,"$COL_COIN_DATE = ?", arrayOf(date))
        db.close()
    }


    companion object {
        const val DB_NAME = "VPN_APP"

        const val TAB_TOTAL_COIN = "tab_total_coin"

        const val COL_ID = "_id"
        const val COL_COIN = "coins"
        const val COL_COIN_DATE = "date"
        const val TOTAL_COIN = "total_coin"
    }
}