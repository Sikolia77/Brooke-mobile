package ke.co.neatline.brookemapper

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ke.co.neatline.brookemapper.api.ApiInterface
import ke.co.neatline.brookemapper.models.Message
import retrofit2.*
import android.util.Patterns
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import ke.co.neatline.brookemapper.models.LoginBody
import ke.co.neatline.brookemapper.models.RecoverPasswordBody
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginPage: AppCompatActivity() {
    lateinit var dialog: Dialog
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val forgot = findViewById<TextView>(R.id.forgotPassword)

        forgot.setOnClickListener {
            showDialog()
        }

//        Theme_Black_NoTitleBar_Fullscreen - style theme changed from...
        dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog)

        preferences = this.getSharedPreferences("ut_manager", MODE_PRIVATE)
        editor = preferences.edit()
        postLoginDetails()
    }

    private fun postLoginDetails(){
        val next = findViewById<Button>(R.id.next)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val error = findViewById<TextView>(R.id.error)
        val progress = findViewById<ProgressBar>(R.id.progress)

        next.setOnClickListener {
            error.text = ""
            if(!isValidEmail(email.text.toString())) {
                 error.text = "Invalid email address"
                return@setOnClickListener
            }
            if(password.text.toString().length < 6) {
                error.text = "Password is too short!"
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE
            val loginBody = LoginBody(
                email.text.toString(),
                password.text.toString(),
            )

            val apiInterface = ApiInterface.create().loginUser(loginBody)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    System.out.println(response?.body())
                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        editor.putString("token",response?.body()?.token!!)

                        editor.commit()
                        startActivity(Intent(this@LoginPage,Tools::class.java))
                        finish()
                    }
                    else {
                        editor.putString("token","")
                        editor.commit()
                        error.text = response?.body()?.error
                    }
                }
                override fun onFailure(call: Call<Message>?, t: Throwable?) {
                    progress.visibility = View.GONE
                    System.out.println(t)
                    error.text = "Connection to server failed"
                    editor.putString("token","")
                    editor.commit()
                }
            })
        }
    }

    private fun showDialog() {
        getNewPassword(dialog)
        dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.background_transparent);
        dialog.show()
    }

    private fun getNewPassword(d:Dialog) {
        val dialogSubmit = d.findViewById<Button>(R.id.dialogSubmit)
        val dialogEmail = d.findViewById<EditText>(R.id.dialogEmail)
        val error = d.findViewById<TextView>(R.id.error)

        val hide = d.findViewById<ConstraintLayout>(R.id.parent)

        hide.setOnClickListener {
            d.hide()
        }

        dialogSubmit.setOnClickListener {
            error.text = ""
            if(!isValidEmail(dialogEmail.text.toString())) {
                error.text = "Invalid email address"
                error.text = ""
                return@setOnClickListener
            }
            val passwordRecover = RecoverPasswordBody(
                dialogEmail.text.toString()
            )

            val apiInterface = ApiInterface.create().recoverPassword(passwordRecover)

            apiInterface.enqueue( object : Callback<Message> {

                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {

                    System.out.println(response?.body())
                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        lifecycleScope.launch {
                            delay(3000)
                            dialog.hide()
                        }
                    }
                    else {
                        editor.putString("token","")
                        editor.commit()
                        error.text = response?.body()?.error
                    }
                }
                override fun onFailure(call: Call<Message>?, t: Throwable?) {
                    System.out.println(t)
                    error.text = "Connection to server failed"
                    editor.putString("token","")
                    editor.commit()
                }
            })
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, LandingPage::class.java)
        startActivity(i)
        finish()
    }

}