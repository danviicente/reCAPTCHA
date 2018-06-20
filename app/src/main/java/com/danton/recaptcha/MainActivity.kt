package com.danton.recaptcha

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mIsSecretInvalid = false
    var mIsTokenInvalid = false
    var mIsTokenUsed = false
    var mIsNoErrorCaptcha = true
    companion object {
        // Unique tag required for the intent extra
        val RESULT_TEXT = "RESULT_TEXT"
        val ERROR_SECRET = "ERROR_SECRET"
        // Unique tag for the intent reply
        val TEXT_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            loadWebpage()

        buttonSecretInvalid.setOnClickListener(){
            if(mIsSecretInvalid){
                buttonSecretInvalid.background = resources.getDrawable(R.color.red)
                mIsNoErrorCaptcha = true
                mIsSecretInvalid = false
            } else {
                buttonSecretInvalid.background = resources.getDrawable(R.color.green)
                buttonTokenIvalid.background = resources.getDrawable(R.color.red)
                buttonTokenUsed.background = resources.getDrawable(R.color.red)
                mIsNoErrorCaptcha = false
                mIsSecretInvalid = true
            }
        }
        buttonTokenIvalid.setOnClickListener(){
            if(mIsTokenInvalid){
                buttonTokenIvalid.background = resources.getDrawable(R.color.red)
                mIsNoErrorCaptcha = true
                mIsTokenInvalid = false
            } else {
                buttonSecretInvalid.background = resources.getDrawable(R.color.red)
                buttonTokenIvalid.background = resources.getDrawable(R.color.green)
                buttonTokenUsed.background = resources.getDrawable(R.color.red)
                mIsNoErrorCaptcha = false
                mIsTokenInvalid = true
            }
        }
        buttonTokenUsed.setOnClickListener(){
            if(mIsTokenUsed){
                buttonTokenUsed.background = resources.getDrawable(R.color.red)
                mIsNoErrorCaptcha = true
                mIsTokenUsed = false
            } else {
                buttonSecretInvalid.background = resources.getDrawable(R.color.red)
                buttonTokenIvalid.background = resources.getDrawable(R.color.red)
                buttonTokenUsed.background = resources.getDrawable(R.color.green)
                mIsNoErrorCaptcha = false
                mIsTokenUsed = true
            }
        }
        }

    fun loadWebpage() {
        webview
        webview.loadUrl("http://www.mocky.io/v2/5b294c022f00005400f56135")
        webview.settings.javaScriptEnabled = true
        webview.addJavascriptInterface(BridigeWebViewClass(), "BridgeWebViewClass")
    }

    fun launchSecondActivity(g_response: String) {

        val intent = Intent(this, SecondActivity::class.java)
        if(mIsNoErrorCaptcha){
            intent.putExtra(RESULT_TEXT, g_response)
            intent.putExtra(ERROR_SECRET, false)
        } else if (mIsSecretInvalid){
            intent.putExtra(RESULT_TEXT, g_response)
            intent.putExtra(ERROR_SECRET, true)
        } else if (mIsTokenUsed){
            intent.putExtra(RESULT_TEXT, "03ACgFB9sAxRhFemuXSV9LrFh7WEdTiyYEr2BS8-UqyN1C2B3XllbdnDoEU2AsDera4pQ9cr1ivIba-UTMBC7mFe_JgIjeVPIjzr8mo5r_iT6uxSnrr0-KTFgppXGIqDrAe_y2-WPX5xYGx5-ILJ5xg0ZxVkcPUQ6qxtUZTJKQ0m7GlKud_RoKwsAoaDvH7MSOCpORJXlWYKyiKpvg8IbTxXw6w9QlLUZVvqd8cfRhzVn8S7A0rYS9gMj0ioSU3PF-eRQFD205MrxsryE-wEiaT0vCYWnwnGI_luNIjuT-JICPhSMEJuJY-UAolI6gmMT8R13JEGsZ6pa74STPNaJ5HKXwf2uRT-SP1A")
            intent.putExtra(ERROR_SECRET, false)
        } else {
            intent.putExtra(RESULT_TEXT, "ExemploTokenInvalido")
            intent.putExtra(ERROR_SECRET, false)
        }
        startActivityForResult(intent, TEXT_REQUEST)
    }

    inner class BridigeWebViewClass{

        @JavascriptInterface
        fun reCaptchaCallbackInAndroid(g_response: String) {
            Log.d("reCaptcha", "O TOKEN É: $g_response")
            launchSecondActivity(g_response)
        }
    }
}
