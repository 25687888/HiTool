package com.sendinfo.standard.tools


import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Layout.Alignment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AlignmentSpan
import android.text.style.BackgroundColorSpan
import android.text.style.BulletSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.LeadingMarginSpan
import android.text.style.MaskFilterSpan
import android.text.style.QuoteSpan
import android.text.style.RelativeSizeSpan
import android.text.style.ScaleXSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.blankj.utilcode.util.Utils

import android.graphics.BlurMaskFilter.Blur

/**
 * 文本工具
 */
//TextTool.getBuilder("").setBold().setAlign(Layout.Alignment.ALIGN_CENTER)
//	.append("测试").append("Url\n").setUrl(URL_VONTOOLS)
//	.append("列表项\n").setBullet(60, getResources().getColor(R.color.baby_blue))
//	.append("  测试引用\n").setQuoteColor(getResources().getColor(R.color.baby_blue))
//	.append("首行缩进\n").setLeadingMargin(30, 50)
//	.append("测试").append("上标").setSuperscript().append("下标\n").setSubscript()
//	.append("测试").append("点击事件\n").setClickSpan(clickableSpan)
//	.append("测试").append("serif 字体\n").setFontFamily("serif")
//	.append("测试").append("图片\n").setResourceId(R.drawable.logo)
//	.append("测试").append("前景色").setForegroundColor(Color.GREEN).append("背景色\n").setBackgroundColor(getResources().getColor(R.color.baby_blue))
//	.append("测试").append("删除线").setStrikethrough().append("下划线\n").setUnderline()
//	.append("测试").append("sans-serif 字体\n").setFontFamily("sans-serif")
//	.append("测试").append("2倍字体\n").setProportion(2)
//	.append("测试").append("monospace字体\n").setFontFamily("monospace")
//	.append("测试").append("普通模糊效果字体\n").setBlur(3, BlurMaskFilter.Blur.NORMAL)
//	.append("测试").append(" 粗体 ").setBold().append(" 斜体 ").setItalic().append(" 粗斜体 \n").setBoldItalic()
//	.append("测试").append("横向2倍字体\n").setXProportion(2)
//
//	.append("\n测试正常对齐\n").setAlign(Layout.Alignment.ALIGN_NORMAL)
//	.append("测试居中对齐\n").setAlign(Layout.Alignment.ALIGN_CENTER)
//	.append("测试相反对齐\n").setAlign(Layout.Alignment.ALIGN_OPPOSITE)
//	.into(mTvAboutSpannable);

object TextTool {

    /**
     * 获取建造者
     *
     * @param text 样式字符串文本
     * @return [Builder]
     */
    fun getBuilder(text: CharSequence): Builder {
        return Builder(text)
    }

    class Builder(private var text: CharSequence?) {

        private val defaultValue = 0x12000000

        private var flag: Int = 0
        @ColorInt
        private var foregroundColor: Int = 0
        @ColorInt
        private var backgroundColor: Int = 0
        @ColorInt
        private var quoteColor: Int = 0

        private var isLeadingMargin: Boolean = false
        private var first: Int = 0
        private var rest: Int = 0

        private var isBullet: Boolean = false
        private var gapWidth: Int = 0
        private var bulletColor: Int = 0

        private var proportion: Float = 0.toFloat()
        private var xProportion: Float = 0.toFloat()
        private var isStrikethrough: Boolean = false
        private var isUnderline: Boolean = false
        private var isSuperscript: Boolean = false
        private var isSubscript: Boolean = false
        private var isBold: Boolean = false
        private var isItalic: Boolean = false
        private var isBoldItalic: Boolean = false
        private var fontFamily: String? = null
        private var align: Alignment? = null

        private var imageIsBitmap: Boolean = false
        private var bitmap: Bitmap? = null
        private var imageIsDrawable: Boolean = false
        private var drawable: Drawable? = null
        private var imageIsUri: Boolean = false
        private var uri: Uri? = null
        private var imageIsResourceId: Boolean = false
        @DrawableRes
        private var resourceId: Int = 0

        private var clickSpan: ClickableSpan? = null
        private var url: String? = null

        private var isBlur: Boolean = false
        private var radius: Float = 0.toFloat()
        private var style: Blur? = null

        private val mBuilder: SpannableStringBuilder


        init {
            flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            foregroundColor = defaultValue
            backgroundColor = defaultValue
            quoteColor = defaultValue
            proportion = -1f
            xProportion = -1f
            mBuilder = SpannableStringBuilder()
        }

        /**
         * 设置标识
         *
         * @param flag
         *  * [Spanned.SPAN_INCLUSIVE_EXCLUSIVE]
         *  * [Spanned.SPAN_INCLUSIVE_INCLUSIVE]
         *  * [Spanned.SPAN_EXCLUSIVE_EXCLUSIVE]
         *  * [Spanned.SPAN_EXCLUSIVE_INCLUSIVE]
         *
         * @return [Builder]
         */
        fun setFlag(flag: Int): Builder {
            this.flag = flag
            return this
        }

