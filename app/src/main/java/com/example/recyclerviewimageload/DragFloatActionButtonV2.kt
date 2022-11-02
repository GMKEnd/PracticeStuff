package com.example.recyclerviewimageload

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton

class DragFloatActionButtonV2 : AppCompatButton {
    private var parentHeight: Int = 0

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    private var lastY: Int = 0

    private var isDrag: Boolean = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val rawY = event.rawY.toInt()
        when (event.action and MotionEvent.ACTION_MASK) {
            // 按下
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                isDrag = false
                parent.requestDisallowInterceptTouchEvent(true)
                lastY = rawY
                val parent: ViewGroup
                if (getParent() != null) {
                    parent = getParent() as ViewGroup
                    parentHeight = parent.height
                }
            }
            // 拖动
            MotionEvent.ACTION_MOVE -> {
                isDrag = parentHeight > 0
                val dy = rawY - lastY
                val distance = dy.toDouble().toInt()
                if (distance == 0) {
                    isDrag = false
                } else {
                    var y = y + dy
                    //检测是否到达边缘
                    y = if (y < 0) 0F else if (y > parentHeight - height) (parentHeight - height).toFloat() else y
                    setY(y)
                    lastY = rawY
                }
            }
            // 松开
            MotionEvent.ACTION_UP -> if (!isNotDrag()) {
                isPressed = false
            }
        }
        return !isNotDrag() || super.onTouchEvent(event)
    }

    private fun isNotDrag(): Boolean {
        return !isDrag
    }
}

