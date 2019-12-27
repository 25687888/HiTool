package com.base.library.view.recyleview

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * 作用: 使RecyclerView使用LinearLayoutManager嵌套在ScrollView中
 * 作者: TALE(赵小白) email:vvtale@gmail.com  
 * 日期: 2015-08-28 13:56 
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class FullyLinearLayoutManager : LinearLayoutManager {
    private var dividerHeight = 2

    private val mMeasuredDimension = IntArray(2)

    constructor(context: Context) : super(context) {}

    constructor(context: Context, dividerHeight: Int) : super(context) {
        this.dividerHeight = dividerHeight
    }

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    ) {
    }

    override fun onMeasure(
        recycler: RecyclerView.Recycler, state: RecyclerView.State,
        widthSpec: Int, heightSpec: Int
    ) {
        val widthMode = View.MeasureSpec.getMode(widthSpec)
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        val widthSize = View.MeasureSpec.getSize(widthSpec)
        val heightSize = View.MeasureSpec.getSize(heightSpec)


        var width = 0
        var height = 0
        for (i in 0 until itemCount) {
            measureScrapChild(
                recycler, i,
                View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                mMeasuredDimension
            )

            if (orientation == LinearLayoutManager.HORIZONTAL) {
                width = width + mMeasuredDimension[0]
                if (i == 0) {
                    height = mMeasuredDimension[1]
                }
            } else {
                height = height + mMeasuredDimension[1]
                if (i == 0) {
                    width = mMeasuredDimension[0]
                }
            }
        }
        when (widthMode) {
            View.MeasureSpec.EXACTLY -> width = widthSize + dividerHeight
        }

        when (heightMode) {
            View.MeasureSpec.EXACTLY -> height = heightSize + dividerHeight
        }

        setMeasuredDimension(width, height)
    }

    private fun measureScrapChild(
        recycler: RecyclerView.Recycler, position: Int, widthSpec: Int,
        heightSpec: Int, measuredDimension: IntArray
    ) {
        try {
            val view = recycler.getViewForPosition(0)//fix 动态添加时报IndexOutOfBoundsException

            if (view != null) {
                val p = view.layoutParams as RecyclerView.LayoutParams

                val childWidthSpec = ViewGroup.getChildMeasureSpec(
                    widthSpec,
                    paddingLeft + paddingRight, p.width
                )

                val childHeightSpec = ViewGroup.getChildMeasureSpec(
                    heightSpec,
                    paddingTop + paddingBottom, p.height
                )

                view.measure(childWidthSpec, childHeightSpec)
                if (orientation == LinearLayoutManager.VERTICAL) {
                    measuredDimension[0] = view.measuredWidth + p.leftMargin + p.rightMargin
                    measuredDimension[1] = view.measuredHeight + p.bottomMargin + p.topMargin + dividerHeight
                } else {
                    measuredDimension[0] = view.measuredWidth + p.leftMargin + p.rightMargin + dividerHeight
                    measuredDimension[1] = view.measuredHeight + p.bottomMargin + p.topMargin
                }
                recycler.recycleView(view)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        private val TAG = FullyLinearLayoutManager::class.java.simpleName
    }
}