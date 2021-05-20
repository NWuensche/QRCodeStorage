package de.nwuensche.qrcodestorage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.budiyev.android.codescanner.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            startActivity(Intent(this, QRActivity::class.java))
        }
    }
}