package ke.co.neatline.brookemapper.Forms

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.jwt.JWT
import ke.co.neatline.brookemapper.*
import ke.co.neatline.brookemapper.api.ApiInterface
import ke.co.neatline.brookemapper.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Form1AgrovetMonitoring: AppCompatActivity() {
    lateinit var user:TextView
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var dialog: Dialog

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agrovets_monitor_form)

        preferences = this.getSharedPreferences("ut_manager", MODE_PRIVATE)
        editor = preferences.edit()



        val jwt = JWT(preferences.getString("token","gsdhjdsajfdsjkfdjk:gsdhjsdhjsdjhsdsdfjhsdfjh:ghsdghdsghvgdsh")!!)
        if (jwt.expiresAt!!.before(Date())) {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }

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
                    this@Form1AgrovetMonitoring,
                    android.R.layout.simple_spinner_item,
                    countries
                )
                county.adapter = countys
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val autoDate = findViewById<View>(R.id.date) as EditText

        val dateF = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
        val date = dateF.format(Calendar.getInstance().time)
        autoDate.setText(date);

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
        searchAgrovetsMonitoring(dialog)
        dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.background_transparent);
        dialog.show()
    }


    private fun searchAgrovetsMonitoring(d: Dialog) {
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

            val apiInterface = ApiInterface.create().searchAgrovetMonitoring(searchId.text.toString())

            apiInterface.enqueue( object : Callback<List<AgrovetMonitoringGetBody>> {
                override fun onResponse(call: Call<List<AgrovetMonitoringGetBody>>, response: Response<List<AgrovetMonitoringGetBody>>?) {
                    progress.visibility = View.GONE
                    if(response?.body()?.size!! > 0) {
                        System.out.println(response.body())
                        dialog.hide()
                        prefillForms(response?.body()?.get(0)!!)

                    }else {
                        error.text = "Feature not found!"
                    }
                }
                override fun onFailure(call: Call<List<AgrovetMonitoringGetBody>>, t: Throwable) {
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
        val name = findViewById<EditText>(R.id.name)
        val country = findViewById<Spinner>(R.id.country)
        val county = findViewById<Spinner>(R.id.county)
        val town = findViewById<EditText>(R.id.town)
        val unit = findViewById<EditText>(R.id.unit)
        val location = findViewById<EditText>(R.id.location)
        val date = findViewById<EditText>(R.id.date)
        val quater = findViewById<Spinner>(R.id.quater)
        val project = findViewById<EditText>(R.id.project)
        val progress = findViewById<ProgressBar>(R.id.progress)

        val lat = intent.getDoubleExtra("lat",0.0)
        val lng = intent.getDoubleExtra("lng",0.0)

        next.setOnClickListener {
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
                country.selectedItem.toString(),
                county.selectedItem.toString(),
                town.text.toString(),
                unit.text.toString(),
                location.text.toString(),
                date.text.toString(),
                quater.selectedItem.toString(),
                project.text.toString(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",

            )

            val apiInterface = ApiInterface.create().postAgrovetsMonitoring(agrovetMonitoringBody)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        print("the token is ${response?.body()?.token}")
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

    private fun prefillForms (body: AgrovetMonitoringGetBody) {
        val error = findViewById<TextView>(R.id.error)
        val name = findViewById<EditText>(R.id.name)
        val country = findViewById<Spinner>(R.id.country)
        val county = findViewById<Spinner>(R.id.county)
        val town = findViewById<EditText>(R.id.town)
        val unit = findViewById<EditText>(R.id.unit)
        val location = findViewById<EditText>(R.id.location)
        val date = findViewById<EditText>(R.id.date)
        val quater = findViewById<Spinner>(R.id.quater)
        val project = findViewById<EditText>(R.id.project)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val next = findViewById<Button>(R.id.next)

        next.text = "Update"

        //Bind data
        name.setText(body.Name)
        updateSpinner(country,body.Country)
        updateSpinner(county,body.County)
        town.setText(body.Town)
        unit.setText(body.Unit)
        location.setText(body.Location)
        date.setText(body.Date)
        updateSpinner(quater,body.Quater)
        project.setText(body.Project)

        val id = body.ID

        next.setOnClickListener {
            error.text = ""

            if (TextUtils.isEmpty(name.text.toString())) {
                error.text = "Name cannot be empty!"
                return@setOnClickListener
            }


            progress.visibility = View.VISIBLE
            val formBody1 = AgrovetMonitoringFormBody1(
                id,
                name.text.toString(),
                country.selectedItem.toString(),
                county.selectedItem.toString(),
                town.text.toString(),
                unit.text.toString(),
                location.text.toString(),
                date.text.toString(),
                quater.selectedItem.toString(),
                project.text.toString(),
            )

            System.out.println("The form data is " + formBody1)

            val apiInterface = ApiInterface.create().putAgrovetsMonitoring(body.ID,formBody1)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    if(response?.body()?.success !== null){
                        System.out.println("The body is "+ response.body())
                        val intent = Intent(this@Form1AgrovetMonitoring, Form2AgrovetMonitoring::class.java)
                        intent.putExtra("isUpdating", "true")
                        intent.putExtra("id",response?.body()?.token)
                        intent.putExtra("Education", body.Education)
                        intent.putExtra("Profession", body.Profession)
                        intent.putExtra("Registration", body.Registration)
                        intent.putExtra("Premises", body.Premises)
                        intent.putExtra("Pest", body.Pest)
                        intent.putExtra("Labelling", body.Labelling)
                        intent.putExtra("Dispense", body.Dispense)
                        intent.putExtra("Vital", body.Vital)
                        intent.putExtra("History", body.History)
                        intent.putExtra("DecisionMaking", body.DecisionMaking)
                        intent.putExtra("Prevention", body.Prevention)
                        intent.putExtra("Effects", body.Effects)
                        intent.putExtra("Referral", body.Referral)
                        intent.putExtra("FollowUp", body.FollowUp)
                        intent.putExtra("Conduct", body.Conduct)
                        startActivity(intent)
                        finish()
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
        val i = Intent(this, Monitoring::class.java)
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