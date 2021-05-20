package de.nwuensche.qrcodestorage

import android.Manifest
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

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }

        //No toast, because this will be always shown in permission dialog, which is annoying
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                //Asking for permission
                if (!alreadyAskedPermission) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),REQUEST_CODE)
                    alreadyAskedPermission = true
                } else { // Permission not granted, thus this lambda called again
                    Toast.makeText(this, "Camera initialization error: ${it.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    //TODO Implement Close
    private fun askPermissionCameraOrClose() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

            // permission is already granted


        }else{

            //persmission is not granted yet
            //Asking for permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),REQUEST_CODE)

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