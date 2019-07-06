package org.example.aafinder;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/*
 * open source custom array adapter
 * modified for Movie objects
 * and custom layout 
 * the lack of code indents and formatting 
 * were not present in open source
 */
public class MovieAdapter extends ArrayAdapter<Meeting> {
private ArrayList<Meeting> meetings;
private Activity activity;

public MovieAdapter(Context meetingActivityContext, MeetingActivity meetingActivity, int textViewResourceId, ArrayList<Meeting> meetings) {
super(meetingActivity, textViewResourceId, meetings);
this.meetings = meetings;
activity = meetingActivity;
}


public static class ViewHolder{
	public TextView timeLabel;
	public TextView time;
	public TextView dayLabel;
	public TextView day;
	public TextView cityLabel;
	public TextView city;
	public TextView distance;
	public TextView groupLabel;
	public TextView group;
	public TextView detailsLabel;
	public TextView details;
}

// inflate the list view and format
// text and image views 


    // set selected item's background to blue
    
  

@Override
public View getView(int position, View convertView, ViewGroup parent) {
	
View v = convertView;
ViewHolder holder;
//Log.e("movie adapter", "inside getview");
if (v == null) {	
LayoutInflater vi =
(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
v = vi.inflate(R.layout.list_item, null);
//Log.e("movie adapter: ", "number meetings: "+String.format("%s", meetings.size()));
holder = new ViewHolder();
//holder.timeLabel = (TextView) v.findViewById(R.id.fe);
holder.time = (TextView) v.findViewById(R.id.time);
//holder.dayLabel = (TextView) v.findViewById(R.id.dayLabel);
//holder.day = (TextView) v.findViewById(R.id.day);
//holder.cityLabel = (TextView) v.findViewById(R.id.cityLabel);
holder.city = (TextView) v.findViewById(R.id.city);
holder.distance = (TextView) v.findViewById(R.id.distance);
//holder.groupLabel = (TextView) v.findViewById(R.id.groupLabel);
holder.group = (TextView) v.findViewById(R.id.group);
//holder.detailsLabel = (TextView) v.findViewById(R.id.detailsLabel);
holder.details = (TextView) v.findViewById(R.id.details);

v.setTag(holder);

}
else
holder=(ViewHolder)v.getTag();
//Log.e("movie adapter: ", "number meetings: "+String.format("%s", meetings.size()));

final Meeting meeting = meetings.get(position);
// list rows alternate color
if (meeting!= null) {

	//holder.timeLabel.setText("Time:     ");
	//holder.timeLabel.setTypeface(holder.timeLabel.getTypeface(), Typeface.BOLD);
	holder.time.setText(meeting.getTime());
	//holder.dayLabel.setText("Day:     ");
	//holder.dayLabel.setTypeface(holder.dayLabel.getTypeface(), Typeface.BOLD);
	//holder.day.setText(meeting.getDay());
	//holder.cityLabel.setText("City:      ");
	//holder.cityLabel.setTypeface(holder.cityLabel.getTypeface(), Typeface.BOLD);
	holder.city.setText(meeting.getCity());
	holder.distance.setText(String.format("%s",meeting.getDistance().intValue())+" miles");
	//holder.groupLabel.setText("Group: ");
	//holder.groupLabel.setTypeface(holder.groupLabel.getTypeface(), Typeface.BOLD);
	holder.group.setText(meeting.getGroupName());
	//holder.detailsLabel.setText("Details: ");
	//holder.detailsLabel.setTypeface(holder.detailsLabel.getTypeface(), Typeface.BOLD);
	holder.details.setText(meeting.getType());
	//v.setAlpha((float) 0.7);
	//Log.e("movie adapter title: ", movie.getTitle());

	
	/*if (position  == 0)
		v.setBackgroundColor(0x80000000);
	else if ((position % 2) == 0) 
		v.setBackgroundColor(0x80000000);
	else if ((position % 2) == 1)
		v.setBackgroundColor(0xFF606060);*/
	
	//NOW YOU CHECK IF THAT POSTION WAS THE ONE CLICKED, IT SETS THE COLOR BLUE
	//if(meeting.isSelected())  	  		    
		//v.setBackgroundColor(0xff0099cc);
	
	/*if (position  == 0) {
		v.setBackgroundColor(0xFF000000);
		Log.e("movie adapter", "setting: " + String.format("index: %s", position) + "to: black");
	}
	else if ((position % 2) == 0) {
		v.setBackgroundColor(0xFF000000);
		Log.e("movie adapter", "setting: " + String.format("index: %s", position) + "to: black");
	}
	else if ((position % 2) == 1) {
		v.setBackgroundColor(0xFF606060);
		Log.e("movie adapter", "setting: " + String.format("index: %s", position) + "to: gray");
	}
	
		//NOW YOU CHECK IF THAT POSTION WAS THE ONE CLICKED, IT SETS THE COLOR BLUE
		if(meeting.isSelected()) {  	  		
			v.setBackgroundColor(0xff0099cc);
    		Log.e("movie adapter", "setting: " + String.format("index: %s", position) + "to: blue");
		}
	
	if (v.isSelected()) {
        //v.setBackgroundResource(R.color.blue);
		Log.e("adapter", String.format("position: %s", position));
		//v.setBackgroundColor(0x01060019);
        }
    */
	/*if (position % 2 == 0){
	    v.setBackgroundResource(R.drawable.alterselector1);
	} else {
	    v.setBackgroundResource(R.drawable.alterselector2);
	}*/
	
}
return v;
}
@Override
public void notifyDataSetChanged() {
    super.notifyDataSetChanged();  
}
}