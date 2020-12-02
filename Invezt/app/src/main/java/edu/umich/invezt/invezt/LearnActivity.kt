package edu.umich.invezt.invezt
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import edu.umich.invezt.invezt.App.Companion.inveztID
import org.json.JSONObject
import java.util.HashMap
import kotlin.collections.ArrayList

class LearnActivity : AppCompatActivity() {
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String>? = null
    private lateinit var wikiLinks: HashMap<String, String>

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

    // Navigates to Cart Activity
    fun toCart(view: View?) {
        if (inveztID == null) {
            toast("You must sign-in first")
        }
        else {
            val intent : Intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

// Checks if given pattern string has a info link
// If link exist starts intent and navigates to web address
    private fun handleClick(pattern: String?) {
        val urlString: String?

    when {
        pattern.equals(getString(R.string.bear_flag)) -> {
            urlString = wikiLinks[getString(R.string.bear_flag)]
        }
        pattern.equals(getString(R.string.bull_flag)) -> {
            urlString = wikiLinks[getString(R.string.bull_flag)]
        }
        pattern.equals(getString(R.string.resistance)) -> {
            urlString = wikiLinks[getString(R.string.resistance)]
        }
        pattern.equals(getString(R.string.support)) -> {
            urlString = wikiLinks[getString(R.string.support)]
        }
        pattern.equals(getString(R.string.elliott)) -> {
            urlString = wikiLinks[getString(R.string.elliott)]
        }
        pattern.equals(getString(R.string.cup)) -> {
            urlString = wikiLinks[getString(R.string.cup)]
        }
        pattern.equals(getString(R.string.channel)) -> {
            urlString = wikiLinks[getString(R.string.channel)]
        }
        else -> {
            return
        }
    }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
        startActivity(intent)
    }

    // Adds a pattern to the cart of the given user
    private fun addToCart(inveztid: String, pattern_name: String) {
        if (inveztID == null) {
            toast("You must sign-in first")
            return
        }

        val queue = Volley.newRequestQueue(this)
        val url = "https://167.71.176.115/add_to_cart/"
        val params = mapOf(
            "inveztid" to inveztid,
            "pattern_name" to pattern_name
        )

        val postRequest = JsonObjectRequest(url, JSONObject(params),
            {
                toast("Pattern is in cart.")
            },
            {
                toast("Failed adding pattern to cart.")
            })

        queue.add(postRequest)
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
                // Initializes the expandable list with data from database
                val listData = HashMap<String, List<String>>()
                wikiLinks = HashMap<String, String>()
                val keys = response.names()
                for (i in 0 until keys.length()) {
                    val menuItems: MutableList<String> = java.util.ArrayList()
                    val item = response.getJSONObject(keys.getString(i))
                    menuItems.add(item.getString("description"))
                    wikiLinks[keys.getString(i)] = item.getString("wiki_link")
                    menuItems.add("Link")
                    menuItems.add("Add")
                    listData[keys.getString(i)] = menuItems
                }

                titleList = ArrayList(listData.keys)
                adapter = CustomExpandableListAdapter(this, titleList as ArrayList<String>, listData)
                expandableListView!!.setAdapter(adapter)
                expandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                    if (listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(childPosition).equals("Link")) {
                        handleClick((titleList as ArrayList<String>)[groupPosition])
                    } else if (listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(childPosition).equals("Add")) {
                        val patternName = (this.adapter as CustomExpandableListAdapter).getGroup(groupPosition).toString()
                        val id = inveztID.toString()
                        addToCart(id, patternName)
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
