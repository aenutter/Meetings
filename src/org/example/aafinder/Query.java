package org.example.aafinder;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Query extends Activity {
	public String day;
	public String time;
	public String state;
	public String fellowship;
	String locInfo = null;
	String locInfo1 = null;
    double myLocationLatitude;
    double myLocationLongitude;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.query);
        
        GPSTracker myGPSTracker = new GPSTracker(this);
        
        myLocationLatitude = myGPSTracker.getLatitude();
        myLocationLongitude = myGPSTracker.getLongitude();
        
        Spinner s = (Spinner) findViewById(R.id.spinner);    
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.days, android.R.layout.simple_spinner_item);    
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        s.setAdapter(adapter);
        //Log.e("query fellowship spinner: ", String.format("%s", s.getPrompt()));
        
        
        Spinner t = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<?> adapter2 = ArrayAdapter.createFromResource(getBaseContext(), R.array.times, android.R.layout.simple_spinner_item);    
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        t.setAdapter(adapter2);
        
        Spinner u = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<?> adapter3 = ArrayAdapter.createFromResource(getBaseContext(), R.array.states, android.R.layout.simple_spinner_item);    
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        u.setAdapter(adapter3);

        Spinner v = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<?> adapter4 = ArrayAdapter.createFromResource(getBaseContext(), R.array.fellowships, android.R.layout.simple_spinner_item);    
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        v.setAdapter(adapter4);
        
        s.setOnItemSelectedListener(new MyOnItemSelectedListener());
        t.setOnItemSelectedListener(new MyOnItemSelectedListener2());
        u.setOnItemSelectedListener(new MyOnItemSelectedListener3());
        v.setOnItemSelectedListener(new MyOnItemSelectedListener4());
        
        Button goButton = (Button)findViewById(R.id.go_button);

        // Create reference to UI elements
        goButton.setOnClickListener( new OnClickListener() {
        	public void onClick(View view) {
        		Intent callingIntent = getIntent();
        		//Log.e("Inside Query.class Parameters:", callingIntent.getAction());
        		if (callingIntent.getAction().toString().compareTo("QUERY_MEETINGS") == 0) {
        			Intent intent = new Intent(getBaseContext(), MeetingActivity.class);
        			intent.setAction("QUERY_MEETINGS");
        			Uri uri = Uri.parse(day + '-' + time + '-' + state + '-' + fellowship);
        			//Log.e("Inside Query.class Calling EarthQuake.class", uri.toString());
        			intent.setData(uri);
        			startActivity(intent);
        		
        		} else if (callingIntent.getAction().toString().compareTo("MAP_MEETINGS") == 0) {
        			SetGeoPointsTask setGeoPointsTask = new SetGeoPointsTask();
        			Boolean result = null;
				
        			try {
						result = setGeoPointsTask.execute().get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			if (!result)
        				Toast.makeText(Query.this, "No Meetings Found For Selected Time: " +          
        		    			time, Toast.LENGTH_LONG).show(); 	
        		}
        	}
        });
    }
    
	//usually, subclasses of AsyncTask are declared inside the activity class.
	//that way, you can easily modify the UI thread from here
	class SetGeoPointsTask extends AsyncTask<String, Integer, Boolean> {
		protected Boolean doInBackground(String... arg0) {
			if (openURL()) 
				return true;
			else
				return false;
		}
		// onPostExecute displays the results of the AsyncTask.
		protected void onPostExecute(Boolean _result) {
			

		}
	}
    
    private boolean openURL() {
    	    Random diceRoller = new Random(500);
    	    locInfo = diceRoller.toString();
    	    locInfo1 = locInfo.replace("(", "");
    	    locInfo = locInfo1.replace(")", "");
    	    locInfo1 = locInfo.replace("@", "");
    	    locInfo = locInfo1.replace("java.util.", "");
    	    // Add your data
    	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
    	    nameValuePairs.add(new BasicNameValuePair("time",time));
    	    nameValuePairs.add(new BasicNameValuePair("day", day));
    	    nameValuePairs.add(new BasicNameValuePair("file", locInfo));
    	    nameValuePairs.add(new BasicNameValuePair("fellowship", fellowship));
    	    nameValuePairs.add(new BasicNameValuePair("state", state));
    	    nameValuePairs.add(new BasicNameValuePair("latitude", String.format("%s", myLocationLatitude)));
    	    nameValuePairs.add(new BasicNameValuePair("longitude", String.format("%s", myLocationLongitude)));
    	    
    	    HttpResponse response = null;
        	HttpClient httpclient = new DefaultHttpClient();    
        	  
        	  HttpPost httppost = new HttpPost(getString(R.string.base_url) + "meeting_query.php?day="+URLEncoder.encode(nameValuePairs.toString()));  
        	      // Add your data    
        	  //Log.e("Meetings: ", getString(R.string.base_url) + "meeting_query.php?time=&day="+URLEncoder.encode(nameValuePairs.toString()));
        	  //Log.e("Meetings: ", getString(R.string.base_url) + "meeting_query.php?time=&day="+nameValuePairs.toString()); 
        	    try { 
				      response = httpclient.execute(httppost); 
				      //Log.e("myapp", "response " + response.toString()); 
				  } catch (ClientProtocolException e) { 
				      e.printStackTrace(); 
				  } catch (IOException e) { 
				      e.printStackTrace(); 
				  } 
				  String responseBody = null;
				  //Log.e("Code: ",  getString(response.getStatusLine().getStatusCode()));
				  //Log.e("Phrase: ",  response.getStatusLine().getReasonPhrase());
				  try {
					responseBody = EntityUtils.toString(response.getEntity());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 

				  
				  
			//Log.e("Return: ",  responseBody);
			if (responseBody.compareTo("true") == 0) {	  
				String locInfo2 = String.format(getString(R.string.base_url)+"%s",locInfo);      
				//Log.e("Meetings", String.format(getString(R.string.base_url)+"%s",locInfo));
				Uri uri = null;
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri); 
				//Uri uri1 = Uri.parse("geo:0,0?q=http://aenutter.onlinewebshop.net/example.xml");
				Uri uri1 = Uri.parse("geo:0,0?q="+locInfo2);
				//Log.e("URI", uri1.toString());
				mapIntent.setData(uri1);
        	
				startActivity(Intent.createChooser(mapIntent, "Sample Map ")); 
				return true;
			} else {
				return false;
			}
    	}     
	public class MyOnItemSelectedListener implements OnItemSelectedListener {    
    	public void onItemSelected(AdapterView<?> parent,        
    			View view, int pos, long id) {      
    		//Toast.makeText(parent.getContext(), "The day is " +          
    			//parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();    
    			day = parent.getItemAtPosition(pos).toString();
    	}    
    	public void onNothingSelected(AdapterView<?> parent) {      
    		// Do nothing.    }}
    	}
		
		
    }
	
	public class MyOnItemSelectedListener2 implements OnItemSelectedListener {    
    	public void onItemSelected(AdapterView<?> parent,        
    			View view, int pos, long id) {      
    		//Toast.makeText(parent.getContext(), "The time is " +          
    			//parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();    
    			time = parent.getItemAtPosition(pos).toString();
    	}    
    	public void onNothingSelected(AdapterView<?> parent) {      
    		// Do nothing.    }}
    	}
		
		
    }
	
	public class MyOnItemSelectedListener3 implements OnItemSelectedListener {    
    	public void onItemSelected(AdapterView<?> parent,        
    			View view, int pos, long id) {      
    		//Toast.makeText(parent.getContext(), "The time is " +          
    			//parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();    
    			state = parent.getItemAtPosition(pos).toString();
    	}    
    	public void onNothingSelected(AdapterView<?> parent) {      
    		// Do nothing.    }}
    	}
	}
	
	public class MyOnItemSelectedListener4 implements OnItemSelectedListener {    
    	public void onItemSelected(AdapterView<?> parent,        
    			View view, int pos, long id) {      
    		//Toast.makeText(parent.getContext(), "The time is " +          
    			//parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();    
    			fellowship = parent.getItemAtPosition(pos).toString();
    	}    
    	public void onNothingSelected(AdapterView<?> parent) {      
    		// Do nothing.    }}
    	}
	}
    
   
}