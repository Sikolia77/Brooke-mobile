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
import androidx.viewpager2.adapter.StatefulAdapter
import com.auth0.android.jwt.JWT
import ke.co.neatline.brookemapper.*
import ke.co.neatline.brookemapper.api.ApiInterface
import ke.co.neatline.brookemapper.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

//12713082

class Abattoirs: AppCompatActivity() {
    lateinit var user:TextView
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var dialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.abattoirs_form)

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

        val country = findViewById<Spinner>(R.id.country)
        val county = findViewById<Spinner>(R.id.county)

        val countryAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.country,
            android.R.layout.simple_spinner_item
        )

        country.adapter = countryAdapter

        country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val countries = when(position){
                    0 -> arrayOf("Mombasa", "Kwale", "Kilifi", "Tana River",
                        "Lamu",
                        "Taita–Taveta",
                        "Garissa",
                        "Wajir",
                        "Mandera",
                        "Marsabit",
                        "Isiolo",
                        "Meru",
                        "Tharaka-Nithi",
                        "Embu",
                        "Kitui",
                        "Machakos",
                        "Makueni",
                        "Nyandarua",
                        "Nyeri",
                        "Kirinyaga",
                        "Murang'a",
                        "Kiambu",
                        "Turkana",
                        "West Pokot",
                        "Samburu",
                        "Trans-Nzoia",
                        "Uasin Gishu",
                        "Elgeyo-Marakwet",
                        "Nandi",
                        "Baringo",
                        "Laikipia",
                        "Nakuru",
                        "Narok",
                        "Kajiado",
                        "Kericho",
                        "Bomet",
                        "Kakamega",
                        "Vihiga",
                        "Bungoma",
                        "Busia",
                        "Siaya",
                        "Kisumu",
                        "Homa Bay",
                        "Migori",
                        "Kisii",
                        "Nyamira",
                        "Nairobi"
                    )
                    1 -> arrayOf("Central", "Western", "Eastern", "Northern")
                    2 -> arrayOf("Arusha", "Dar es Salaam", "Dodoma", "Geita", "Iringa", "Kagera",
                        "Katavi",
                        "Kigoma",
                        "Kilimanjaro"	,
                        "Lindi"	,
                        "Manyara"	,
                        "Mara"	,
                        "Mbeya"	,
                        "Mjini Magharibi"	,
                        "Morogoro"	,
                        "Mtwara"	,
                        "Mwanza"	,
                        "Njombe"	,
                        "Pemba North"	,
                        "Pemba South"	,
                        "Pwani"	,
                        "Rukwa"	,
                        "Ruvuma"	,
                        "Shinyanga"	,
                        "Simiyu"	,
                        "Singida"	,
                        "Songwe"	,
                        "Tabora"	,
                        "Tanga"	,
                        "Unguja North"	,
                        "Unguja South"
                    )
                    3 -> arrayOf("Jonglei"	,
                        "Fangak" 	,
                        "Bieh" 	,
                        "Akobo" 	,
                        "Maiwut" 	,
                        "Latjor" 	,
                        "Boma" 	,
                        "Central Upper Nile" 	,
                        "Northern Upper Nile "	,
                        "Fashoda" 	,
                        "Ruweng" 	,
                        "Southern Liech "	,
                        "Northern Liech" 	,
                        "Gogrial" 	,
                        "Twic" 	,
                        "Tonj" 	,
                        "Gok" 	,
                        "Western Lake "	,
                        "Eastern Lake" 	,
                        "Aweil East "	,
                        "Lol" 	,
                        "Aweil"

                    )
                    4 -> arrayOf("Awdal",
                        "Sanaag",
                        "Sool",
                        "Togdeer",
                        "Marodi Jeh",
                        "Sahil")
                    else -> emptyArray()
                }
                val countys = ArrayAdapter(
                    this@Abattoirs,
                    android.R.layout.simple_spinner_item,
                    countries
                )
                county.adapter = countys
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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
        searchAbattoirs(dialog)
        dialog.window?.setBackgroundDrawableResource(R.drawable.background_transparent);
        dialog.show()
    }

    private fun searchAbattoirs(d: Dialog) {
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

            val apiInterface = ApiInterface.create().searchAbattoirs(searchId.text.toString())

            apiInterface.enqueue( object : Callback<List<AbattoirGetBody>> {
                override fun onResponse(call: Call<List<AbattoirGetBody>>, response: Response<List<AbattoirGetBody>>?) {
                    progress.visibility = View.GONE
                    if(response?.body() !== null && response?.body()?.size!! > 0) {
                        System.out.println(response.body())
                        dialog.hide()
                        prefillForms(response?.body()?.get(0)!!)

                    }else {
                        error.text = "Feature not found!"
                    }
                }
                override fun onFailure(call: Call<List<AbattoirGetBody>>, t: Throwable) {
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
        val country = findViewById<Spinner>(R.id.country)
        val county = findViewById<Spinner>(R.id.county)
        val subcounty = findViewById<EditText>(R.id.subcounty)
        val wards = findViewById<EditText>(R.id.ward)
        val status = findViewById<EditText>(R.id.status)

        val lat = intent.getDoubleExtra("lat",0.0)
        val lng = intent.getDoubleExtra("lng",0.0)


        next.setOnClickListener {
            error.text = ""

            if (TextUtils.isEmpty(name.text.toString())) {
                error.text = "Abattoir Name cannot be empty!"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(status.text.toString())) {
                error.text = "Status cannot be empty!"
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE
            val abattoirBody = AbattoirBody(
                name.text.toString(),
                country.selectedItem.toString(),
                county.selectedItem.toString(),
                lng,
                lat,
                subcounty.text.toString(),
                wards.text.toString(),
                status.text.toString(),
                user.text.toString()
            )

            val apiInterface = ApiInterface.create().postAbattoir(abattoirBody)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    System.out.println(response?.body())
                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        val intent = Intent(this@Abattoirs, Home::class.java)
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

    private fun prefillForms (body: AbattoirGetBody) {
        val next = findViewById<Button>(R.id.next)
        val error = findViewById<TextView>(R.id.error)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val name = findViewById<EditText>(R.id.name)
        val country = findViewById<Spinner>(R.id.country)
        val county = findViewById<Spinner>(R.id.county)
        val subcounty = findViewById<EditText>(R.id.subcounty)
        val ward = findViewById<EditText>(R.id.ward)
        val status = findViewById<EditText>(R.id.status)

        next.text = "Update"

        //Bind data
        name.setText(body.Name)
        updateSpinner(country,body.Country)
        updateSpinner(county,body.County)
        subcounty.setText(body.SubCounty)
        ward.setText(body.Ward)
        status.setText(body.Status)


        val id = body.ID
        val objectid = body.ObjectID

        next.setOnClickListener {
            error.text = ""
            progress.visibility = View.VISIBLE

            val abattoirBody = AbattoirGetBody(
                id,
                objectid,
                name.text.toString(),
                country.selectedItem.toString(),
                county.selectedItem.toString(),
                subcounty.text.toString(),
                ward.text.toString(),
                status.text.toString(),
                user.text.toString()
            )


            val apiInterface = ApiInterface.create().putAbattoir(body.ID,abattoirBody)
            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {

                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        val countdonwtimer = object: CountDownTimer(3000,1){
                            override fun onTick(p0: Long) {
                            }
                            override fun onFinish() {
                                progress.visibility = View.GONE
                                startActivity(Intent(this@Abattoirs, Home::class.java))
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

    private fun updateSpinner(spinner: Spinner, value: String?) {
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