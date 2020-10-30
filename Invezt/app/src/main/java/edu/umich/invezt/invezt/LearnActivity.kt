package edu.umich.invezt.invezt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import edu.umich.invezt.invezt.ExpandableListData.data

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
            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableListAdapter(this, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)
            expandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                if (listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(childPosition).equals("Links")) {
                        handleClick((titleList as ArrayList<String>)[groupPosition])
                    }
                false
            }
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
}
