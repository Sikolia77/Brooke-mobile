package ke.co.neatline.brookemapper.Forms

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ke.co.neatline.brookemapper.Home
import ke.co.neatline.brookemapper.PointHome
import ke.co.neatline.brookemapper.R
import ke.co.neatline.brookemapper.api.ApiInterface
import ke.co.neatline.brookemapper.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Form3AgrovetMonitoring: AppCompatActivity() {
    lateinit var user:TextView
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agrovets_monitor_form3)

        preferences = this.getSharedPreferences("ut_manager", MODE_PRIVATE)
        editor = preferences.edit()

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, PointHome::class.java))
        }

        dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.searchfeature)

        var isEditing = intent.getBooleanExtra("isUpdating", false)

        chooseAction(isEditing)

    }

    private fun chooseAction(editing: Boolean) {
        if(editing) {
            showDialog()
        }
        else {
            postFormDetails()
        }
    }

    private fun showDialog() {

        dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.background_transparent);
        dialog.show()
    }


    private fun postFormDetails(){
        val next = findViewById<Button>(R.id.next)
        val error = findViewById<TextView>(R.id.error)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val dispense = findViewById<Spinner>(R.id.dispense)
        val vital = findViewById<Spinner>(R.id.vital)
        val history = findViewById<Spinner>(R.id.history)
        val decisionmaking = findViewById<Spinner>(R.id.decisionmaking)
        val prevention = findViewById<Spinner>(R.id.prevention)
        val effects = findViewById<Spinner>(R.id.effects)
        val referral = findViewById<Spinner>(R.id.referral)
        val followup = findViewById<Spinner>(R.id.followup)
        val conduct = findViewById<Spinner>(R.id.conduct)

        next.setOnClickListener {
            error.text = ""

            progress.visibility = View.VISIBLE
            val agrovetMonitoringBody3 = AgrovetMonitoringBody3(
                dispense.selectedItem.toString(),
                vital.selectedItem.toString(),
                history.selectedItem.toString(),
                decisionmaking.selectedItem.toString(),
                prevention.selectedItem.toString(),
                effects.selectedItem.toString(),
                referral.selectedItem.toString(),
                followup.selectedItem.toString(),
                conduct.selectedItem.toString()
            )
            val id=intent.getStringExtra("id")
            val apiInterface = ApiInterface.create().postAgrovetsMonitoringForm3(id!!,agrovetMonitoringBody3)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    System.out.println(response?.body())
                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        val intent = Intent(this@Form3AgrovetMonitoring, Form1AgrovetMonitoring::class.java)
                        intent.putExtra("id",response?.body()?.token)
                        intent.putExtra("isUpdating", "false")
                        startActivity(intent)
                    }
                    else {
                        error.text = response?.body()?.error
                    }
                }
                override fun onFailure(call: Call<Message>?, t: Throwable?) {
                    System.out.println(t)
                    progress.visibility = View.GONE
                    error.text = "Connection to server failed"
                }
            })
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, Home::class.java)
        startActivity(i)
        finish()
    }

    fun updateSpinner(spinner: Spinner, value: String?) {
        val myAdap: ArrayAdapter<String> =
            spinner.getAdapter() as ArrayAdapter<String>
        val spinnerPosition = myAdap.getPosition(value)
        spinner.setSelection(spinnerPosition);
    }

}