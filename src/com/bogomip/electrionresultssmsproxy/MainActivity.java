package com.bogomip.electrionresultssmsproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import android.util.Log;

public class MainActivity extends Activity {

	static final String TAG = MainActivity.class.getSimpleName();
	Date date;
	    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        HTTPGetter httpgetter = new HTTPGetter();

        try {
			httpgetter.execute(new URI("http://10.42.0.1/~spencersr/supdog.json"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    private class HTTPGetter extends AsyncTask<URI, Void, String> {

        protected String doInBackground(URI ... uri) {  
        	
			HttpClient client = new DefaultHttpClient();
			HttpGet getter = new HttpGet(uri[0]);

			try {
				HttpResponse response = client.execute(getter);
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				InputStreamReader isRdr = new InputStreamReader(is);
				BufferedReader bfRdr = new BufferedReader(isRdr);
				StringBuilder strBldr = new StringBuilder();
				String line = null;

				while ((line = bfRdr.readLine()) != null) {
					strBldr.append(line);
				}

				String result = strBldr.toString();

				JSONObject jObj = new JSONObject(result);
				
				Log.i(TAG, jObj.toString(2));

				SmsManager sms = SmsManager.getDefault();

				sms.sendTextMessage(jObj.getString("to"), null, jObj.getString("msg"), null, null);

				return jObj.toString(2);

			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			
			return "awwcrap";
        	
        }

        protected void onPostExecute(String result){
            
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_quit) {
        	finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
