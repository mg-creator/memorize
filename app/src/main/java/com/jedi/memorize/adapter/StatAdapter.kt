package com.jedi.memorize.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jedi.memorize.R
import com.jedi.memorize.model.Stat

class StatAdapter(val onStatClick: (Stat) -> Unit): RecyclerView.Adapter<StatAdapter.StatViewHolder>() {

    private var dataset: List<Stat> = emptyList()

    inner class StatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.statName)
        private val timeTextView: TextView = itemView.findViewById(R.id.statTime)
        private val dateTextView: TextView = itemView.findViewById(R.id.statDate)

        fun renderStat(stat: Stat) {
            nameTextView.text = stat.name
            timeTextView.text = stat.score
            dateTextView.text = stat.date
            itemView.setOnClickListener {
                onStatClick(stat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatAdapter.StatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stat, parent, false)
        return StatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: StatAdapter.StatViewHolder, position: Int) {
        holder.renderStat(dataset[position])
    }

    fun setData(newDataset: List<Stat>, statEmptyView: View) {
        dataset = newDataset.sortedBy { it.score }
        if(dataset.isEmpty()) {
            statEmptyView.visibility = View.VISIBLE
        } else {
            statEmptyView.visibility = View.INVISIBLE
        }
        notifyDataSetChanged()
    }
}