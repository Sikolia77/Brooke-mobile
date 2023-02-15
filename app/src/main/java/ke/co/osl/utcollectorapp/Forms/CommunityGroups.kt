package ke.co.osl.utcollectorapp.Forms

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
import ke.co.osl.utcollectorapp.*
import ke.co.osl.utcollectorapp.api.ApiInterface
import ke.co.osl.utcollectorapp.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

//12713082

class CommunityGroups: AppCompatActivity() {
    lateinit var user:TextView
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.communitygroups_form)

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
        searchCommunityGroups(dialog)
        dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.background_transparent);
        dialog.show()
    }

    private fun searchCommunityGroups(d: Dialog) {
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

            val apiInterface = ApiInterface.create().searchCommunityGroups(searchId.text.toString())

            apiInterface.enqueue( object : Callback<List<CommunityGroupsGetBody>> {
                override fun onResponse(call: Call<List<CommunityGroupsGetBody>>, response: Response<List<CommunityGroupsGetBody>>?) {
                    progress.visibility = View.GONE
                    if(response?.body() !== null && response?.body()?.size!! > 0) {
                        System.out.println(response.body())
                        dialog.hide()
                        prefillForms(response?.body()?.get(0)!!)

                    }else {
                        error.text = "Feature not found!"
                    }
                }
                override fun onFailure(call: Call<List<CommunityGroupsGetBody>>, t: Throwable) {
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
        val county = findViewById<Spinner>(R.id.county)
        val subcounty = findViewById<EditText>(R.id.subcounty)
        val ward = findViewById<EditText>(R.id.ward)
        val members = findViewById<EditText>(R.id.members)
        val equines = findViewById<EditText>(R.id.equines)


        val lat = intent.getDoubleExtra("lat",0.0)
        val lng = intent.getDoubleExtra("lng",0.0)

        next.setOnClickListener {
            error.text = ""

            if (TextUtils.isEmpty(name.text.toString())) {
                error.text = "Agrovet Name cannot be empty!"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(members.text.toString())) {
                error.text = "Number of Members cannot be empty!"
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE
            val communitygroupsBody = CommunityGroupsBody(
                name.text.toString(),
                county.selectedItem.toString(),
                lng,
                lat,
                subcounty.text.toString(),
                ward.text.toString(),
                members.text.toString(),
                equines.text.toString(),
                user.text.toString()
            )


            val apiInterface = ApiInterface.create().postCommunityGroups(communitygroupsBody)

            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                    progress.visibility = View.GONE
                    System.out.println(response?.body())
                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        val intent = Intent(this@CommunityGroups, Home::class.java)
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

    private fun prefillForms (body: CommunityGroupsGetBody) {
        val next = findViewById<Button>(R.id.next)
        val error = findViewById<TextView>(R.id.error)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val name = findViewById<EditText>(R.id.name)
        val county = findViewById<Spinner>(R.id.county)
        val subcounty = findViewById<EditText>(R.id.subcounty)
        val ward = findViewById<EditText>(R.id.ward)
        val members = findViewById<EditText>(R.id.members)
        val equines = findViewById<EditText>(R.id.equines)

        next.text = "Update"

        //Bind data
        name.setText(body.Name)
        updateSpinner(county,body.County)
        subcounty.setText(body.SubCounty)
        ward.setText(body.Ward)
        members.setText(body.Members)
        equines.setText(body.Equines)


        val id = body.ID
        val objectid = body.ObjectID

        next.setOnClickListener {
            error.text = ""
            progress.visibility = View.VISIBLE

            val communitygroupsBody = CommunityGroupsGetBody(
                id,
                objectid,
                name.text.toString(),
                county.selectedItem.toString(),
                subcounty.text.toString(),
                ward.text.toString(),
                members.text.toString(),
                equines.text.toString(),
                user.text.toString()
            )

            val apiInterface = ApiInterface.create().putCommunityGroups(body.ID,communitygroupsBody)
            apiInterface.enqueue( object : Callback<Message> {
                override fun onResponse(call: Call<Message>?, response: Response<Message>?) {

                    if(response?.body()?.success !== null){
                        error.text = response?.body()?.success
                        val countdonwtimer = object: CountDownTimer(3000,1){
                            override fun onTick(p0: Long) {
                            }
                            override fun onFinish() {
                                progress.visibility = View.GONE
                                startActivity(Intent(this@CommunityGroups, Home::class.java))
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