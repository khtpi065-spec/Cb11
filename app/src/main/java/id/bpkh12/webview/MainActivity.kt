package id.bpkh12.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var progress: ProgressBar
    private val url="https://bpkh12.planologi.kehutanan.go.id/"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView=findViewById(R.id.webview)
        progress=findViewById(R.id.progress)

        webView.settings.apply {
            javaScriptEnabled=true
            domStorageEnabled=true
            databaseEnabled=true
            setSupportZoom(true)
            builtInZoomControls=false
            displayZoomControls=false
            useWideViewPort=true
            loadWithOverviewMode=true
            allowFileAccess=false
            allowContentAccess=false
            mixedContentMode=WebSettings.MIXED_CONTENT_NEVER_ALLOW
            cacheMode=WebSettings.LOAD_DEFAULT
        }
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView,false)
        if (WebViewFeature.isFeatureSupported(WebViewFeature.START_SAFE_BROWSING)) {
            WebView.startSafeBrowsing(this) {}
        }
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.setForceDark(webView.settings,WebSettingsCompat.FORCE_DARK_OFF)
        }

        webView.webViewClient=object: WebViewClient() {
            override fun shouldOverrideUrlLoading(v:WebView,r:WebResourceRequest):Boolean {
                return r.url.scheme!="https"
            }
            override fun onPageStarted(v:WebView,u:String?,f:Bitmap?){progress.visibility=View.VISIBLE}
            override fun onPageFinished(v:WebView,u:String?){progress.visibility=View.GONE}
        }
        webView.webChromeClient=WebChromeClient()
        if(savedInstanceState==null) webView.loadUrl(url) else webView.restoreState(savedInstanceState)
    }

    override fun onBackPressed() {
        if(webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
    override fun onSaveInstanceState(out:Bundle){webView.saveState(out);super.onSaveInstanceState(out)}
    override fun onDestroy(){webView.stopLoading();webView.destroy();super.onDestroy()}
}
