package braintouch.mx.plusmarks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class AppInfo extends Activity {
	DefaultHttpClient http_client = new DefaultHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_app_info);
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onResume() {
            super.onResume();
            Intent intent = getIntent();
            AccountManager accountManager = AccountManager.get(getApplicationContext());
            Account account = (Account)intent.getExtras().get("account"); //ah appengine
            accountManager.getAuthToken(account, "allServices", false, new GetAuthTokenCallback(), null);
    }
    
    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_info, menu);
		return true;
	}
	
	private class GetAuthTokenCallback implements AccountManagerCallback {
	    public void run(AccountManagerFuture result) {
	            Bundle bundle;
	            try {
	                    bundle = (Bundle) result.getResult();
	                    Intent intent = (Intent)bundle.get(AccountManager.KEY_INTENT);
	                    if(intent != null) { 
	                            startActivity(intent);
	                    } else {
	                            onGetAuthToken(bundle);
	                    }
	            } catch (OperationCanceledException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	            } catch (AuthenticatorException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	            } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	            }
	    }
	};
	
	protected void onGetAuthToken(Bundle bundle) {
        String auth_token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
        Log.d("TOKEN", "####### TOKEN "+ auth_token);
     //   new GetCookieTask().execute(auth_token);
}
	
	 private class GetCookieTask extends AsyncTask {
			
		 @Override
		 protected Boolean doInBackground(Object... tokens) {
                 try {
                         // Don't follow redirects
                         http_client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
                         
                         HttpGet http_get = new HttpGet("https://yourapp.appspot.com/_ah/login?continue=http://localhost/&auth=" + tokens[0]);
                         HttpResponse response;
                         response = http_client.execute(http_get);
                         if(response.getStatusLine().getStatusCode() != 302)
                                 // Response should be a redirect
                                 return false;
                         
                         for(Cookie cookie : http_client.getCookieStore().getCookies()) {
                                 if(cookie.getName().equals("ACSID"))
                                         return true;
                         }
                 } catch (ClientProtocolException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                 } catch (IOException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                 } finally {
                         http_client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
                 }
                 return false;
         }
         
         protected void onPostExecute(Boolean result) {
                 new AuthenticatedRequestTask().execute("http://yourapp.appspot.com/admin/");
         }

		 
 }
	 
	    private class AuthenticatedRequestTask extends AsyncTask {
			
	    	@Override
	    	protected HttpResponse doInBackground(Object... urls) {
                    try {
                            HttpGet http_get = new HttpGet((String )urls[0]);
                            return http_client.execute(http_get);
                    } catch (ClientProtocolException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    }
                    return null;
            }
            
            protected void onPostExecute(HttpResponse result) {
                    try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(result.getEntity().getContent()));
                            String first_line = reader.readLine();
                            Toast.makeText(getApplicationContext(), first_line, Toast.LENGTH_LONG).show();                          
                    } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    }
            }

	 
    }

}


