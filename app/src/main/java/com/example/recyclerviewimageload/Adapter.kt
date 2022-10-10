package com.example.recyclerviewimageload

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val testList: List<String?>, context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_ITEM_IMAGE = 0
    val TYPE_ITEM_TEXT = 1
    val TYPE_ITEM_LOADING = 2
    val mItemList = testList
    val mContext = context

    val TYPE_HEADER = 10
    val TYPE_FOOTER = 20

    val headerList = mutableListOf<String>()
    val footerList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM_TEXT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_item_card, parent, false)
                TextViewHolder(view) }
            TYPE_ITEM_IMAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_item, parent, false)
                ImageViewHolder(view) }
            // header % footer
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_view, parent, false)

                HeaderViewHolder(view) }
            TYPE_FOOTER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.footer_view, parent, false)
                FooterViewHolder(view) }
            // load
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading, parent, false)
                LoadViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                holder.bind(position)
            }
            is TextViewHolder -> {
                holder.bind(position.toString())
            }
            is LoadViewHolder -> {
                holder.bind()
            }
            // header & footer
            is HeaderViewHolder -> {
                holder.bind(position)
            }
            is FooterViewHolder -> {
                holder.bind(footerList[position - testList.size - headerList.size])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        // 检测当前位置是否为头部/尾部
        if (isHeaderPos(position)) {
            return TYPE_HEADER
        }
        if (isFooterPos(position)) {
            return TYPE_FOOTER
        }

        return when (testList[position - headerList.size]) {
            // 检测是否需要替换成加载界面
            null -> TYPE_ITEM_LOADING
            mContext.getString(R.string.pic) -> TYPE_ITEM_IMAGE
            else -> TYPE_ITEM_TEXT
        }
    }

    fun addHeader(init: Boolean = false) {
        if (init) {
            headerList.add("starter")
        } else {
            headerList.add("header")
        }
    }

    fun removeHeader() {
        headerList.removeLast()
    }

    fun getHeaderCount(): Int {
        return headerList.size
    }

    fun addFooter(text: String = "footer") {
        footerList.add(text)
    }

    fun removeFooter() {
        footerList.removeLast()
    }

    fun getFooterCount(): Int {
        return footerList.size
    }

    private fun isHeaderPos(position: Int): Boolean {
        return position < headerList.size
    }

    private fun isFooterPos(position: Int): Boolean {
        return position > mItemList.size + headerList.size - 1
    }

    override fun getItemCount(): Int {
        return mItemList.size + headerList.size + footerList.size
    }

    class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.image_item)

        fun bind(position: Int) {
            if (position % 2 == 0) {
                imageView.setImageResource(R.drawable.edited)
            } else if (position % 3 == 0) {
                imageView.setImageResource(R.drawable.unknown)
            } else {
                imageView.setImageResource(R.drawable._749_png_300)
            }
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
            progressbar.alpha = 0.5f
        }
    }

    class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.header_title)
        fun bind(position: Int) {
            textView.text = position.toString()
        }
    }

    class FooterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.footer_title)
        fun bind(text: String) {
            textView.text = text
        }
    }
}