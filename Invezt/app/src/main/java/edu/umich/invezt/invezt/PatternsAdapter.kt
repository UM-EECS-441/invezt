package edu.umich.invezt.invezt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.pattern_row.view.*
import org.json.JSONArray

class PatternsAdapter(private val cart: JSONArray) : RecyclerView.Adapter<PatternsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pattern_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.patternName.text = cart[position].toString()
        holder.price.text = "$ 0.99"
    }

    override fun getItemCount() = cart.length()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patternName: TextView = itemView.findViewById(R.id.patternName)
        val price: TextView = itemView.findViewById(R.id.price)
    }

}
