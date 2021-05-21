package de.nwuensche.qrcodestorage

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter


class RAdapter(private val itemNames: List<String>, private val clipboard: ClipboardManager) :
    RecyclerView.Adapter<RAdapter.ViewHolder>() {
    val writer = QRCodeWriter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = itemNames.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.textView.context
        holder.textView.text = itemNames[position]
        holder.copyView.setOnClickListener {
            val clip = ClipData.newPlainText("QR Value", itemNames[position])
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                context,
                context.resources.getString(R.string.copied_to_clipboard),
                Toast.LENGTH_SHORT
            ).show()
        }
        holder.showQRView.setOnClickListener {
            val SIZE = 512
            val bitMatrix = writer.encode(itemNames[position], BarcodeFormat.QR_CODE, SIZE, SIZE)
            val bmp = Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.RGB_565)

            for (x in 0 until bitMatrix.width) {
                for (y in 0 until bitMatrix.height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }

            val dialog = AlertDialog.Builder(context)
            val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog, null)
            dialog.setView(dialogLayout)
            dialogLayout.findViewById<ImageView>(R.id.qrDialogView).apply {
                setImageBitmap(bmp)
            }
            dialog.create().apply {
                show()
                window?.setLayout(SIZE, SIZE)//Adjust Width to match barcode
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.textView)
        val copyView = view.findViewById<ImageView>(R.id.copyView)
        val showQRView = view.findViewById<ImageView>(R.id.showQRView)
    }
}