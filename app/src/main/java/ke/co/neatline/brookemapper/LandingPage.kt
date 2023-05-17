package ke.co.neatline.brookemapper

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class LandingPage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        requestLocationPermission()


        val progress = findViewById<ProgressBar>(R.id.progress)

        val timer = object: CountDownTimer(7000, 1) {
            override fun onTick(millisUntilFinished: Long) {

                progress.progress = (7000 - (millisUntilFinished).toInt())
            }


            override fun onFinish() {
                startActivity(Intent(this@LandingPage,LoginPage::class.java))
                finish()
            }
        }
        timer.start()
    }

    fun requestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    }
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    }
                    else -> {
                    }
                }
            }
        }
        // 7o7gi9h9
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

}


