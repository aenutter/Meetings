package org.example.aafinder;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
//import android.net.wifi.WifiInfo;
//import android.net.wifi.WifiManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
//import android.util.Log;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import org.example.aafinder.TodosOverviewActivity;

public class Main extends Activity {
	public String day;
	public String time;
	public String state;
	public String fellowship;
	String locInfo = null;
	static final private int TIME_DIALOG = 2;
	static final private int NULL_DIALOG = 3;
	static final private int CREATE_MEETING_DIALOG = 4;
	private TextView editTextTime, editTextType, editTextGroupName, editTextStreet, editTextPhone, editTextCity, editTextZipcode, editTextLocation;    
	private int mHour;    
	private int mMinute;    
	Meeting selectedMeeting;    
	BroadcastReceiver mConnReceiver;
	Button mapButton;
    Button editDeleteButton;
    Button createButton;
    Button socialShareButton;
    Button favoritesButton;
    
	public final String LOGTAG = "Main.java";	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        mapButton = (Button)findViewById(R.id.mapButton);
        editDeleteButton = (Button)findViewById(R.id.editDeleteButton);
        createButton = (Button)findViewById(R.id.createButton);
        socialShareButton = (Button)findViewById(R.id.socialShareButton);
        favoritesButton = (Button)findViewById(R.id.favoritesButton);
            
            mConnReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                    intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
        intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
                    intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                    intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

                    ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        	//Toast.makeText(Main.this, "Network connection established, please resume.", Toast.LENGTH_LONG).show();
        	mapButton.setOnClickListener( new OnClickListener() {
            	public void onClick(View view) {
              		Intent intent = new Intent();
              		intent.setClass(getApplicationContext(), Query.class);
              		intent.setAction("MAP_MEETINGS");
              		//Uri uri = Uri.parse(day + '-' + time + '-' + state);
              		//Log.e("Inside WebViewDemo Calling EarthQuake.class", uri.toString());
              		//intent.setData(uri);
              		startActivity(intent);

              	}
              });
        	favoritesButton.setOnClickListener( new OnClickListener() {
              	public void onClick(View view) {
              		Intent intent = new Intent();
              		intent.setClass(getApplicationContext(), TodosOverviewActivity.class);
              		//intent.setAction("QUERY_MEETINGS");
              		//Uri uri = Uri.parse(day + '-' + time + '-' + state);
              		        		//intent.setData(uri);
              		startActivity(intent);
              		
              		
              	}
              });
              editDeleteButton.setOnClickListener( new OnClickListener() {
              	public void onClick(View view) {
              		Intent intent = new Intent();
              		intent.setClass(getApplicationContext(), Query.class);
              		intent.setAction("QUERY_MEETINGS");
              		//Uri uri = Uri.parse(day + '-' + time + '-' + state);
              		        		//intent.setData(uri);
              		startActivity(intent);
              		
              		
              	}
              });
              createButton.setOnClickListener( new OnClickListener() {
              	public void onClick(View view) {
              		//WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
              		//WifiInfo wifiInfo = manager.getConnectionInfo(); 
              		//String MACAddress = wifiInfo.getMacAddress(); 
              	selectedMeeting  = new Meeting("", "8:00 AM", "", "", "", "", "", "", "", "", ""/*, "38:E7:D8:Bf:53:38" + " D8:B3:77:39:97:F0 " + MACAddress*/, "", "", "", false, 0);
              	showDialog(CREATE_MEETING_DIALOG);	
              	}
              });
              socialShareButton.setOnClickListener( new OnClickListener() {
                	public void onClick(View view) {
                		//WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
                		//WifiInfo wifiInfo = manager.getConnectionInfo(); 
                		//String MACAddress = wifiInfo.getMacAddress(); 
                	socialShare();	
                	}
                });
        }

            }};
        
            registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)); 

            
    }
    
    @Override
	protected void onDestroy() {
	//Log.e(LOGTAG, "onPause");
	super.onDestroy();
	//Log.e("main activity on pause", "un registering receiver");
	//Log.e("over view", "delete db: "+this.deleteDatabase("todotable.db"));
	}
    
    @Override
	protected void onPause() {
	//Log.e(LOGTAG, "onPause");
	super.onPause();
	//Log.e("main activity on pause", "un registering receiver");
	try {
		unregisterReceiver(mConnReceiver);
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
	registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	
    ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        	mapButton.setOnClickListener( new OnClickListener() {
            	public void onClick(View view) {
              		Intent intent = new Intent();
              		intent.setClass(getApplicationContext(), Query.class);
              		intent.setAction("MAP_MEETINGS");
              		//Uri uri = Uri.parse(day + '-' + time + '-' + state);
              		//Log.e("Inside WebViewDemo Calling EarthQuake.class", uri.toString());
              		//intent.setData(uri);
              		startActivity(intent);

              	}
              });
              editDeleteButton.setOnClickListener( new OnClickListener() {
              	public void onClick(View view) {
              		Intent intent = new Intent();
              		intent.setClass(getApplicationContext(), Query.class);
              		intent.setAction("QUERY_MEETINGS");
              		//Uri uri = Uri.parse(day + '-' + time + '-' + state);
              		        		//intent.setData(uri);
              		startActivity(intent);
              		
              		
              	}
              });
              createButton.setOnClickListener( new OnClickListener() {
              	public void onClick(View view) {
              		//WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
              		//WifiInfo wifiInfo = manager.getConnectionInfo(); 
              		//String MACAddress = wifiInfo.getMacAddress(); 
              	selectedMeeting  = new Meeting("", "8:00 AM", "", "", "", "", "", "", "", "", ""/*, "38:E7:D8:Bf:53:38" + " D8:B3:77:39:97:F0 " + MACAddress*/, "", "", "", false, 0);
              	showDialog(CREATE_MEETING_DIALOG);	
              	}
              });

        	            } else {
            Toast.makeText(this, "No network connection available.\nPlease connect to a network and try again.", Toast.LENGTH_LONG).show();
            
        }


	}

	public void socialShare() {
 		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
 		sharingIntent.setType("text/plain");
 		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this app and meeting I found");
 		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check out this app I found\nhttps://play.google.com/store/apps/details?id=org.example.aafinder&feature=more_from_developer");
 		//sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check out this meeting!\nTime:  "+meeting.getTime()+"\nDay:  "+meeting.getDay()+"\nStreet:  "+meeting.getStreet()+"\nCity:  "+meeting.getCity()+"\nCoordinates:  "+meeting.getLatitude()+" "+meeting.getLongitude());
		
 			
 		startActivity(Intent.createChooser(sharingIntent, "Share"));
	                return;
 	}	 
	
	//usually, subclasses of AsyncTask are declared inside the activity class.
	//that way, you can easily modify the UI thread from here
	class SetGeoPointsTask extends AsyncTask<String, Integer, Boolean> {
		protected Boolean doInBackground(String... arg0) {
			if (selectedMeeting.setGeoPoints(selectedMeeting)) 
					return true;
			else
				return false;
		}
		// onPostExecute displays the results of the AsyncTask.
		protected void onPostExecute(Boolean _result) {
    	  //Toast.makeText(Main.this, "asynch result: "+_result, Toast.LENGTH_LONG).show();
		}
	}
    
	//usually, subclasses of AsyncTask are declared inside the activity class.
		//that way, you can easily modify the UI thread from here
		class CreateMeetingTask extends AsyncTask<String, Integer, Boolean> {
			protected Boolean doInBackground(String... arg0) {
				if (selectedMeeting.createMeeting(selectedMeeting)) 
						return true;
				else
					return false;
			}
			// onPostExecute displays the results of the AsyncTask.
			protected void onPostExecute(Boolean _result) {
	    	  //Toast.makeText(Main.this, "asynch result: "+_result, Toast.LENGTH_LONG).show();
			}
		}
		
    @Override
    public Dialog onCreateDialog(int id) {
      switch(id) {
                  case (TIME_DIALOG) :
        	return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);

                  case (NULL_DIALOG) :
                  
                  AlertDialog.Builder nullDialog = new AlertDialog.Builder(this);
                  nullDialog.setTitle("Input Error");
                  nullDialog.setMessage("Street and City Cannot be Empty");
                  String button6String = "OK";
                  nullDialog.setPositiveButton(button6String,
                		  new AlertDialog.OnClickListener() {
                		  public void onClick(DialogInterface dialog, int arg1) {
                			//createMeetingDialog(selectedMeeting);
                		  }
                		  });
                  nullDialog.setCancelable(true);
                  nullDialog.setOnCancelListener(new OnCancelListener() {
                		  public void onCancel(DialogInterface dialog) {
                		  //eatenByGrue();
                		  }
                		  });
                  return nullDialog.create();
        case (CREATE_MEETING_DIALOG) :
        	
        	LayoutInflater layoutInflater2 = LayoutInflater.from(this);
	        View quakeCreateView = layoutInflater2.inflate(R.layout.meeting_edit, null);
            
            AlertDialog.Builder createMeetingDialog = new AlertDialog.Builder(this);
            createMeetingDialog.setView(quakeCreateView);
        	createMeetingDialog.setTitle("Create Meeting");
        	createMeetingDialog.setIcon(R.drawable.dark_social_add_group);
            String button7String = "Save";
            String button8String = "Cancel";
            editTextTime = (TextView)quakeCreateView.findViewById(R.id.startTime);
            //selectedQuake.setTime(editTextTime.getText().toString());
            //editTextTime.setText(selectedMeeting.getTime().toString());
            
            //TextView editTextDay = (TextView)editQuakeDialog.findViewById(R.id.day);
            //editTextDay.setText(selectedQuake.getDay());
            
            editTextGroupName = (TextView)quakeCreateView.findViewById(R.id.groupName);
            editTextGroupName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            //editTextGroupName.setMovementMethod(ScrollingMovementMethod.getInstance());
            //selectedQuake.setGroupName(editTextGroupName.getText().toString());
            //editTextGroupName.setHorizontallyScrolling(true); 
            //editTextGroupName.setText(selectedMeeting.getGroupName());
            
            editTextType = (TextView)quakeCreateView.findViewById(R.id.type);
            //selectedQuake.setType(editTextType.getText().toString());
            editTextType.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            //editTextType.setMovementMethod(ScrollingMovementMethod.getInstance());
            //editTextType.setText(selectedMeeting.getType());
            
            editTextPhone= (TextView)quakeCreateView.findViewById(R.id.phone);
            //selectedQuake.setPhone(editTextPhone.getText().toString());
            editTextPhone.setInputType(InputType.TYPE_CLASS_PHONE);
            //editTextPhone.setText(selectedMeeting.getPhone());

            editTextStreet = (TextView)quakeCreateView.findViewById(R.id.street);
            //selectedQuake.setStreet(editTextStreet.getText().toString());
            editTextStreet.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            //editTextStreet.setMovementMethod(ScrollingMovementMethod.getInstance());
            //editTextStreet.setText(selectedMeeting.getStreet());
            
            editTextCity = (TextView)quakeCreateView.findViewById(R.id.city);
            editTextCity.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            //selectedQuake.setCity(editTextCity.getText().toString());
            //editTextCity.setText(selectedMeeting.getCity());
            
            editTextLocation = (TextView)quakeCreateView.findViewById(R.id.notes);
            editTextLocation.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            //selectedQuake.setCity(editTextCity.getText().toString());
            //editTextLocation.setText(selectedMeeting.getLocation());
            
            editTextZipcode = (TextView)quakeCreateView.findViewById(R.id.zipcode);
            editTextZipcode.setInputType(InputType.TYPE_CLASS_NUMBER);
            //selectedQuake.setCity(editTextCity.getText().toString());
            //editTextZipcode.setText(selectedMeeting.getZipcode());
            
            //TextView editTextState = (TextView)editQuakeDialog.findViewById(R.id.state);
            //editTextState.setText(selectedQuake.getState());
            //selectedQuake.setDay(day);
            //selectedQuake.setState(state);
            editTextCity.setHint("Cannnot Be Empty");
            editTextStreet.setHint("Cannnot Be Empty");
            editTextGroupName.setHint("Group Name");
            editTextType.setHint("Closed");
            editTextPhone.setHint("(XXX) XXX-XXXX");
            editTextTime.setHint("8:00 AM");
            editTextLocation.setHint("Optional~(Upstairs First Door on Left)");
            editTextZipcode.setHint("XXXXX");
            //selectedQuake.setTime(time);
            Spinner spinnerStates = (Spinner) quakeCreateView.findViewById(R.id.spinner4);
            ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);    
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
            spinnerStates.setAdapter(adapter);
            
            //int pos4=adapter4.getPosition(selectedQuake.getState()); 
            //spinnerStates.setSelection(pos4); 
            spinnerStates.setOnItemSelectedListener(new MyOnItemSelectedListener2());

            Spinner spinnerDays = (Spinner) quakeCreateView.findViewById(R.id.spinner5);
            ArrayAdapter<?> adapter7 = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);    
            adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
            spinnerDays.setAdapter(adapter7);
            //int pos5=adapter5.getPosition(selectedQuake.getDay()); 
            //spinnerDays.setSelection(pos5); 
            spinnerDays.setOnItemSelectedListener(new MyOnItemSelectedListener());
            
            Spinner spinnerFellowships = (Spinner) quakeCreateView.findViewById(R.id.spinner6);
            ArrayAdapter<?> adapter8 = ArrayAdapter.createFromResource(this, R.array.fellowships, android.R.layout.simple_spinner_item);    
            adapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
            spinnerFellowships.setAdapter(adapter8);
            //int pos5=adapter5.getPosition(selectedQuake.getDay()); 
            //spinnerDays.setSelection(pos5); 
            spinnerFellowships.setOnItemSelectedListener(new MyOnItemSelectedListener3());
            
            // add a click listener to the button        
            editTextTime.setOnClickListener(new View.OnClickListener() {
            	public void onClick(View v) {
            		showDialog(TIME_DIALOG);
            	}        
            });
            
            
            
            createMeetingDialog.setPositiveButton(button7String,
          		  new AlertDialog.OnClickListener() {
          		  public void onClick(DialogInterface dialog, int arg1) {
          			//createMeetingDialog(selectedMeeting);
          			selectedMeeting.setTime(editTextTime.getText().toString());
	        		  
	        		  if ((editTextStreet.getText().toString().equals("")) || (editTextCity.getText().toString().equals(""))) {
	        			showDialog(NULL_DIALOG);
	        			return;
	        		  }
	        		  
	        		  editTextCity.setText(editTextCity.getText().toString().replace(",", " "));
	        		  editTextCity.setText(editTextCity.getText().toString().replace("&", " and "));
	        		  if (editTextCity.length() == 0) selectedMeeting.setCity(editTextCity.getHint().toString()); 
	        		  else selectedMeeting.setCity(editTextCity.getText().toString());
	        		  
	        		  editTextStreet.setText(editTextStreet.getText().toString().replace(",", " "));
	        		  editTextStreet.setText(editTextStreet.getText().toString().replace("&", " and "));
	        		  if (editTextStreet.length() == 0) selectedMeeting.setStreet(editTextStreet.getHint().toString()); 
	        		  else selectedMeeting.setStreet(editTextStreet.getText().toString());
	        		  
	        		  if (editTextTime.length() == 0) selectedMeeting.setTime(editTextTime.getHint().toString()); 
	        		  else selectedMeeting.setTime(editTextTime.getText().toString());
	        		  
	        		  editTextType.setText(editTextType.getText().toString().replace(",", " "));
	        		  editTextType.setText(editTextType.getText().toString().replace("&", " and "));
	        		  if (editTextType.length() == 0) selectedMeeting.setType(" "); 
	        		  else selectedMeeting.setType(editTextType.getText().toString());
	        		  
	        		  editTextGroupName.setText(editTextGroupName.getText().toString().replace(",", " "));
	        		  editTextGroupName.setText(editTextGroupName.getText().toString().replace("&", " and ")); 
	        		  if (editTextGroupName.length() == 0) selectedMeeting.setGroupName(" "); 
	        		  else selectedMeeting.setGroupName(editTextGroupName.getText().toString());
	      		  
	        		  editTextPhone.setText(editTextPhone.getText().toString().replace(",", " "));
	        		  editTextPhone.setText(editTextPhone.getText().toString().replace("&", " and "));
	        		  if (editTextPhone.length() == 0) selectedMeeting.setPhone(" "); 
	        		  else selectedMeeting.setPhone(editTextPhone.getText().toString());
	        		  
	        		  editTextLocation.setText(editTextLocation.getText().toString().replace(",", " "));
	        		  editTextLocation.setText(editTextLocation.getText().toString().replace("&", " and "));
	        		  if (editTextLocation.length() == 0) selectedMeeting.setLocation(" "); 
	        		  else selectedMeeting.setLocation(editTextLocation.getText().toString());
	        		  
	        		  editTextZipcode.setText(editTextZipcode.getText().toString().replace(",", " "));
	        		  editTextZipcode.setText(editTextZipcode.getText().toString().replace("&", " and "));
	        		  if (editTextZipcode.length() == 0) selectedMeeting.setZipcode("0"); 
	        		  else selectedMeeting.setZipcode(editTextZipcode.getText().toString());
	        		  
	        		  selectedMeeting.setState(state);
	        		  selectedMeeting.setDay(day);
	        		  selectedMeeting.setFellowship(fellowship);
	        		  
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
	      	  			CreateMeetingTask createMeetingTask = new CreateMeetingTask();
	      	  			Boolean createMeetingresult = null;
	      	  			try {
	      				  createMeetingresult = createMeetingTask.execute(null, null, null).get();
	      	  			} catch (InterruptedException e) {
	      	  				// TODO Auto-generated catch block
	      	  				e.printStackTrace();
	      	  			} catch (ExecutionException e) {
	      	  				// TODO Auto-generated catch block
	      	  				e.printStackTrace();
	      	  			}
	      	  			if (createMeetingresult) {
	        				  String sayWhat = String.format("Time:  %s\nDay:  %s\nGroup:  %s\nType:  %s\nPhone:  %s\nStreet:  %s\nCity:  %s\nState:%s",
									  selectedMeeting.getTime(), selectedMeeting.getDay(), selectedMeeting.getGroupName(), selectedMeeting.getType(), 
									  selectedMeeting.getPhone(), selectedMeeting.getStreet(), selectedMeeting.getCity(), selectedMeeting.getState());
	        				  String title = String.format("Create Success!");
	        				  
	        				  showDialog(title, sayWhat, getBaseContext());
	          				
	        			  } else {
	        				  String sayWhat = String.format("Time:  %s\nDay:  %s\nGroup:  %s\nType:  %s\nPhone:  %s\nStreet:  %s\nCity:  %s\nState:  %s",
								  selectedMeeting.getTime(), selectedMeeting.getDay(), selectedMeeting.getGroupName(), selectedMeeting.getType(), 
								  selectedMeeting.getPhone(), selectedMeeting.getStreet(), selectedMeeting.getCity(), selectedMeeting.getState());
	        				  String title = String.format("** Create Failure! **");
	    				  
	        				  showDialog(title, sayWhat, getBaseContext());
	        			  }
	        			  
	        		  } else {
	        			String sayWhat = String.format("Unable to Geo Code latitude and longitude for given:" + "\n\n" +
	  		        		"Street: %s\nCity: %s\nState: %s",  selectedMeeting.getStreet(), selectedMeeting.getCity(), selectedMeeting.getState());
	        			String title = String.format("** GEO Code Failure! **");
	    				  
      				  	showDialog(title, sayWhat, getBaseContext());
	        		  }
	        		}

          		  
          		  });
            createMeetingDialog.setCancelable(true);
            createMeetingDialog.setOnCancelListener(new OnCancelListener() {
          		  public void onCancel(DialogInterface dialog) {
          		  //eatenByGrue();
          		  }
          		  });
            createMeetingDialog.setNegativeButton(button8String,
            		  new AlertDialog.OnClickListener() {
            		  public void onClick(DialogInterface dialog, int arg1) {
            			//createMeetingDialog(selectedMeeting);
            		  }
            		  });
              
            return createMeetingDialog.create();


        
    }
      return null;
    }
    
    @Override
    public void onPrepareDialog(int id, Dialog dialog) {
      switch(id) {
      case (CREATE_MEETING_DIALOG) :
    	  AlertDialog alertDialog = (AlertDialog)dialog;
      Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
      button.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(
              R.drawable.dark_content_save), null, null, null);
      
      Button cancelButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
      cancelButton.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(
              R.drawable.dark_navigation_cancel), null, null, null);


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
  		 
  	        	editTextTime.setText(((String) DateFormat.format(delegate,c)+" PM")); 
  		    } 
  		    if(hourOfDay<12) 
  		    { 
  		    	editTextTime.setText(((String) DateFormat.format(delegate,c)+" AM")); 
  		    } 
  		}
  	};
 

 	public void showDialog(String title, String sayWhat, Context context){ 
				
        LayoutInflater li = LayoutInflater.from(context);
        View quakeDetailsView = li.inflate(R.layout.toast_layout, null);

        AlertDialog.Builder quakeDialog = new AlertDialog.Builder(this);
        quakeDialog.setTitle(title);
        quakeDialog.setView(quakeDetailsView);
        
        quakeDialog.setView(quakeDetailsView);
        quakeDialog.setMessage(sayWhat);
                
        String button1String = "OK";
        quakeDialog.setPositiveButton(button1String,
      		  new AlertDialog.OnClickListener() {
      		  public void onClick(DialogInterface dialog, int arg1) {
      		        		  }
      		  });
                quakeDialog.setCancelable(true);
        quakeDialog.setOnCancelListener(new OnCancelListener() {
      		  public void onCancel(DialogInterface dialog) {
      		  //eatenByGrue();
      		  }
      		  });
        quakeDialog.show();
        		
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
    			state = parent.getItemAtPosition(pos).toString();
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
    			fellowship = parent.getItemAtPosition(pos).toString();
    	}    
    	public void onNothingSelected(AdapterView<?> parent) {      
    		// Do nothing.    }}
    	}
		
		
    }


}
