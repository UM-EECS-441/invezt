package edu.umich.invezt.invezt

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import kotlinx.android.synthetic.main.activity_scan.*
import java.util.*

class ScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        getPatterns()
    }

    // Navigates to ComputerVision Activity
    fun toVision(view: View?) {
        val intent = Intent(this, ComputerVisionActivity::class.java)
        startActivity(intent)
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

        queue.add(getRequest)
    }
}