package com.example.recyclerviewimageload

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: Adapter
    var testList = mutableListOf<String?>()
    var isLoading = false
    var lastText = true

    companion object {
        var mToast: Toast? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentInit()
        // 初始化RV，添加OnScrollListener
//        initRVScrollLoad()
    }

    private fun fragmentInit() {
        val mTabLayout: TabLayout = findViewById(R.id.tab_layout)
        val mViewPager: ViewPager = findViewById(R.id.viewpager)

        val mFragments = listOf(HomeFragment(), TestFragment())
        val mTitle = listOf("home", "test")
        val mFragmentAdapter = FragmentAdapter(supportFragmentManager, mFragments, mTitle)

        mViewPager.adapter = mFragmentAdapter
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.setOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when(position) {
                    0 -> displayToast("switch to Home")
                    1 -> displayToast("switch to Test")
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                //state的状态有三个，0还没开始，1正在滑动，2滑动完毕
            }
        })
    }

    private fun displayToast(text: String) {
        if (mToast != null) {
            mToast!!.cancel()
        }
        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        mToast!!.show()
    }

    private fun initRVScrollLoad() {
        var refreshReady = false
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    // 检测发生滑动，最后一个完全可见的元素的位置是否为列表末尾
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == testList.size - 1 + mAdapter.getHeaderCount()) {
                        mAdapter.addFooter("上拉加载更多")
                        mAdapter.notifyItemInserted(testList.size - 1  + mAdapter.getHeaderCount())
                        // 移动屏幕到能显示加载的位置
                        mRecyclerView.layoutManager?.scrollToPosition(testList.size - 1 + mAdapter.getHeaderCount())
                        refreshReady = true
//                        pullToLoad()
                    }
                }
            }
        })
    }

    fun pullToLoad() {
        mRecyclerView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                return true
            }
        })
    }
}

