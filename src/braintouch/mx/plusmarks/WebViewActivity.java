package braintouch.mx.plusmarks;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		WebView  mWebView = (WebView) findViewById(R.id.webView1);
        
        // Activo JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        
        // Cargamos la url que necesitamos    
        String data = "<script>(function() { var cx = '016669396214518678215:a7xzaneohpk'; var gcse = document.createElement('script'); gcse.type = 'text/javascript';gcse.async = true; gcse.src = (document.location.protocol == 'https:' ? 'https:' : 'http:') + '//www.google.com/cse/cse.js?cx=' + cx; var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(gcse, s); })(); </script> <gcse:search></gcse:search>";
      //  String data = "<script>(function() {var cx = '003399478185866364779:prwutnkhi08'; var gcse = document.createElement('script'); gcse.type = 'text/javascript';gcse.async = true;gcse.src = (document.location.protocol == 'https:' ? 'https:' : 'http:') +'//www.google.com/cse/cse.js?cx=' + cx;var s = document.getElementsByTagName('script')[0];s.parentNode.insertBefore(gcse, s);})();</script><gcse:search></gcse:search>";
      mWebView.loadData(data, "text/html; charset=UTF-8", null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}

}
