package com.base.library.view.recyleview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils

/**
 * 作用: 简易的计算显示RecyclerView间隔
 * 作者: 赵小白 email:edisonzsw@icloud.com
 * 日期: 2015-08-26-0026 23:50 
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class DividerItemDecoration : RecyclerView.ItemDecoration {

    private var mDivider: Drawable? = null
    private var dividerHeight = 1
    private var mOrientation: Int = 0
    private var showLastLine = true// 是否显示最后一条线

    /** 显示一个纯色的间隔符  */
    constructor(orientation: Int, resource: Int) {
        mDivider = ColorDrawable(ColorUtils.getColor(resource))
        setOrientation(orientation)
    }

    constructor(orientation: Int, resource: Int, showLastLine: Boolean) {
        this.showLastLine = showLastLine
        mDivider = ColorDrawable(ColorUtils.getColor(resource))
        setOrientation(orientation)
    }

    constructor(context: Context, orientation: Int, resource: Int, height: Int, isColor: Boolean) {
        this.dividerHeight = height
        val resources = context.resources
        if (isColor) {
            mDivider = ColorDrawable(ColorUtils.getColor(resource))
        } else {
            mDivider = resources.getDrawable(resource)
        }
        setOrientation(orientation)
    }

    constructor(
        context: Context,
        orientation: Int,
        resource: Int,
        height: Int,
        isColor: Boolean,
        showLastLine: Boolean
    ) {
        this.showLastLine = showLastLine
        this.dividerHeight = height
        val resources = context.resources
        if (isColor) {
            mDivider = ColorDrawable(ColorUtils.getColor(resource))
        } else {
            mDivider = resources.getDrawable(resource)
        }
        setOrientation(orientation)
    }

    fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw IllegalArgumentException("invalid orientation")
        }
        mOrientation = orientation
    }

    override fun onDraw(c: Canvas, parent: RecyclerView) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }


    fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        var childCount = parent.childCount
        if (!showLastLine) {
            childCount = childCount - 1
        }
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val v = RecyclerView(parent.context)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + dividerHeight
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c)
        }
    }

    fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        var childCount = parent.childCount
        if (!showLastLine) {
            childCount = childCount - 1
        }
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val v = RecyclerView(parent.context)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + dividerHeight
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, dividerHeight)
        } else {
            outRect.set(0, 0, dividerHeight, 0)
        }
    }

    companion object {
        val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL

        val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }
}