        /**
         * 设置前景色
         *
         * @param color 前景色
         * @return [Builder]
         */
        fun setForegroundColor(@ColorInt color: Int): Builder {
            this.foregroundColor = color
            return this
        }

        /**
         * 设置背景色
         *
         * @param color 背景色
         * @return [Builder]
         */
        fun setBackgroundColor(@ColorInt color: Int): Builder {
            this.backgroundColor = color
            return this
        }

        /**
         * 设置引用线的颜色
         *
         * @param color 引用线的颜色
         * @return [Builder]
         */
        fun setQuoteColor(@ColorInt color: Int): Builder {
            this.quoteColor = color
            return this
        }

        /**
         * 设置缩进
         *
         * @param first 首行缩进
         * @param rest  剩余行缩进
         * @return [Builder]
         */
        fun setLeadingMargin(first: Int, rest: Int): Builder {
            this.first = first
            this.rest = rest
            isLeadingMargin = true
            return this
        }

        /**
         * 设置列表标记
         *
         * @param gapWidth 列表标记和文字间距离
         * @param color    列表标记的颜色
         * @return [Builder]
         */
        fun setBullet(gapWidth: Int, color: Int): Builder {
            this.gapWidth = gapWidth
            bulletColor = color
            isBullet = true
            return this
        }

        /**
         * 设置字体比例
         *
         * @param proportion 比例
         * @return [Builder]
         */
        fun setProportion(proportion: Float): Builder {
            this.proportion = proportion
            return this
        }

        /**
         * 设置字体横向比例
         *
         * @param proportion 比例
         * @return [Builder]
         */
        fun setXProportion(proportion: Float): Builder {
            this.xProportion = proportion
            return this
        }

        /**
         * 设置删除线
         *
         * @return [Builder]
         */
        fun setStrikethrough(): Builder {
            this.isStrikethrough = true
            return this
        }

        /**
         * 设置下划线
         *
         * @return [Builder]
         */
        fun setUnderline(): Builder {
            this.isUnderline = true
            return this
        }

        /**
         * 设置上标
         *
         * @return [Builder]
         */
        fun setSuperscript(): Builder {
            this.isSuperscript = true
            return this
        }

        /**
         * 设置下标
         *
         * @return [Builder]
         */
        fun setSubscript(): Builder {
            this.isSubscript = true
            return this
        }

        /**
         * 设置粗体
         *
         * @return [Builder]
         */
        fun setBold(): Builder {
            isBold = true
            return this
        }

        /**
         * 设置斜体
         *
         * @return [Builder]
         */
        fun setItalic(): Builder {
            isItalic = true
            return this
        }

        /**
         * 设置粗斜体
         *
         * @return [Builder]
         */
        fun setBoldItalic(): Builder {
            isBoldItalic = true
            return this
        }

        /**
         * 设置字体
         *
         * @param fontFamily 字体
         *
         *  * monospace
         *  * serif
         *  * sans-serif
         *
         * @return [Builder]
         */
        fun setFontFamily(fontFamily: String?): Builder {
            this.fontFamily = fontFamily
            return this
        }

        /**
         * 设置对齐
         *
         * @param align 对其方式
         *
         *  * [Alignment.ALIGN_NORMAL]正常
         *  * [Alignment.ALIGN_OPPOSITE]相反
         *  * [Alignment.ALIGN_CENTER]居中
         *
         * @return [Builder]
         */
        fun setAlign(align: Alignment?): Builder {
            this.align = align
            return this
        }

        /**
         * 设置图片
         *
         * @param bitmap 图片位图
         * @return [Builder]
         */
        fun setBitmap(bitmap: Bitmap): Builder {
            this.bitmap = bitmap
            imageIsBitmap = true
            return this
        }

        /**
         * 设置图片
         *
         * @param drawable 图片资源
         * @return [Builder]
         */
        fun setDrawable(drawable: Drawable): Builder {
            this.drawable = drawable
            imageIsDrawable = true
            return this
        }

        /**
         * 设置图片
         *
         * @param uri 图片uri
         * @return [Builder]
         */
        fun setUri(uri: Uri): Builder {
            this.uri = uri
            imageIsUri = true
            return this
        }

        /**
         * 设置图片
         *
         * @param resourceId 图片资源id
         * @return [Builder]
         */
        fun setResourceId(@DrawableRes resourceId: Int): Builder {
            this.resourceId = resourceId
            imageIsResourceId = true
            return this
        }

        /**
         * 设置点击事件
         *
         * 需添加view.setMovementMethod(LinkMovementMethod.getInstance())
         *
         * @param clickSpan 点击事件
         * @return [Builder]
         */
        fun setClickSpan(clickSpan: ClickableSpan): Builder {
            this.clickSpan = clickSpan
            return this
        }

        /**
         * 设置超链接
         *
         * 需添加view.setMovementMethod(LinkMovementMethod.getInstance())
         *
         * @param url 超链接
         * @return [Builder]
         */
        fun setUrl(url: String): Builder {
            this.url = url
            return this
        }

