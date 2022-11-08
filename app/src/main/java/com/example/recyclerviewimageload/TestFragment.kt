package com.example.recyclerviewimageload

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class TestFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var mView: View

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: Adapter

    var testList = mutableListOf<String?>()
    var isLoading = false
    var lastText = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.test_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        mContext = requireContext()
        mView = requireView()

        init()
        btnInit()
        refreshInit()
        loadInit()
    }

    // 页面初始化
    private fun init() {
        for (i in 0..9) {
            testList.add((i + 1).toString())
        }
        mRecyclerView = mView.findViewById(R.id.recycler_view)
        mAdapter = Adapter(testList, mContext)
        mAdapter.addHeader(true)
        mAdapter.addHeader(true)

        mRecyclerView.adapter = mAdapter
    }

    // 按钮初始化
    private fun btnInit() {
        // 可移动按钮
        val dragBtn: DragFloatActionButton = mView.findViewById(R.id.drag_btn)
        dragBtn.setOnClickListener {
            mRecyclerView.smoothScrollToPosition(0)
        }
        // 浮窗按钮
        val popupBtn: DragFloatActionButtonV2 = mView.findViewById(R.id.popup_btn)
        popupBtn.setOnClickListener {
            popupInit(it)
        }
    }

    // 刷新相关
    private fun refreshInit() {
        val swipeRefresh: SwipeRefreshLayout = mView.findViewById(R.id.swipe_refresh)
        swipeRefresh.setColorSchemeResources(R.color.purple_200, R.color.teal_200, R.color.black)
        swipeRefresh.setOnRefreshListener {
            val handler = Handler()
            handler.postDelayed({
                testList.add(0, "new")
                mAdapter.notifyItemInserted(mAdapter.getHeaderCount())
                swipeRefresh.isRefreshing = false
            }, 1000)
        }
    }

    // 加载相关
    private fun loadInit() {
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    // 检测发生滑动，最后一个完全可见的元素的位置是否为列表末尾
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == testList.size - 1 + mAdapter.getHeaderCount()) {
                        Log.i("scroll!!", "end reached!")
                        load()
                        isLoading = true
                    }
                }
            }
        })
    }

    // 悬浮框相关
    @SuppressLint("ClickableViewAccessibility")
    private fun popupInit(view: View) {
        val nView = layoutInflater.inflate(R.layout.drawer_popup, null, false)

        val popupWindow = PopupWindow(nView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        // 加载动画
        popupWindow.animationStyle = R.anim.anim_pop
        popupWindow.isTouchable = true
        popupWindow.setTouchInterceptor { _, _ -> false }
        popupWindow.setBackgroundDrawable(object : ColorDrawable(0x00000000) {})

        if (view.y < view.top / 2) {
            popupWindow.showAsDropDown(view, -10, 0)
        } else {
            popupWindow.showAsDropDown(view, -10, -700)
        }

        // 添加头部
        val addHeaderBtn: Button = nView.findViewById(R.id.add_header)
        addHeaderBtn.setOnClickListener {
            mAdapter.addHeader()
            mAdapter.notifyItemInserted(0)
            mRecyclerView.layoutManager?.scrollToPosition(0)
        }
        // 移除头部
        val removeHeaderBtn: Button = nView.findViewById(R.id.remove_header)
        removeHeaderBtn.setOnClickListener {
            if (mAdapter.getHeaderCount() == 0) {
                Log.i("removeHeader", "failed, there is none")
                val toastText = getString(R.string.no_header)
                displayToast(toastText)
            } else {
                mAdapter.removeHeader()
                mAdapter.notifyItemRemoved(0)
            }
        }
        // 添加尾部
        val addFooterBtn: Button = nView.findViewById(R.id.add_footer)
        addFooterBtn.setOnClickListener {
            mAdapter.addFooter()
            mAdapter.notifyItemInserted(mAdapter.itemCount - 1)
            mRecyclerView.layoutManager?.scrollToPosition(mAdapter.itemCount - 1)
        }
        // 移除尾部
        val removeFooterBtn: Button = nView.findViewById(R.id.remove_footer)
        removeFooterBtn.setOnClickListener {
            if (mAdapter.getFooterCount() == 0) {
                Log.i("removeFooter", "failed, there is none")
                val toastText = getString(R.string.no_footer)
                displayToast(toastText)
            } else {
                mAdapter.removeFooter()
                mAdapter.notifyItemRemoved(mAdapter.itemCount)
            }
        }
    }

    private fun load() {
        Log.i("loadstat", "$isLoading")
        // 添加一个空值，在Adapter内会归类于加载view
        testList.add(null)
        mAdapter.notifyItemInserted(testList.size - 1 + mAdapter.getHeaderCount())
        // 移动屏幕到能显示加载的位置
        mRecyclerView.layoutManager?.scrollToPosition(testList.size - 1 + mAdapter.getHeaderCount())

        // 使用Handler不断循环执行
        val handler = Handler()
        handler.postDelayed({
            // 移除加载Item
            testList.removeAt(testList.size - 1)
            val currentPosition: Int = testList.size + mAdapter.getHeaderCount()
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

    private fun displayToast(text: String) {
        if (MainActivity.mToast != null) {
            MainActivity.mToast!!.cancel()
        }
        MainActivity.mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT)
        MainActivity.mToast!!.show()
    }
}