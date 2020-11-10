package edu.umich.invezt.invezt

import android.content.Context
import edu.umich.invezt.invezt.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import org.json.JSONArray
import java.io.Console
import java.util.*

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

class ScanActivity : AppCompatActivity() {

    private var patterns = Vector<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val spinner: Spinner = findViewById(R.id.patterns_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.patterns_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.prompt = "Title";

        getPatterns();
    }


    fun getPatterns() {
        val queue = newRequestQueue(this)
        val url = "https://167.71.176.115/patterns_list/"

        val getRequest = JsonObjectRequest(url, null,
            { response ->
                val patternsReceived = response.getJSONArray("patterns") // "["Bear Flag", "Bull Flag"]"
                val x = patternsReceived[0]
                val y = patternsReceived[1]
                for (i in 0 until patternsReceived.length()) {
                    patterns.addElement(patternsReceived[i].toString())
                }
            },
            {
                toast("Error getting list of patterns.")
            }
        )

        queue.add(getRequest)
    }
}