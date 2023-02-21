package com.example.recyclerviewimageload

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardAdapter : RecyclerView.Adapter<CardAdapter.TextViewHolder>() {
    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        return TextViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_card, parent, false))
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.textView.text = "text:$position"
    }

    override fun getItemCount(): Int {
        return 10
    }
}