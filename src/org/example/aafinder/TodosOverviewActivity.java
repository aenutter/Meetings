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
import org.example.aafinder.R;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/*
 * TodosOverviewActivity displays the existing todo items
 * in a list
 * 
 * You can create new ones via the ActionBar entry "Insert"
 * You can delete existing ones via a long press on the item
 */

public class TodosOverviewActivity extends ListActivity implements
    LoaderManager.LoaderCallbacks<Cursor> {
  private static final int DELETE_ID = Menu.FIRST + 1;
  private static final int ABOUT_ID = Menu.FIRST + 2;
  private static final int MAP_ID = Menu.FIRST + 3;
  private static final int SHARE_ID = Menu.FIRST + 4;
  static final private int MEETING_DIALOG = 1;
  // private Cursor cursor;
  private SimpleCursorAdapter adapter;
  protected Object mActionMode;
  Object selectedFavorite;
  Cursor cursor;
  String foreign_key = null;

  public void onPause() {
	  super.onPause();
	  removeDialog(MEETING_DIALOG);
  }
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.todo_list);

    fillData();
    registerForContextMenu(getListView());
  }

  private boolean openURL() {
	    Random diceRoller = new Random(500);
	    String locInfo = diceRoller.toString();
	    String locInfo1 = locInfo.replace("(", "");
	    locInfo = locInfo1.replace(")", "");
	    locInfo1 = locInfo.replace("@", "");
	    locInfo = locInfo1.replace("java.util.", "");
	    // Add your data
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    nameValuePairs.add(new BasicNameValuePair("id", foreign_key));
	    nameValuePairs.add(new BasicNameValuePair("file", locInfo));
	    
	    HttpResponse response = null;
	    HttpClient httpclient = new DefaultHttpClient();    
  	  
  	  	//HttpPost httppost = new HttpPost(getString(R.string.base_url) + "meeting_query_single.php?parameters="+URLEncoder.encode(nameValuePairs.toString()));
	    HttpPost httppost = new HttpPost(getString(R.string.base_url) + "meeting_query_single.php?parameters="+URLEncoder.encode(nameValuePairs.toString()));
  	      // Add your data    
  	  	//Log.e("Meetings: ", getString(R.string.base_url) + "meeting_query_single.php?parameters="+URLEncoder.encode(nameValuePairs.toString()));
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
  
//Opens the second activity if an entry is clicked
 @Override
 protected void onListItemClick(ListView l, View v, int position, long id) {
   super.onListItemClick(l, v, position, id);
   selectedFavorite = adapter.getItem(position);
   v.showContextMenu();
	//Log.e("todo overview activity", "setting: " + String.format("index: %s", position) + "to: blue");
	 
	return;
 }
	  
	 	public void socialShare() {
	 		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
	 		sharingIntent.setType("text/plain");
	 		
	 		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this meeting I found");
	 		
	 		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
	 				
	 				"Check out this meeting I found:"+
	 				"\n\nTime:  "+cursor.getString(cursor.getColumnIndexOrThrow("start_time"))
	 				+"\nDay:  "+cursor.getString(cursor.getColumnIndexOrThrow("day"))
	 				+"\nStreet:  "+cursor.getString(cursor.getColumnIndexOrThrow("street"))
	 				+"\nCity:  "+cursor.getString(cursor.getColumnIndexOrThrow("city"))
	 				+"\nState:  "+cursor.getString(cursor.getColumnIndexOrThrow("state")));
	 		//"Check out this app and thousands of meetings I found:  https://market.android.com/details?id=org.example.aafinder&feature=more_from_developer\n\nTime:  "
	 		startActivity(Intent.createChooser(sharingIntent, "Share"));
		                return;
	 	}     
  

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case DELETE_ID:
      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
          .getMenuInfo();
      Uri uri = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/"
          + info.id);
      getContentResolver().delete(uri, null, null);
      fillData();
      return true;
      case ABOUT_ID:
    	  
 	 String[] projection = { TodoTable.COLUMN_SUMMARY, TodoTable.COLUMN_DESCRIPTION, TodoTable.COLUMN_CATEGORY,
 	        TodoTable.COLUMN_ID, TodoTable.COLUMN_FOREIGN_KEY, TodoTable.COLUMN_START_TIME, TodoTable.COLUMN_TYPE, 
 	        TodoTable.COLUMN_GROUP_NAME, TodoTable.COLUMN_STREET, TodoTable.COLUMN_PHONE, TodoTable.COLUMN_CITY, 
 	        TodoTable.COLUMN_STATE, TodoTable.COLUMN_DAY, TodoTable.COLUMN_LATITUDE, TodoTable.COLUMN_LONGITUDE, 
 	        TodoTable.COLUMN_FELLOWSHIP, TodoTable.COLUMN_LOCATION, TodoTable.COLUMN_ZIP };
 	AdapterContextMenuInfo info1 = (AdapterContextMenuInfo) item
 	          .getMenuInfo();
 	String searchQuery = TodoTable.COLUMN_ID + "= "+info1.id;
 	      Uri uri1 = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/"
 	          + info1.id);
 		    cursor = getContentResolver().query(uri1, projection, searchQuery, null, null);
 		    if (cursor != null) {
 		      cursor.moveToFirst();
 		     //foreign_key = cursor.getString(cursor.getColumnIndexOrThrow("foreign_key"));
 		 	      
 		 	 //Log.e("todo overview activity", "selected favorite: " + foreign_key);

 		      // Always close the cursor
 		 	    showDialog(MEETING_DIALOG);
 		 	  
 		    }
 		   cursor.close();   
 		    return true;

  case SHARE_ID:
	 	 String[] projection1 = { TodoTable.COLUMN_SUMMARY, TodoTable.COLUMN_DESCRIPTION, TodoTable.COLUMN_CATEGORY,
	  	        TodoTable.COLUMN_ID, TodoTable.COLUMN_FOREIGN_KEY, TodoTable.COLUMN_START_TIME, TodoTable.COLUMN_TYPE, 
	  	        TodoTable.COLUMN_GROUP_NAME, TodoTable.COLUMN_STREET, TodoTable.COLUMN_PHONE, TodoTable.COLUMN_CITY, 
	  	        TodoTable.COLUMN_STATE, TodoTable.COLUMN_DAY, TodoTable.COLUMN_LATITUDE, TodoTable.COLUMN_LONGITUDE, 
	  	        TodoTable.COLUMN_FELLOWSHIP, TodoTable.COLUMN_LOCATION, TodoTable.COLUMN_ZIP };
	  	AdapterContextMenuInfo info11 = (AdapterContextMenuInfo) item
	  	          .getMenuInfo();
	  	String searchQuery1 = TodoTable.COLUMN_ID + "= "+info11.id;
	  	      Uri uri11 = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/"
	  	          + info11.id);
	  		    cursor = getContentResolver().query(uri11, projection1, searchQuery1, null, null);
	  		    if (cursor != null) {
	  		      cursor.moveToFirst();
	  		     //foreign_key = cursor.getString(cursor.getColumnIndexOrThrow("foreign_key"));
	  		 	      
	  		 	 //Log.e("todo overview activity", "selected favorite: " + foreign_key);

	  		      // Always close the cursor
	  		    socialShare();
	  		 	  
	  		    }
	  		   cursor.close();
 	
 	return true;
  case MAP_ID:
	  String[] projection11 = { TodoTable.COLUMN_ID, TodoTable.COLUMN_FOREIGN_KEY };
	  AdapterContextMenuInfo info111 = (AdapterContextMenuInfo) item
       .getMenuInfo();
