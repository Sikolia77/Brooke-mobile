package ke.co.neatline.brookemapper

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*

class Tools: AppCompatActivity() {
    lateinit var dialog: Dialog
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tools)


//        Theme_Black_NoTitleBar_Fullscreen - style theme changed from...
        dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog)

        preferences = this.getSharedPreferences("ut_manager", MODE_PRIVATE)
        editor = preferences.edit()
        openMonitoringTools()
    }

    private fun openMonitoringTools(){
        val progress = findViewById<ProgressBar>(R.id.progress)
        val datacollection = findViewById<Button>(R.id.datacollection)
        val monitoring = findViewById<Button>(R.id.monitoring)

        datacollection.setOnClickListener { startActivity(Intent(this@Tools, Home::class.java)) }

        monitoring.setOnClickListener { startActivity(Intent(this@Tools, Monitoring::class.java)) }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, LoginPage::class.java)
        startActivity(i)
        finish()
    }

}