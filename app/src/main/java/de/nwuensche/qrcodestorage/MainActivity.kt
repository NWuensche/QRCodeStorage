package de.nwuensche.qrcodestorage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.budiyev.android.codescanner.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    val itemList = mutableListOf<String>()
    val rAdapter = RAdapter(itemList)
    //TODO Licenses Libraries
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemList.add("ihi")
        itemList.add("lol")


        val rView = findViewById<RecyclerView>(R.id.qrlist).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = rAdapter
        }


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
            data?.getStringExtra("value")?.let {
                itemList.add(it)
                rAdapter.notifyDataSetChanged()
            } //Add String if present
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}