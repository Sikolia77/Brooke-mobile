package ke.co.neatline.brookemapper.Forms

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ke.co.neatline.brookemapper.PointHome
import ke.co.neatline.brookemapper.R
import ke.co.neatline.brookemapper.api.ApiInterface
import ke.co.neatline.brookemapper.models.AgrovetMonitoringBody2
import ke.co.neatline.brookemapper.models.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Form2AgrovetMonitoring: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agrovets_monitor_form2)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, PointHome::class.java))
        }

//        var id = intent.getStringExtra("id")
        var isUpdating = intent.getStringExtra("isUpdating")
        if (isUpdating != null) {
            chooseAction(isUpdating)
        }

        System.out.println("The form is $isUpdating")
//        if(id == null)
//            id = ""
//        chooseAction (id)
    }

    private fun chooseAction(isUpdating: String) {
        if(isUpdating == "false") {
            postFormDetails()
        }else if(isUpdating == "true") {
            prefillForms()
        }
    }

    private fun postFormDetails(){
        val next = findViewById<Button>(R.id.next)
        val error = findViewById<TextView>(R.id.error)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val education = findViewById<Spinner>(R.id.education)
        val profession = findViewById<Spinner>(R.id.profession)
        val registration = findViewById<Spinner>(R.id.registration)
        val premises = findViewById<Spinner>(R.id.premises)
        val pest = findViewById<Spinner>(R.id.pest)
        val labelling = findViewById<Spinner>(R.id.labelling)

        next.setOnClickListener {
            error.text = ""
            progress.visibility = View.VISIBLE
            val agrovetMonitoringBody2 = AgrovetMonitoringBody2(
                education.selectedItem.toString(),
                profession.selectedItem.toString(),
                registration.selectedItem.toString(),
                premises.selectedItem.toString(),
                pest.selectedItem.toString(),
                labelling.selectedItem.toString()
            )
            val intent = intent
            val id=intent.getStringExtra("id")
            System.out.print("the id is $id")
            val apiInterface = ApiInterface.create().postAgrovetsMonitoringForm2(id!!,agrovetMonitoringBody2)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    System.out.println(response?.body())
                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        print("the form 2 token is ${response?.body()?.token}")

                        val intent = Intent(this@Form2AgrovetMonitoring, Form3AgrovetMonitoring::class.java)
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

    private fun prefillForms() {
        val error = findViewById<TextView>(R.id.error)
        val education = findViewById<Spinner>(R.id.education)
        val profession = findViewById<Spinner>(R.id.profession)
        val registration = findViewById<Spinner>(R.id.registration)
        val premises = findViewById<Spinner>(R.id.premises)
        val pest = findViewById<Spinner>(R.id.pest)
        val labelling = findViewById<Spinner>(R.id.labelling)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val next = findViewById<Button>(R.id.next)

        next.text = "Update"

        //Bind data
        updateSpinner(education,intent.getStringExtra("Education"))
        updateSpinner(profession,intent.getStringExtra("Profession"))
        updateSpinner(registration,intent.getStringExtra("Registration"))
        updateSpinner(premises,intent.getStringExtra("Premises"))
        updateSpinner(pest,intent.getStringExtra("Pest"))
        updateSpinner(labelling,intent.getStringExtra("Labelling"))


        next.setOnClickListener {
            error.text = ""
            progress.visibility = View.VISIBLE
            val agrovetMonitoringBody2 = AgrovetMonitoringBody2(
                education.selectedItem.toString(),
                profession.selectedItem.toString(),
                registration.selectedItem.toString(),
                premises.selectedItem.toString(),
                pest.selectedItem.toString(),
                labelling.selectedItem.toString(),
            )

            val id=intent.getStringExtra("id")
            val apiInterface = ApiInterface.create().postAgrovetsMonitoringForm2(id!!,agrovetMonitoringBody2)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    if(response?.body()?.success !== null){
                        System.out.println("The body is "+ response.body())
                        val intent1 = Intent(this@Form2AgrovetMonitoring, Form3AgrovetMonitoring::class.java)
                        intent1.putExtra("isUpdating", "true")
                        intent.putExtra("id",response?.body()?.token)
                        intent1.putExtra("Dispense", intent.getStringExtra("Dispense"))
                        intent1.putExtra("Vital", intent.getStringExtra("Vital"))
                        intent1.putExtra("History", intent.getStringExtra("History"))
                        intent1.putExtra("DecisionMaking", intent.getStringExtra("DecisionMaking"))
                        intent1.putExtra("Prevention", intent.getStringExtra("Prevention"))
                        intent1.putExtra("Effects", intent.getStringExtra("Effects"))
                        intent1.putExtra("Referral", intent.getStringExtra("Referral"))
                        intent1.putExtra("FollowUp", intent.getStringExtra("FollowUp"))
                        intent1.putExtra("Conduct", intent.getStringExtra("Conduct"))
                        startActivity(intent1)
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

    fun updateSpinner(spinner: Spinner, value: String?) {
        val myAdap: ArrayAdapter<String> =
            spinner.getAdapter() as ArrayAdapter<String>
        val spinnerPosition = myAdap.getPosition(value)
        spinner.setSelection(spinnerPosition);
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, Form1AgrovetMonitoring::class.java)
        startActivity(i)
        finish()
    }
}


//

