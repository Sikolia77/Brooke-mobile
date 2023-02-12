package ke.co.osl.utcollectorapp

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Incidences: AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var actionBarToggle: ActionBarDrawerToggle
    lateinit var toolbar: Toolbar
    lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobilealert_home)

        // Request User To Permit Location

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    }
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    }
                    permissions.getOrDefault(Manifest.permission.CAMERA, false) -> {
                    }
                    permissions.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false) -> {
                    }
                    else -> {
                    }
                }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE))

        //This section comprises of the navigation tools
        toolbar = findViewById(R.id.appbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer)
        navView = findViewById(R.id.nav_view)
        back = findViewById(R.id.back)

        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.isDrawerIndicatorEnabled
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()
        navView = findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> goHome()
                R.id.about -> showAbout()
                R.id.privacypolicy -> showPrivacyPolicy()
//                R.id.meterreaders -> beginMeterReading()
                R.id.datacollectors -> beginDataCollection()
            }

            true
        }

        val leaks = findViewById<ImageView>(R.id.leaks)
        val sewerburst = findViewById<ImageView>(R.id.sewerburst)
        val supplyfail = findViewById<ImageView>(R.id.supplyfail)
        val illegalconnections = findViewById<ImageView>(R.id.illegalconnections)

        leaks.setOnClickListener {
            sendLeakageData()
        }

        sewerburst.setOnClickListener {
            sendSewerBurstData()
        }

        supplyfail.setOnClickListener {
            sendSupplyFailData()
        }

        illegalconnections.setOnClickListener {
            sendIllegalConnectionData()
        }

        reportIncident ()
    }

    private fun goHome() {
        startActivity(Intent(this, Incidences::class.java))
    }

    private fun showAbout() {
        startActivity(Intent(this, About::class.java))
    }

    private fun showPrivacyPolicy() {
        startActivity(Intent(this, PrivacyPolicy::class.java))
    }

//    private fun beginMeterReading() {
//        startActivity(Intent(this, LoginPageMobileAlert::class.java))
////        startActivity(Intent(this, Incidences::class.java))
//    }

    private fun beginDataCollection() {
        startActivity(Intent(this, LoginPage::class.java))
    }

    private fun sendIllegalConnectionData() {
        val intent = Intent(this@Incidences, Reporting::class.java)
        intent.putExtra("ReportIncident", "Illegal Connection")
        startActivity(intent)
    }

    private fun sendSupplyFailData() {
        val intent = Intent(this@Incidences, Reporting::class.java)
        intent.putExtra("ReportIncident", "Supply Fail")
        startActivity(intent)
    }

    private fun sendSewerBurstData() {
        val intent = Intent(this@Incidences, Reporting::class.java)
        intent.putExtra("ReportIncident", "Sewer Burst")
        startActivity(intent)
    }

    private fun sendLeakageData() {
        val intent = Intent(this@Incidences, Reporting::class.java)
        intent.putExtra("ReportIncident", "Leakage")
        startActivity(intent)
    }

    private fun reportIncident() {

    }

}