package org.example.aafinder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
//import android.util.Log;
//import android.view.ActionMode;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TimePicker;
import android.widget.Toast;


public class MeetingActivity extends Activity {
	 protected Object mActionMode;
	public String day;
	public String state;
	public String fellowship;
	ListView meetingListView;
	ArrayAdapter<Meeting> aa;
	String[] temp2;    
	static final private int MEETING_DIALOG = 1;
	static final private int TIME_DIALOG_ID = 2;
	static final private int NULL_DIALOG = 3;
	static final private int SECURITY_DIALOG = 4;
	//static final private int EDIT_MEETING_DIALOG = 5;
	private TextView editTextTime, editTextType, editTextGroupName, editTextStreet, editTextPhone, editTextCity, editTextLocation, editTextZipcode;    
	private int mHour;    
	private int mMinute;    
	Meeting meeting;
	  private Uri todoUri;

	  private static int lastClickId = -1;
	//ArrayAdapter<Movie> aa;
		ArrayList<Meeting> meetings = new ArrayList<Meeting>();
		ArrayList<Meeting> temp = new ArrayList<Meeting>();
		ListView listView;
		Meeting selectedMeeting;
		public Bitmap placeholder;
		MovieAdapter movieAdapter;
		RefreshMeetingsTask refreshMeetingsTask;
		BroadcastReceiver mConnReceiver;
		int screenWidth;
	    int screenHeight;
	    double myLocationLatitude;
	    double myLocationLongitude;
	    

	    
	    @Override
  public void onCreate(Bundle savedInstanceState) {
	  
	  super.onCreate(savedInstanceState);
     
      setContentView(R.layout.meetingmain);
      //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      //listView.setSelector(android.R.color.darker_gray);
      
      GPSTracker myGPSTracker = new GPSTracker(this);
      
      myLocationLatitude = myGPSTracker.getLatitude();
      myLocationLongitude = myGPSTracker.getLongitude();
      
      String Text = "My current location is: " +
    		    "Latitud = " + myLocationLatitude +
    		    "Longitud = " + myLocationLongitude;
      //Log.e("meeting activity", Text);
      
      Intent intent = getIntent();
      intent.getData();
    	String temp = intent.getDataString();
    	
    	
    	temp2 = temp.split("-", 4);
    RefreshMeetingsTask refreshMeetingsTask = new RefreshMeetingsTask();

    refreshMeetingsTask.execute(null, null, null);
    refreshMeetingsTask = new RefreshMeetingsTask();
    Boolean result = null;
    //Log.e("result", String.format("%s", result));
	try {
		result = refreshMeetingsTask.execute(null, null, null).get();
	} catch (InterruptedException e1) {
		// TODO Auto-generated catch block
		//Log.e("exception", String.format("%s", e1));
		e1.printStackTrace();
	} catch (ExecutionException e1) {
		// TODO Auto-generated catch block
		//Log.e("exception", String.format("%s", e1));
		e1.printStackTrace();
	}  
	
	
	
	if (!result) {
    	Toast.makeText(MeetingActivity.this, "No Meetings Found For Selected Time: " +          
		temp2[1], Toast.LENGTH_LONG).show();
    	//finish();
	}
  }
  


  private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

   // Called when the action mode is created; startActionMode() was called
   public boolean onCreateActionMode(ActionMode mode, Menu menu) {
    // Inflate a menu resource providing context menu items
    MenuInflater inflater = mode.getMenuInflater();
    // Assumes that you have "contexual.xml" menu resources
    inflater.inflate(R.menu.contextual, menu);
    return true;
   }

