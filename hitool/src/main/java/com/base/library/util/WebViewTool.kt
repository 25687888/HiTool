package com.base.library.util

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.util.regex.Pattern

/**
 * 作用: WebView工具类
 * 作者: 赵小白 email:vvtale@gmail.com  
 * 日期: 2018/5/21 16:29 
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
object WebViewTool {
    fun setWebData(content: String, mWebView: WebView, mProgressBar: ProgressBar?) {
        var content = content
        // 设置WebView的属性，此时可以去执行JavaScript脚本`
        mWebView.settings.javaScriptEnabled = true // 设置支持javascript脚本
        mWebView.settings.allowFileAccess = true // 允许访问文件
        mWebView.settings.javaScriptCanOpenWindowsAutomatically = true
        mWebView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        mWebView.settings.defaultTextEncodingName = "UTF-8"//设置默认为utf-8
        //		mWebView.getSettings().setDefaultFontSize( (int) (46 / scale) );
        //		mWebView.getSettings().setMinimumFontSize( (int) (38 / scale) );
        mWebView.settings.setSupportZoom(false)// 支持缩放
        mWebView.settings.builtInZoomControls = false // 设置显示缩放按钮
        //		mWebView.getSettings().setTextZoom(80); // 设置字体大小
        mWebView.setBackgroundColor(0)
        //		mWebView.getSettings().setUseWideViewPort(true);
        //		mWebView.getSettings().setLoadWithOverviewMode(true);
        //		mWebView.setInitialScale(960 * 100 / getScrnHeight());

        mWebView.settings.useWideViewPort = true//设置此属性，可任意比例缩放
        mWebView.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)

        mWebView.settings.domStorageEnabled = true
        //priority high
        mWebView.settings.setAppCacheEnabled(true)
        mWebView.settings.databaseEnabled = true
        //add by wjj end
        //解决图片不显示
        mWebView.settings.blockNetworkImage = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.settings.mixedContentMode = 0
        }
        val ua = mWebView.settings.userAgentString
        mWebView.settings.useWideViewPort = true
        mWebView.settings.loadWithOverviewMode = true
        if (content.startsWith("http://") || content.startsWith("https://")) {
            mWebView.settings.useWideViewPort = true
            mWebView.settings.loadWithOverviewMode = true

            if (mProgressBar != null) {
                mProgressBar.visibility = View.VISIBLE
                mProgressBar.max = 100
            }

            // 当webview里面能点击是 在当前页面上显示！
            mWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                    if (url == null) {
                        return false
                    }

                    try {
                        if (url.startsWith("http:") || url.startsWith("https:")) {
                            view.loadUrl(url)
                            return true
                        } else {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            view.context.startActivity(intent)
                            return true
                        }
                    } catch (e: Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                        return false
                    }

                }

                /** 网页页面开始加载的时候，执行的回调方法  */
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {//网页页面开始加载的时候
                    mWebView.isEnabled = false// 当加载网页的时候将网页进行隐藏
                    super.onPageStarted(view, url, favicon)
                }
            }

            mWebView.webChromeClient = object : WebChromeClient() {
                /** 当WebView加载之后，返回 HTML 页面的标题 Title  */
                override fun onReceivedTitle(view: WebView, title: String) {
                    //判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
                    if (!TextUtils.isEmpty(title) && title.toLowerCase().contains("error")) {
                    }
                }

                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onPermissionRequest(request: PermissionRequest?) {
                    request?.grant(request.resources)
                }

                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    if (mProgressBar != null) {
                        mProgressBar.progress = newProgress
                        if (newProgress == 100) {
                            mProgressBar.visibility = View.GONE
                        } else {
                            if (mProgressBar.visibility == View.GONE) {
                                mProgressBar.visibility = View.VISIBLE
                                // pb.setProgress(newProgress);
                            }
                        }
                    }
                    super.onProgressChanged(view, newProgress)
                }

                //				@Override
                //				public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams)
                //				{
                //					mUploadCallbackAboveL = filePathCallback;
                //					take();
                //					return true;
                //				}
                //
                //				public void openFileChooser(ValueCallback<Uri> uploadMsg)
                //				{
                //					mUploadMessage = uploadMsg;
                //					take();
                //				}
                //
                //				public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType)
                //				{
                //					mUploadMessage = uploadMsg;
                //					take();
                //				}
                //
                //				public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
                //				{
                //					mUploadMessage = uploadMsg;
                //					take();
                //				}
            }

            mWebView.loadUrl(content)
        } else {
            content = "<style>\n" + "    img {\n" + "        max-width: 100%;\n" + "        width: 100%;\n" +
                    "        height: auto\n" + "    }\n" + "    \n" + "    div {\n" + "        width: 100%;\n" +
                    "        max-width: 100%;\n" + "        height: auto\n" + "    }\n" + "    \n" + "    p {\n" +
                    "        width: 100%;\n" + "        max-width: 100%;\n" + "        height: auto\n" + "    }\n" + "</style>\n" +
                    content + "<script type=\"text/javascript\">" + "var imgs = document.getElementsByTagName('img');" +
                    "for(var i = 0; i<tables.length; i++){" +  // 逐个改变

                    "imgs[i].style.width = '100%';" +  // 宽度改为100%

                    "imgs[i].style.height = 'auto';" + "}" + "var ps = document.getElementsByTagName('p');" +
                    "for(var i = 0; i<tables.length; i++){" +  // 逐个改变

                    "ps[i].style.width = '100%';" +  // 宽度改为100%

                    "ps[i].style.height = 'auto';" + "}" + "</script>"
            val regEx = "</?[^>]+>"
            val pat = Pattern.compile(regEx)
            val mat = pat.matcher(content)
            val rs = mat.find()
            if (rs) {
                if (content.contains("https"))
                //如果含有包括https
                {
                    val mWebviewclient = object : WebViewClient() {
                        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                            handler.proceed()
                            //handler.cancel(); 默认的处理方式，WebView变成空白页
                            //handler.process();接受证书
                            //handleMessage(Message msg); 其他处理
                        }
                    }
                    mWebView.webViewClient = mWebviewclient
                } else
                // 当webview里面能点击是 在当前页面上显示！
                {
                    mWebView.webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                            view.loadUrl(url)
                            return true
                        }
                    }
                }
                mWebView.loadData(content, "text/html; charset=UTF-8", null)
                // mWebView.loadData(fmtString(content), "text/html", "utf-8");
            }
        }
        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            fixWebView(mWebView)
        }
    }

    @TargetApi(11)
    private fun fixWebView(mWebView: WebView) {
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_")
    }
}