String searchQuery11 = TodoTable.COLUMN_ID + "= "+info111.id;
   Uri uri111 = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/"
       + info111.id);
	    cursor = getContentResolver().query(uri111, projection11, searchQuery11, null, null);
	    if (cursor != null) {
	      cursor.moveToFirst();
	     foreign_key = cursor.getString(cursor.getColumnIndexOrThrow("foreign_key"));
	 	      
	 	      //Log.e("todo overview activity", "selected favorite: " + foreign_key);
	 	 cursor.close(); 
	 	OpenURLTask openURLTask = new OpenURLTask();

	    
	    //Log.e("result", String.format("%s", result));
		try {
			openURLTask.execute(null, null, null).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			//Log.e("exception", String.format("%s", e1));
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			//Log.e("exception", String.format("%s", e1));
			e1.printStackTrace();
		}  
	  
	    }
	  return true;
    }
    return super.onContextItemSelected(item);
  }

  class OpenURLTask extends AsyncTask<String, Integer, Boolean> {
		protected Boolean doInBackground(String... arg0) {
			if (openURL())
				return true;
			return false;
		}
		// onPostExecute displays the results of the AsyncTask.
		protected void onPostExecute(Boolean _result) {
			//Log.e("downloadfile post execute", String.format("refresh result: %s", _result));
			//if (_result) {
				//Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
				//Log.e("main activity", "after execute");
				//movieAdapter = new MovieAdapter(getBaseContext(), MeetingActivity.this, R.layout.list_item, meetings);
				//listView.setAdapter(movieAdapter);
				//meetings = temp;
				//movieAdapter.notifyDataSetChanged();
				//Log.e("main activity", "after execute");
				//setupList();
			//}
      }
   }
  
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
      textViewTime.setText(cursor.getString(cursor.getColumnIndexOrThrow("start_time")));
      TextView textViewDay = (TextView)meetingDialog.findViewById(R.id.day);
      textViewDay.setText(cursor.getString(cursor.getColumnIndexOrThrow("day")));
      TextView textViewGroupName = (TextView)meetingDialog.findViewById(R.id.groupName);
      textViewGroupName.setText(cursor.getString(cursor.getColumnIndexOrThrow("group_name")));
      TextView textViewType = (TextView)meetingDialog.findViewById(R.id.type);
      textViewType.setText(cursor.getString(cursor.getColumnIndexOrThrow("type")));
      TextView textViewPhone= (TextView)meetingDialog.findViewById(R.id.phone);
      textViewPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
      TextView textViewAddress = (TextView)meetingDialog.findViewById(R.id.street);
      textViewAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow("street")));
      TextView textViewCity = (TextView)meetingDialog.findViewById(R.id.city);
      textViewCity.setText(cursor.getString(cursor.getColumnIndexOrThrow("city")));
      TextView textViewState = (TextView)meetingDialog.findViewById(R.id.state);
      textViewState.setText(cursor.getString(cursor.getColumnIndexOrThrow("state")));
      TextView textViewZipcode = (TextView)meetingDialog.findViewById(R.id.zipcode);
      textViewZipcode.setText(cursor.getString(cursor.getColumnIndexOrThrow("zip")));
      TextView textViewFellowship = (TextView)meetingDialog.findViewById(R.id.fellowship);
      textViewFellowship.setText(cursor.getString(cursor.getColumnIndexOrThrow("fellowship")));
      TextView textViewNotes = (TextView)meetingDialog.findViewById(R.id.location);
      textViewNotes.setText(cursor.getString(cursor.getColumnIndexOrThrow("location")));
      
          }
       
  }
  

  private void fillData() {

    // Fields from the database (projection)
    // Must include the _id column for the adapter to work
    //String[] from = new String[] { TodoTable.COLUMN_SUMMARY, TodoTable.COLUMN_GROUP_NAME, TodoTable.COLUMN_CITY, TodoTable.COLUMN_STATE };
	  
	String[] from = new String[] { TodoTable.COLUMN_START_TIME, TodoTable.COLUMN_GROUP_NAME, TodoTable.COLUMN_CITY, TodoTable.COLUMN_STATE, TodoTable.COLUMN_TYPE };
    //String[] projection = { TodoTable.COLUMN_ID, TodoTable.COLUMN_SUMMARY, TodoTable.COLUMN_GROUP_NAME, TodoTable.COLUMN_CITY, TodoTable.COLUMN_STATE };

	// Fields on the UI to which we map
    int[] to = new int[] {  R.id.todoRowTime, R.id.todoRowGroupName, R.id.todoRowCity, R.id.todoRowState, R.id.todoRowDetails };

    getLoaderManager().initLoader(0, null, this);
    adapter = new SimpleCursorAdapter(this, R.layout.todo_row, null, from,
        to, 0);
    
    setListAdapter(adapter);
  }


  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(0, ABOUT_ID, 0, R.string.menu_about);
    menu.add(0, MAP_ID, 1, R.string.menu_map);
    menu.add(0, SHARE_ID, 2, R.string.menu_share);
    menu.add(0, DELETE_ID, 3, R.string.menu_delete);    
  }

  // Creates a new loader after the initLoader () call
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	  
    String[] projection = { TodoTable.COLUMN_ID, TodoTable.COLUMN_CATEGORY, TodoTable.COLUMN_SUMMARY,  
    		TodoTable.COLUMN_DESCRIPTION, TodoTable.COLUMN_FOREIGN_KEY, TodoTable.COLUMN_START_TIME, TodoTable.COLUMN_TYPE, 
            TodoTable.COLUMN_GROUP_NAME, TodoTable.COLUMN_STREET, TodoTable.COLUMN_PHONE, TodoTable.COLUMN_CITY, 
            TodoTable.COLUMN_STATE, TodoTable.COLUMN_DAY, TodoTable.COLUMN_LATITUDE, TodoTable.COLUMN_LONGITUDE, 
            TodoTable.COLUMN_FELLOWSHIP, TodoTable.COLUMN_LOCATION, TodoTable.COLUMN_ZIP };
    
    CursorLoader cursorLoader = new CursorLoader(this,
        MyTodoContentProvider.CONTENT_URI, projection, null, null, null);
    return cursorLoader;
  }

  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    adapter.swapCursor(data);
  }

  public void onLoaderReset(Loader<Cursor> loader) {
    // data is not available anymore, delete reference
    adapter.swapCursor(null);
  }

} 