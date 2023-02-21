package com.example.recyclerviewimageload

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdapterFragment : Fragment() {

    private var mContext: Context? = null

    private var mView: View? = null

    private var mRecyclerView: RecyclerView? = null

    private var mAdapter: ConcatAdapter? = null

    private var done = false

    private var imm: InputMethodManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.adapter_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        mContext = requireContext()
        mView = requireView()

        init()
        loadInit()
    }

    // 页面初始化
    private fun init() {
        mRecyclerView = mView?.findViewById(R.id.recycler_view)
        mRecyclerView?.layoutManager = LinearLayoutManager(mContext)
        val config = ConcatAdapter.Config.Builder().setIsolateViewTypes(true).build()
        mAdapter = ConcatAdapter(config, CardAdapter())
        mRecyclerView?.adapter = mAdapter

        imm = mContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        val dragBtn: EditText? = mView?.findViewById(R.id.editTextTextPersonName)
        dragBtn?.setOnClickListener {
            doinvoke()
        }

        val drBtn: Button? = mView?.findViewById(R.id.button)
        drBtn?.setOnClickListener {
            doCheck()
        }
    }

    private fun loadInit() {
        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                // 检测发生滑动，最后一个完全可见的元素的位置是否为列表末尾
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == 9) {
                    Log.i("scroll!!", "end reached!")
                    if (!done) {
                        load()
                        done = true
                    }
                }
            }
        })
    }

    private fun load() {
        val tt = ImageAdapter()

        mAdapter?.addAdapter(tt)
        Log.i("loadstat", "111111")
        Handler().postDelayed({
            mAdapter?.removeAdapter(tt)
        }, 1000)
    }

    private fun doinvoke() {
        Log.i("loadstat", "111111")
    }

    private fun doCheck() {
        imm?.showInputMethodPicker()
        Log.i("tfg", "cur subtype:${imm}")
    }
}