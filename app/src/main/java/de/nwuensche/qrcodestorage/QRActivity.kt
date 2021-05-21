package de.nwuensche.qrcodestorage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*

class QRActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private val REQUEST_CODE = 1
    private var alreadyAskedPermission = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qractivity)
//        askPermissionCameraOrClose()
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        //TODO Internationalize
        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
                val d = Intent()
                d.putExtra("value", it.text)
                setResult(RESULT_OK, d)
                finish()
        }

        //No toast, because this will be always shown in permission dialog, which is annoying
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                //Asking for permission
                if (!alreadyAskedPermission) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),REQUEST_CODE) // Permission Dialog
                    alreadyAskedPermission = true
                } else { // Permission not granted or touched outside of dialog, thus this lambda called again
                    val d = Intent()
                    d.putExtra("value", "") //There is no empty QR Code, so this is enough to show permission denied
                    setResult(RESULT_OK, d)
                    finish()
                }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    //TODO Rebase Code
}