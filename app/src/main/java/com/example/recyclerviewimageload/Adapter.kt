package com.example.recyclerviewimageload

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val testList: List<String?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_ITEM_IMAGE = 0
    val TYPE_ITEM_TEXT = 1
    val TYPE_ITEM_LOADING = 2
    val mItemList = testList

    class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.image_item)

        fun bind(text: String, position: Int) {
            if (position % 2 == 0) {
                imageView.setImageResource(R.drawable.edited)
            } else if (position % 3 == 0) {
                imageView.setImageResource(R.drawable.unknown)
            } else {
                imageView.setImageResource(R.drawable._749_png_300)
            }
            imageView.contentDescription = text
        }
    }

    class TextViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.text_item)

        fun bind (text: String) {
            textView.text = text
        }
    }

    class LoadViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val progressbar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun bind() {
            progressbar.alpha = 1f
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM_TEXT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_item_card, parent, false)
                TextViewHolder(view) }
            TYPE_ITEM_LOADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading, parent, false)
                LoadViewHolder(view) }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_item, parent, false)
                ImageViewHolder(view) }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                testList[position]?.let { holder.bind(it, position) }
            }
            is TextViewHolder -> {
                testList[position]?.let { holder.bind(it) }
            }
            is LoadViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (testList[position]) {
            // 检测是否需要替换成加载界面
            null -> TYPE_ITEM_LOADING
            "picture" -> TYPE_ITEM_IMAGE
            else -> TYPE_ITEM_TEXT
        }
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

}