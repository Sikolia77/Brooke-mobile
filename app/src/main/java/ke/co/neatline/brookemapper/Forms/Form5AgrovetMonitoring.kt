package ke.co.neatline.brookemapper.Forms

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ke.co.neatline.brookemapper.Home
import ke.co.neatline.brookemapper.Monitoring
import ke.co.neatline.brookemapper.PointHome
import ke.co.neatline.brookemapper.R
import ke.co.neatline.brookemapper.api.ApiInterface
import ke.co.neatline.brookemapper.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Form5AgrovetMonitoring: AppCompatActivity() {
    lateinit var user:TextView
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agrovets_monitor_form5)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, PointHome::class.java))
        }

        var isUpdating = intent.getStringExtra("isUpdating")

        if (isUpdating != null) {
            chooseAction(isUpdating)
        }

        System.out.println("Now in form 4 isupdating is $isUpdating")

    }

    private fun chooseAction(isUpdating: String) {
        if(isUpdating == "false") {
            postFormDetails()
        } else if(isUpdating == "true") {
            prefillForms()
        }
    }

    private fun postFormDetails(){
        val next = findViewById<Button>(R.id.next)
        val error = findViewById<TextView>(R.id.error)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val steroids = findViewById<Spinner>(R.id.steroids)
        val eyepreparation = findViewById<Spinner>(R.id.eyepreparation)
        val dewormers = findViewById<Spinner>(R.id.dewormers)
        val antiseptic = findViewById<Spinner>(R.id.antiseptic)
        val pyrethroids = findViewById<Spinner>(R.id.pyrethroids)
        val painrelief = findViewById<Spinner>(R.id.painrelief)
        val injectableantibiotic = findViewById<Spinner>(R.id.injectableantibiotic)
        val oralantibiotic = findViewById<Spinner>(R.id.oralantibiotic)
        val welfareproducts = findViewById<Spinner>(R.id.welfareproducts)
        val medicinestorage = findViewById<Spinner>(R.id.medicinestorage)
        val equipmentdisposable = findViewById<Spinner>(R.id.equipmentDisposables)

        next.setOnClickListener {
            error.text = ""

            val lat = intent.getDoubleExtra("lat",0.0)
            val lng = intent.getDoubleExtra("lng",0.0)

            System.out.println(lat)
            System.out.println(lng)

            progress.visibility = View.VISIBLE
            val formBody = AgrovetMonitoringBody5(
                steroids.selectedItem.toString(),
                eyepreparation.selectedItem.toString(),
                dewormers.selectedItem.toString(),
                antiseptic.selectedItem.toString(),
                pyrethroids.selectedItem.toString(),
                painrelief.selectedItem.toString(),
                injectableantibiotic.selectedItem.toString(),
                oralantibiotic.selectedItem.toString(),
                welfareproducts.selectedItem.toString(),
                medicinestorage.selectedItem.toString(),
                equipmentdisposable.selectedItem.toString()
            )

            val id=intent.getStringExtra("id")

            val apiInterface = ApiInterface.create().postAgrovetsMonitoringForm5(id!!,formBody)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    System.out.println(response?.body())
                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        val intent = Intent(this@Form5AgrovetMonitoring, Form6AgrovetMonitoring::class.java)
                        intent.putExtra("id",response?.body()?.token)
                        intent.putExtra("isUpdating", "false")
                        startActivity(intent)
                    }

                    else {
                        progress.visibility = View.GONE
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

    private fun prefillForms () {
        val next = findViewById<Button>(R.id.next)
        val error = findViewById<TextView>(R.id.error)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val steroids = findViewById<Spinner>(R.id.steroids)
        val eyepreparation = findViewById<Spinner>(R.id.eyepreparation)
        val dewormers = findViewById<Spinner>(R.id.dewormers)
        val antiseptic = findViewById<Spinner>(R.id.antiseptic)
        val pyrethroids = findViewById<Spinner>(R.id.pyrethroids)
        val painrelief = findViewById<Spinner>(R.id.painrelief)
        val injectableantibiotic = findViewById<Spinner>(R.id.injectableantibiotic)
        val oralantibiotic = findViewById<Spinner>(R.id.oralantibiotic)
        val welfareproducts = findViewById<Spinner>(R.id.welfareproducts)
        val medicinestorage = findViewById<Spinner>(R.id.medicinestorage)
        val equipmentdisposable = findViewById<Spinner>(R.id.equipmentDisposables)

        next.text = "Update"

        //Bind data
        updateSpinner(steroids,intent.getStringExtra("steroids"))
        updateSpinner(eyepreparation,intent.getStringExtra("eyepreparation"))
        updateSpinner(dewormers,intent.getStringExtra("dewormers"))
        updateSpinner(antiseptic,intent.getStringExtra("antiseptic"))
        updateSpinner(pyrethroids,intent.getStringExtra("pyrethroids"))
        updateSpinner(painrelief,intent.getStringExtra("painrelief"))
        updateSpinner(injectableantibiotic,intent.getStringExtra("injectableantibiotic"))
        updateSpinner(oralantibiotic,intent.getStringExtra("oralantibiotic"))
        updateSpinner(welfareproducts,intent.getStringExtra("welfareproducts"))
        updateSpinner(medicinestorage,intent.getStringExtra("medicinestorage"))
        updateSpinner(equipmentdisposable,intent.getStringExtra("equipmentdisposable"))

        next.setOnClickListener {
            error.text = ""

            progress.visibility = View.VISIBLE
            val formBody = AgrovetMonitoringBody5(
                steroids.selectedItem.toString(),
                eyepreparation.selectedItem.toString(),
                dewormers.selectedItem.toString(),
                antiseptic.selectedItem.toString(),
                pyrethroids.selectedItem.toString(),
                painrelief.selectedItem.toString(),
                injectableantibiotic.selectedItem.toString(),
                oralantibiotic.selectedItem.toString(),
                welfareproducts.selectedItem.toString(),
                medicinestorage.selectedItem.toString(),
                equipmentdisposable.selectedItem.toString(),
            )

            val id=intent.getStringExtra("id")
            val apiInterface = ApiInterface.create().postAgrovetsMonitoringForm5(id!!,formBody)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    if(response?.body()?.success !== null){
                        System.out.println("The body is "+ response.body())
                        val intent1 = Intent(this@Form5AgrovetMonitoring, Form6AgrovetMonitoring::class.java)
                        intent1.putExtra("isUpdating", "true")
                        intent1.putExtra("id",response?.body()?.token)
                        intent1.putExtra("ProductDisplay", intent.getStringExtra("ProductDisplay"))
                        intent1.putExtra("AppropriateWastes", intent.getStringExtra("AppropriateWastes"))
                        intent1.putExtra("RecordKeeping", intent.getStringExtra("RecordKeeping"))
                        startActivity(intent1)
                    }

                    else {
                        progress.visibility = View.GONE
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
        val i = Intent(this, Form4AgrovetMonitoring::class.java)
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