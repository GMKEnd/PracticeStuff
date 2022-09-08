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
        mAdapter = Adapter(testList, this)
        // 初始化RV，添加OnScrollListener
        initRV()
        // 移到顶部
        val toTopBtn: Button = findViewById(R.id.to_top)
        toTopBtn.setOnClickListener {
            mRecyclerView.smoothScrollToPosition(0)
        }
        // 添加头部
        val addHeaderBtn: Button = findViewById(R.id.add_header)
        addHeaderBtn.setOnClickListener {
            mAdapter.addHeader()
            testList.add(0, "header")
            mAdapter.notifyItemInserted(0)
        }
        // 移除头部
        val removeHeaderBtn: Button = findViewById(R.id.remove_header)
        removeHeaderBtn.setOnClickListener {
            if (mAdapter.getHeaderCount() == 0) {
                Log.i("removeHeader", "failed, there is none")
            } else {
                mAdapter.removeHeader()
                testList.removeAt(0)
                mAdapter.notifyItemRemoved(0)
            }
        }
        // 添加尾部
        val addFooterBtn: Button = findViewById(R.id.add_footer)
        addFooterBtn.setOnClickListener {
            mAdapter.addFooter()
            testList.add("footer")
            mAdapter.notifyItemInserted(testList.size - 1)
        }
        // 移除尾部
        val removeFooterBtn: Button = findViewById(R.id.remove_footer)
        removeFooterBtn.setOnClickListener {
            if (mAdapter.getFooterCount() == 0) {
                Log.i("removeFooter", "failed, there is none")
            } else {
                mAdapter.removeFooter()
                testList.removeAt(testList.size - 1)
                mAdapter.notifyItemRemoved(testList.size - 1)
            }
        }
    }

    private fun initRV() {
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
/*            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    // 检测发生滑动，最后一个完全可见的元素的位置是否为列表末尾
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == testList.size - 1) {
                        Log.i("scroll!!", "end reached!")
                        load()
                        isLoading = true
                    }
                }
            }*/
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
            // 来回加载文字/图片
            lastText = if (lastText) {
                for (i in 0..9) {
                    testList.add(getString(R.string.pic))
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