   // Called each time the action mode is shown. Always called after
   // onCreateActionMode, but
   // may be called multiple times if the mode is invalidated.
   public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
    return false; // Return false if nothing is done
   }

   // Called when the user selects a contextual menu item
   public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    switch (item.getItemId()) {
    case R.id.addToFavorites: {
		addToFavorites();
        break;
		}	
    case R.id.about:
    	showDialog(MEETING_DIALOG);
     mode.finish(); // Action picked, so close the CAB
     return true;
    case R.id.share:
    	socialShare();
     mode.finish(); // Action picked, so close the CAB
     return true;
    case R.id.edit:
    	editMeetingDialog();
     mode.finish(); // Action picked, so close the CAB
     return true;
    case R.id.delete:
    	DeleteMeetingTask deleteMeetingTask = new DeleteMeetingTask();
		  Boolean deleteMeetingresult = null;
		  try {
			deleteMeetingresult = deleteMeetingTask.execute(null, null, null).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			  if (deleteMeetingresult) {
				  refreshMeetingsTask = new RefreshMeetingsTask();
      			  Boolean result = null;
      			  try {
					result = refreshMeetingsTask.execute(null, null, null).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

      			  if (!result)
      				  Toast.makeText(MeetingActivity.this, "No Meetings Found For Selected Time: " +          
            			temp2[1], Toast.LENGTH_LONG).show();

			  }
     mode.finish(); // Action picked, so close the CAB
     return true;

    default:
     return false;
    }
	return false;
   }

   // Called when the user exits the action mode
   public void onDestroyActionMode(ActionMode mode) {
    mActionMode = null;
   }
  };

	
	@Override
	protected void onPause() {
	//Log.e(LOGTAG, "onPause");
	super.onPause();
	//Log.e("main activity on pause", "un registering receiver");
	try {
		//unregisterReceiver(mConnReceiver);
	} 
	catch(IllegalArgumentException e) {
		e.printStackTrace();
	}
	}
	
	@Override
	protected void onResume() {
	//Log.e(LOGTAG, "onResume");
	super.onResume();
	//Log.e("on resume", "registering receiver");

	//registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	
	
	public void setupList() {
		ListView listView = (ListView) findViewById(R.id.ListViewId);
		//listView.setSelector(R.drawable.selector);
		listView.setAdapter(movieAdapter);
		//Log.e("meeting activity meeting count: ", String.format("%s", meetings.size()));

		
		
		
		
		
		class ItemHighlighterListener implements OnItemClickListener{
		    private View lastSelectedView = null;
		    public void clearSelection()
		    {
		        if(lastSelectedView != null) 
		        	lastSelectedView.setBackgroundColor(0xff020202);
		    }
		    //public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				//	long arg3) {
				// TODO Auto-generated method stub
				
			//}
		    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		    	clearSelection();
		    	
		        lastSelectedView = view;
		        view.setBackgroundColor(0xff0099cc);
		        meeting = meetings.get(arg2);
            	//Log.e("meeting activity", "setting: " + String.format("index: %s", arg2) + "to: blue");
            	 if (mActionMode != null) {
  	  		       return;
  	  		      }
  	  		      mActionMode = startActionMode(mActionModeCallback);
  	  		     //view.setSelected(true);
				return;
		    }
			
		}
		
		
		

		listView.setOnItemClickListener(new ItemHighlighterListener ());
		
		/*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> listView, View view,
                    int index, long arg3) {
            	meeting = meetings.get(index);
            	Log.e("meeting activity", String.format("index: %s", index));
            	 if (mActionMode != null) {
  	  		       //return false;
  	  		      }
  	  		      mActionMode = startActionMode(mActionModeCallback);
  	  		     listView.setSelected(true);
				return false;
  	  		     }
		});*/ 
	}
	//usually, subclasses of AsyncTask are declared inside the activity class.
	//that way, you can easily modify the UI thread from here
	class RefreshMeetingsTask extends AsyncTask<String, Integer, Boolean> {
		protected Boolean doInBackground(String... arg0) {
			if (refreshMeetings())
				return true;
			return false;
		}
		// onPostExecute displays the results of the AsyncTask.
		protected void onPostExecute(Boolean _result) {
			//Log.e("downloadfile post execute", String.format("refresh result: %s", _result));
			//if (_result) {
				//Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
				//Log.e("main activity", "after execute");
				movieAdapter = new MovieAdapter(getBaseContext(), MeetingActivity.this, R.layout.list_item, meetings);
				//listView.setAdapter(movieAdapter);
				meetings = temp;
				movieAdapter.notifyDataSetChanged();
				//Log.e("main activity", "after execute");
				setupList();
			//}
        }
     }
	
	//usually, subclasses of AsyncTask are declared inside the activity class.
			//that way, you can easily modify the UI thread from here
			class UpdateMeetingTask extends AsyncTask<String, Integer, Boolean> {
				protected Boolean doInBackground(String... arg0) {
					if (meeting.updateMeeting(meeting)) 
						return true;
					else
						return false;
				}
				// onPostExecute displays the results of the AsyncTask.
				protected void onPostExecute(Boolean _result) {
		    	  //Toast.makeText(MeetingActivity.this, "update meeting asynch result: "+_result, Toast.LENGTH_LONG).show();
				}
			}
	
	//usually, subclasses of AsyncTask are declared inside the activity class.
		//that way, you can easily modify the UI thread from here
		class DeleteMeetingTask extends AsyncTask<String, Integer, Boolean> {
			protected Boolean doInBackground(String... arg0) {
				if (deleteMeeting(meeting.getID())) 
					return true;
				else
					return false;
			}
			// onPostExecute displays the results of the AsyncTask.
			protected void onPostExecute(Boolean _result) {
				//finish();
				//Toast.makeText(MeetingActivity.this, "delete meeting asynch result: "+_result, Toast.LENGTH_LONG).show();
			}
		}
  	
	//usually, subclasses of AsyncTask are declared inside the activity class.
	//that way, you can easily modify the UI thread from here
	class SetGeoPointsTask extends AsyncTask<String, Integer, Boolean> {
		protected Boolean doInBackground(String... arg0) {
			if (meeting.setGeoPoints(meeting)) 
					return true;
			else
				return false;
		}
		// onPostExecute displays the results of the AsyncTask.
		protected void onPostExecute(Boolean _result) {
    	  //Toast.makeText(MeetingActivity.this, "asynch result: "+_result, Toast.LENGTH_LONG).show();
		}
	}
  	
		
	

  private Boolean refreshMeetings() {
	  
	
// Get the XML
	  URL url = null;
	    
		  //HttpPost httppost = new HttpPost("http://192.168.10.36/server.php");
		  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	        nameValuePairs.add(new BasicNameValuePair("time",temp2[1]));
	        nameValuePairs.add(new BasicNameValuePair("day", temp2[0]));
	        nameValuePairs.add(new BasicNameValuePair("state", temp2[2]));
	        nameValuePairs.add(new BasicNameValuePair("fellowship", temp2[3]));
	        //nameValuePairs.add(new BasicNameValuePair("action", "validate_password"));
	    temp.clear();      
		  

	    //Log.e("Refresh earthmeetings: ", getString(R.string.base_url) + "server.php?day=" +nameValuePairs.toString());
	    String parameters = URLEncoder.encode(nameValuePairs.toString());
	    //Log.e("Query: ", getString(R.string.base_url) + "server.php?parameters=" +parameters);
	    Uri uri = Uri.parse(getString(R.string.base_url) + "server.php?parameters=" +parameters);
	    try {
			url = new URL(uri.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //Log.e("HTTP Response ", url.toString());     
	    URLConnection connection = null;
	    try {
			connection = url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//Log.e("refresh meetings", "exception: " + e);
			e.printStackTrace();
		}
	    HttpURLConnection httpConnection = (HttpURLConnection)connection; 
	    int responseCode = 0;
		try {
			responseCode = httpConnection.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    

	    if (responseCode == HttpURLConnection.HTTP_OK) { 
	      InputStream in = null;
		try {
			in = httpConnection.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	          
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	      // Parse the earthmeeting feed.
	      Document dom = null;
		try {
			dom = db.parse(in);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
	      Element docEle = dom.getDocumentElement();
	        
	      // Clear the old earthmeetings
	      meetings.clear();
	          
	      // Get a list of each earthmeeting entry.
	      NodeList nl = docEle.getElementsByTagName("rec");
	      //Log.e("meeting activity", "number meetings: "+String.format("%s", nl.getLength()));
	      if (nl != null && nl.getLength() > 0) {
	        for (int i = 0 ; i < nl.getLength(); i++) {
	          Element entry = (Element)nl.item(i);
	          
	          Element _id = (Element) entry.getElementsByTagName("id").item(0);
	          Element _startTime = (Element)entry.getElementsByTagName("start_time").item(0);
	          Element _type = (Element)entry.getElementsByTagName("type").item(0);
	          Element _groupName = (Element)entry.getElementsByTagName("group_name").item(0);
	          Element _street = (Element)entry.getElementsByTagName("street").item(0);
	          Element _phone = (Element)entry.getElementsByTagName("phone").item(0);
	          Element _city = (Element)entry.getElementsByTagName("city").item(0);
	          Element _state = (Element)entry.getElementsByTagName("state").item(0);
	          Element _day = (Element)entry.getElementsByTagName("day").item(0);
	          Element _latitude = (Element)entry.getElementsByTagName("latitude").item(0);
	          Element _longitude = (Element)entry.getElementsByTagName("longitude").item(0);
	          //Element _MAC = (Element)entry.getElementsByTagName("MAC").item(0);
	          Element _fellowship = (Element)entry.getElementsByTagName("fellowship").item(0);
	          Element _location = (Element)entry.getElementsByTagName("location").item(0);
	          Element _zipcode = (Element)entry.getElementsByTagName("zip").item(0);
	          
	          //Log.e("meeting activity", "latitude: "+_latitude.getFirstChild().getNodeValue());
	          
	          //Log.e("meeting activity", "longitude: "+_longitude.getFirstChild().getNodeValue());
	          
	          String id, startTime = null, type = null, groupName = null, street = null, phone = null, city = null, state = null, day = null, latitude = null, longitude = null,  /* MAC = null*/ fellowship = null, location = null, zipcode = null;    
	          id = _id.getFirstChild().getNodeValue();
	          startTime = _startTime.getFirstChild().getNodeValue();
	          type = _type.getFirstChild().getNodeValue();
	          groupName = _groupName.getFirstChild().getNodeValue();
	          //Log.e("Add Meeting: ", "id: " +id +" group: " +groupName);

	          street = _street.getFirstChild().getNodeValue();
	          phone = _phone.getFirstChild().getNodeValue();
	          city = _city.getFirstChild().getNodeValue();
	          state = _state.getFirstChild().getNodeValue();
	          day = _day.getFirstChild().getNodeValue();
	          latitude = _latitude.getFirstChild().getNodeValue();
	          longitude = _longitude.getFirstChild().getNodeValue();
	          fellowship = _fellowship.getFirstChild().getNodeValue();
	          location = _location.getFirstChild().getNodeValue();
	          zipcode = _zipcode.getFirstChild().getNodeValue();
	          
	         
	         
	         //MAC = _MAC.getFirstChild().getNodeValue();
	          //Log.e("Meeting", startTime + groupName + street + phone + city + state + day);
	          Meeting meeting = new Meeting(id, startTime, type, groupName, street, phone, city, state, day, latitude, longitude, /*, MAC*/fellowship, location, zipcode, false, 0);
	          //Log.e("Add Meeting: ", "id: " +id +" phone: " +phone);
	         meeting.distanceMatrix(meeting, (float)myLocationLatitude, (float)myLocationLongitude);
	          // Process a newly found earthmeeting
	          addNewMeeting(meeting);
	        
	          
		        
		      
	      		        }
	        
	      //sort by balance
	        Collections.sort(temp, new Comparator<Meeting>(){
	           public int compare(Meeting m1, Meeting m2) {
	              return m1.getDistance().compareTo(m2.getDistance());
	           }
	        });
	    	
	    	
	    	int j = 0;
	    	for (j=0; j<temp.size();j++) {
	    	//arrayList.add(temp.get(j));
	    	//Log.e("array list", "distance: "+String.format("%s",temp.get(j).getDistance())+" group: "+temp.get(j).getGroupName());
	    	}

	        return true;
	      }	
	    }
	    return false;
	}
  
  

    private void addNewMeeting(Meeting _meeting) {
	  // Add the new meeting to our list of earthmeetings.
	  temp.add(_meeting);

	  
	}
    
public void addToFavorites() {
    ContentValues values = new ContentValues();
    
	values.put(TodoTable.COLUMN_CATEGORY, "Reminder");
    values.put(TodoTable.COLUMN_SUMMARY, "Summary");
    values.put(TodoTable.COLUMN_DESCRIPTION, "Description");
    //Log.e("Add to favorites: ", "id: " +meeting.getID() +" group: " +meeting.getGroupName());

    values.put(TodoTable.COLUMN_FOREIGN_KEY, meeting.getID());
    values.put(TodoTable.COLUMN_START_TIME, meeting.getTime());
    values.put(TodoTable.COLUMN_TYPE, meeting.getType());
    values.put(TodoTable.COLUMN_GROUP_NAME, meeting.getGroupName());
    values.put(TodoTable.COLUMN_STREET, meeting.getStreet());
    values.put(TodoTable.COLUMN_PHONE, meeting.getPhone());
    values.put(TodoTable.COLUMN_CITY, meeting.getCity());
    values.put(TodoTable.COLUMN_STATE, meeting.getState());
    values.put(TodoTable.COLUMN_DAY, meeting.getDay());
    values.put(TodoTable.COLUMN_LATITUDE, meeting.getLatitude());
    values.put(TodoTable.COLUMN_LONGITUDE, meeting.getLongitude());
    values.put(TodoTable.COLUMN_FELLOWSHIP, meeting.getFellowship());
    values.put(TodoTable.COLUMN_LOCATION, meeting.getLocation());
    values.put(TodoTable.COLUMN_ZIP, meeting.getZipcode());

    todoUri = getContentResolver().insert(MyTodoContentProvider.CONTENT_URI, values);
}
    
    //static final private int MENU_UPDATE = Menu.FIRST;
    
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);

      menu.add(0, MENU_UPDATE, Menu.NONE, R.string.menu_update);
                  
      return true;
    }*/
            
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
      super.onOptionsItemSelected(item);
           
      switch (item.getItemId()) {
        case (MENU_UPDATE): {
        	refreshMeetingsTask = new RefreshMeetingsTask();
        	Boolean result = null;
            try {
				result = refreshMeetingsTask.execute(null, null, null).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (!result) {
            	Toast.makeText(MeetingActivity.this, "No Meetings Found For Selected Time: " +          
        			temp2[1], Toast.LENGTH_LONG).show();
            	movieAdapter.notifyDataSetChanged();
            }
            return true;
        }
      } 
      return false;
    }*/
    
    @Override
    public Dialog onCreateDialog(int id) {
      switch(id) {
      	
      		
        case (MEETING_DIALOG) :
          LayoutInflater li = LayoutInflater.from(this);
          View meetingDetailsView = li.inflate(R.layout.meeting_details, null);

          AlertDialog.Builder meetingDialog = new AlertDialog.Builder(this);
          meetingDialog.setTitle("View Meeting");
          meetingDialog.setIcon(R.drawable.dark_social_group);
          meetingDialog.setView(meetingDetailsView);
          //String button1String = "Edit";
          //String button2String = "Delete";
          //String button3String = "Cancel";

          new AlertDialog.Builder(this);
        
          meetingDialog.setCancelable(true);
          meetingDialog.setOnCancelListener(new OnCancelListener() {
        		  public void onCancel(DialogInterface dialog) {
        		  //eatenByGrue();
        		  }
        		  });
          return meetingDialog.create();
          case (TIME_DIALOG_ID) :
        	return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
        case (SECURITY_DIALOG) :
            
            AlertDialog.Builder securityDialog = new AlertDialog.Builder(this);
        	securityDialog.setTitle("** Security Error **");
        	securityDialog.setMessage("Only The Meeting Creator Can Edit/Delete This Meeting");
            String button7String = "OK";
            securityDialog.setPositiveButton(button7String,
          		  new OnClickListener() {
          		  public void onClick(DialogInterface dialog, int arg1) {
          			//updateMeetingDialog(meeting);
          			  
          		  }
          		  });
            securityDialog.setCancelable(true);
            securityDialog.setOnCancelListener(new OnCancelListener() {
          		  public void onCancel(DialogInterface dialog) {
          		  //eatenByGrue();
          		  }
          		  });
            return securityDialog.create();

      }
      	
      return null;
    }

    @Override
    public void onPrepareDialog(int id, Dialog dialog) {
      switch(id) {
 
      case (MEETING_DIALOG) :
        	
        	AlertDialog meetingDialog = (AlertDialog)dialog;
       
        
                meetingDialog.setTitle("Meeting Details");
        TextView textViewTime = (TextView)meetingDialog.findViewById(R.id.startTime);
        textViewTime.setText(meeting.getTime());
        TextView textViewDay = (TextView)meetingDialog.findViewById(R.id.day);
        textViewDay.setText(meeting.getDay());
        TextView textViewGroupName = (TextView)meetingDialog.findViewById(R.id.groupName);
        textViewGroupName.setText(meeting.getGroupName());
        TextView textViewType = (TextView)meetingDialog.findViewById(R.id.type);
        textViewType.setText(meeting.getType());
        TextView textViewPhone= (TextView)meetingDialog.findViewById(R.id.phone);
        textViewPhone.setText(meeting.getPhone());
        TextView textViewAddress = (TextView)meetingDialog.findViewById(R.id.street);
        textViewAddress.setText(meeting.getStreet());
        TextView textViewCity = (TextView)meetingDialog.findViewById(R.id.city);
        textViewCity.setText(meeting.getCity());
        TextView textViewState = (TextView)meetingDialog.findViewById(R.id.state);
        textViewState.setText(meeting.getState());
        TextView textViewZipcode = (TextView)meetingDialog.findViewById(R.id.zipcode);
        textViewZipcode.setText(meeting.getZipcode());
        TextView textViewFellowship = (TextView)meetingDialog.findViewById(R.id.fellowship);
        textViewFellowship.setText(meeting.getFellowship());
        TextView textViewNotes = (TextView)meetingDialog.findViewById(R.id.location);
        textViewNotes.setText(meeting.getLocation());

	
          
          
        
            }
  
        
            
    }
    // the callback received when the user "sets" the time in the dialogprivate 
 	TimePickerDialog.OnTimeSetListener mTimeSetListener =    new TimePickerDialog.OnTimeSetListener() {
 		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
 			mHour = hourOfDay;
 			mMinute = minute;
 			//updateDisplay();
 			
 			Calendar c = Calendar.getInstance(); 
 	        c.set(Calendar.HOUR_OF_DAY, mHour); 
 	        //instead of c.set(Calendar.HOUR, hour); 
 	        c.set(Calendar.MINUTE, mMinute); 
 	        //c.set(Calendar.AM_PM, mHour);
 	        //editTextTime.setText(new StringBuilder().append(Calendar.HOUR_OF_DAY).append(":").append(Calendar.MINUTE).append(" "+Calendar.AM));
 	        String delegate = "h:mm";  
 	        
 	        if(hourOfDay>=12) 
 		    { 
 		 
 	        	editTextTime.setText((String) DateFormat.format(delegate,c)+" PM"); 
 		    } 
 		    if(hourOfDay<12) 
 		    { 
 		    	editTextTime.setText((String) DateFormat.format(delegate,c)+" AM"); 
 		    } 
 		}
 	};

	private boolean deleteMeeting(String id) {
		HttpPost httppost;

		HttpClient httpclient;
		//Log.e("delete meeting httppost: ", getString(R.string.base_url)+"delete_meeting.php?id="+id);
		httppost = new HttpPost(getString(R.string.base_url)+"delete_meeting.php?id="+id);
		
		//Log.e("EarthMeeting.java delete meeting httppost: ", getString(R.string.base_url)+"delete_meeting.php?id="+id);
		//Log.e("Delete Meeting URL: ",  "http://aenutter.onlinewebshop.net/delete_meeting.php?id="+id);
		//Log.e("HttpPost", "http://192.168.10.36:80/delete_meeting.php?id="+id);
		httpclient = new DefaultHttpClient();
		// Send POST message  with given parameters to the HTTP server.
		HttpResponse response = null;
		String responseBody = null;
		try {                    
		response = httpclient.execute(httppost);


		
		//Log.e("Delete Meeting Code: ", String.format("%s",response.getStatusLine().getStatusCode()));
		responseBody = EntityUtils.toString(response.getEntity());
		}
		catch (Exception e) {
		// Exception handling
			//Log.e("Exception", e.toString());
			e.printStackTrace();
		 
		}
			
	
	if (!(responseBody.compareTo("true") == 0)) {	
		return true;
	} else {
		return false;
	}
		//refreshEarthmeetings();
		
	}
		
	public class MyOnItemSelectedListener4 implements OnItemSelectedListener {    
    	public void onItemSelected(AdapterView<?> parent,        
    			View view, int pos, long id, Meeting meeting) {      
    		//Toast.makeText(parent.getContext(), "The time is " +          
    			//parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();    
    			state = parent.getItemAtPosition(pos).toString();
    	}    
    	public void onNothingSelected(AdapterView<?> parent) {      
    		// Do nothing.    }}
    	}
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			meeting.setState(arg0.getItemAtPosition(arg2).toString());
			// TODO Auto-generated method stub
			
		}
		
		
    }
	
	public class MyOnItemSelectedListener5 implements OnItemSelectedListener {    
    	public void onItemSelected(AdapterView<?> parent,        
    			View view, int pos, long id, Meeting meeting) {      
    		//Toast.makeText(parent.getContext(), "The day is " +          
    			//parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();    
    			day = parent.getItemAtPosition(pos).toString();
    	}    
    	public void onNothingSelected(AdapterView<?> parent) {      
    		// Do nothing.    }}
    	}
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			//Log.e("listener 5", "selected item #: "+String.format("%s", arg2));
			//Log.e("listener 5 before set day", "existing day: "+selectedMeeting.getDay());
			meeting.setDay(arg0.getItemAtPosition(arg2).toString());
			//Log.e("listener 5 after set day", "existing day: "+selectedMeeting.getDay());
			// TODO Auto-generated method stub
			
		}
	}
	public class MyOnItemSelectedListener6 implements OnItemSelectedListener {    
    	public void onItemSelected(AdapterView<?> parent,        
    			View view, int pos, long id, Meeting meeting) {      
    		//Toast.makeText(parent.getContext(), "The day is " +          
    			//parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();    
    			fellowship = parent.getItemAtPosition(pos).toString();
    	}    
    	public void onNothingSelected(AdapterView<?> parent) {      
    		// Do nothing.    }}
    	}
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			meeting.setFellowship(arg0.getItemAtPosition(arg2).toString());
			// TODO Auto-generated method stub
			
		}
		
    }
	
 	public void showDialog(String title, String sayWhat, Context context){ 
		
        LayoutInflater li = LayoutInflater.from(context);
        View meetingDetailsView = li.inflate(R.layout.toast_layout, null);

        AlertDialog.Builder meetingDialog = new AlertDialog.Builder(this);
        meetingDialog.setTitle(title);
        meetingDialog.setView(meetingDetailsView);
        
        meetingDialog.setView(meetingDetailsView);
        meetingDialog.setMessage(sayWhat);
                
        String button1String = "OK";
        meetingDialog.setPositiveButton(button1String,
      		  new AlertDialog.OnClickListener() {
      		  public void onClick(DialogInterface dialog, int arg1) {
      		        		  }
      		  });
                meetingDialog.setCancelable(true);
        meetingDialog.setOnCancelListener(new OnCancelListener() {
      		  public void onCancel(DialogInterface dialog) {
      		  //eatenByGrue();
      		  }
      		  });
        meetingDialog.show();
        		
	} 

 	public void editMeetingDialog() {
 		LayoutInflater inflater=LayoutInflater.from(MeetingActivity.this);
 		View meetingEditView=inflater.inflate(R.layout.meeting_edit, null);
 		//Button posButton=(Button) meetingEditView.findViewById(R.id.pos);
 		//Button negButton=(Button) meetingEditView.findViewById(R.id.neg);
 		
 		AlertDialog.Builder builder = new AlertDialog.Builder(this);

 		
 		builder.setView(meetingEditView);
 		builder.setTitle("Edit Meeting");
 		builder.setIcon(R.drawable.dark_content_edit);

 		editTextTime = (TextView)meetingEditView.findViewById(R.id.startTime);
	        editTextTime.setText(meeting.getTime());
	        //TextView editTextDay = (TextView)editMeetingDialog.findViewById(R.id.day);
	        //editTextDay.setText(meeting.getDay());
	        editTextGroupName = (TextView)meetingEditView.findViewById(R.id.groupName);
	        editTextGroupName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
	        //editTextGroupName.setMovementMethod(ScrollingMovementMethod.getInstance());
	        editTextGroupName.setText(meeting.getGroupName());
	        
	        editTextType = (TextView)meetingEditView.findViewById(R.id.type);
	        editTextType.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
	        //editTextType.setMovementMethod(ScrollingMovementMethod.getInstance());
	        editTextType.setText(meeting.getType());
	        
	        editTextPhone= (TextView)meetingEditView.findViewById(R.id.phone);
	        editTextPhone.setInputType(InputType.TYPE_CLASS_PHONE);
	        editTextPhone.setText(meeting.getPhone());
	        
	        editTextStreet = (TextView)meetingEditView.findViewById(R.id.street);
	        editTextStreet.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
	        //editTextStreet.setMovementMethod(ScrollingMovementMethod.getInstance());
	        editTextStreet.setText(meeting.getStreet());
	        
	        editTextCity = (TextView)meetingEditView.findViewById(R.id.city);
	        editTextCity.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
	        editTextCity.setText(meeting.getCity());
	        
        editTextLocation = (TextView)meetingEditView.findViewById(R.id.notes);
        editTextLocation.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        //meeting.setCity(editTextCity.getText().toString());
        editTextLocation.setText(meeting.getLocation());
        
        editTextZipcode = (TextView)meetingEditView.findViewById(R.id.zipcode);
        editTextZipcode.setInputType(InputType.TYPE_CLASS_NUMBER);
        //meeting.setCity(editTextCity.getText().toString());
        editTextZipcode.setText(meeting.getZipcode());
        
	      editTextCity.setHint("Cannnot Be Empty");
      editTextStreet.setHint("Cannnot Be Empty");
      editTextGroupName.setHint("Group Name");
      editTextType.setHint("Closed");
      editTextPhone.setHint("(XXX) XXX-XXXX");
      editTextTime.setHint("8:00 AM");
      editTextLocation.setHint("Optional~(Upstairs First Door on Left)");
      editTextZipcode.setHint("XXXXX");
	        //Log.e("Inside onprepare dialog State: ", meeting.getState());
			//Log.e("Inside onpe dialog Day: ", meeting.getDay());
	        
	        //TextView editTextState = (TextView)editMeetingDialog.findViewById(R.id.state);
	        //editTextState.setText(meeting.getState());
	        
	        Spinner spinnerStates = (Spinner) meetingEditView.findViewById(R.id.spinner4);
	        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);    
	        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	        spinnerStates.setAdapter(adapter4);
	        int pos4=adapter4.getPosition(meeting.getState()); 
	        spinnerStates.setSelection(pos4);
	        spinnerStates.setSelection(pos4, false);
	        spinnerStates.setOnItemSelectedListener(new MyOnItemSelectedListener4());
	        
	        Spinner spinnerDays = (Spinner) meetingEditView.findViewById(R.id.spinner5);
	        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);    
	        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	        spinnerDays.setAdapter(adapter5);
	        int pos5=adapter5.getPosition(meeting.getDay());
	        //Log.e("editmeeting dialog", "selected item #: "+String.format("%s", pos5)+" day: "+meeting.getDay()+" City: "+meeting.getCity());
	        spinnerDays.setSelection(pos5); 
	        spinnerDays.setSelection(pos5, false);
	        spinnerDays.setOnItemSelectedListener(new MyOnItemSelectedListener5());
	        
	        Spinner spinnerFellowships = (Spinner) meetingEditView.findViewById(R.id.spinner6);
	        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this, R.array.fellowships, android.R.layout.simple_spinner_item);    
	        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	        spinnerFellowships.setAdapter(adapter6);
	        int pos6=adapter6.getPosition(meeting.getFellowship()); 
        spinnerFellowships.setSelection(pos6); 
        spinnerFellowships.setSelection(pos6, false);
        spinnerFellowships.setOnItemSelectedListener(new MyOnItemSelectedListener6());
	        
	        editTextTime.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		showDialog(TIME_DIALOG_ID);
	        	}        
	        });
 		
 		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
	  		  public void onClick(DialogInterface dialog, int arg1) {
	  			if ((editTextStreet.getText().toString().equals("")) || (editTextCity.getText().toString().equals(""))) {
	  	  			showDialog(NULL_DIALOG);
	  	  			return;
	  	  		  }
	  		
	  		
	  	  	  editTextCity.setText(editTextCity.getText().toString().replace(",", " "));
	  		  editTextCity.setText(editTextCity.getText().toString().replace("&", " and "));
	  		  if (editTextCity.length() == 0) meeting.setCity(editTextCity.getHint().toString()); 
	  		  else meeting.setCity(editTextCity.getText().toString());
	  		  
	  		  editTextStreet.setText(editTextStreet.getText().toString().replace(",", " "));
	  	  editTextStreet.setText(editTextStreet.getText().toString().replace("&", " and "));
	  		  if (editTextStreet.length() == 0) meeting.setStreet(editTextStreet.getHint().toString()); 
	  		  else meeting.setStreet(editTextStreet.getText().toString());
	  		  
	  		  if (editTextTime.length() == 0) meeting.setTime(editTextTime.getHint().toString()); 
	  		  else meeting.setTime(editTextTime.getText().toString());
	  		  
	  		  editTextType.setText(editTextType.getText().toString().replace(",", " "));
	  	  editTextType.setText(editTextType.getText().toString().replace("&", " and "));
	  		  if (editTextType.length() == 0) meeting.setType(" "); 
	  		  else meeting.setType(editTextType.getText().toString());
	  		  
	  		  editTextGroupName.setText(editTextGroupName.getText().toString().replace(",", " "));
	  	  editTextGroupName.setText(editTextGroupName.getText().toString().replace("&", " and ")); 
	  		  if (editTextGroupName.length() == 0) meeting.setGroupName(" "); 
	  		  else meeting.setGroupName(editTextGroupName.getText().toString());
	  	  
	  		  editTextPhone.setText(editTextPhone.getText().toString().replace(",", " "));
	  	  editTextPhone.setText(editTextPhone.getText().toString().replace("&", " and "));
	  		  if (editTextPhone.length() == 0) meeting.setPhone(" "); 
	  		  else meeting.setPhone(editTextPhone.getText().toString());
	  		  
	  		editTextLocation.setText(editTextLocation.getText().toString().replace(",", " "));
	    editTextLocation.setText(editTextLocation.getText().toString().replace("&", " and "));
	    if (editTextLocation.length() == 0) meeting.setLocation(" "); 
	    else meeting.setLocation(editTextLocation.getText().toString());
	    
	    editTextZipcode.setText(editTextZipcode.getText().toString().replace(",", " "));
	    editTextZipcode.setText(editTextZipcode.getText().toString().replace("&", " and "));
	    if (editTextZipcode.length() == 0) meeting.setZipcode("0"); 
	    else meeting.setZipcode(editTextZipcode.getText().toString());
	    
	  	  		  //meeting.setDay(day);
	  	  		  //meeting.setState(state);
	  	  		  //meeting.setFellowship(fellowship);
	  	  		  //Log.e("Inside oncreate dialog State: ", meeting.getState());
	  	  		  //Log.e("Inside oncreate dialog Day: ", meeting.getDay());
	  	  		  //state = meeting.getState();
	  	  		  //day = meeting.getDay();

	  		  //meeting.setTime(editTextTime.getText().toString());
	    SetGeoPointsTask setGeoPointsTask = new SetGeoPointsTask();	  
	    Boolean result = null;
		try {
			result = setGeoPointsTask.execute(null, null, null).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    
	    
	    //Log.e("editmeeting dialog after asycn", "result: "+result);  		
	  		  if (result) {
	  			UpdateMeetingTask updateMeetingTask = new UpdateMeetingTask();
    			  Boolean updateMeetingresult = null;
    			  try {
    				  updateMeetingresult = updateMeetingTask.execute(null, null, null).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  			  if (updateMeetingresult) {

	  				String sayWhat = String.format("Time:  %s\nDay:  %s\nGroup:  %s\nType:  %s\nPhone:  %s\nStreet:  %s\nCity:  %s\nState:  %s",
	  					  meeting.getTime(), meeting.getDay(), meeting.getGroupName(), meeting.getType(), 
	  					  meeting.getPhone(), meeting.getStreet(), meeting.getCity(), meeting.getState());
	  				String title = String.format("Update Success!");
	  			  
	  				showDialog(title, sayWhat, getBaseContext());
	  			  } else {
	  				  String sayWhat = String.format("Time:  %s\nDay:  %s\nGroup:  %s\nType:  %s\nPhone:  %s\nStreet:  %s\nCity:  %s\nState:  %s",
	  				  meeting.getTime(), meeting.getDay(), meeting.getGroupName(), meeting.getType(), 
	  				  meeting.getPhone(), meeting.getStreet(), meeting.getCity(), meeting.getState());
	  				  String title = String.format("** Update Failure! **");
	  		  
	  				  showDialog(title, sayWhat, getBaseContext());
	  			  }

	  		  } else {
	  			String sayWhat = String.format("Unable to Geo Code latitude and longitude" + "\n" +
	          		"for given address, city and state" );
	  			String title = String.format("** Geo Code Failure! **");
	  			  
	  		  	showDialog(title, sayWhat, getBaseContext());
	  		  }
	  		refreshMeetingsTask = new RefreshMeetingsTask();
  			result = null;
	            try {
					result = refreshMeetingsTask.execute(null, null, null).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            if (!result)
	            	Toast.makeText(MeetingActivity.this, "No Meetings Found For Selected Time: " +          
      			temp2[1], Toast.LENGTH_LONG).show();
	  		}
	  		  
	  		  
	  	});
        
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        
        Button saveButton = new Button(this); 
        saveButton = ((Button)alert.findViewById(android.R.id.button1));
        saveButton.setPadding(30, 0, 0, 0);
      
        saveButton.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(
                R.drawable.dark_content_save), null, null, null);

        Button cancelButton = new Button(this); 
        cancelButton = ((Button)alert.findViewById(android.R.id.button2));
        cancelButton.setPadding(30, 0, 0, 0);
      
        cancelButton.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(
                R.drawable.dark_navigation_cancel), null, null, null);
	}
 	public void socialShare() {
 		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
 		sharingIntent.setType("text/plain");
 		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this meeting I found");
 		//sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, URLEncoder.encode("https://play.google.com/store/apps/details?id=org.example.aafinder&feature=more_from_developer")+
 		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check out this meeting!\nTime:  "+meeting.getTime()+"\nDay:  "+meeting.getDay()+"\nStreet:  "+meeting.getStreet()+"\nCity:  "+meeting.getCity()+"\nCoordinates:  "+meeting.getLatitude()+" "+meeting.getLongitude());
		
 			
 		startActivity(Intent.createChooser(sharingIntent, "Share"));
	                return;
 	}            
}