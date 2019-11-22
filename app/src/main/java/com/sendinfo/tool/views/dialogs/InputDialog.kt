package com.sendinfo.tool.views.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.sendinfo.tool.R

class InputDialog(context: Context) : Dialog(context, R.style.DialogStyle) {
    private var mTitleTextView: TextView? = null
    private var etInfo: EditText? = null
    private var mConfirmButton: Button? = null
    private var mConfirmClickListener: View.OnClickListener? = null
    private var mTitleText: String? = null
    private var mConfirmText: String? = null
    private var infoText: String? = null

    init {
        window?.setGravity(Gravity.CENTER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_dialog)
        mTitleTextView = findViewById(R.id.title_text)
        etInfo = findViewById(R.id.etInfo)
        mConfirmButton = findViewById(R.id.confirm_button)
        mConfirmButton?.setOnClickListener {
            if (mConfirmClickListener != null) {
                mConfirmClickListener?.onClick(it)
            } else {
                dismiss()
            }
        }
        findViewById<ImageView>(R.id.ivClose).setOnClickListener { dismiss() }
        if (mTitleText != null) {
            mTitleTextView?.text = mTitleText
        }
        if (infoText != null) {
            etInfo?.setText(infoText)
        }
        if (mConfirmText != null) {
            mConfirmButton?.text = mConfirmText
        }
    }

    fun getContentText(): String = etInfo?.text.toString().trim()

    fun setContentText(infoText: String): InputDialog {
        this.infoText = infoText
        return this
    }

    fun setTitleText(mTitleText: String): InputDialog {
        this.mTitleText = mTitleText
        return this
    }

    fun setConfirmText(text: String): InputDialog {
        mConfirmText = text
        return this
    }

    fun setConfirmClickListener(listener: View.OnClickListener): InputDialog {
        mConfirmClickListener = listener
        return this
    }
}