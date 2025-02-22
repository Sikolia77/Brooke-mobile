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

class Form3AgrovetMonitoring: AppCompatActivity() {
    lateinit var user:TextView
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agrovets_monitor_form3)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, PointHome::class.java))
        }

        var isUpdating = intent.getStringExtra("isUpdating")

        if (isUpdating != null) {
            chooseAction(isUpdating)
        }

        System.out.println("Now in form 3 isupdating is $isUpdating")

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
            val formBody = AgrovetMonitoringBody3(
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

            val intent = intent

            val id=intent.getStringExtra("id")
            System.out.print("form 3 id is $id and body is $formBody")
            val apiInterface = ApiInterface.create().postAgrovetsMonitoringForm3(id!!,formBody)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    System.out.println(response?.body())
                    if(response?.body()?.success !== null){
                        System.out.println("the error is " + response?.body()?.success)
                        error.text = response?.body()?.success
                        val intent = Intent(this@Form3AgrovetMonitoring, Form4AgrovetMonitoring::class.java)
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
        val dispense = findViewById<Spinner>(R.id.dispense)
        val vital = findViewById<Spinner>(R.id.vital)
        val history = findViewById<Spinner>(R.id.history)
        val decisionmaking = findViewById<Spinner>(R.id.decisionmaking)
        val prevention = findViewById<Spinner>(R.id.prevention)
        val effects = findViewById<Spinner>(R.id.effects)
        val referral = findViewById<Spinner>(R.id.referral)
        val followup = findViewById<Spinner>(R.id.followup)
        val conduct = findViewById<Spinner>(R.id.conduct)
        next.text = "Update"

        //Bind data
        updateSpinner(dispense,intent.getStringExtra("Dispense"))
        updateSpinner(vital,intent.getStringExtra("Vital"))
        updateSpinner(history,intent.getStringExtra("History"))
        updateSpinner(decisionmaking,intent.getStringExtra("DecisionMaking"))
        updateSpinner(prevention,intent.getStringExtra("Prevention"))
        updateSpinner(effects,intent.getStringExtra("Effects"))
        updateSpinner(referral,intent.getStringExtra("Referral"))
        updateSpinner(followup,intent.getStringExtra("FollowUp"))
        updateSpinner(conduct,intent.getStringExtra("Conduct"))

        next.setOnClickListener {
            error.text = ""

            progress.visibility = View.VISIBLE
            val formBody = AgrovetMonitoringBody3(
                dispense.selectedItem.toString(),
                vital.selectedItem.toString(),
                history.selectedItem.toString(),
                decisionmaking.selectedItem.toString(),
                prevention.selectedItem.toString(),
                effects.selectedItem.toString(),
                referral.selectedItem.toString(),
                followup.selectedItem.toString(),
                conduct.selectedItem.toString(),
            )

            val id=intent.getStringExtra("id")
            System.out.println("The form id is: $id")
            val apiInterface = ApiInterface.create().postAgrovetsMonitoringForm3(id!!,formBody)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    if(response?.body()?.success !== null){
                        System.out.println("The body is "+ response.body())
                        val intent1 = Intent(this@Form3AgrovetMonitoring, Form4AgrovetMonitoring::class.java)
                        intent1.putExtra("isUpdating", "true")
                        intent1.putExtra("id",response?.body()?.token)
                        intent1.putExtra("Recognition", intent.getStringExtra("Recognition"))
                        intent1.putExtra("Findings", intent.getStringExtra("Findings"))
                        intent1.putExtra("ProblemCause", intent.getStringExtra("Findings"))
                        intent1.putExtra("RationalTreatment", intent.getStringExtra("RationalTreatment"))
                        intent1.putExtra("DoseCalculation", intent.getStringExtra("DoseCalculation"))
                        intent1.putExtra("SideEffects", intent.getStringExtra("SideEffects"))
                        intent1.putExtra("RouteAdministration", intent.getStringExtra("RouteAdministration"))
                        intent1.putExtra("OwnerExplanation", intent.getStringExtra("OwnerExplanation"))
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
        val i = Intent(this, Form2AgrovetMonitoring::class.java)
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