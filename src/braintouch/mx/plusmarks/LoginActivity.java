package braintouch.mx.plusmarks;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class LoginActivity extends Activity {

    protected AccountManager accountManager;
    protected Intent intent;
    private ArrayList<CustomObject> objects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType("com.google");
     //   this.setListAdapter(new ArrayAdapter(this, R.layout.list_item, getAccountNames(accounts)));  
        setContentView(R.layout.activity_login); 
        String[] projection = new String[] { Browser.BookmarkColumns.FAVICON, Browser.BookmarkColumns.TITLE , Browser.BookmarkColumns.URL , Browser.BookmarkColumns.DATE
 };
        
        ArrayList<String> urls = new ArrayList<String>();
        ArrayList<String> urls2 = new ArrayList<String>(); 
         objects = new ArrayList<CustomObject>();

          Cursor mCur = managedQuery(android.provider.Browser.BOOKMARKS_URI, projection, null, null, null);
            mCur.moveToFirst();
            int titleIdx = mCur.getColumnIndex(Browser.BookmarkColumns.TITLE);
            int urlIdx = mCur.getColumnIndex(Browser.BookmarkColumns.URL);
            int dateIdx = mCur.getColumnIndex(Browser.BookmarkColumns.DATE);
            int icon = mCur.getColumnIndex(Browser.BookmarkColumns.FAVICON);

            while (mCur.isAfterLast() == false) {
        //    	Log.d("BOOKMARKS", "n" + mCur.getString(titleIdx));
        //    	Log.d("BOOKMARKS", "n" + mCur.getString(urlIdx));
            	urls.add(mCur.getString(urlIdx));
            	// mCur.getBlob(icon);
            	CustomObject obj = new CustomObject(mCur.getString(titleIdx), mCur.getString(dateIdx), mCur.getString(urlIdx));
            	objects.add(obj);
            	mCur.moveToNext(); 
            } 
          /**  Cursor mCur2 = managedQuery(Browser.BOOKMARKS_URI,
                    Browser.HISTORY_PROJECTION, null, null, null);
            if (mCur2.moveToFirst()) {
                while (mCur2.isAfterLast() == false) {
                  //  Log.v("titleIdx", mCur2
                  //          .getString(Browser.HISTORY_PROJECTION_TITLE_INDEX));
                    Log.v("urlIdx", mCur2
                            .getString(Browser.HISTORY_PROJECTION_URL_INDEX)); 
                	urls.add(mCur2.getString(Browser.HISTORY_PROJECTION_URL_INDEX));
                    mCur2.moveToNext();
                }
            }**/
            
            String[] columns = new String[] {
            		Browser.BookmarkColumns.TITLE,
            		Browser.BookmarkColumns.URL,
            		Browser.BookmarkColumns.DATE
            };
            
            int[] to = new int[] { 
            	    R.id.textView1,
            	    R.id.textView2,
            	    R.id.textView3,
            	  };

            ListView listView = (ListView) findViewById(R.id.listView1);
            Button button = (Button)findViewById(R.id.buttonFind); 
            
             
            CustomAdapter customAdapter = new CustomAdapter(this, objects);
            listView.setAdapter(customAdapter);
            
           // SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, columns,  to, 0);
           // listView.setAdapter(dataAdapter);
            
          //  listView.setAdapter(new ArrayAdapter(this, R.layout.list_item, urls.toArray()));  
            button.setOnClickListener(new OnClickListener() { 
				@Override
				public void onClick(View v) {
					 Intent intent = new Intent(v.getContext(), WebViewActivity.class);
 			         startActivity(intent);
				}
			});
            
            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                	CustomObject o = objects.get(position);
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(o.getProp3()));
                    startActivity(myIntent);
                }
            });
            
       //     Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strURL));
        //    startActivity(myIntent);
            
    }
    
    private String[] getAccountNames(Account[] accounts) {
    	String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    } 
  /**  @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
    	 Account account =accounts[0];
         Intent intent = new Intent(this, AppInfo.class);
         intent.putExtra("account", account);
         startActivity(intent);
    }**/ 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { 
        return true;
    }
    
  
}

 class CustomObject {

    private String prop1; 
    private String prop2;
    private String prop3;

    public CustomObject(String prop1, String prop2, String prop3) {
        this.prop1 = prop1;
        this.prop2 = prop2;
        this.prop3 = prop3;

    }

    public String getProp1() {
        return prop1;
    }

    public String getProp2() {
       return prop2;
    }
    
    public String getProp3() {
        return prop3;
     }
}


 class CustomAdapter extends BaseAdapter {

	   private LayoutInflater inflater;
	   private ArrayList<CustomObject> objects;

	   private class ViewHolder {
	      TextView textView1;
	      TextView textView2;
	      TextView textView3;

	   }

	   public CustomAdapter(Context context, ArrayList<CustomObject> objects) {
	      inflater = LayoutInflater.from(context);
	      this.objects = objects;
	   }

	   public int getCount() {
	      return objects.size();
	   }

	   public CustomObject getItem(int position) {
	      return objects.get(position);
	   }

	   public long getItemId(int position) {
	      return position;
	   }

	   public View getView(int position, View convertView, ViewGroup parent) {
	      ViewHolder holder = null;
	      if(convertView == null) {
	         holder = new ViewHolder();
	         convertView = inflater.inflate(R.layout.list_item, null);
	         holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
	         holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
	         holder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
	         convertView.setTag(holder);
	      } else {
	         holder = (ViewHolder) convertView.getTag();
	      }
	      holder.textView1.setText(objects.get(position).getProp1());
	      java.util.Date date = new java.util.Date( Long.valueOf((objects.get(position).getProp2()))); 
	      java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
	      String fecha = sdf.format(date);
	      
	      holder.textView2.setText(fecha);
	      holder.textView3.setText(objects.get(position).getProp3());

	      return convertView;
	   }
	}

