package de.nwuensche.qrcodestorage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RAdapter(private val itemNames: List<String>) : RecyclerView.Adapter<RAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = itemNames.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.textView.context
        holder.textView.text = itemNames[position]
        holder.copyView.setOnClickListener {
            Toast.makeText(context, "tst - $position", Toast.LENGTH_SHORT).show()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.textView)
        val copyView = view.findViewById<ImageView>(R.id.copyView)
    }
}