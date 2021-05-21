package de.nwuensche.qrcodestorage

import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import androidx.room.Room
import com.budiyev.android.codescanner.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    val db: ItemDB by lazy {  Room.databaseBuilder(this, ItemDB::class.java, "DB").allowMainThreadQueries().build() }
    val itemList: MutableList<String> by lazy { db.itemDAO().getAllItemValues().toMutableList() } // Used to query DB only once
    val rAdapter: RAdapter by lazy { RAdapter(itemList, getSystemService(CLIPBOARD_SERVICE) as ClipboardManager) }
    //TODO Licenses Libraries
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.qrlist).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = rAdapter
        }

        findViewById<FloatingActionButton>(R.id.floatingActionButton).apply {
            setOnClickListener {
                startActivityForResult(Intent(this@MainActivity, QRActivity::class.java), 0) //Dont care about requestCode, but need result
            }
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
            data?.getStringExtra("value")?.let {
                itemList.add(it) //Used for display
                db.itemDAO().insertItem(ItemModel(value = it)) //Used for persistence
                rAdapter.notifyItemInserted(itemList.size - 1)
            } //Add String if present
        }
        //TODO Cut Long Text on View
        super.onActivityResult(requestCode, resultCode, data)
    }
}