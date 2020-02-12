package io.github.m_mememe.npb_stats_ref

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun teamPageTransition(view: View){
        val intent: Intent = Intent(this@MainActivity, TeamActivity::class.java)
        val button: Button = findViewById(view.id) as Button
        val teamName: String = button.text.toString()
        intent.putExtra("buttonText", teamName)
        startActivity(intent)
    }
}
