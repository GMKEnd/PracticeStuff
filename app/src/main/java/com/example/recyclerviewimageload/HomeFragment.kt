package com.example.recyclerviewimageload

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var mView: View

    private var test = false
    private val handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        mContext = requireContext()
        mView = requireView()

        handlerTestInit()
        infoInit()
    }

    private fun infoInit() {
        val infoBtn: Button = mView.findViewById(R.id.info)
        infoBtn.setOnClickListener {
            startActivity(Intent(context, InfoActivity::class.java))
        }
    }

    private fun handlerTestInit() {
        val judgeBtn: Button = mView.findViewById(R.id.judge)
        val postBtn: Button = mView.findViewById(R.id.post)
        val text: TextView = mView.findViewById(R.id.textView)
        judgeBtn.setOnClickListener {
            test = !test
            if (test) {
                text.text = getText(R.string.ttt)
            } else {
                text.text = getText(R.string.fff)
            }
        }
        postBtn.setOnClickListener {
            handler.postDelayed({
                if (test) {
                    displayToast("test is true!")
                } else {
                    displayToast("test is false!")
                }
            }, 5000)
        }
    }

    private fun displayToast(text: String) {
        if (MainActivity.mToast != null) {
            MainActivity.mToast!!.cancel()
        }
        MainActivity.mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT)
        MainActivity.mToast!!.show()
    }
}