package com.sendinfo.tool.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sendinfo.tool.module.WebMvpActivity

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val i = Intent(context, WebMvpActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }
}