package com.danton.recaptcha

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.content_second.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class SecondActivity : AppCompatActivity() {

    var captcha_token = ""
    var secret_error = false
    var errors = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        setSupportActionBar(toolbar)

        val intent = intent
        //get result on intent
        captcha_token = intent.getStringExtra(MainActivity.RESULT_TEXT)
        secret_error = intent.getBooleanExtra(MainActivity.ERROR_SECRET, false)
        result_token.text = captcha_token
        if(secret_error){

            doAsync {
                //faz request para validar o token
                val result = URL("https://www.google.com/recaptcha/api/siteverify?secret=${resources.getString(R.string.secret_key_error)}&response=$captcha_token").readText()
                val gson = Gson()
                //converte o string recebido
                val mRecapchaResult = gson?.fromJson(result, RecapchaResult.RecapchaResult::class.java)

                uiThread {
                    Log.d("Request", result)
                    progress_request.visibility = View.GONE
                    setViews(mRecapchaResult)
                }
            }

        } else {
            doAsync {
                //faz request para validar o token
                val result = URL("https://www.google.com/recaptcha/api/siteverify?secret=${resources.getString(R.string.secret_key)}&response=$captcha_token").readText()
                val gson = Gson()
                //converte o string recebido
                val mRecapchaResult = gson?.fromJson(result, RecapchaResult.RecapchaResult::class.java)

                uiThread {
                    Log.d("Request", result)
                    progress_request.visibility = View.GONE
                    setViews(mRecapchaResult)
                }
            }
        }


    }

    fun setViews(mRecapchaResult: RecapchaResult.RecapchaResult){
        layout_result.visibility = View.VISIBLE
        layout_token.visibility = View.VISIBLE

        if(mRecapchaResult.success.equals(resources.getString(R.string.success_response))){
            result_text.text = resources.getString(R.string.success)
            result_text.setTextColor(resources.getColor(R.color.green))
        } else {
            result_text.text = resources.getString(R.string.fail)
            result_text.setTextColor(resources.getColor(R.color.red))
            error_layout.visibility = View.VISIBLE

            for(i in mRecapchaResult.list_errors.indices){
                errors += "${mRecapchaResult.list_errors[i]}\n"
            }
            result_text_error.text = errors
        }

    }

}
