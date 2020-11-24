package edu.umich.invezt.invezt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap
import kotlin.collections.ArrayList

class LearnActivity : AppCompatActivity() {
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String>? = null

// Initializes the expandable list view, sets adapter, and sets onClickListener for child items
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
        title = getString(R.string.app_name)
        expandableListView = findViewById(R.id.expendableList)
        if (expandableListView != null) {
            getArticles()
        }
    }

// Checks if given pattern string has a info link
// If link exist starts intent and navigates to web address
    private fun handleClick(pattern: String?) {
        val urlString: String?

    when {
        pattern.equals(getString(R.string.bear_flag)) -> {
            urlString = getString(R.string.bear_link)
        }
        pattern.equals(getString(R.string.bull_flag)) -> {
            urlString = getString(R.string.bull_link)
        }
        pattern.equals(getString(R.string.resistance)) -> {
            urlString = getString(R.string.resist_link)
        }
        pattern.equals(getString(R.string.support)) -> {
            urlString = getString(R.string.support_link)
        }
        pattern.equals(getString(R.string.elliot)) -> {
            urlString = getString(R.string.elliot_link)
        }
        pattern.equals(getString(R.string.cup)) -> {
            urlString = getString(R.string.cup_link)
        }
        pattern.equals(getString(R.string.channel)) -> {
            urlString = getString(R.string.channel_link)
        }
        else -> {
            return
        }
    }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
        startActivity(intent)
    }

    // Gets the article information for the patterns
    // Response is formatted like this:
    //    {
    //        "Bear Flag": {
    //            "pattern_name": "Bear Flag",
    //            "image": "bearflag.png",
    //            "description": "This is some description of a bear flag to be edited later.",
    //            "wiki_link": "https://speedtrader.com/how-to-trade-flag-patterns/",
    //            "price": 0.0
    //    },
    //        "Bull Flag": {
    //            "pattern_name": "Bull Flag",
    //            "image": "bullflag.png",
    //            "description": "This is some description of a bull flag to be edited later.",
    //            "wiki_link": "https://speedtrader.com/how-to-trade-flag-patterns/",
    //            "price": 0.0
    //    },
    private fun getArticles() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://167.71.176.115/pattern_articles/"

        val request = JsonObjectRequest(url, null,
            { response ->
                // Assign the information to the xml parts here
                val listData = HashMap<String, List<String>>()
                var keys = response.names()
                for (i in 0 until keys.length()) {
                    val menuItems: MutableList<String> = java.util.ArrayList()
                    val item = response.getJSONObject(keys.getString(i))
                    //menuItems.add(item.getString("pattern_name"))
                    menuItems.add(item.getString("description"))
                    listData[keys.getString(i)] = menuItems
                }

                titleList = ArrayList(listData.keys)
                adapter = CustomExpandableListAdapter(this, titleList as ArrayList<String>, listData)
                expandableListView!!.setAdapter(adapter)
                expandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                    if (listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(childPosition).equals("Links")) {
                        handleClick((titleList as ArrayList<String>)[groupPosition])
                    }
                    false
                }
            },
            {
                toast("Error getting pattern information.")
            }
        )

        queue.add(request)
    }

    // Gets all the patterns that a given user can access
    // This includes all free patterns + patterns purchased
    // Response is formatted like this:
    //    {
    //        "inveztid": "a72f0f1c08fa7441cc23f80a1eebe579067c5b7884e336cf0cbb2adf7209d5d1",
    //        "patterns": [
    //            "Bear Flag",
    //            "Bull Flag",
    //            "Channel",
    //            "Cup and Handle",
    //            "Elliott Wave",
    //            "Resistance Line",
    //            "Support Line"
    //        ]
    //    }

    private fun getPatternsBought(inveztid: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://167.71.176.115/patterns_bought/${inveztid}/"

        val request = JsonObjectRequest(url, null,
            { response ->

            },
            {
                toast("Error getting patterns bought.")
            }
        )

        queue.add(request)
    }
}
