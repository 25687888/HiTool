package com.base.library.view.recyleview

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

import java.util.concurrent.atomic.AtomicBoolean


/**
 * 作用: 自动跑马灯的RecyclerView
 * 作者: 赵小白 email:edisonzsw@icloud.com 
 * 日期: 16/5/21 01:04 
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class MarqueeRecyclerView : RecyclerView {
    internal var thread: Thread? = null
    internal var shouldContinue = AtomicBoolean(false)
    internal var mHandler: Handler? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    private fun init() {
        //主线程的handler，用于执行Marquee的滚动消息
        mHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    1//不论是竖直滚动还是水平滚动，都是偏移5个像素
                    -> this@MarqueeRecyclerView.scrollBy(5, 5)
                }
            }
        }
        if (thread == null) {
            thread = object : Thread() {
                override fun run() {
                    while (shouldContinue.get()) {
                        try {
                            Thread.sleep(100)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                        val msg = mHandler!!.obtainMessage()
                        msg.what = 1
                        msg.sendToTarget()
                    }
                    //退出循环时清理handler
                    mHandler = null
                }
            }
        }
    }

    override
            /**
             * 在附到窗口的时候开始滚动
             */
    fun onAttachedToWindow() {
        super.onAttachedToWindow()
        shouldContinue.set(true)
        init()
        thread!!.start()
    }

    override
            /**
             * 在脱离窗口时处理相关内容
             */
    fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopMarquee()
    }

    /**
     * 停止滚动
     */
    fun stopMarquee() {
        shouldContinue.set(false)
        thread = null
    }

    //	/**
    //	 * Adapter类
    //	 */
    //	public static class InnerAdapter extends Adapter<InnerAdapter.MarqueHolder>
    //	{
    //		private List<TestDto> mData;
    //		private int size;
    //		private LayoutInflater mLayoutInflater;
    //
    //		public InnerAdapter(List<TestDto> data, Context context)
    //		{
    //			mData = data;
    //			size = mData.size();
    //			mLayoutInflater = LayoutInflater.from(context);
    //		}
    //
    //		@Override
    //		public MarqueHolder onCreateViewHolder(ViewGroup parent, int viewType)
    //		{
    //
    //			View view = mLayoutInflater.inflate(R.layout.item_main_limit, parent, false);
    //
    //			return new MarqueHolder(view);
    //		}
    //
    //		@Override
    //		public void onBindViewHolder(MarqueHolder holder, int position)
    //		{
    //			TestDto dto = mData.get(position%size);
    //			holder.iTvName.setText(dto.getTitle());
    //			ImageUtil.loadImg(holder.iImageView,dto.getImage());
    //		}
    //
    //		@Override
    //		public int getItemCount()
    //		{
    //			return Integer.MAX_VALUE;
    //		}
    //
    //		/**
    //		 * * ViewHolder类
    //		 **/
    //		static class MarqueHolder extends ViewHolder
    //		{
    //			TextView iTvName,iTvPrice,iTvOld;
    //			ImageView iImageView;
    //
    //			public MarqueHolder(View view)
    //			{
    //				super(view);
    //				iTvName = (TextView) view.findViewById(R.id.iTvName);
    //				iTvPrice = (TextView) view.findViewById(R.id.iTvPrice);
    //				iTvOld = (TextView) view.findViewById(R.id.iTvOld);
    //				iImageView = (ImageView) view.findViewById(R.id.iImageView);
    //			}
    //		}
    //	}
}