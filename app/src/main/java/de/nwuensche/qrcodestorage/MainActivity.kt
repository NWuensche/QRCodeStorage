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
            startActivityForResult(Intent(this, QRActivity::class.java), 0) //Dont care about requestCode, but need result

        }
    }

    //if went back in qr code activity, then resultCode is RESULT_CANCELED
    //if Camera Permission denied or touch outside of dialog, then data["value"] is "" (no empty QR Code can exist)
    //if successful data["value"] is set
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_CANCELED) { // Went back in App -> Do nothing
        } else if (data?.getStringExtra("value") == "") { //Permission error
            Toast.makeText(this, "Cannot Scan QR Code Without Camera Permission!", Toast.LENGTH_LONG).show()
        } else { //Have QR Code
            Toast.makeText(this, "Scan result: ${data?.getStringExtra("value")}", Toast.LENGTH_LONG).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}