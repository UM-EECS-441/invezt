package edu.umich.invezt.invezt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toScan(view: View?) {
        val intent = Intent(this, ComputerVisionActivity::class.java)
        startActivity(intent)
    }

    fun toLearn(view: View?) {
        val intent = Intent(this, LearnActivity::class.java)
        startActivity(intent)
    }
}