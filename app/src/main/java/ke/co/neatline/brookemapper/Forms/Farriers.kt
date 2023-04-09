package ke.co.neatline.brookemapper.Forms

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.jwt.JWT
import ke.co.neatline.brookemapper.*
import ke.co.neatline.brookemapper.api.ApiInterface
import ke.co.neatline.brookemapper.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

//12713082

class Farriers: AppCompatActivity() {
    lateinit var user:TextView
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.farries_form)

        preferences = this.getSharedPreferences("ut_manager", MODE_PRIVATE)
        editor = preferences.edit()

        user = findViewById(R.id.user)

        val jwt = JWT(preferences.getString("token","gsdhjdsajfdsjkfdjk:gsdhjsdhjsdjhsdsdfjhsdfjh:ghsdghdsghvgdsh")!!)
        if (jwt.expiresAt!!.before(Date())) {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }else {
            user.text = jwt.getClaim("Name").asString()
        }

        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener {
            startActivity(Intent(this, Home::class.java))
        }

        dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.searchfeature)

        var isUpdating = intent.getBooleanExtra("isUpdating", false)

        chooseAction(isUpdating)

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
        searchFarrier(dialog)
        dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.background_transparent);
        dialog.show()
    }

    private fun searchFarrier(d: Dialog) {
        val searchtitle = d.findViewById<TextView>(R.id.searchtitle)
        val submit = d.findViewById<Button>(R.id.submit)
        val error = d.findViewById<TextView>(R.id.error)
        val searchId = d.findViewById<EditText>(R.id.searchId)
        val progress = d.findViewById<ProgressBar>(R.id.progress)

        searchtitle.text = "Search by ObjectID"

        submit.setOnClickListener {

            progress.visibility = View.VISIBLE

            if (TextUtils.isEmpty(searchtitle.text.toString())) {
                progress.visibility = View.GONE
                error.text = "Enter Object ID here to search!"
                return@setOnClickListener
            }

            val apiInterface = ApiInterface.create().searchFarrier(searchId.text.toString())

            apiInterface.enqueue( object : Callback<List<FarrierGetBody>> {
                override fun onResponse(call: Call<List<FarrierGetBody>>, response: Response<List<FarrierGetBody>>?) {
                    progress.visibility = View.GONE
                    if(response?.body() !== null && response?.body()?.size!! > 0) {
                        System.out.println(response.body())
                        dialog.hide()
                        prefillForms(response?.body()?.get(0)!!)

                    }else {
                        error.text = "Feature not found!"
                    }
                }
                override fun onFailure(call: Call<List<FarrierGetBody>>, t: Throwable) {
                    progress.visibility = View.GONE
                    System.out.println(t)
                    error.text = "Connection to server failed"
                }
            })

        }
    }

    private fun postFormDetails(){
        val next = findViewById<Button>(R.id.next)
        val error = findViewById<TextView>(R.id.error)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val name = findViewById<EditText>(R.id.name)
        val contact = findViewById<EditText>(R.id.contact)
        val group = findViewById<EditText>(R.id.group)
        val county = findViewById<Spinner>(R.id.county)
        val subcounty = findViewById<EditText>(R.id.subcounty)
        val ward = findViewById<EditText>(R.id.ward)
        val servicearea = findViewById<EditText>(R.id.servicearea)

        val lat = intent.getDoubleExtra("lat",0.0)
        val lng = intent.getDoubleExtra("lng",0.0)

        next.setOnClickListener {
            error.text = ""

            if (TextUtils.isEmpty(name.text.toString())) {
                error.text = "Agrovet Name cannot be empty!"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(servicearea.text.toString())) {
                error.text = "Service Area cannot be empty!"
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE
            val farriersBody = FarriersBody(
                name.text.toString(),
                contact.text.toString(),
                group.text.toString(),
                county.selectedItem.toString(),
                lng,
                lat,
                subcounty.text.toString(),
                ward.text.toString(),
                servicearea.text.toString(),
                user.text.toString()
            )

            System.out.println("THE FARRIERS BODY IS $farriersBody")

            val apiInterface = ApiInterface.create().postFarrier(farriersBody)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    System.out.println(response?.body())
                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        val intent = Intent(this@Farriers, Home::class.java)
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

    private fun prefillForms (body: FarrierGetBody) {
        val next = findViewById<Button>(R.id.next)
        val error = findViewById<TextView>(R.id.error)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val name = findViewById<EditText>(R.id.name)
        val contact = findViewById<EditText>(R.id.contact)
        val group = findViewById<EditText>(R.id.group)
        val county = findViewById<Spinner>(R.id.county)
        val subcounty = findViewById<EditText>(R.id.subcounty)
        val ward = findViewById<EditText>(R.id.ward)
        val servicearea = findViewById<EditText>(R.id.servicearea)

        next.text = "Update"

        //Bind data
        name.setText(body.Name)
        contact.setText(body.Contact)
        group.setText(body.Group)
        updateSpinner(county,body.County)
        subcounty.setText(body.SubCounty)
        ward.setText(body.Ward)
        servicearea.setText(body.ServiceArea)


        val id = body.ID
        val objectid = body.ObjectID

        next.setOnClickListener {
            error.text = ""
            progress.visibility = View.VISIBLE

            val farriersBody = FarrierGetBody(
                id,
                objectid,
                name.text.toString(),
                contact.text.toString(),
                group.text.toString(),
                county.selectedItem.toString(),
                subcounty.text.toString(),
                ward.text.toString(),
                servicearea.text.toString(),
                user.text.toString()
            )

            System.out.println("Agrovets is " + farriersBody)

            val apiInterface = ApiInterface.create().putFarrier(body.ID,farriersBody)
            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {

                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        val countdonwtimer = object: CountDownTimer(3000,1){
                            override fun onTick(p0: Long) {
                            }
                            override fun onFinish() {
                                progress.visibility = View.GONE
                                startActivity(Intent(this@Farriers, Home::class.java))
                                finish()
                            }
                        }
                        countdonwtimer.start()

                    }
                    else {
                        progress.visibility = View.GONE
                        error.text = response?.body()?.error
                    }
                }
                override fun onFailure(call: Call<Message>?, t: Throwable?) {
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
        val i = Intent(this, PointHome::class.java)
        startActivity(i)
        finish()
    }

}