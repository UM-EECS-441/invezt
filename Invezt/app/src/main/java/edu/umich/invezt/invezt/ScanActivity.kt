package edu.umich.invezt.invezt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class ScanActivity : AppCompatActivity() {
    //var pattern: String? = null
    var spinner: Spinner? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        spinner = findViewById(R.id.patterns_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        /*ArrayAdapter.createFromResource(
            this,
            R.array.patterns_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.prompt = "Title";*/

    }


    fun toCV(view: View?){
        val intent = Intent(this, ComputerVisionActivity::class.java)
        var pattern = "BULL_BEAR_FLAGS"

        when (spinner!!.selectedItem.toString()){
            "Support/Resistance" -> pattern="SUPPORT_RESISTANCE"
            "Bear/Bull Flag" -> pattern="BULL_BEAR_FLAGS"
        }

        intent.putExtra("PATTERN_NAME", pattern);
        Log.d("PATTERN", pattern)
        startActivity(intent)
    }
}