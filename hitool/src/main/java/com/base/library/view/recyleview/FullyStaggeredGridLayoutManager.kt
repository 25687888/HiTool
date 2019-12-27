package com.base.library.view.recyleview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 作用: 使RecyclerView使用GridLayoutManager嵌套在ScrollView中
 * 作者: TALE(赵小白) email:edisonzsw@icloud.com
 * 日期: 2015-08-28 13:58 
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class FullyStaggeredGridLayoutManager(spanCount: Int, orientation: Int) :
    StaggeredGridLayoutManager(spanCount, orientation) {
    var mwidth = 0
    var mheight = 0

    private val mMeasuredDimension = IntArray(2)

    override fun onMeasure(
        recycler: RecyclerView.Recycler, state: RecyclerView.State, widthSpec: Int,
        heightSpec: Int
    ) {
        val widthMode = View.MeasureSpec.getMode(widthSpec)
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        val widthSize = View.MeasureSpec.getSize(widthSpec)
        val heightSize = View.MeasureSpec.getSize(heightSpec)

        var width = 0
        var height = 0
        val count = getItemCount()
        val span = getSpanCount()
        for (i in 0 until count) {
            measureScrapChild(
                recycler, i,
                View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                mMeasuredDimension
            )

            if (getOrientation() === HORIZONTAL) {
                if (i % span == 0) {
                    width = width + mMeasuredDimension[0]
                }
                if (i == 0) {
                    height = mMeasuredDimension[1]
                }
            } else {
                if (i % span == 0) {
                    height = height + mMeasuredDimension[1]
                }
                if (i == 0) {
                    width = mMeasuredDimension[0]
                }
            }
        }

        when (widthMode) {
            View.MeasureSpec.EXACTLY -> width = widthSize
        }

        when (heightMode) {
            View.MeasureSpec.EXACTLY -> height = heightSize
        }
        mheight = height
        mwidth = width
        setMeasuredDimension(width, height)
    }

    private fun measureScrapChild(
        recycler: RecyclerView.Recycler, position: Int, widthSpec: Int,
        heightSpec: Int, measuredDimension: IntArray
    ) {
        if (position < getItemCount()) {
            try {
                val view = recycler.getViewForPosition(0)//fix 动态添加时报IndexOutOfBoundsException
                if (view != null) {
                    val p = view!!.getLayoutParams() as RecyclerView.LayoutParams
                    val childWidthSpec = ViewGroup.getChildMeasureSpec(
                        widthSpec,
                        getPaddingLeft() + getPaddingRight(), p.width
                    )
                    val childHeightSpec = ViewGroup.getChildMeasureSpec(
                        heightSpec,
                        getPaddingTop() + getPaddingBottom(), p.height
                    )
                    view!!.measure(childWidthSpec, childHeightSpec)
                    measuredDimension[0] = view!!.getMeasuredWidth() + p.leftMargin + p.rightMargin
                    measuredDimension[1] = view!!.getMeasuredHeight() + p.bottomMargin + p.topMargin
                    recycler.recycleView(view)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}