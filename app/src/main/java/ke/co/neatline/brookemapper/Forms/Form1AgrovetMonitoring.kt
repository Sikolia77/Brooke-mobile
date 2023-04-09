package ke.co.neatline.brookemapper.Forms

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
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

class Form1AgrovetMonitoring: AppCompatActivity() {
    lateinit var user:TextView
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agrovets_monitor_form)

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
        val name = findViewById<EditText>(R.id.name)
        val county = findViewById<Spinner>(R.id.county)
        val town = findViewById<EditText>(R.id.town)
        val unit = findViewById<EditText>(R.id.unit)
        val location = findViewById<EditText>(R.id.location)
        val date = findViewById<EditText>(R.id.date)
        val quater = findViewById<EditText>(R.id.quater)
        val project = findViewById<EditText>(R.id.project)
        val progress = findViewById<ProgressBar>(R.id.progress)

        val lat = intent.getDoubleExtra("lat",0.0)
        val lng = intent.getDoubleExtra("lng",0.0)

        next.setOnClickListener {
            error.text = ""

            if (TextUtils.isEmpty(name.text.toString())) {
                error.text = "Name cannot be empty!"
                return@setOnClickListener
            }


            if (TextUtils.isEmpty(unit.text.toString())) {
                error.text = "Operational Unit cannot be empty!"
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE

            val agrovetMonitoringBody = AgrovetMonitoringBody(
                name.text.toString(),
                lng,
                lat,
                county.selectedItem.toString(),
                town.text.toString(),
                unit.text.toString(),
                location.text.toString(),
                date.text.toString(),
                quater.text.toString(),
                project.text.toString(),
            )

            val apiInterface = ApiInterface.create().postAgrovetsMonitoring(agrovetMonitoringBody)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        val intent = Intent(this@Form1AgrovetMonitoring, Form2AgrovetMonitoring::class.java)

                        intent.putExtra("id",response?.body()?.token)
                        intent.putExtra("isUpdating", "false")
                        startActivity(intent)
                    }
                    else {
                        error.text = response?.body()?.error
                    }
                }
                override fun onFailure(call: Call<Message>?, t: Throwable?) {
                    progress.visibility = View.GONE
                    System.out.println(t)
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