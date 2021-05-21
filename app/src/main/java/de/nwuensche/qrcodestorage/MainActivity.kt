package de.nwuensche.qrcodestorage

import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.budiyev.android.codescanner.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marcoscg.licenser.Library
import com.marcoscg.licenser.License
import com.marcoscg.licenser.LicenserDialog


//INFO Horizontal layout works out of the box
class MainActivity : AppCompatActivity() {
    val db: ItemDB by lazy {
        Room.databaseBuilder(this, ItemDB::class.java, "DB").allowMainThreadQueries().build()
    }
    val itemList: MutableList<String> by lazy {
        db.itemDAO().getAllItemValues().reversed().toMutableList()
    } // Used to query DB only once // Newest First
    val rAdapter: RAdapter by lazy {
        RAdapter(
            itemList,
            getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        )
    } //Can't init Clipboard in Adapter, so do it here
    val lManager = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.qrlist).apply {
            layoutManager = lManager
            adapter = rAdapter
        }

        findViewById<FloatingActionButton>(R.id.floatingActionButton).apply {
            setOnClickListener {
                startActivityForResult(
                    Intent(this@MainActivity, QRActivity::class.java),
                    0
                ) //Dont care about requestCode, but need result
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.license -> LicenserDialog(this)
                .setLibrary(
                    Library(
                        "Android Support Libraries",
                        "https://developer.android.com/topic/libraries/support-library/index.html",
                        License.APACHE2
                    )
                )
                .setLibrary(
                    Library(
                        "QR Code Pictogram",
                        "https://www.apache.org/licenses/LICENSE-2.0.txt",
                        License.APACHE2
                    )
                )
                .setLibrary(
                    Library(
                        "Licenser",
                        "https://github.com/marcoscgdev/Licenser",
                        License.MIT
                    )
                )
                .setLibrary(
                    Library(
                        "Code Scanner",
                        "https://github.com/yuriy-budiyev/code-scanner",
                        License.MIT
                    )
                )
                .setLibrary(
                    Library(
                        "ZXing",
                        "https://github.com/zxing/zxing",
                        License.APACHE2
                    )
                )
                .setPositiveButton(
                    android.R.string.ok
                ) { dialogInterface, _ -> dialogInterface.dismiss() }
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    //if went back in qr code activity, then resultCode is RESULT_CANCELED
    //if Camera Permission denied or touch outside of dialog, then data["value"] is "" (no empty QR Code can exist)
    //if successful data["value"] is set
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode == RESULT_CANCELED -> { // Went back in App -> Do nothing
            }
            data?.getStringExtra("value") == "" -> { //Permission error
                Toast.makeText(
                    this,
                    resources.getString(R.string.permission_denied_camera),
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> { //Have QR Code
                data?.getStringExtra("value")?.let {
                    itemList.add(0, it) //Used for display, newest at top
                    db.itemDAO()
                        .insertItem(ItemModel(value = it)) //Used for persistence, append last and reverse on startup
                    rAdapter.notifyItemInserted(0) //Update View
                    lManager.scrollToPositionWithOffset(
                        0,
                        0
                    ) //Scroll up, because new item there (otherwise will stay at old first item
                } //Add String if present
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}