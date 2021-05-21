package de.nwuensche.qrcodestorage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class RAdapter(private val itemNames: List<String>) : RecyclerView.Adapter<RAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = itemNames.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.buttonTextView.text = "Button $position - ${itemNames[position]}"
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonTextView = view.findViewById<Button>(R.id.buttonItem)
    }
}