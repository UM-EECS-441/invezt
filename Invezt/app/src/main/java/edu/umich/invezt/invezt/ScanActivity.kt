package edu.umich.invezt.invezt

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import kotlinx.android.synthetic.main.activity_scan.*
import java.util.*

/*class ScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        getPatterns()
    }

    // Gets a list of all patterns that need to be displayed. Stored in the member variable "patterns"
    private fun getPatterns() {
        val queue = newRequestQueue(this)
        val url = "https://167.71.176.115/patterns_list/"

        val getRequest = JsonObjectRequest(url, null,
            { response ->
                val patterns = Vector<String>()
                val patternsReceived = response.getJSONArray("patterns") // "["Bear Flag", "Bull Flag"]"
                // Add beginner dummy variable
                //patterns.addElement("--Select a pattern--")
                for (i in 0 until patternsReceived.length()) {
                    val patternName = patternsReceived[i].toString()
                    // Don't allow the users to scan for support and resistance lines
                    // They should automatically be detected
                    if (patternName != "Support Line" && patternName != "Resistance Line") {
                        patterns.addElement(patternName)
                    }
                }

                // Assign pattern names here
                patterns_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, patterns)
            },
            {
                toast("Error getting list of patterns.")
            }
        )

        queue.add(getRequest)*/

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

//Bull Flag
//Bear Flag
    //Resistance Line
    //Support Line

    fun toCV(view: View?){
        val intent = Intent(this, ComputerVisionActivity::class.java)
        var pattern = "BULL_BEAR_FLAGS"

        /*when (spinner!!.selectedItem.toString()){
            "Support Line" -> pattern="SUPPORT_RESISTANCE"
            "Resistance Line" -> pattern="SUPPORT_RESISTANCE"
            "Bear Flag" -> pattern="BULL_BEAR_FLAGS"
            "Bull Flag" -> pattern="BULL_BEAR_FLAGS"
        }*/
        when (spinner!!.selectedItem.toString()){
            "Support/Resistance" -> pattern="SUPPORT_RESISTANCE"
            "Bear/Bull Flag" -> pattern="BULL_BEAR_FLAGS"
        }

        intent.putExtra("PATTERN_NAME", pattern);
        Log.d("PATTERN", pattern)
        startActivity(intent)
    }
}