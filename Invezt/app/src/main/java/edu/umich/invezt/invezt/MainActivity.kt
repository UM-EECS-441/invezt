package edu.umich.invezt.invezt

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

// Navigates to ScanActivity
    fun toScan(view: View?) {
        val intent = Intent(this, ScanActivity::class.java)
        startActivity(intent)
    }

// Navigates to LearnActivity
    fun toLearn(view: View?) {
        val intent = Intent(this, LearnActivity::class.java)
        startActivity(intent)
    }
}