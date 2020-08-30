package com.A_Developer.CoinVpn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class HelpUsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var txtName : EditText
    private lateinit var txtEmailAddress : EditText
    private lateinit var txtDesc : EditText
    private lateinit var btnSendFeedback : Button

    var isDataValid = true
    var mailContent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_us)
        supportActionBar!!.hide()

        initControl()
    }

    override fun onClick(view: View?) {
        when(view){
            btnSendFeedback -> {
                validData()
            }
        }
    }

    private fun initControl(){
        txtName = findViewById(R.id.txt_name)
        txtEmailAddress = findViewById(R.id.txt_email_address)
        txtDesc = findViewById(R.id.txt_desc)
        btnSendFeedback = findViewById(R.id.btn_send_feedback)

        btnSendFeedback.setOnClickListener(this)
    }

    private fun validData(){
        txtName.apply {
            if(text.toString().isNullOrEmpty()){
                error = "Name can\'t be blank!!."
                isDataValid = false
            }
        }
        txtEmailAddress.apply {
            if (this.text.toString().isNullOrEmpty()){
                error = "E-Mail can\'t be blank!!."
                isDataValid = false
            }
        }
        txtDesc.apply {
            if(this.text.toString().isNullOrEmpty()){
                error = "Description can\'t be blank"
                isDataValid = false
            }
        }

        if(isDataValid) {
            sendMail()
        } else {
            resetBoxes()
        }
    }

    private fun sendMail() {
        mailContent = "Name        : ${txtName.text}\n" +
                "E-Mail      : ${txtEmailAddress.text}\n" +
                "Description : ${txtDesc.text}\n"
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf("nitesh.v.suvagia@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT,"FeedBack from user")
            putExtra(Intent.EXTRA_TEXT,mailContent)
            type = "message/rfc822"
        }
        try {
            startActivity(emailIntent)
        } catch (e : Exception){ }
        resetBoxes()
    }

    private fun resetBoxes(){
        txtName.text = null
        txtEmailAddress.text = null
        txtDesc.text = null
        isDataValid = true
    }
}