        /**
         * 设置模糊
         *
         * 尚存bug，其他地方存在相同的字体的话，相同字体出现在之前的话那么就不会模糊，出现在之后的话那会一起模糊
         *
         * 推荐还是把所有字体都模糊这样使用
         *
         * @param radius 模糊半径（需大于0）
         * @param style  模糊样式
         *  * [Blur.NORMAL]
         *  * [Blur.SOLID]
         *  * [Blur.OUTER]
         *  * [Blur.INNER]
         *
         * @return [Builder]
         */
        fun setBlur(radius: Float, style: Blur): Builder {
            this.radius = radius
            this.style = style
            this.isBlur = true
            return this
        }

        /**
         * 追加样式字符串
         *
         * @param text 样式字符串文本
         * @return [Builder]
         */
        fun append(text: CharSequence): Builder {
            setSpan()
            this.text = text
            return this
        }

        /**
         * 创建样式字符串
         *
         * @return 样式字符串
         */
        fun create(): SpannableStringBuilder {
            setSpan()
            return mBuilder
        }

        fun into(textView: TextView?) {
            setSpan()
            if (textView != null) {
                textView.text = mBuilder
            }
        }

        /**
         * 设置样式
         */
        private fun setSpan() {
            val start = mBuilder.length
            mBuilder.append(this.text)
            val end = mBuilder.length
            if (foregroundColor != defaultValue) {
                mBuilder.setSpan(ForegroundColorSpan(foregroundColor), start, end, flag)
                foregroundColor = defaultValue
            }
            if (backgroundColor != defaultValue) {
                mBuilder.setSpan(BackgroundColorSpan(backgroundColor), start, end, flag)
                backgroundColor = defaultValue
            }
            if (isLeadingMargin) {
                mBuilder.setSpan(LeadingMarginSpan.Standard(first, rest), start, end, flag)
                isLeadingMargin = false
            }
            if (quoteColor != defaultValue) {
                mBuilder.setSpan(QuoteSpan(quoteColor), start, end, 0)
                quoteColor = defaultValue
            }
            if (isBullet) {
                mBuilder.setSpan(BulletSpan(gapWidth, bulletColor), start, end, 0)
                isBullet = false
            }
            if (proportion != -1f) {
                mBuilder.setSpan(RelativeSizeSpan(proportion), start, end, flag)
                proportion = -1f
            }
            if (xProportion != -1f) {
                mBuilder.setSpan(ScaleXSpan(xProportion), start, end, flag)
                xProportion = -1f
            }
            if (isStrikethrough) {
                mBuilder.setSpan(StrikethroughSpan(), start, end, flag)
                isStrikethrough = false
            }
            if (isUnderline) {
                mBuilder.setSpan(UnderlineSpan(), start, end, flag)
                isUnderline = false
            }
            if (isSuperscript) {
                mBuilder.setSpan(SuperscriptSpan(), start, end, flag)
                isSuperscript = false
            }
            if (isSubscript) {
                mBuilder.setSpan(SubscriptSpan(), start, end, flag)
                isSubscript = false
            }
            if (isBold) {
                mBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, flag)
                isBold = false
            }
            if (isItalic) {
                mBuilder.setSpan(StyleSpan(Typeface.ITALIC), start, end, flag)
                isItalic = false
            }
            if (isBoldItalic) {
                mBuilder.setSpan(StyleSpan(Typeface.BOLD_ITALIC), start, end, flag)
                isBoldItalic = false
            }
            if (fontFamily != null) {
                mBuilder.setSpan(TypefaceSpan(fontFamily), start, end, flag)
                fontFamily = null
            }
            if (align != null) {
                mBuilder.setSpan(AlignmentSpan.Standard(align!!), start, end, flag)
                align = null
            }
            if (imageIsBitmap || imageIsDrawable || imageIsUri || imageIsResourceId) {
                if (imageIsBitmap) {
                    mBuilder.setSpan(ImageSpan(Utils.getApp(), bitmap!!), start, end, flag)
                    bitmap = null
                    imageIsBitmap = false
                } else if (imageIsDrawable) {
                    mBuilder.setSpan(ImageSpan(drawable!!), start, end, flag)
                    drawable = null
                    imageIsDrawable = false
                } else if (imageIsUri) {
                    mBuilder.setSpan(ImageSpan(Utils.getApp(), uri!!), start, end, flag)
                    uri = null
                    imageIsUri = false
                } else {
                    mBuilder.setSpan(ImageSpan(Utils.getApp(), resourceId), start, end, flag)
                    resourceId = 0
                    imageIsResourceId = false
                }
            }
            if (clickSpan != null) {
                mBuilder.setSpan(clickSpan, start, end, flag)
                clickSpan = null
            }
            if (url != null) {
                mBuilder.setSpan(URLSpan(url), start, end, flag)
                url = null
            }
            if (isBlur) {
                mBuilder.setSpan(MaskFilterSpan(BlurMaskFilter(radius, style)), start, end, flag)
                isBlur = false
            }
            flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        }
    }
}
