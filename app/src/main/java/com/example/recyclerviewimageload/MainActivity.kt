package com.example.recyclerviewimageload

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: Adapter
    var testList = mutableListOf<String?>()
    var isLoading = false
    var lastText = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0..9) {
            testList.add((i + 1).toString())
        }
        mRecyclerView = findViewById(R.id.recycler_view)
        mAdapter = Adapter(testList)

        initRV()

        val btn: Button = findViewById(R.id.to_top)
        btn.setOnClickListener {
            mRecyclerView.smoothScrollToPosition(0)
        }
    }

    private fun initRV() {
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    // 检测发生滑动，最后一个完全可见的元素的位置是否为列表末尾
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == testList.size - 1) {
                        Log.i("scroll!!", "endreached!")
                        load()
                        isLoading = true
                    }
                }
            }
        })
    }

    fun load() {
        Log.i("loadstat", "$isLoading")
        // 添加一个空值，在Adapter内会归类于加载view
        testList.add(null)
        mAdapter.notifyItemInserted(testList.size - 1)
        // 移动屏幕到能显示加载的位置
        mRecyclerView.layoutManager?.scrollToPosition(testList.size - 1)

        // 使用Handler不断循环执行
        val handler = Handler()
        handler.postDelayed({
            // 移除加载Item
            testList.removeAt(testList.size - 1)
            val currentPosition: Int = testList.size
            mAdapter.notifyItemRemoved(currentPosition)

            lastText = if (lastText) {
                for (i in 0..9) {
                    testList.add("picture")
                }
                false
            } else {
                // 通过在列表内添加，新增10个元素
                for (i in 0..9) {
                    testList.add((i + 1 + currentPosition).toString())
                }
                true
            }

            mAdapter.notifyDataSetChanged()
            isLoading = false
        }, 2000)
    }
}

