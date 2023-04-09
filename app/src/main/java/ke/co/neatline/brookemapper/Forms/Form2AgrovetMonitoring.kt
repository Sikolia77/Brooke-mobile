package ke.co.neatline.brookemapper.Forms

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ke.co.neatline.brookemapper.*

class Form2AgrovetMonitoring: AppCompatActivity() {
    lateinit var user:TextView
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agrovets_monitor_form2)

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
        val education = findViewById<Spinner>(R.id.education)
        val registration = findViewById<Spinner>(R.id.registration)
        val premises = findViewById<Spinner>(R.id.premises)
        val pest = findViewById<Spinner>(R.id.pest)
        val labelling = findViewById<Spinner>(R.id.labelling)

        next.setOnClickListener {val intent = Intent(this@Form2AgrovetMonitoring, Monitoring::class.java)
                startActivity(intent)
        }

//        next.setOnClickListener {
//            error.text = ""
//
//            progress.visibility = View.VISIBLE
//            val agrovetMonitoringBody = AgrovetMonitoringBody2(
//                education.selectedItem.toString(),
//                registration.selectedItem.toString(),
//                premises.selectedItem.toString(),
//                pest.selectedItem.toString(),
//                labelling.selectedItem.toString()
//            )
//            val id=intent.getStringExtra("id")
//            System.out.println("the id is $id");
//            val apiInterface = ApiInterface.create().postAgrovetsMonitoringForm2(id!!,agrovetMonitoringBody)
//
//            apiInterface.enqueue( object : Callback<Message> {
//                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
//                    progress.visibility = View.GONE
//                    System.out.println(response?.body())
//                    if(response?.body()?.success !== null){
//                        error.text = response?.body()?.success
//                        val intent = Intent(this@Form2AgrovetMonitoring, Form3AgrovetMonitoring::class.java)
//                        intent.putExtra("id",response?.body()?.token)
//                        intent.putExtra("isUpdating", "false")
//                        startActivity(intent)
//                    }
//                    else {
//                        error.text = response?.body()?.error
//                    }
//                }
//                override fun onFailure(call: Call<Message>?, t: Throwable?) {
//                    System.out.println(t)
//                    progress.visibility = View.GONE
//                    error.text = "Connection to server failed"
//                }
//            })
//        }

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