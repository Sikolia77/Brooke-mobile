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

class Form4AgrovetMonitoring: AppCompatActivity() {
    lateinit var user:TextView
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agrovets_monitor_form4)

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
        val recognition = findViewById<Spinner>(R.id.recognition)
        val findings = findViewById<Spinner>(R.id.findings)
        val problemcause = findViewById<Spinner>(R.id.problemcause)
        val rationaltreatment = findViewById<Spinner>(R.id.rationaltreatment)
        val dosecalculation = findViewById<Spinner>(R.id.dosecalculation)
        val sideeffects = findViewById<Spinner>(R.id.sideeffects)
        val routeadministration = findViewById<Spinner>(R.id.routeofadministration)
        val ownerexplanation = findViewById<Spinner>(R.id.ownerexplanation)

        next.setOnClickListener {
            error.text = ""

            val lat = intent.getDoubleExtra("lat",0.0)
            val lng = intent.getDoubleExtra("lng",0.0)

            System.out.println(lat)
            System.out.println(lng)

            progress.visibility = View.VISIBLE
            val formBody = AgrovetMonitoringBody4(
                recognition.selectedItem.toString(),
                findings.selectedItem.toString(),
                problemcause.selectedItem.toString(),
                rationaltreatment.selectedItem.toString(),
                dosecalculation.selectedItem.toString(),
                routeadministration.selectedItem.toString(),
                ownerexplanation.selectedItem.toString()
            )

            val id=intent.getStringExtra("id")
            System.out.print("form four id is $id")
            val apiInterface = ApiInterface.create().postAgrovetsMonitoringForm4(id!!,formBody)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    System.out.println(response?.body())
                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        val intent = Intent(this@Form4AgrovetMonitoring, Form5AgrovetMonitoring::class.java)
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
        val recognition = findViewById<Spinner>(R.id.recognition)
        val findings = findViewById<Spinner>(R.id.findings)
        val problemcause = findViewById<Spinner>(R.id.problemcause)
        val rationaltreatment = findViewById<Spinner>(R.id.rationaltreatment)
        val dosecalculation = findViewById<Spinner>(R.id.dosecalculation)
        val sideeffects = findViewById<Spinner>(R.id.sideeffects)
        val routeadministration = findViewById<Spinner>(R.id.routeofadministration)
        val ownerexplanation = findViewById<Spinner>(R.id.ownerexplanation)

        next.text = "Update"

        //Bind data
        updateSpinner(recognition,intent.getStringExtra("Dispense"))
        updateSpinner(findings,intent.getStringExtra("Vital"))
        updateSpinner(problemcause,intent.getStringExtra("History"))
        updateSpinner(rationaltreatment,intent.getStringExtra("DecisionMaking"))
        updateSpinner(dosecalculation,intent.getStringExtra("Prevention"))
        updateSpinner(sideeffects,intent.getStringExtra("Effects"))
        updateSpinner(routeadministration,intent.getStringExtra("Referral"))
        updateSpinner(ownerexplanation,intent.getStringExtra("FollowUp"))

        next.setOnClickListener {
            error.text = ""

            progress.visibility = View.VISIBLE
            val formBody = AgrovetMonitoringBody4(
                recognition.selectedItem.toString(),
                findings.selectedItem.toString(),
                problemcause.selectedItem.toString(),
                rationaltreatment.selectedItem.toString(),
                dosecalculation.selectedItem.toString(),
                routeadministration.selectedItem.toString(),
                ownerexplanation.selectedItem.toString(),
            )

            val id=intent.getStringExtra("id")
            val apiInterface = ApiInterface.create().postAgrovetsMonitoringForm4(id!!,formBody)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    if(response?.body()?.success !== null){
                        System.out.println("The body is "+ response.body())
                        val intent1 = Intent(this@Form4AgrovetMonitoring, Form5AgrovetMonitoring::class.java)
                        intent1.putExtra("isUpdating", "true")
                        intent1.putExtra("id",response?.body()?.token)
                        intent1.putExtra("Steroids", intent.getStringExtra("Steroids"))
                        intent1.putExtra("EyePreparation", intent.getStringExtra("EyePreparation"))
                        intent1.putExtra("Dewormers", intent.getStringExtra("Dewormers"))
                        intent1.putExtra("Antiseptic", intent.getStringExtra("Pyrethroids"))
                        intent1.putExtra("Pyrethroids", intent.getStringExtra("Pyrethroids"))
                        intent1.putExtra("PainRelief", intent.getStringExtra("PainRelief"))
                        intent1.putExtra("InjectableAntibiotic", intent.getStringExtra("InjectableAntibiotic"))
                        intent1.putExtra("OralAntibiotic", intent.getStringExtra("OralAntibiotic"))
                        intent1.putExtra("WelfareProducts", intent.getStringExtra("WelfareProducts"))
                        intent1.putExtra("MedicineStorage", intent.getStringExtra("MedicineStorage"))
                        intent1.putExtra("EquipmentDisposables", intent.getStringExtra("EquipmentDisposables"))
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
        val i = Intent(this, Form3AgrovetMonitoring::class.java)
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