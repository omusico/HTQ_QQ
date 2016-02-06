package hq.king.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HttpActivity extends Activity {
	private WebView mWebView;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_http);
		findView();
		init();
		
	}
	private void findView()
	{
		mWebView=(WebView) findViewById(R.id.activity_http_webView);
	}
	private void init()
	{
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
			
		});
		mWebView.loadUrl("http://qzone.qq.com");
	}

}
